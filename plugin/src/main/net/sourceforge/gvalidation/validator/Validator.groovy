
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

package net.sourceforge.gvalidation.validator

/**
 * Generic base interface for all validator implementation
 *
 * @author Nick Zhu (nzhu@jointsource.com)
 */
public interface Validator {

    /**
     * Validate method
     *
     * @param propertyValue value of the property to be validated
     * @param bean object owner of the property
     * @param parameter configuration parameter for the constraint
     * @return return true if validation passes otherwise false
     */
    def validate(propertyValue, bean, parameter)


}