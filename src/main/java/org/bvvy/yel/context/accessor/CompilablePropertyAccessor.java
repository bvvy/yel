package org.bvvy.yel.context.accessor;

import org.bvvy.yel.exp.CodeFlow;
import org.objectweb.asm.MethodVisitor;

/**
 * @author bvvy
 * @date 2022/1/27
 */
public interface CompilablePropertyAccessor extends PropertyAccessor {

    boolean isCompilable();

    void generateCode(String name, MethodVisitor mv, CodeFlow cf);

    Class<?> getPropertyType();
}
