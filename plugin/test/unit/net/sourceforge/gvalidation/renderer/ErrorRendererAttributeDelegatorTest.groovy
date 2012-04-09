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

import net.sourceforge.gvalidation.BaseTestCase
import net.sourceforge.gvalidation.Errors
import org.springframework.context.MessageSource

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRendererAttributeDelegatorTest extends BaseTestCase {

    public void testErrorRendererAttrDetection(){
        def hasAttributes = [new ErrorRendererAttributeDelegator().isAttributeSet(['errorRenderer': 'something']),
                new ErrorRendererAttributeDelegator().isAttributeSet(['errorRenderer': null])]
        def expectedResults = [true, false]

        for(int i = 0; i < hasAttributes.size(); ++i){
            def hasAttribute = hasAttributes[i]
            def expectedResult = expectedResults[i]

            assert hasAttribute == expectedResult
        }
    }

    public void testAttributesRemovedDuringChecking(){
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()
        def attributes = ['errorRenderer': 'something']

        assert delegator.isAttributeSet(attributes) == true
        assert delegator.isAttributeSet(attributes) == true
    }

    public void testInvalidErrorConfig(){
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()

        def errors = new Errors()

        def builder = [model: [errors: errors]]
        def node = [:]
        def attributes = ['errorRenderer': 'incorrect config']
        def outcome = [:]
        def app = [messageSource: [] as MessageSource]

        delegator.errorRenderer = [register: {n, styles ->
            outcome['invoked'] = true
            outcome['styles'] = styles
        }] as ErrorRenderer

        delegator.delegate(app, builder, node, attributes)

        assert outcome['invoked'] == null
    }

    public void testValidConfig(){
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()

        def errors = new Errors()

        def builder = [model: [errors: errors]]
        def node = [:]
        def attributes = ['errorRenderer': 'for: email']
        def outcome = [:]
        def app = [messageSource: [] as MessageSource]

        delegator.errorRenderer = [register: {m, n, styles, errorField, messageSource ->
            outcome['invoked'] = true
            outcome['styles'] = styles
            outcome['errorField'] = errorField
        }] as ErrorRenderer

        delegator.delegate(app, builder, node, attributes)

        assert outcome['invoked'] == true
        assert outcome['styles'] == ['default']
        assert outcome['errorField'] == 'email'
    }

}
