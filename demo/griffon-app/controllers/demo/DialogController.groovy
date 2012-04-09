package demo

class DialogController {
    def model
    def view
    def parentWindow

    void mvcGroupInit(Map args) {
        parentWindow = args.parentWindow
        doLater{
            view.dialog.show()
        }
    }

    def login = { evt = null ->
        model.validate()
    }

    def cancel = {evt = null ->
        model.username = ''
        model.password = ''
    }

    def close = { evt = null ->
        destroyMVCGroup('Dialog')
    }
}
