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

import org.springframework.context.MessageSource
import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.models.PlainModelBean
import net.sourceforge.gvalidation.models.ModelBean
import net.sourceforge.gvalidation.BaseTestCase

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRendererTest extends BaseTestCase {

    public void testIgnoreNotAnnotatedModel(){
        ErrorRenderer.decoratorClassMap = [
                highlight: MockHighlightErrorDecorator.class,
                popup: MockPopupErrorDecorator.class
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def model = new PlainModelBean()
        def node = [:]
        def styles = ['highlight']
        def errorField = "email"
        def messageSource = [:] as MessageSource

        def results = renderer.register(model, node, styles, errorField, messageSource)

        assert results.size() == 0
    }

    public void testInvokeDecoratorByStyleName(){
        ErrorRenderer.decoratorClassMap = [
                highlight: MockHighlightErrorDecorator.class,
                popup: MockPopupErrorDecorator.class
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def model = new ModelBean()
        def node = [:]
        def styles = ['highlight']
        def errorField = "email"
        def messageSource = [:] as MessageSource

        def results = renderer.register(model, node, styles, errorField, messageSource)

        assert results.size() == 1
        assert results[0] instanceof MockHighlightErrorDecorator
        assert results[0].isRegistered() == true
        assert results[0].getModel() == model
        assert results[0].getErrorField() == errorField
        assert results[0].targetComponent == node
        assert results[0].messageSource == messageSource
    }

    public void testInvokeDefaultDecoratorIfStyleIsOmitted(){
        ErrorRenderer.decoratorClassMap = [
                'default': MockDefaultErrorDecorator.class,
                highlight: MockHighlightErrorDecorator.class,
                popup: MockPopupErrorDecorator.class
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def node = [:]
        def model = [errors:new Errors()]
        def styles = []
        def errorField = "email"
        def messageSource = [:] as MessageSource

        def results = renderer.register(model, node, styles, errorField, messageSource)

        assert results.size() == 1
        assert results[0] instanceof MockDefaultErrorDecorator
        assert results[0].isRegistered() == true
    }

    public void testInvokeDefaultDecoratorIfStyleIsUnkown(){
        ErrorRenderer.decoratorClassMap = [
                'default': MockDefaultErrorDecorator.class,
                highlight: MockHighlightErrorDecorator.class,
                popup: MockPopupErrorDecorator.class
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def node = [:]
        def model = [errors:new Errors()]
        def styles = ['unknown']
        def errorField = "email"
        def messageSource = [:] as MessageSource

        def results = renderer.register(model, node, styles, errorField, messageSource)

        assert results.size() == 1
        assert results[0] instanceof HighlightErrorDecorator
        assert results[0].isRegistered() == true
    }

    public class MockHighlightErrorDecorator extends ClosureErrorDecorator {}

    public class MockPopupErrorDecorator extends ClosureErrorDecorator {}

    public class MockDefaultErrorDecorator extends ClosureErrorDecorator {}
}
