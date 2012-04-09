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

package net.sourceforge.gvalidation.artifact;

import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;
import griffon.core.GriffonClass;
import griffon.core.GriffonApplication;

/**
 * @author Nick Zhu
 */
public class ConstraintArtifactHandler extends ArtifactHandlerAdapter {
    /**
     * Use GriffonConstraintClass.TYPE instead
     *
     * @see GriffonConstraintClass
     */
    @Deprecated
    public static final String TYPE = GriffonConstraintClass.TYPE;

    /**
     * Use GriffonConstraintClass.TRAILING instead
     *
     * @see GriffonConstraintClass
     */
    @Deprecated
    public static final String TRAILING = GriffonConstraintClass.TRAILING;

    public ConstraintArtifactHandler(GriffonApplication app) {
        super(app, GriffonConstraintClass.TYPE, GriffonConstraintClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonConstraintClass(getApp(), clazz);
    }
}
