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

package net.sourceforge.gvalidation.swing

import javax.swing.ImageIcon

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class Icons {
    public static final ImageIcon ERROR_ICON = new ImageIcon(Icons.classLoader.getResource('net/sourceforge/gvalidation/error.png'))
    public static final ImageIcon SUCCESS_ICON = new ImageIcon(Icons.classLoader.getResource('net/sourceforge/gvalidation/success.png'))
    public static final ImageIcon CLOSE_ICON = new ImageIcon(Icons.classLoader.getResource('net/sourceforge/gvalidation/close.png'))
}
