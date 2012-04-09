import groovy.beans.Bindable
import net.sourceforge.gvalidation.annotation.Validatable

@Validatable(realTime=true)
class DemoModel {
    @Bindable String name
    @Bindable String email
    @Bindable String webSite
    @Bindable String creditCard
    @Bindable String postalCode
    @Bindable String password
    @Bindable String passwordRepeat
    @Bindable int orderAmount
    @Bindable int secondOrderAmount
    @Bindable int magicNumber

    @Bindable String fieldToValidate

    static constraints = {
        name(blank: false, minSize: 3)
        email(blank: false, email: true)
        webSite(blank: true, url: true)
        creditCard(blank: false, creditCard: true)
        postalCode(blank: false, minSize: 7, matches:/[a-zA-Z]\d[a-zA-Z] \d[a-zA-Z]\d/)
        password(blank: false, size: 3..15)
        passwordRepeat(blank: false, validator:{val, model-> val==model.password})
        orderAmount(nullable: false, min: 1, max: 20)
        secondOrderAmount(nullable: false, range: 1..10)
        magicNumber(magic: true)
    }
}