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

package net.sourceforge.gvalidation.artifact

import org.junit.Test
import static org.junit.Assert.*
import griffon.core.GriffonApplication

/**
 * @author Nick Zhu
 */
class ConstraintArtifactHandlerTest {

    @Test
    public void ensureArtifactClassCreation() {
        def artifactHandler = new ConstraintArtifactHandler([] as GriffonApplication)

        def artifactClass = artifactHandler.newGriffonClassInstance(this.getClass())

        assertNotNull("Artifact class can not be null", artifactClass)
        assertTrue("Artifact class type is incorrect", artifactClass instanceof GriffonConstraintClass)
    }

}