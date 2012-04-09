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
import java.awt.Color
import javax.swing.JComponent
import javax.swing.JLabel
import net.sourceforge.gvalidation.BaseTestCase

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class HighlightErrorDecoratorTest extends BaseTestCase {

    public void testBgColorChange(){
        HighlightErrorDecorator decorator = new HighlightErrorDecorator()

        def errorField = "email"
        def errors = new Errors()

        def model = [errors: errors]
        def color = Color.WHITE
        def node = [getBackground: {color}, setBackground: {c -> color = c}] as JComponent
        def messageResource = [:] as MessageSource


        decorator.register(model, node, errorField, messageResource)
        errors.rejectValue(errorField, 'emailErrorCode')

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()

        assert decorator.originalBgColor == Color.WHITE
        assert node.background == Color.PINK
    }

    public void testRestoreBgColor(){
        HighlightErrorDecorator decorator = new HighlightErrorDecorator()

        def errorField = "email"
        def errors = new Errors()

        def model = [errors: errors]
        def color = Color.WHITE
        def node = [getBackground: {color}, setBackground: {c -> color = c}] as JComponent
        def messageResource = [:] as MessageSource


        decorator.register(model, node, errorField, messageResource)
        errors.rejectValue(errorField, 'emailErrorCode')

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()
        decorator.undecorate()
        pause()

        assert decorator.originalBgColor == Color.WHITE
        assert node.background == Color.WHITE
    }

    public void testRestoreBgColorWithMultiErrors(){
        HighlightErrorDecorator decorator = new HighlightErrorDecorator()

        def errorField = "email"
        def errors = new Errors()

        def model = [errors: errors]
        def node = new JLabel("something")
        def color = node.background
        def messageResource = [:] as MessageSource


        decorator.register(model, node, errorField, messageResource)

        errors.rejectValue(errorField, 'emailErrorCode1')
        errors.rejectValue(errorField, 'emailErrorCode2')

        decorator.decorate(errors, errors.getFieldError(errorField))
        pause()
        decorator.undecorate()
        pause()

        assert decorator.originalBgColor == color
        assert node.background == color
    }

}
