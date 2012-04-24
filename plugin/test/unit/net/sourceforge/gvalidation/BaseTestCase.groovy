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

package net.sourceforge.gvalidation

import net.sourceforge.gvalidation.annotation.ValidatableASTTransformation
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
abstract class BaseTestCase extends GroovyTestCase {

    def generateModel(fileName = "AnnotatedModel.groovy") {
        def file = new File("test/unit/net/sourceforge/gvalidation/models/${fileName}")
        assert file.exists()

        TransformTestHelper invoker = new TransformTestHelper(new ValidatableASTTransformation(), CompilePhase.PARSING)
        def modelClass = invoker.parse(file)
        def model = modelClass.newInstance()

        return model
    }

    def pause() {
        Thread.sleep(300)
    }

    def longPause() {
        Thread.sleep(5000)
    }

}
