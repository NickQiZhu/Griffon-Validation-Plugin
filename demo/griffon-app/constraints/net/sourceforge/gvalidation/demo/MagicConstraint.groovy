package net.sourceforge.gvalidation.demo

class MagicConstraint {

    def validate(propertyValue, bean, parameter) {
        if (!parameter)
            return true

        return propertyValue < 0 || propertyValue == 42
    }

}