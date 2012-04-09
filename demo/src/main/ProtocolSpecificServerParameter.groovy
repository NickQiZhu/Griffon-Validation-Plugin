import net.sourceforge.gvalidation.annotation.Validatable
import groovy.beans.Bindable

/**
 * Created by nick.zhu
 */
@Validatable
class ProtocolSpecificServerParameter extends ServerParameter{
    @Bindable String protocol

    def beforeValidation = {
        setDisplayName "${protocol}://${serverName}:${port}"
    }

    static constraints = {
        protocol(blank: false, nullable: false)
    }
}
