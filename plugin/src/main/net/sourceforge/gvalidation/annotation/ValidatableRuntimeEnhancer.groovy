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

package net.sourceforge.gvalidation.annotation

import java.beans.PropertyChangeListener
import net.sourceforge.gvalidation.util.MetaUtils
import net.sourceforge.gvalidation.ValidationEnhancer
import griffon.core.UIThreadManager
import javax.swing.SwingUtilities

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
@Singleton
class ValidatableRuntimeEnhancer {

    void degrade(model){
        ValidationEnhancer.degrade(model)
    }

    void enhance(model) {
        // TODO check if obj implements griffon.core.Observable
        if (realTimeOn(model) && annotatedWithBindable(model)) {
            model.addPropertyChangeListener({e ->
                if (e.propertyName != "errors") {
                    if (valueNotChanged(e) || isSetToBlankString(e) || isSetToZero(e))
                        return

                    UIThreadManager.instance.executeAsync {
                        model.validate(e?.propertyName)
                    }
                }
            } as PropertyChangeListener)
        }
    }

    private boolean realTimeOn(model) {
        Validatable annotation = getValidatableAnnotation(model)
        return annotation?.realTime()
    }

    private Validatable getValidatableAnnotation(model) {
        return model.getClass().getAnnotation(Validatable.class)
    }

    private boolean annotatedWithBindable(model) {
        return MetaUtils.hasMethod(model, 'addPropertyChangeListener')
    }

    private boolean valueNotChanged(e) {
        return e.oldValue == e.newValue
    }

    private boolean isSetToBlankString(e) {
        return e.oldValue == null && e.newValue == ""
    }

    private boolean isSetToZero(e) {
        return e.oldValue == null && e.newValue == 0
    }

}
