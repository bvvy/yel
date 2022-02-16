package org.bvvy.yel.context.accessor;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;

/**
 * @author bvvy
 */
public class MapAccessor implements CompilablePropertyAccessor {
    @Override
    public boolean canRead(Context context, Object target, String name) {
        return (target instanceof Map && ((Map<?, ?>) target).containsKey(name));
    }

    @Override
    public TypedValue read(Context context, Object target, String name) {
        Map<?, ?> map = (Map<?, ?>) target;
        Object value = map.get(name);
        return new TypedValue(value);
    }

    @Override
    public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
        String descriptor = cf.lastDescriptor();
        if (descriptor == null || !descriptor.equals("Ljava/util/Map")) {
            if (descriptor == null) {
                cf.loadTarget(mv);
            }
            CodeFlow.insertCheckCast(mv, "Ljava/util/Map");
        }
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
    }

    @Override
    public boolean isCompilable() {
        return true;
    }

    @Override
    public Class<?> getPropertyType() {
        return Object.class;
    }
}
