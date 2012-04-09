/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.renderer

import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.BaseTestCase

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class OnWithErrorDecoratorTest extends BaseTestCase{

    public void testTurnVisible(){
        OnWithErrorDecorator decorator = new OnWithErrorDecorator()

        def errorField = "email"
        def errors = new Errors()
        errors.rejectValue(errorField, 'emailErrorCode')

        def model = [errors:errors]
        def node = [visible:false]
        def messageResource = [:]

        decorator.register(model, node, errorField, messageResource)

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()

        assert node.visible == true
    }

    public void testTurnOffVisible(){
        OnWithErrorDecorator decorator = new OnWithErrorDecorator()

        def errorField = "email"
        def errors = new Errors()
        errors.rejectValue(errorField, 'emailErrorCode')

        def model = [errors:errors]
        def node = [visible:true]
        def messageResource = [:]

        decorator.register(model, node, errorField, messageResource)

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()
        decorator.undecorate()
        pause()

        assert node.visible == false
    }

}
