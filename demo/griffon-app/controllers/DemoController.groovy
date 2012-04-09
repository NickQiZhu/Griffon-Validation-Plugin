import griffon.core.ArtifactManager
import java.beans.PropertyChangeListener

class DemoController {
    // these will be injected by Griffon
    def model
    def view

    def serverParameter = new ServerParameter()

    def protocolSpecificServerParameter = new ProtocolSpecificServerParameter()

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
    }

    def submit = {evt = null ->
        model.errors.clear()
        if (model.validate()) {
            doOutside {
                // do something interesting
            }
        }
    }

    def validateField = {evt = null ->
        model.errors.clear()
        if (model.validate(model.fieldToValidate)) {
            doOutside {
                // do something interesting
            }
        }
    }

    def validateServerParameter = {evt = null ->
        if (serverParameter.validate()) {
            doOutside {
                // do something interesting
            }
        }
    }

     def validateProtocolSpecificServerParameter = {evt = null ->
        if (protocolSpecificServerParameter.validate()) {
            doOutside {
                // do something interesting
            }
        }
    }

    def startDialog = {evt = null ->
        doLater{
            createMVCGroup('Dialog', ['parentWindow':view.window])
        }
    }

    def onDebug = {args ->
        println args
    }
}