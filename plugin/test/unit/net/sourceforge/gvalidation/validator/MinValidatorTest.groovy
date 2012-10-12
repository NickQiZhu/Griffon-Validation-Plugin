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

import net.sourceforge.gvalidation.validator.MinValidator

/**
 * Created by nick.zhu
 */

class MinValidatorTest extends GroovyTestCase {
    private MinValidator min = new MinValidator()

    public void testMinValidation() {
        checkNulls(min)

        validateWithNumbers(min)

        def now = new Date()

        validateWithDates(min, now)

        tryWithDifferentTypes(min, now)
    }

    private def tryWithDifferentTypes(min, Date now) {
        assertFalse("Should not be valid", (boolean) min.validate("today", this, now))
    }

    private def validateWithDates(min, Date now) {
        assertFalse("Should not be valid", (boolean) min.validate(now - 1, this, now))
        assertTrue("Should be valid", (boolean) min.validate(now + 1, this, now))
    }

    private def validateWithNumbers(min) {
        assertFalse("Should not be valid", (boolean) min.validate(70, this, 80))
        assertTrue("Should be valid", (boolean) min.validate(98, this, 80))
    }

    private def checkNulls(min) {
        assertTrue("Should not be valid", (boolean) min.validate("blahblah", this, null))
        assertTrue("Should ignore null", (boolean) min.validate(null, this, 80))
    }

    public void testBlanks() {
        assertTrue("Should be valid", (boolean) min.validate("", this, 1))
    }

    // bug #2984137
    public void testLessThanMin() {
        assertFalse("Should not be valid", (boolean) min.validate(0, this, 1))
    }

    public void testZero() {
        assertTrue("1 should be > 0", (boolean) min.validate(1, this, 0))
        assertFalse("-1 should be < 0", (boolean) min.validate(-1, this, 0))
    }

    public void testOne() {
        assertTrue("1 should be >= 1", (boolean) min.validate(1, this, 1))
    }

}