/*
 * Copyright 2010 the original author or authors.
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

package net.sourceforge.gvalidation.models

import net.sourceforge.gvalidation.annotation.Validatable
import groovy.beans.Bindable
import org.codehaus.griffon.runtime.core.AbstractGriffonModel

/**
 * Created by nick.zhu
 */
@Validatable(realTime=true)
class AnnotatedModel extends AbstractGriffonModel {
    @Bindable String id
    @Bindable String email = " "
    @Bindable String zipCode = " "
    @Bindable Integer code

    static constraints = {
        id(nullable: false, blank: false)
        email(email: true)
        zipCode(nullable: true, blank: true, minSize: 6)
        code(min:10, max:99)
    }
}
