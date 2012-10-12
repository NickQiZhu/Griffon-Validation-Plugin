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

import net.sourceforge.gvalidation.validator.MaxValidator

/**
 * Created by nick.zhu
 */

class MaxValidatorTest extends GroovyTestCase {

    private MaxValidator max = new MaxValidator()

    public void testMaxlValidation() {
        checkNulls(max)

        validateWithNumbers(max)

        def now = new Date()

        validateWithDates(max, now)

        tryWithDifferentTypes(max, now)
    }

    private def tryWithDifferentTypes(MaxValidator max, Date now) {
        assertFalse("Should not be valid", (boolean) max.validate("today", this, now))
    }

    private def validateWithDates(MaxValidator max, Date now) {
        assertFalse("Should not be valid", (boolean) max.validate(now + 1, this, now))
        assertTrue("Should be valid", (boolean) max.validate(now - 1, this, now))
    }

    private def validateWithNumbers(MaxValidator max) {
        assertFalse("Should not be valid", (boolean) max.validate(90, this, 80))
        assertTrue("Should be valid", (boolean) max.validate(78, this, 80))
    }

    private def checkNulls(MaxValidator max) {
        assertFalse("Should not be valid", (boolean) max.validate("blahblah", this, null))
        assertTrue("Should be valid", (boolean) max.validate(null, this, 80))
    }

    public void testZero(){
        assertFalse("2 should be > 0", max.validate(2, this, 0))
        assertTrue("-2 should be < 0", max.validate(-2, this, 0))
    }

    public void testOne(){
        assertTrue("1 should be >= 1", max.validate(1, this, 1))
    }

}