package demo

import groovy.beans.Bindable
import net.sourceforge.gvalidation.annotation.Validatable

@Validatable
class DialogModel {
    @Bindable String username
    @Bindable String password

    static def constraints = {
        username(blank: false, minSize: 4)
        password(blank: false, minSize: 4, maxSize: 8)
    }
}