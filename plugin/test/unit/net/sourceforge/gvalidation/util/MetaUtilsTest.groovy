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

import net.sourceforge.gvalidation.util.MetaUtils

/**
 * Created by nick.zhu
 */
class MetaUtilsTest extends GroovyTestCase {

    def fieldClass = new FieldClass()
    def methodClass = new MethodClass()
    def closureClass = new ClosureClass()

    public void testFindClosure() {
        assertTrue("Should be true", MetaUtils.hasClosure(closureClass, 'doSomething'))
        assertFalse("Should be false", MetaUtils.hasClosure(methodClass, 'doSomethingElse'))
        assertFalse("Should be false", MetaUtils.hasClosure([:], 'doSomethingElse'))
    }

    public void testFindMethod() {
        assertTrue("Should be true", MetaUtils.hasMethod(methodClass, 'doSomething'))
        assertFalse("Should be false", MetaUtils.hasMethod(closureClass, 'doSomethingElse'))
        assertFalse("Should be false", MetaUtils.hasMethod([:], 'doSomethingElse'))
    }

    public void testFindAnyMethod() {
        assertTrue("Should be true", MetaUtils.hasMethodOrClosure(methodClass, 'doSomething'))
        assertFalse("Should be false", MetaUtils.hasMethodOrClosure(methodClass, 'doSomethingElse'))
        assertTrue("Should be true", MetaUtils.hasMethodOrClosure(closureClass, 'doSomething'))
        assertFalse("Should be false", MetaUtils.hasMethodOrClosure(closureClass, 'doSomethingElse'))
        assertFalse("Should be false", MetaUtils.hasMethodOrClosure([:], 'doSomethingElse'))
    }

    public void testFieldExistence(){
        assertTrue("Field should exist", MetaUtils.fieldExistOnTarget(fieldClass, 'errors'))
        assertFalse("Field should not exist", MetaUtils.fieldExistOnTarget(fieldClass, 'notFound'))
        assertTrue("Field should exist", MetaUtils.fieldExistOnTarget([errors:[]], 'errors'))
        assertFalse("Field should not exist", MetaUtils.fieldExistOnTarget([errors:[]], 'notFound'))
        assertFalse("Should ignore null", MetaUtils.fieldExistOnTarget(null, 'notFound'))
        assertFalse("Should ignore null", MetaUtils.fieldExistOnTarget(fieldClass, null))
    }

    class FieldClass {
        def errors
    }

    class MethodClass {
        boolean done

        def doSomething() {
            done = true
        }
    }

    class ClosureClass {
        boolean done

        def doSomething = {
            done = true
        }
    }
}

