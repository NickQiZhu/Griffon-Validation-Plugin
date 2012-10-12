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

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRendererAttributeDelegator {
    private static final String ERROR_RENDERER_ATTRIBUTE_NAME = 'errorRenderer'

    protected ErrorRenderer errorRenderer = new ErrorRenderer()

    public boolean isAttributeSet(Map attributes) {
        return attributes.get(ERROR_RENDERER_ATTRIBUTE_NAME) != null
    }

    def delegate(app, builder, node, attributes) {
        def attribute = attributes.remove(ERROR_RENDERER_ATTRIBUTE_NAME)

        ConfigReader reader = new ConfigReader(attribute)

        if (reader.isConfigured()) {
            def model = builder.model

            errorRenderer.register(
                    model,
                    node,
                    reader.getRenderStyles(),
                    reader.getErrorField(),
                    app
            )
        }
    }
}
