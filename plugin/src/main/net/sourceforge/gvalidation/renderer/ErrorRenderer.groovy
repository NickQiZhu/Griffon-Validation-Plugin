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

import net.sourceforge.gvalidation.util.MetaUtils

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRenderer {

    static final Class<? extends ErrorDecorator> DEFAULT_DECORATOR_CLASS = HighlightErrorDecorator.class
    static Map<String, ErrorDecorator> decoratorClassMap = [
            'default': DEFAULT_DECORATOR_CLASS,
            'highlight': HighlightErrorDecorator.class,
            'onWithError': OnWithErrorDecorator.class,
            'popup' : PopupErrorDecorator.class
    ]

    def register(model, node, styles, errorField, messageSource) {
        if(notValidatable(model))
            return []

        if (!styles)
            styles = [ConfigReader.DEFAULT_STYLE]

        def decorators = []

        styles.each { style ->
            Class<ErrorDecorator> decoratorClass = decoratorClassMap[style]

            if (!decoratorClass)
                decoratorClass = DEFAULT_DECORATOR_CLASS

            ErrorDecorator decorator = decoratorClass.newInstance()
            decorator.register(model, node, errorField, messageSource)
            decorators << decorator
        }

        return decorators
    }

    private boolean notValidatable(model) {
        return !MetaUtils.fieldExistOnTarget(model, 'errors')
    }

}
