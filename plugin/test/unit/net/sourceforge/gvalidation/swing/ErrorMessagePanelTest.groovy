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

package net.sourceforge.gvalidation.swing

import javax.swing.JLabel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.BaseTestCase
import griffon.core.i18n.NoSuchMessageException

/**
 * Created by nick.zhu
 */
class ErrorMessagePanelTest extends BaseTestCase {

    public void testErrorPanelGeneration() {
        ErrorMessagePanel errorPanel = buildErrorPanel()

        def errorLabel = errorPanel.getContentPanel().getComponents()[0]

        assertTrue "Label type is incorrect", errorLabel instanceof JLabel
        assertEquals "Error message is incorrect", " errorMessage", errorLabel.text
        assertTrue "Error border was not generated", errorPanel.getContentPanel().getBorder() instanceof CompoundBorder
    }

    private ErrorMessagePanel buildErrorPanel() {
        Errors errors = new Errors()

        errors.reject("errorCode")

        def messageSource = [getMessage: {code, args -> return "errorMessage" }]
        ErrorMessagePanel errorPanel = new ErrorMessagePanel(messageSource)

        errorPanel.errors = errors

        pause()

        return errorPanel
    }

    public void testErrorReset(){
        ErrorMessagePanel errorPanel = buildErrorPanel()

        errorPanel.errors = new Errors()

        pause()

        assertEquals "Error panel should be empty", 0, errorPanel.getContentPanel().components.size()
        assertTrue "Error border was not generated", errorPanel.getContentPanel().getBorder() instanceof EmptyBorder
    }

    public void testErrorResetWithNull(){
        ErrorMessagePanel errorPanel = buildErrorPanel()

        errorPanel.errors = null

        pause()

        assertEquals "Error panel should be empty", 0, errorPanel.getContentPanel().components.size()
        assertTrue "Error border was not generated", errorPanel.getContentPanel().getBorder() instanceof EmptyBorder
    }

    public void testLabelCreation(){
        def messageSource = [getMessage: {code, args -> return "errorMessage" }]
        ErrorMessagePanel errorPanel = new ErrorMessagePanel(messageSource)

        JLabel label = errorPanel.createErrorLabel([errorCode:'errorCode', arguments:[]])

        assertEquals "Label message is incorrect", " errorMessage", label.getText()
    }

    public void testLabelCreationWithDefaultMessage(){
        def messageSource = [getMessage: {code, args ->
            if(code == "errorCode")
                throw new NoSuchMessageException("")
            else if(code == "defaultErrorCode")
                return "defaultMessage"
            else
                throw new IllegalArgumentException()
        }]
        
        ErrorMessagePanel errorPanel = new ErrorMessagePanel(messageSource)

        JLabel label = errorPanel.createErrorLabel([errorCode:'errorCode', defaultErrorCode:'defaultErrorCode', arguments:[]])

        assertEquals "Label message is incorrect", " defaultMessage", label.getText()
    }
}
