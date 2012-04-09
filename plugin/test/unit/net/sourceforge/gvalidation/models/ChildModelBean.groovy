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

/**
 * Created by nick.zhu
 */
@Validatable
class ChildModelBean extends AnnotatedModel {
    @Bindable String name

    static constraints = {
        name(nullable: false)
        email(email: true, blank: false)
        zipCode(nullable: false, blank: false)
    }
}
