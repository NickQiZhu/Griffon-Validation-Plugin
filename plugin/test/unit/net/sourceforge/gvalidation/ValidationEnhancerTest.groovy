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

import net.sourceforge.gvalidation.models.ModelBean
import net.sourceforge.gvalidation.models.UnknownConstraintModelBean
import net.sourceforge.gvalidation.models.NullToleranceModelBean
import net.sourceforge.gvalidation.models.NoConstraintModelBean
import net.sourceforge.gvalidation.models.InvalidConstraintModelBean
import net.sourceforge.gvalidation.models.CustomConstraintModelBean
import net.sourceforge.gvalidation.models.BindableModelBean
import java.beans.PropertyChangeListener
import net.sourceforge.gvalidation.util.MetaUtils
import net.sourceforge.gvalidation.models.AnnotatedModel

/**
 * Created by nick.zhu
 */
class ValidationEnhancerTest extends BaseTestCase {

    public void testModelEnhancementReturn() {
        def model = generateModel('ModelBean.groovy')

        def enhancer = ValidationEnhancer.enhance(model)

        assertTrue "Enhancer instance returned in incorrect", enhancer instanceof ValidationEnhancer
    }

    public void testModelConstraintInvocation() {
        def model = generateModel('ModelBean.groovy')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertFalse("Validation result should be false", result)
        assertTrue("Model should have error", model.hasErrors())
        assertTrue("id field should have error", model.errors.hasFieldErrors('id'))

        def fieldError = model.errors.getFieldError('id')

        assertEquals("Error code is not correct", "modelBean.id.nullable.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.nullable.message", fieldError.defaultErrorCode)
        assertEquals("Error arg field is not correct", "id", fieldError.arguments[0])
        assertEquals("Error arg bean is not correct", "ModelBean", fieldError.arguments[1])
        assertEquals("Error arg constraint is not correct", "null", fieldError.arguments[2])
        assertEquals("Error arg constraint config is not correct", false, fieldError.arguments[3])
    }

    public void testErrorCorrection() {
        def model = generateModel('ModelBean.groovy')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertFalse("Validation result should be false", result)

        model.id = "Soemthing"

        result = model.validate()

        assertTrue("Validation result should not be false", result)
    }

    public void testValidationWithNoConstraint() {
        def model = generateModel('NoConstraintModelBean.groovy')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testValidationWithInvalidConstraint() {
        def model = generateModel('InvalidConstraintModelBean.groovy')

        ValidationEnhancer.enhance(model)

        try {
            boolean result = model.validate()
            fail("Exception should be thrown with invalid constraint configuration")
        } catch (IllegalStateException ex) {
            // good
        }
    }

    public void testUnknownConstraintIsIgnored() {
        def model = generateModel('UnknownConstraintModelBean.groovy')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testNullAndBlankTolerance() {
        def model = generateModel('NullToleranceModelBean.groovy')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testCustomValidator() {
        ConstraintRepository.instance.register('magic',
                [validate: {property, bean, parameter ->
                    return false
                }])

        def model = generateModel('CustomConstraintModelBean.groovy')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertFalse "Custom validation should have failed", result

        def fieldError = model.errors.getFieldError('number')

        assertEquals("Error code is not correct", "customConstraintModelBean.number.magic.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.magic.message", fieldError.defaultErrorCode)
    }

    public void testFieldLevelValidation() {
        def model = generateModel('ModelBean.groovy')

        model.email = 'invalidEmail'

        ValidationEnhancer.enhance(model)

        boolean result = model.validate(['email'])

        assertFalse "Field validation should have failed", result

        assertFalse model.errors.hasFieldErrors('id')
        assertTrue model.errors.hasFieldErrors('email')

        def fieldError = model.errors.getFieldError('email')

        assertEquals("Error code is not correct", "modelBean.email.email.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.email.message", fieldError.defaultErrorCode)
    }

    public void testFieldLevelValidationWithMultipleExecution() {
        def model = generateModel('ModelBean.groovy')

        model.email = 'invalidEmail'

        ValidationEnhancer.enhance(model)

        model.validate(['email'])

        boolean result = model.validate(['id'])

        assertFalse "Field validation should have failed", result

        def fieldError = model.errors.getFieldError('id')

        assertEquals("Error code is not correct", "modelBean.id.nullable.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.nullable.message", fieldError.defaultErrorCode)

        model.email = "somebody@email.com"

        assertTrue "Email should be valid", model.validate(['email'])
    }

    public void testFieldLevelValidationWithMultipleFields() {
        def model = generateModel('ModelBean.groovy')

        model.email = 'invalidEmail'

        ValidationEnhancer.enhance(model)

        boolean result = model.validate(['id', 'email'])

        Thread.sleep(100)
        assertFalse "Field validation should have failed", result

        assertTrue model.errors.hasFieldErrors('id')
        assertTrue model.errors.hasFieldErrors('email')
    }

    public void testFieldLevelValidationWithSingleString() {
        def model = generateModel('ModelBean.groovy')

        model.email = 'invalidEmail'

        ValidationEnhancer.enhance(model)

        boolean result = model.validate('email')

        assertFalse "Field validation should have failed", result

        assertFalse model.errors.hasFieldErrors('id')
        assertTrue model.errors.hasFieldErrors('email')
    }

    public void testFieldLevelValidationShouldNotResetAllErrors() {
        def model = generateModel('ModelBean.groovy')

        ValidationEnhancer.enhance(model)

        model.email = 'invalidEmail'

        model.validate()

        model.email = 'abc@abc.com'

        boolean result = model.validate(['email'])

        assertTrue "Field validation should not have failed", result

        assertTrue "ID error should still be there", model.errors.hasFieldErrors('id')
        assertFalse "Email should not contain any error", model.errors.hasFieldErrors('email')
    }

    public void testEnhancement() {
        def model = generateModel()

        assertEquals("Incorrect number of validate methods were synthesized", 2, model.metaClass.methods.count {it.name == 'validate'})

        assertFalse("Model should have been enhanced", ValidationEnhancer.isNotEnhanced(model))

        assertTrue("Model should have validate method", MetaUtils.hasMethodOrClosure(model, "validate"))
    }

    public void testMultipleEnhance(){
        def m1 = generateModel()
        def m2 = generateModel()

        assertNotSame('Enhancer instances should not be the same', m1.__validationEnhancer, m2.__validationEnhancer)

        m2.validate()

        assertFalse("Model should have error", m1.hasErrors())
    }

}