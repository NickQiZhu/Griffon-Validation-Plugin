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

import net.sourceforge.gvalidation.validator.InListValidator

/**
 * Created by nick.zhu
 */

class InListValidatorTest extends GroovyTestCase {

    public void testInListlValidation(){
        InListValidator inList = new InListValidator()

        def names = ['Joe', 'John', 'Bob']

        assertFalse("Should not be valid", (boolean) inList.validate("blahblah", this, null))
        assertFalse("Should not be in the list", (boolean) inList.validate("blahblah", this, names))
        assertTrue("Should ignore null", (boolean) inList.validate(null, this, names))
        assertTrue("Should be in the list", (boolean) inList.validate("Bob", this, names))
    }

}