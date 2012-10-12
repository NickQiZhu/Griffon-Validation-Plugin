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

package net.sourceforge.gvalidation;

import griffon.core.UIThreadManager;
import net.sourceforge.gvalidation.util.MetaUtils;

import java.util.*;


/**
 * Created by nick.zhu
 */
class Errors {
    private static final String GET_PROPERTY_CHANGE_LISTENERS_METHOD_NAME = "getPropertyChangeListeners";

    private ErrorContainer parent;
    private HashMap<String, List<FieldError>> fieldErrors = new HashMap<String, List<FieldError>>();
    private LinkedList<SimpleError> globalErrors = new LinkedList<SimpleError>();
    private List<ErrorListener> errorListeners = new ArrayList<ErrorListener>();

    public Errors() {
        this(null);
    }

    public Errors(ErrorContainer parent) {
        this.parent = parent;
    }

    public LinkedList<SimpleError> getGlobalErrors() {
        return globalErrors;
    }

    public HashMap<String, List<FieldError>> getFieldErrors() {
        return fieldErrors;
    }

    public void reject(String errorCode) {
        reject(errorCode, Collections.emptyList());
    }

    public void reject(String errorCode, String defaultErrorCode) {
        reject(errorCode, defaultErrorCode, Collections.emptyList());
    }

    public void reject(String errorCode, List arguments) {
        reject(errorCode, null, arguments);
    }

    public void reject(String errorCode, String defaultErrorCode, List arguments) {
        Errors oldErrors = cloneErrors();

        SimpleError error = new SimpleError();

        error.setErrorCode(errorCode);
        error.setDefaultErrorCode(defaultErrorCode);
        error.setArguments(arguments);

        globalErrors.add(error);

        fireErrorChangedEventOnParent(oldErrors);
    }

    private Errors cloneErrors() {
        Errors clone = new Errors();

        clone.parent = parent;
        clone.fieldErrors = (HashMap<String, List<FieldError>>) fieldErrors.clone();
        clone.globalErrors = (LinkedList<SimpleError>) globalErrors.clone();

        return clone;
    }

    private void fireErrorChangedEventOnParent(Errors oldErrors) {
        UIThreadManager.getInstance().executeSync(new Runnable() {
            public void run() {
                if (parent != null && hasPropertyChangeNotifier()) {
                    parent.setErrors(new Errors());
                    parent.setErrors(Errors.this);
                }
            }
        });
    }

    private boolean hasPropertyChangeNotifier() {
        return MetaUtils.hasMethod(parent, GET_PROPERTY_CHANGE_LISTENERS_METHOD_NAME);
    }

    public boolean hasGlobalErrors() {
        return !globalErrors.isEmpty();
    }

    public void rejectValue(String field, String errorCode) {
        rejectValue(field, errorCode, Collections.emptyList());
    }

    public void rejectValue(String field, String errorCode, String defaultErrorCode) {
        rejectValue(field, errorCode, defaultErrorCode, Collections.emptyList());
    }

    public void rejectValue(String field, String errorCode, List arguments) {
        rejectValue(field, errorCode, null, arguments);
    }

    public void rejectValue(String field, String errorCode, String defaultErrorCode, List arguments) {
        if (!fieldErrors.containsKey(field))
            fieldErrors.put(field, new LinkedList<FieldError>());

        Errors oldErrors = cloneErrors();

        FieldError fieldError = new FieldError();

        fieldError.setField(field);
        fieldError.setErrorCode(errorCode);
        fieldError.setDefaultErrorCode(defaultErrorCode);
        fieldError.setArguments(arguments);

        fieldErrors.get(field).add(fieldError);

        fireFieldErrorAddedEvent(fieldError);

        fireErrorChangedEventOnParent(oldErrors);
    }

    private void fireFieldErrorAddedEvent(final FieldError fieldError) {
        UIThreadManager.getInstance().executeSync(new Runnable() {
            public void run() {
                for (ErrorListener listener : errorListeners) {
                    listener.onFieldErrorAdded(fieldError);
                }
            }
        });
    }

    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }

    public boolean hasFieldErrors(String field) {
        return fieldErrors.containsKey(field) && fieldErrors.get(field).size() > 0;
    }

    public FieldError getFieldError(String field) {
        List<FieldError> fieldErrorList = fieldErrors.get(field);
        return fieldErrorList != null && fieldErrorList.size() > 0 ? fieldErrorList.iterator().next() : null;
    }

    public List<FieldError> getFieldErrors(String field) {
        return fieldErrors.get(field);
    }

    public boolean hasErrors() {
        return hasGlobalErrors() || hasFieldErrors();
    }

    public void clear() {
        Errors oldErrors = cloneErrors();

        for (String key : fieldErrors.keySet()) {
            List<FieldError> errors = fieldErrors.get(key);
            fireFieldErrorRemovedEvent(errors);
        }

        fieldErrors.clear();
        globalErrors.clear();

        fireErrorChangedEventOnParent(oldErrors);
    }

    private void fireFieldErrorRemovedEvent(final List<FieldError> errors) {
        if (errors == null || errors.size() == 0)
            return;

        UIThreadManager.getInstance().executeSync(new Runnable() {
            public void run() {
                for (ErrorListener listener : errorListeners) {
                    listener.onFieldErrorRemoved(errors);
                }
            }
        });
    }

    public Iterator<SimpleError> iterator() {
        List<SimpleError> allErrors = new LinkedList<SimpleError>();
        List<SimpleError> globalErrorsCopy = (List<SimpleError>) globalErrors.clone();
        Map<String, List<FieldError>> fieldErrorsCopy = (Map<String, List<FieldError>>) fieldErrors.clone();

        allErrors.addAll(globalErrorsCopy);

        for (List<FieldError> e : fieldErrorsCopy.values()) {
            allErrors.addAll(e);
        }

        return allErrors.iterator();
    }

    public void addListener(ErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    public boolean hasListener(ErrorListener errorListener) {
        return errorListeners.contains(errorListener);
    }

    public boolean removeListener(ErrorListener errorListener) {
        return errorListeners.remove(errorListener);
    }

    public List<FieldError> removeErrorOnField(String field) {
        Errors oldErrors = cloneErrors();

        List<FieldError> errors = fieldErrors.remove(field);

        fireFieldErrorRemovedEvent(errors);

        fireErrorChangedEventOnParent(oldErrors);

        return errors;
    }

}
