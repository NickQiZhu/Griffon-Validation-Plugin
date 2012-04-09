package demo

actions {
    action(id: 'loginAction',
            name: 'Login',
            closure: controller.login
    )

    action(id: 'cancelAction',
            name: 'Cancel',
            closure: controller.cancel
    )

    action(id: 'closeAction',
            name: 'Close',
            closure: controller.close
    )
}

dialog = dialog(modal: true,
        title: "Demo Dialog",
        locationRelativeTo: controller.parentWindow,
        minimumSize: [400, 150]) {
    migLayout(layoutConstraints: 'align center', columnConstraints: '[right] 20 [left] 20 [left]')

    label("User Name: * ")
    textField(columns: 32, text: bind('username', source: model, mutual: true),
            errorRenderer: 'for: username, styles: [highlight, popup]',
            constraints: 'growx, wrap')

    label("Password: * ")
    passwordField(columns: 32, text: bind('password', source: model, mutual: true),
            errorRenderer: 'for: password, styles: [highlight, popup]',
            constraints: 'growx, wrap')

    button(loginAction)
    button(cancelAction)
    button(closeAction, constraints: 'wrap')

}

