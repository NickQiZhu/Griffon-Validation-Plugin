/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.swing;

import griffon.util.ApplicationHolder;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;

import java.util.Map;

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorMessagePanelFactory extends AbstractFactory {
    public ErrorMessagePanel newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        return new ErrorMessagePanel(ApplicationHolder.getApplication());
    }
}
