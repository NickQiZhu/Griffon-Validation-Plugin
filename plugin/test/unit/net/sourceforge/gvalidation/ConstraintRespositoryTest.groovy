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

package net.sourceforge.gvalidation

/**
 * Created by nick.zhu
 */
class ConstraintRespositoryTest extends GroovyTestCase {

    public void testBuiltInConstraint(){
        ConstraintRepository repo = ConstraintRepository.instance

        assertNotNull "Builtin constraint nullable not found", repo.getValidators().nullable
        assertTrue "Builtin constraint email not found", repo.getValidators().containsKey('email')
    }

    public void testCustomConstraintRegistration(){
        ConstraintRepository repo = ConstraintRepository.instance

        repo.register('custom', new TestConstraint())

        assertNotNull "Custom constraint not found", repo.getValidators().custom        
    }

    public void testInitialization(){
        ConstraintRepository repo = ConstraintRepository.instance

        def artifactInfos = [
                [logicalPropertyName:'custom', newInstance:{new TestConstraint()}],
                [logicalPropertyName:'magic', newInstance:{new TestConstraint()}]
        ]
        
        def app = [artifactManager: [constraintClasses:artifactInfos]]

        repo.initialize(app)

        assertTrue "Custom constraint not registered", repo.containsConstraint('custom')
        assertTrue "Magic constraint not registered", repo.containsConstraint('magic')
    }

}

class TestConstraint {
    // empty test constraint
}
