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

package net.sourceforge.gvalidation.util

/**
 * Created by nick.zhu
 */
final class MetaUtils {
    private def MetaUtils() {}

    static boolean hasClosure(obj, closureName) {
        if (obj?.hasProperty(closureName) && obj?."${closureName}" instanceof Closure)
            return true
        else
            return false
    }


    static boolean hasMethod(obj, methodName) {
        def method = obj?.metaClass?.methods?.find {
            it.name == methodName
        }

        if (method)
            return true
        else
            return false
    }

    static boolean hasMethodOrClosure(obj, name) {
        return hasMethod(obj, name) || hasClosure(obj, name)
    }

    static boolean fieldExistOnTarget(target, name) {
        if(target == null || name == null)
            return false

        if(target instanceof Map)
            return target.containsKey(name)

        return target.metaClass.properties.find {it.name == name} != null
    }
}
