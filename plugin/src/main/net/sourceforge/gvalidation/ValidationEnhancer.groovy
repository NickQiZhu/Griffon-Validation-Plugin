/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.ClassUtils
import net.sourceforge.gvalidation.util.MetaUtils

/**
 * Created by nick.zhu
 */
class ValidationEnhancer {

    static final def BEFORE_VALIDATION_CALLBACK_NAME = 'beforeValidation'
    static final def CONSTRAINT_PROPERTY_NAME = "constraints"

    private List fields

    private def model

    private ValidationEnhancer(bean) {
        model = bean
    }

    /**
     * This closure gets invoked when validate method on the model is
     * executed each time
     */
    def doValidate = {params = null ->
        if (MetaUtils.hasMethodOrClosure(model, BEFORE_VALIDATION_CALLBACK_NAME))
            model."${BEFORE_VALIDATION_CALLBACK_NAME}"()

        this.fields = generateTargetFields(params)

        try {
            if(isFieldBasedValidation(fields)){
                model.errors.clear()
            }else{
                fields.each{
                    model.errors.removeErrorOnField(it)
                }
            }

            evaluateParentClassConstraintsRecursively(model.getClass())

            evaluateCurrentClassConstraints()

            if(isFieldBasedValidation(fields))
                return hasAnyError(model)
            else{
                return hasFieldValidationFailed(fields)
            }
        } finally {
            this.fields = null
        }
    }

    private def hasAnyError(model) {
        !model.hasErrors()
    }

    private boolean isFieldBasedValidation(List fields) {
        return !fields
    }

    private boolean hasFieldValidationFailed(List fields) {
        boolean hasError = false

        fields.each {
            hasError |= !model.errors.hasFieldErrors(it)
        }

        return hasError
    }

    private List generateTargetFields(params) {
        List targets = null

        if (params instanceof List)
            targets = params
        else if (params != null)
            targets = [params]

        return targets
    }


    private def evaluateParentClassConstraintsRecursively(clazz) {
        def superClass = clazz.getSuperclass()

        if(superClass && superClass != Object)
            evaluateParentClassConstraintsRecursively(superClass)
        else
            return

        if (!hasConstraintsDefined(superClass))
            return

        Closure parentConstraints = superClass."${CONSTRAINT_PROPERTY_NAME}"
        parentConstraints.delegate = this
        parentConstraints.call()
    }

    private def evaluateCurrentClassConstraints() {
        if (!hasConstraintsDefined(model))
            return

        Closure constraints = model.getProperty(CONSTRAINT_PROPERTY_NAME)
        constraints.delegate = this
        constraints.call()
    }

    private boolean hasConstraintsDefined(object) {
        try {
            return object."${CONSTRAINT_PROPERTY_NAME}" != null
        } catch (MissingPropertyException ex) {
            // TODO: need to find a better way to do this than relying on exception
            return false
        }
    }

    /**
     * Method missing here is used to capture all constraint invocation on a model
     *
     * @param name property name
     * @param args constraint config
     */
    def methodMissing(String name, args) {
        if (!model.hasProperty(name)) {
            throw new IllegalStateException("""Invalid constraint configuration detected.
                    Property [${name}] with constraint configured is missing.""")
        }

        def propertyValue = model.getProperty(name)

        boolean valid = processConstraints(args, propertyValue, name)

        return valid
    }

    private boolean processConstraints(args, propertyValue, String name) {
        if (noValidationForField(name))
            return true

        boolean valid = true

        def constraintsMap = args[0]

        constraintsMap.each {constraint, config ->
            valid = performValidation(constraint, propertyValue, config, name)
        }

        return valid
    }

    private boolean noValidationForField(String name) {
        return fields && !fields.contains(name)
    }

    private def performValidation(constraint, propertyValue, config, name) {
        def validator = ConstraintRepository.instance.getValidator(constraint)

        if (validator) {
            return executeValidator(validator, propertyValue, config, name, constraint)
        } else {
            return handleMissingValidator(constraint)
        }
    }

    private def executeValidator(validator, propertyValue, config, name, constraint) {
        def success = false

        if (validator instanceof Closure) {
            success = validator.call(propertyValue, model, config)
        } else {
            success = validator.validate(propertyValue, model, config)
        }

        if (success)
            return true

        model.errors.rejectValue(name,
                buildErrorCode(name, constraint),
                buildDefaultErrorCode(constraint),
                buildErrorArguments(name, model, propertyValue, config))

        return false
    }

    private def handleMissingValidator(constraint) {
        return true
    }

    private def buildDefaultErrorCode(constraint) {
        return "default.${constraint}.message"
    }

    private def buildErrorCode(fieldName, constraint) {
        def className = StringUtils.uncapitalize(getShortClassName(model))

        return "${className}.${fieldName}.${constraint}.message"
    }

    private List buildErrorArguments(String name, model, propertyValue, config) {
        return [name, getShortClassName(model), "${propertyValue}", config]
    }

    private String getShortClassName(model) {
        if (!model)
            return "null"

        return ClassUtils.getShortClassName(model.getClass())
    }

}
