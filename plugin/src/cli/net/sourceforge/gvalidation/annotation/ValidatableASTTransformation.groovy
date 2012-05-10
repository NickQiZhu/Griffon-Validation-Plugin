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

package net.sourceforge.gvalidation.annotation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.stmt.BlockStatement

import org.codehaus.groovy.ast.VariableScope

import org.codehaus.groovy.ast.FieldNode

import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import net.sourceforge.gvalidation.Errors

import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.ConstructorNode
import net.sourceforge.gvalidation.ValidationEnhancer
import net.sourceforge.gvalidation.ErrorContainer

/**
 * Groovy AST transformation class that enhances any class
 * annotated as @Validatable during compile time automatically 
 *
 * Created by nick.zhu
 */
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class ValidatableASTTransformation implements ASTTransformation {

    private static final String VALIDATION_ENHANCER_PROPERTY_NAME = '__validationEnhancer'
    private static final String ERRORS_PROPERTY_NAME = '__errors'

    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        ClassNode targetClassNode = sourceUnit.getAST()?.classes.first()

        if (!alreadyAnnotatedBySuperClass(targetClassNode) && annotatedWithValidatable(targetClassNode)) {
            targetClassNode.addInterface(ClassHelper.make(ErrorContainer.class));

//            if (hasNoEnhancerField(targetClassNode)) {
                injectEnhancerField(targetClassNode)
//            }

//            if (hasNoValidateMethod(targetClassNode)) {
                injectValidateMethod(targetClassNode)
//            }

//            if (hasNoErrorsField(targetClassNode)) {
                injectErrorsField(targetClassNode)
//            }

//            if (hasNoGetErrorsMethod(targetClassNode)) {
                injectGetErrorsMethod(targetClassNode)
//            }

//            if (hasNoSetErrorsMethod(targetClassNode)) {
                injectSetErrorsMethod(targetClassNode)
//            }

//            if (hasNoHasErrorsMethod(targetClassNode)) {
                injectHasErrorsMethod(targetClassNode)
//            }
        }
    }

    private boolean alreadyAnnotatedBySuperClass(ClassNode targetClassNode) {
        if(targetClassNode.superClass == null)
            return false

        if(targetClassNode.superClass.getField(ERRORS_PROPERTY_NAME))
            return true
        else
            return alreadyAnnotatedBySuperClass(targetClassNode.superClass)
    }

    private boolean annotatedWithValidatable(ClassNode classNode) {
        def validatableAnnotation = classNode.getAnnotations().find {
            it.classNode.typeClass == Validatable
        }

        return validatableAnnotation != null
    }

    private boolean hasNoEnhancerField(ClassNode targetClassNode) {
        return !targetClassNode.getField(VALIDATION_ENHANCER_PROPERTY_NAME)
    }

    private def injectEnhancerField(ClassNode targetClassNode) {
        FieldNode enhancerField = new FieldNode(VALIDATION_ENHANCER_PROPERTY_NAME,
                Opcodes.ACC_PROTECTED,
                ClassHelper.make(ValidationEnhancer.class),
                targetClassNode,
                new ConstructorCallExpression(ClassHelper.make(ValidationEnhancer.class), new ArgumentListExpression(new VariableExpression('this')))
        )
        targetClassNode.addField(enhancerField)
    }

    private boolean hasNoErrorsField(ClassNode targetClassNode) {
        return !targetClassNode.getField(ERRORS_PROPERTY_NAME)
    }

    private def injectErrorsField(ClassNode targetClassNode) {
        FieldNode errorsField = new FieldNode(ERRORS_PROPERTY_NAME,
                Opcodes.ACC_PROTECTED,
                ClassHelper.make(Errors.class),
                targetClassNode,
                new ConstructorCallExpression(ClassHelper.make(Errors.class), new ArgumentListExpression(new VariableExpression('this')))
        )
        targetClassNode.addField(errorsField)
    }

    private boolean hasNoGetErrorsMethod(ClassNode targetClassNode) {
        return !targetClassNode.hasDeclaredMethod('getErrors', [] as Parameter[])
    }

    private def injectGetErrorsMethod(ClassNode targetClassNode) {
        targetClassNode.addMethod(new MethodNode(
                'getErrors',
                Opcodes.ACC_PUBLIC,
                ClassHelper.make(Errors.class),
                [] as Parameter[],
                [] as ClassNode[],
                new BlockStatement(
                        new AstBuilder().buildFromCode {
                            return this.__errors
                        },
                        new VariableScope()
                )
        ))
    }

    private boolean hasNoSetErrorsMethod(ClassNode targetClassNode) {
        return !targetClassNode.hasMethod('setErrors', [new Parameter(ClassHelper.make(Errors.class, false), "errors")] as Parameter[])
    }

    private def injectSetErrorsMethod(ClassNode targetClassNode) {
        targetClassNode.addMethod(new MethodNode(
                'setErrors',
                Opcodes.ACC_PUBLIC,
                ClassHelper.VOID_TYPE,
                [new Parameter(ClassHelper.make(Errors.class, false), "errors")] as Parameter[],
                [] as ClassNode[],
                new BlockStatement(
                        new AstBuilder().buildFromCode {
                            def newValue = errors
                            def oldValue = this.__errors
                            this.__errors = newValue
                            firePropertyChange('errors', oldValue, newValue)
                            return null
                        },
                        new VariableScope()
                )
        ))
    }

    private boolean hasNoHasErrorsMethod(ClassNode targetClassNode) {
        return !targetClassNode.hasMethod('hasErrors', [] as Parameter[])
    }

    private def injectHasErrorsMethod(ClassNode targetClassNode) {
        targetClassNode.addMethod(new MethodNode(
                'hasErrors',
                Opcodes.ACC_PUBLIC,
                ClassHelper.Boolean_TYPE,
                [] as Parameter[],
                [] as ClassNode[],
                new BlockStatement(
                        new AstBuilder().buildFromCode {
                            return this.__errors.hasErrors()
                        },
                        new VariableScope()
                )
        ))
    }

    private boolean hasNoValidateMethod(ClassNode targetClassNode) {
        return !targetClassNode.hasMethod('validate', [new Parameter(ClassHelper.make(Object.class, false), "fields", ConstantExpression.NULL)] as Parameter[])
    }

    private def injectValidateMethod(ClassNode targetClassNode) {
        targetClassNode.addMethod(new MethodNode(
                'validate',
                Opcodes.ACC_PUBLIC,
                ClassHelper.Boolean_TYPE,
                [new Parameter(ClassHelper.make(Object.class, false), "fields", ConstantExpression.NULL)] as Parameter[],
                [] as ClassNode[],
                new BlockStatement(
                        new AstBuilder().buildFromCode {
                            return this.__validationEnhancer.doValidate(fields)
                        },
                        new VariableScope()
                )
        ))
    }

}
