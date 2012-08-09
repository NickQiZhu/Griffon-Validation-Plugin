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

import net.sourceforge.gvalidation.validator.MaxSizeValidator

/**
 * Created by nick.zhu
 */

class MaxSizeValidatorTest extends GroovyTestCase {
private    MaxSizeValidator maxSize = new MaxSizeValidator()

    public void testMaxSizeValidation() {
        assertFalse("Should not be valid", (boolean) maxSize.validate("blahblah", this, null))
        assertTrue("Should be valid, since null is considered 0 length", (boolean) maxSize.validate(null, this, 5))

        assertFalse("Should not be valid", (boolean) maxSize.validate("something long", this, 5))
        assertTrue("Should be valid", (boolean) maxSize.validate("valid", this, 5))

        assertFalse("Should not be valid", (boolean) maxSize.validate(['a', 'b', 'c'], this, 2))
        assertTrue("Should be valid", (boolean) maxSize.validate(['a', 'b'], this, 3))
    }

    public void testMaxSizeZero(){
        assertFalse("Should not be valid", (boolean) maxSize.validate(['a', 'b', 'c'], this, 0))
        assertTrue("Should be valid", (boolean) maxSize.validate([], this, 0))
    }

}