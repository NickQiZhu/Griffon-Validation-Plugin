import static java.awt.BorderLayout.*
import net.sourceforge.gvalidation.swing.ErrorMessagePanel
import org.apache.commons.lang.NumberUtils
import griffon.plugins.i18n.*

actions {
    action(id: 'submitAction',
            name: 'Validate All',
            closure: controller.submit
    )
    action(id: 'validateFieldAction',
            name: 'Validate Given Field',
            closure: controller.validateField
    )
    action(id: 'annotationValidationAction',
            name: 'Validate Annotated Object',
            closure: controller.validateServerParameter
    )
    action(id: 'inheritanceValidationAction',
            name: 'Validate with Inheritance',
            closure: controller.validateProtocolSpecificServerParameter
    )
    action(id: 'startDialogAction',
            name: 'Validate in Dialog',
            closure: controller.startDialog
    )

}

def stringToIntConverter = {v ->
    if (NumberUtils.isDigits(v))
        try {
            return Integer.parseInt(v)
        } catch (java.lang.NumberFormatException ex) {
            return 0
        }
    else
        return 0
}

window = application(title: 'demo',
        size: [600, 450],
        pack: false,
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image]
) {
    tabbedPane() {
        panel(name: 'Model Validation') {
            borderLayout()

            errorMessages(id: 'errorMessagePanelTab1', constraints: NORTH,
                    errors: bind(source: model, 'errors'))

            panel(constraints: CENTER) {
                gridLayout(columns: 3, rows: 11)
                label('* Name: ')
                textField(text: bind(target: model, 'name'), errorRenderer: 'for: name, styles: [highlight, popup]')
                errorIcon(errorRenderer: 'for: name, styles: [onWithError]')

                label('* Email: ')
                textField(text: bind(target: model, 'email'), errorRenderer: 'for: email')
                errorIcon(errorRenderer: 'for: email, styles: [onWithError]')

                label('Web Site (i.e. http://www.yoursite.com/): ')
                textField(text: bind(target: model, 'webSite'))
                errorIcon(errorRenderer: 'for: webSite, styles: [onWithError]')

                label('* CrediCard Number: ')
                textField(text: bind(target: model, 'creditCard'), errorRenderer: 'for: creditCard')
                errorIcon(errorRenderer: 'for: creditCard, styles: [onWithError]')

                label('* Postal Code (i.e. M4A 3B7): ')
                textField(text: bind(target: model, 'postalCode'), errorRenderer: 'for: postalCode, styles: [highlight, popup]')
                errorIcon(errorRenderer: 'for: postalCode, styles: [onWithError]')

                label('* Password: ')
                passwordField(text: bind(target: model, 'password'), errorRenderer: 'for: password')
                errorIcon(errorRenderer: 'for: password, styles: [onWithError]')

                label('* Retype Password: ')
                passwordField(text: bind(target: model, 'passwordRepeat'), errorRenderer: 'for: passwordRepeat')
                errorIcon(errorRenderer: 'for: passwordRepeat, styles: [onWithError]')

                label('* Order Amount: ')
                textField(text: bind(target: model, 'orderAmount', converter: stringToIntConverter), errorRenderer: 'for: orderAmount')
                errorIcon(errorRenderer: 'for: orderAmount, styles: [onWithError]')

                label('* 2nd Order Amount: ')
                textField(text: bind(target: model, 'secondOrderAmount', converter: stringToIntConverter), errorRenderer: 'for: secondOrderAmount')
                errorIcon(errorRenderer: 'for: secondOrderAmount, styles: [onWithError]')

                label('Magic Number: ')
                textField(text: bind(target: model, 'magicNumber', converter: stringToIntConverter), errorRenderer: 'for: magicNumber, styles: [popup]')
                errorIcon(errorRenderer: 'for: magicNumber, styles: [onWithError]')

                button(submitAction)
            }

            panel(constraints: SOUTH) {
                gridLayout(columns: 2, rows: 1)
                textField(text: bind(target: model, 'fieldToValidate'))
                button(validateFieldAction)
            }
        }

        panel(name: 'Annotation Driven Validation') {
            borderLayout()
            container(new ErrorMessagePanel(MessageSourceHolder.instance.messageSource),
                    id: 'errorMessagePanelTab2', constraints: NORTH,
                    errors: bind(source: controller.serverParameter, 'errors'))

            panel(constraints: CENTER) {
                gridLayout(columns: 2, rows: 4)
                label('* Server Name: ')
                textField(text: bind(target: controller.serverParameter, 'serverName'))
                label('* Port Number: ')
                textField(text: bind(target: controller.serverParameter, 'port', converter: stringToIntConverter))
                label('String Form: ')
                label(text: bind(source: controller.serverParameter, 'displayName'))

                button(annotationValidationAction)
            }
        }

        panel(name: 'Inheritence and Constraints') {
            borderLayout()
            container(new ErrorMessagePanel(MessageSourceHolder.instance.messageSource),
                    id: 'errorMessagePanelTab3', constraints: NORTH,
                    errors: bind(source: controller.protocolSpecificServerParameter, 'errors'))

            panel(constraints: CENTER) {
                gridLayout(columns: 2, rows: 5)
                label('* Protocol: ')
                textField(text: bind(target: controller.protocolSpecificServerParameter, 'protocol'))
                label('* Server Name: ')
                textField(text: bind(target: controller.protocolSpecificServerParameter, 'serverName'))
                label('* Port Number: ')
                textField(text: bind(target: controller.protocolSpecificServerParameter, 'port', converter: stringToIntConverter))
                label('String Form: ')
                label(text: bind(source: controller.protocolSpecificServerParameter, 'displayName'))

                button(inheritanceValidationAction)
            }
        }

        panel(name: 'Validation in Dialog') {
            button(startDialogAction)
        }
    }
}
