/*
 * Copyright 2010-2012 the original author or authors.
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

class ValidationGriffonPlugin {
    // the plugin version
    String version = '1.0.2'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '1.1.0 > *'
    // the other plugins this plugin depends on
    Map dependsOn = [:]
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = 'http://griffon.codehaus.org/Validation+Plugin'
    // URL where source can be found
    String source = 'https://github.com/NickQiZhu/griffon-validation-plugin'

    List authors = [
        [
            name: 'Nick Zhu',
            email: 'nick.qi.zhu@gmail.com'
        ]
    ]
    String title = 'Griffon Validation Plugin'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    String description = '''
A validation plugin that provides Grails like validation capability using constraints
'''
}
