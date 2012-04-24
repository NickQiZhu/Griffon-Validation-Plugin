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

import net.sourceforge.gvalidation.validator.ClosureValidator
import net.sourceforge.gvalidation.ValidationEnhancer
import net.sourceforge.gvalidation.models.ModelBean
import net.sourceforge.gvalidation.BaseTestCase
import net.sourceforge.gvalidation.models.AnnotatedModel

/**
 * Created by nick.zhu
 */
class ClosureValidatorTest extends BaseTestCase {

    public void testBasicClosureBasedValidation(){
        ClosureValidator validator = new ClosureValidator()

        assertTrue "Should be valid", validator.validate(null, this, {value, obj -> true})
        assertFalse "Should not be valid", validator.validate(null, this, {value, obj -> false})
    }
    
    public void testErrorGeneration(){
        def obj = new AnnotatedModel()

        ClosureValidator validator = new ClosureValidator()

        assertFalse "Should be valid", validator.validate(null, obj, {value, bean -> bean.errors.reject('error'); false})

        assertTrue "Error should be generated", obj.hasErrors()
    }
    
}
