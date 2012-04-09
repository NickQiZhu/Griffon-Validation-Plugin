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

package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.MatchesValidator

/**
 * Created by nick.zhu
 */

class MatchesValidatorTest extends GroovyTestCase {

    public void testMatchesValidation(){
        MatchesValidator matches = new MatchesValidator()

        def regex = /[a-zA-Z]+/
        
        assertFalse("Should not be valid", (boolean) matches.validate("blahblah", this, null))
        assertFalse("Should not be valid", (boolean) matches.validate("789abd", this, regex))
        assertTrue("Should be valid", (boolean) matches.validate("somethingValid", this, regex))
        assertTrue("Should ignore null", (boolean) matches.validate(null, this, regex))        
    }

}