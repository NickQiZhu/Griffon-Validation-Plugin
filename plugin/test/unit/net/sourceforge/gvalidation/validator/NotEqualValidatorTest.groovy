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

import net.sourceforge.gvalidation.validator.NotEqualValidator

/**
 * Created by nick.zhu
 */
class NotEqualValidatorTest extends GroovyTestCase {

    public void testNotEqual(){
        NotEqualValidator notEqual = new NotEqualValidator()

        assertTrue("Should be valid", notEqual.validate("bob", this, "alice"))
        assertFalse("Should be valid", notEqual.validate("bob", this, "bob"))
        assertFalse("Should be valid", notEqual.validate(10, this, 10))
        assertTrue("Should be valid", notEqual.validate(9, this, 10))
        assertTrue("Should be valid", notEqual.validate(9, this, "bob"))
    }

}
