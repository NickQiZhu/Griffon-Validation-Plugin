/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.renderer

import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.FieldError
import net.sourceforge.gvalidation.ErrorListener

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
abstract class BaseErrorDecorator implements ErrorDecorator, ErrorListener {

    protected Object model
    protected def messageSource
    protected String errorField
    protected Object targetComponent

    public void register(model, node, errorField, messageSource) {
        this.model = model
        this.targetComponent = node
        this.errorField = errorField
        this.messageSource = messageSource

        this.model.errors.addListener(this)
    }

    public boolean isRegistered() {
        return model != null
    }

    public String getErrorField() {
        return errorField
    }

    def getModel() {
        return model
    }

    def getMessageSource() {
        return messageSource
    }

    def getTargetComponent() {
        return targetComponent
    }

    /**
     * This abstract method should be implemented by subclasses to perform actual UI
     * related decoration. This method is guaranteed to be invoked in edt.
     *
     * @param errors current errors
     * @param fieldError associated field error instance
     */
    abstract protected void decorate(Errors errors, FieldError fieldError)

    /**
     * This abstract method should be implemented by subclasses to perform actual UI
     * related un-decoration. This method is guaranteed to be invoked in edt.
     */
    abstract protected void undecorate()

    @Override
    def onFieldErrorAdded(FieldError error) {
        if (isRelatedError(error))
            decorate(model.errors, error)
    }

    @Override
    def onFieldErrorRemoved(List errors) {
        errors.each {FieldError error ->
            if (isRelatedError(error))
                undecorate()
        }
    }

    protected boolean isRelatedError(FieldError error) {
        return error.field == errorField
    }
}
