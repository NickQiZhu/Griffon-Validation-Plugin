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

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import net.sourceforge.gvalidation.ConstraintRepository
import net.sourceforge.gvalidation.artifact.ConstraintArtifactHandler
import net.sourceforge.gvalidation.renderer.ErrorRendererAttributeDelegator
import net.sourceforge.gvalidation.swing.ErrorIconFactory
import net.sourceforge.gvalidation.swing.ErrorMessagePanelFactory
import net.sourceforge.gvalidation.annotation.ValidatableRuntimeEnhancer
import net.sourceforge.gvalidation.ValidationEnhancer

/**
 * @author Nick Zhu
 */
class ValidationGriffonAddon {
    void addonInit(GriffonApplication app) {
        app.artifactManager.registerArtifactHandler(new ConstraintArtifactHandler(app))
        ConstraintRepository.instance.initialize(app)
    }

    List attributeDelegates = [
            {builder, node, attributes ->
                def attributeDelegator = new ErrorRendererAttributeDelegator()

                if (attributeDelegator.isAttributeSet(attributes)) {
                    attributeDelegator.delegate(ApplicationHolder.application, builder, node, attributes)
                }
            }
    ]

    Map factories = [
            errorIcon: new ErrorIconFactory(),
            errorMessages: new ErrorMessagePanelFactory()
    ]

    Map events = [
            InitializeMVCGroup: { configuration, group ->
                def model = group.model
                ValidatableRuntimeEnhancer.instance.enhance(model)
            }
    ]
}
