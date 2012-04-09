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

import javax.swing.JComponent
import javax.swing.JDialog
import net.sourceforge.gvalidation.BaseTestCase
import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.util.GriffonWindowManager

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class PopupErrorDecoratorTest extends BaseTestCase {

    PopupErrorDecorator decorator = new PopupErrorDecorator()

    public void tearDown(){
        decorator.undecorate()
        pause()
    }

    public void testPopupCreation(){

        def errorField = "email"
        def errors = new Errors()

        def windowManager = [getGriffonWindow: {new JDialog()}] as GriffonWindowManager
        def model = [errors: errors]
        def node = [:] as JComponent
        def messageResource = [getMessage: {code, args -> 'Error message'}]

        decorator.windowManager = windowManager
        decorator.register(model, node, errorField, messageResource)
        errors.rejectValue(errorField, 'emailErrorCode')

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()

        assert decorator.popup != null
        assert decorator.messageLabel.text == "Error message"
        assert decorator.popup.visible == true
    }

    public void testPopupCreationWithMultiErrors(){
        def errorField = "email"
        def errors = new Errors()

        def windowManager = [getGriffonWindow: {new JDialog()}] as GriffonWindowManager
        def model = [errors: errors]
        def node = [:] as JComponent
        def messageResource = [getMessage: {code, args ->
            if (code == 'emailErrorCode1')
                return 'Error message1'
            else
                return 'Error message2'
        }]

        decorator.windowManager = windowManager
        decorator.register(model, node, errorField, messageResource)
        errors.rejectValue(errorField, 'emailErrorCode1')
        errors.rejectValue(errorField, 'emailErrorCode2')

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()

        assert decorator.popup != null
        assert decorator.messageLabel.text == "Error message1"
        assert decorator.popup.visible == true
    }

    public void testHidePopupWhenUndecorating(){
        PopupErrorDecorator decorator = new PopupErrorDecorator()

        def errorField = "email"
        def errors = new Errors()

        def windowManager = [getGriffonWindow: {new JDialog()}] as GriffonWindowManager
        def model = [errors: errors]
        def node = [:] as JComponent
        def messageResource = [getMessage: {code, args -> 'Error message'}]

        decorator.windowManager = windowManager
        decorator.register(model, node, errorField, messageResource)
        errors.rejectValue(errorField, 'emailErrorCode')

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()
        decorator.undecorate()
        pause()

        assert decorator.popup != null
        assert decorator.popup.visible == false
    }

}
