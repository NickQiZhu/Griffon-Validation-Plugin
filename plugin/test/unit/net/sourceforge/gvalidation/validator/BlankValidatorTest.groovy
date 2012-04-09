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

import net.sourceforge.gvalidation.validator.BlankValidator

/**
 * Created by nick.zhu
 */
class BlankValidatorTest extends GroovyTestCase {

    public void testBlankValidation() {
        BlankValidator blank = new BlankValidator()

        assertTrue("Should allow blank", (boolean) blank.validate("", this, true))
        assertTrue("Should allow null", (boolean) blank.validate(null, this, true))
        assertFalse("Should not allow null", (boolean) blank.validate(null, this, false))
        assertFalse("Should not allow blank", (boolean) blank.validate("", this, false))
        assertFalse("Should not allow blank", (boolean) blank.validate(" ", this, false))
        assertTrue("Should be successful", (boolean) blank.validate(" something ", this, false))
    }

    // bug #2983285
    public void testValidatingNonStrings() {
        BlankValidator blank = new BlankValidator()

        assertTrue("Should allow blank", (boolean) blank.validate(10, this, true))
        assertTrue("Should not allow blank", (boolean) blank.validate(10, this, false))
    }

}
