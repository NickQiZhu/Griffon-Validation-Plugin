import net.sourceforge.gvalidation.validator.Validator

@artifact.package@class @artifact.name@ implements Validator {

    /**
     * Generated message
     *
     * @param propertyValue value of the property to be validated
     * @param bean object owner of the property
     * @param parameter configuration parameter for the constraint
     * @return return true if validation passes otherwise false
     */
    def validate(propertyValue, bean, parameter) {
        // insert your custom constraint logic
    }

}