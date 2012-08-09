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

import net.sourceforge.gvalidation.validator.MinSizeValidator

/**
 * Created by nick.zhu
 */

class MinSizeValidatorTest extends GroovyTestCase {
private     MinSizeValidator minSize = new MinSizeValidator()

    public void testMinSizeValidation() {
        assertTrue("Should be valid", (boolean) minSize.validate("blahblah", this, null))
        assertTrue("Should ignore null", (boolean) minSize.validate(null, this, 5))

        assertTrue("Should be valid", (boolean) minSize.validate("something long", this, 5))
        assertFalse("Should not be valid", (boolean) minSize.validate("foo", this, 5))

        assertTrue("Should be valid", (boolean) minSize.validate(['a', 'b', 'c'], this, 2))
        assertFalse("Should not be valid", (boolean) minSize.validate(['a', 'b'], this, 3))
    }

    public void testMinSizeValidationWithZero(){
        assertTrue("Should be valid", (boolean) minSize.validate(['a', 'b', 'c'], this, 0))
        assertTrue("Should be valid", (boolean) minSize.validate([], this, 0))
        assertFalse("Should not be valid", (boolean) minSize.validate([], this, 1))
    }

    public void testNullTolerance(){
        assertTrue("Should be valid", minSize.validate(null, this, 10))
    }

    public void testBlankTolerance(){
        assertTrue("Should be valid", minSize.validate('', this, 10))
    }

}