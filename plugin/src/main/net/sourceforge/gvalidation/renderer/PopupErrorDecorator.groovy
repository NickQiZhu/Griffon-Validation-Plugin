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

import java.awt.Color
import java.awt.FlowLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.plaf.basic.BasicButtonUI
import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.FieldError
import net.sourceforge.gvalidation.swing.Icons
import net.sourceforge.gvalidation.util.ErrorMessageUtils
import net.sourceforge.gvalidation.util.GriffonWindowManager
import java.awt.event.*

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class PopupErrorDecorator extends BaseErrorDecorator implements ComponentListener, FocusListener {

    def windowManager = new GriffonWindowManager()

    JDialog popup
    Color bgColor = new Color(243, 255, 159)
    JLabel image
    JLabel messageLabel
    JButton closeBtn

    @Override
    void register(model, node, errorField, messageSource) {
        super.register(model, node, errorField, messageSource)

        image = new JLabel(Icons.ERROR_ICON)
        messageLabel = new JLabel()
        closeBtn = new JButton(Icons.CLOSE_ICON)
        closeBtn.addActionListener({e -> popup.visible = false} as ActionListener)
        closeBtn.border = null
        closeBtn.borderPainted = false
        closeBtn.background = bgColor
        closeBtn.margin = new Insets(0, 0, 0, 0)
        closeBtn.setUI(new BasicButtonUI())

        windowManager.griffonWindow.addComponentListener(this)
        targetComponent.addComponentListener(this)
        targetComponent.addFocusListener(this)
    }

    @Override
    protected void decorate(Errors errors, FieldError fieldError) {
        if (popup == null)
            initializePopup()

        if (!popup.visible) {
            messageLabel.text = ErrorMessageUtils.getErrorMessage(fieldError, messageSource)
            positionPopup()
            popup.visible = true
        }
    }

    private def initializePopup() {
        popup = new JDialog(windowManager.getWindow(targetComponent))
        popup.getContentPane().setLayout(new FlowLayout());
        popup.setUndecorated(true);
        popup.getContentPane().setBackground(bgColor);
        popup.getContentPane().add(image);
        popup.getContentPane().add(messageLabel);
        popup.getContentPane().add(closeBtn)
        popup.setFocusableWindowState(false);
    }

    private def positionPopup() {
        if (popup != null) {
            popup.setSize(0, 0);
            popup.setLocationRelativeTo(targetComponent)
            def popupLocation = popup.getLocation()
            def targetComponentSize = targetComponent.getSize()
            popup.setLocation((int) (popupLocation.x - targetComponentSize.getWidth() / 2),
                    (int) (popupLocation.y + targetComponentSize.getHeight() / 2))
            popup.pack()
        }
    }

    @Override
    protected void undecorate() {
        popup?.visible = false
    }

    void componentResized(ComponentEvent componentEvent) {
        positionPopup()
    }

    void componentMoved(ComponentEvent componentEvent) {
        positionPopup()
    }

    void componentShown(ComponentEvent componentEvent) {
        positionPopup()
    }

    void componentHidden(ComponentEvent componentEvent) {
        popup?.visible = false
    }

    void focusGained(FocusEvent focusEvent) {
        if (messageLabel.text) {
            positionPopup()
            popup?.visible = true
        }
    }

    void focusLost(FocusEvent focusEvent) {
        popup?.visible = false
    }

}
