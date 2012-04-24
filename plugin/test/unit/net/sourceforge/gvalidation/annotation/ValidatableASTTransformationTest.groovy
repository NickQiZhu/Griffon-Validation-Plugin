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

package net.sourceforge.gvalidation.annotation

import java.beans.PropertyChangeListener
import net.sourceforge.gvalidation.BaseTestCase
import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.models.AnnotatedModel

/**
 * Created by nick.zhu
 */
class ValidatableASTTransformationTest extends BaseTestCase {

    public void testValidateAllInjection() {
        def model = new AnnotatedModel()

        boolean result = model.validate()

        assertFalse "Validation should have failed", result
        assertTrue("Model should have error", model.hasErrors())
        assertTrue("id field should have error", model.errors.hasFieldErrors('id'))
    }

    public void testSelectiveValidationInjection() {
        def model = new AnnotatedModel()

        boolean result = model.validate('email')

        assertFalse "Validation should have failed", result
        assertTrue("Model should have error", model.hasErrors())
        assertTrue("email field should have error", model.errors.hasFieldErrors('email'))
        assertFalse("id field should have error", model.errors.hasFieldErrors('id'))
    }

    public void testGoodValidateAll() {
        def model = new AnnotatedModel()

        model.id = "goodID"
        model.email = "someone@email.com"
        model.zipCode = 123456

        boolean result = model.validate()

        assertTrue "Validation should have passed", result
        assertFalse("Model should not have error", model.hasErrors())
    }

    public void testGoodSelectiveValidation() {
        def model = new AnnotatedModel()

        model.email = "someone@email.com"

        boolean result = model.validate(['email'])

        assertTrue "Validation should have passed", result
        assertFalse("Model should not have error", model.hasErrors())
    }

    public void testErrorPropertyChange(){
        def model = new AnnotatedModel()

        def firedEvent = false
        model.addPropertyChangeListener({e->
            assertEquals(e.propertyName, "errors"); firedEvent = true
        } as PropertyChangeListener)

        model.errors.reject('testError')

        Thread.sleep(100)
        assertTrue "Property change event should have been fired", firedEvent
    }

    public void testErrorGetterSetterInjection(){
        def model = new AnnotatedModel()

        def methods = model.metaClass.methods

        assertNotNull("Dynamic setter is not generated", methods.find{it.name == "setErrors"})

        def firedEvent = false
        model.addPropertyChangeListener({e->
            assertEquals(e.propertyName, "errors"); firedEvent = true
        } as PropertyChangeListener)

        model.errors = new Errors()

        assertTrue "Property change event should have been fired", firedEvent
    }

}
