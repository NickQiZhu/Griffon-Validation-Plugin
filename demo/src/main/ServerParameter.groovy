import groovy.beans.Bindable
import net.sourceforge.gvalidation.annotation.Validatable

/**
 * Created by nick.zhu
 */
@Validatable
class ServerParameter {
    @Bindable String serverName
    @Bindable int port
    @Bindable String displayName

    def beforeValidation = {
        setDisplayName "${serverName}:${port}"
    }

    static constraints = {
        serverName(nullable: false, blank: false)
        port(range: 0..65535)
        displayName(nullable: false, blank: false)
    }
}
