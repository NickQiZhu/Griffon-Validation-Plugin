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

package net.sourceforge.gvalidation.renderer


import net.sourceforge.gvalidation.BaseTestCase
import net.sourceforge.gvalidation.Errors
import org.springframework.context.MessageSource

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorNodeDecoratorTest extends BaseTestCase {
    def resultErrors
    def resultFieldError
    def model = [errors: new Errors()]
    def node = [:]
    def errorField = "username"
    def messageSource = [:] as MessageSource

    public void testNotDecorateUnmatchedFields() {
        ErrorDecorator decorator = new ClosureErrorDecorator(
                decorator: {errors, fieldError -> resultErrors = errors; resultFieldError = fieldError}
        )

        decorator.register(model, node, errorField, messageSource)

        model.errors.rejectValue("email", "emailCode")
        pause()

        assert resultErrors == null
        assert resultFieldError == null
    }

    public void testDecorateMatchingFields() {
        ErrorDecorator decorator = new ClosureErrorDecorator(
                decorator: {errors, fieldError -> resultErrors = errors; resultFieldError = fieldError}
        )

        decorator.register(model, node, errorField, messageSource)

        model.errors.rejectValue(errorField, "${errorField}Code")
        pause()

        assert resultErrors != null
        assert resultFieldError != null
    }

    public void testUndecorateWhenMachingFieldIsRemoved() {
        ErrorDecorator decorator = new ClosureErrorDecorator(
                decorator: {errors, fieldError -> resultErrors = errors; resultFieldError = fieldError},
                undecorator: {resultErrors = null; resultFieldError = null}
        )

        decorator.register(model, node, errorField, messageSource)
        model.errors.rejectValue(errorField, "${errorField}Code")

        model.errors.clear()
        pause()

        assert resultErrors == null
        assert resultFieldError == null
    }

}
