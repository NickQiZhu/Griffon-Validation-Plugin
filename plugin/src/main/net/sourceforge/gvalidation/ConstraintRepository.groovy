/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation

import net.sourceforge.gvalidation.validator.ClosureValidator
import net.sourceforge.gvalidation.validator.InetAddressValidator
import net.sourceforge.gvalidation.validator.UrlValidator
import net.sourceforge.gvalidation.validator.SizeValidator
import net.sourceforge.gvalidation.validator.RangeValidator
import net.sourceforge.gvalidation.validator.NotEqualValidator
import net.sourceforge.gvalidation.validator.MinSizeValidator
import net.sourceforge.gvalidation.validator.MinValidator
import net.sourceforge.gvalidation.validator.MaxSizeValidator
import net.sourceforge.gvalidation.validator.MaxValidator
import net.sourceforge.gvalidation.validator.MatchesValidator
import net.sourceforge.gvalidation.validator.InListValidator
import net.sourceforge.gvalidation.validator.CreditCardValidator
import net.sourceforge.gvalidation.validator.EmailValidator
import net.sourceforge.gvalidation.validator.BlankValidator
import net.sourceforge.gvalidation.validator.NullableValidator
import griffon.core.GriffonApplication

/**
 * Repository class for managing and configuring both built-in and custom constraints
 *
 * @since 0.3
 * @author nick.zhu
 */
@Singleton
class ConstraintRepository {
    private def validators = [
            nullable: new NullableValidator(),
            blank: new BlankValidator(),
            email: new EmailValidator(),
            creditCard: new CreditCardValidator(),
            inList: new InListValidator(),
            matches: new MatchesValidator(),
            max: new MaxValidator(),
            maxSize: new MaxSizeValidator(),
            min: new MinValidator(),
            minSize: new MinSizeValidator(),
            notEqual: new NotEqualValidator(),
            range: new RangeValidator(),
            size: new SizeValidator(),
            url: new UrlValidator(),
            inetAddress: new InetAddressValidator(),
            validator: new ClosureValidator()
    ]

    def initialize(app) {
        app.artifactManager.constraintClasses.each { artifactInfo ->
            def constraintName = artifactInfo.logicalPropertyName
            register(constraintName, artifactInfo.newInstance())
        }
    }

    def getValidators() {
        def results = [:]

        results.putAll validators

        return Collections.unmodifiableMap(results)
    }

    def getValidator(name) {
        return validators[name]
    }

    def register(name, constraint) {
        validators.put(name, constraint)
    }

    def containsConstraint(name) {
        return validators.containsKey(name)
    }
}
