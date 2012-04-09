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

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.*
import static javax.swing.SwingConstants.LEFT
import net.sourceforge.gvalidation.Errors

import net.sourceforge.gvalidation.util.ErrorMessageUtils

/**
 * Created by nick.zhu
 */
class ErrorMessagePanel extends JPanel {
    private ImageIcon errorIcon;
    private ImageIcon successIcon;

    def messageSource
    Errors errors
    JPanel contentPanel

    def ErrorMessagePanel(def messageSource) {
        this.messageSource = messageSource
        this.errorIcon = Icons.ERROR_ICON
        this.successIcon = Icons.SUCCESS_ICON

        setLayout new BorderLayout()
    }

    public Errors getErrors() {
        return errors
    }

    public void setErrors(Errors errors) {
        this.errors = errors

        SwingUtilities.invokeLater({
            removeAll()

            contentPanel = new JPanel()
            contentPanel.setLayout new BoxLayout(contentPanel, BoxLayout.Y_AXIS)

            if (errors && errors.hasErrors()) {
                setErrorMessageBorder()

                errors.each {error ->
                    JLabel errorLabel = createErrorLabel(error)
                    contentPanel.add(errorLabel)
                }
            } else {
                clearErrorMessageBorder()
            }

            add(contentPanel)
            revalidate()
        } as Runnable)
    }

    private def setErrorMessageBorder() {
        def paddingBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5)
        def errorHighlightBorder = BorderFactory.createLineBorder(Color.RED)
        contentPanel.setBorder BorderFactory.createCompoundBorder(errorHighlightBorder, paddingBorder)
    }

    def createErrorLabel(error) {
        def errorMessage = ErrorMessageUtils.getErrorMessage(error, messageSource)

        def errorLabel = new JLabel(" ${errorMessage}", LEFT)

        errorLabel.setIcon errorIcon
        errorLabel.setForeground Color.RED

        return errorLabel
    }

    private def clearErrorMessageBorder() {
        contentPanel.setBorder BorderFactory.createEmptyBorder()
    }

}
