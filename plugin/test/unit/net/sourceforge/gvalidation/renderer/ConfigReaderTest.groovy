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

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ConfigReaderTest extends BaseTestCase {

    public void testDefaultErrorStyleRetrieval() {
        ConfigReader reader = new ConfigReader('for: "email"')

        assert reader.isConfigured() == true
        assert reader.getErrorField() == 'email'
        assert reader.getRenderStyles().size() == 1
        assert reader.getRenderStyles()[0] == 'default'
    }

    public void testParticularErrorStyleRetrieval() {
        ConfigReader reader = new ConfigReader('for: "url", styles: ["highlight", "popup"]')

        assert reader.isConfigured() == true
        assert reader.getErrorField() == 'url'
        assert reader.getRenderStyles().size() == 2
        assert reader.getRenderStyles()[0] == 'highlight'
        assert reader.getRenderStyles()[1] == 'popup'
    }

    public void testSkipEmptyInput() {
        def configs = ['', null, 'styles: [popup]', 'incorrect config']

        configs.each {config ->
            assert new ConfigReader(config).isConfigured() == false
        }
    }

    public void testValidInputs() {
        def configs = [
                'for: "email"',
                'for: email',
                'for: "url", styles: [ highlight, pop ]',
                'styles:[popup],for:url',
                'styles :   [  "popup",   highlight ],    for :url'
        ]

        configs.each {config ->
            assert new ConfigReader(config).isConfigured() == true
        }
    }

    public void testReadWithoutQuotes() {
        ConfigReader reader = new ConfigReader('for: url, styles : [highlight, popup]')

        assert reader.isConfigured() == true
        assert reader.getErrorField() == 'url'
        assert reader.getRenderStyles().size() == 2
        assert reader.getRenderStyles()[0] == 'highlight'
        assert reader.getRenderStyles()[1] == 'popup'
    }

}
