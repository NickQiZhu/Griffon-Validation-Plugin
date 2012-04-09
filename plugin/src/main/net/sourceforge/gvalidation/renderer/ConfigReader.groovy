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

import org.apache.commons.lang.StringUtils
import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ConfigReader {
    public static final String DEFAULT_STYLE = 'default'

    private static final String ERROR_FIELD = 'for'
    private static final String STYLES = 'styles'

    private static final Pattern ERROR_FIELD_PATTERN = ~/${ERROR_FIELD}[\s]*:[\s]*(\w+)/
    private static final Pattern STYLE_OPTIONS_PATTERN = ~/${STYLES}[\s]*:[\s]*\[([\w,\s]+)/

    def configMap = [:]

    def ConfigReader(config) {
        if (!config)
            return

        def quotedConfig = removeQuotes(config)

        quotedConfig = quoteErrorField(quotedConfig)

        quotedConfig = quoteStyleOptions(quotedConfig)

        try {
            GroovyShell shell = new GroovyShell()
            configMap = shell.evaluate("[$quotedConfig]")
        } catch (Exception ex) {
            configMap = [:]
        }
    }

    private def removeQuotes(config) {
        config = config.replaceAll("'", '')
        config = config.replaceAll('"', '')
        return config
    }

    private def quoteErrorField(config) {
        def quotedConfig = config

        Matcher errorMatcher = ERROR_FIELD_PATTERN.matcher(config)

        if (errorMatcher.find()) {
            def errorField = errorMatcher.group(1)
            quotedConfig = config.replace(errorField, "'${errorField}'")
        }

        return quotedConfig
    }

    private def quoteStyleOptions(String config) {
        Matcher stylesMatcher = STYLE_OPTIONS_PATTERN.matcher(config)

        def quotedConfig = config

        if (stylesMatcher.find()) {
            def styleOptions = stylesMatcher.group(1)
            def quotedStyleOptions = ""

            styleOptions.split(',').each {
                quotedStyleOptions += "'${it.trim()}',"
            }

            quotedStyleOptions = StringUtils.removeEnd(quotedStyleOptions, ",")

            quotedConfig = config.replace(styleOptions, quotedStyleOptions)
        }

        return quotedConfig
    }

    def getErrorField() {
        return configMap[ERROR_FIELD]
    }

    def getRenderStyles() {
        def styles = configMap[STYLES]

        if (!styles)
            styles = [DEFAULT_STYLE]

        return styles
    }

    boolean isConfigured() {
        return configMap[ERROR_FIELD]
    }
}
