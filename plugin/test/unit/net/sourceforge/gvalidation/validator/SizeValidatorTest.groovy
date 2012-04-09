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

import net.sourceforge.gvalidation.validator.SizeValidator

/**
 * Created by nick.zhu
 */
class SizeValidatorTest extends GroovyTestCase {

    public void testSizeValidation() {
        SizeValidator size = new SizeValidator()

        assertTrue("Should be valid", size.validate("hello", this, 3..10))
        assertTrue("Should be valid", size.validate(['a', 'b'], this, 1..3))

        assertFalse("Should not be valid", size.validate("hello", this, 3..4))
        assertFalse("Should not be valid", size.validate(['a', 'b', 'c', 'd'], this, 1..3))

        assertFalse("Should not be valid", size.validate(['a', 'b', 'c', 'd'], this, 3))
    }

}
