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

import net.sourceforge.gvalidation.validator.RangeValidator

/**
 * Created by nick.zhu
 */
class RangeValidatorTest extends GroovyTestCase {
    private RangeValidator range = new RangeValidator()

    public void testRangeValidation() {
        assertTrue("Should be valid", range.validate(5, this, 0..10))
        assertFalse("Should not be valid", range.validate(5, this, 0..<5))

        def now = new Date()
        assertTrue("Should be valid", range.validate(now + 5, this, now..now + 10))
        assertFalse("Should not be valid", range.validate(now + 5, this, now..now + 3))

        assertFalse("Should not be valid", range.validate(now, this, now))
    }

    // tracker bug #2983281
    public void testRangeValidationWithInt() {
        assertTrue("Should be valid", range.validate(5, this, 1..65535))
        assertFalse("Should not be valid", range.validate(-5, this, 1..65535))
    }

    public void testRangeWithZero() {
        assertFalse("Should not be valid", range.validate(0, this, 1..10))
        assertFalse("Should not be valid", range.validate(0, this, -10..<0))
        assertTrue("Should be valid", range.validate(0, this, -10..0))
    }

    public void testNullTolerance(){
        assertTrue("Should be valid", range.validate(null, this, 1..10))
    }

    public void testBlankTolerance(){
        assertTrue("Should be valid", range.validate('', this, 1..10))
    }

    public void testMinOne(){
        assertTrue("Should be valid", range.validate(1, this, 1..10))
    }

}
