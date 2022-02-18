package org.bvvy.yel.exp;

import org.bvvy.yel.exp.converter.BytecodeDescriptorConverterService;
import org.bvvy.yel.util.CollectionUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author bvvy
 */
public class CodeFlow {

    private final Deque<List<String>> compilationScopes;
    private final BytecodeDescriptorConverterService converterService;

    public CodeFlow(String className, ClassVisitor cv) {
        this.compilationScopes = new ArrayDeque<>();
        this.compilationScopes.add(new ArrayList<>());
        this.converterService = new BytecodeDescriptorConverterService();
    }


    public void insertDescriptorConversion(MethodVisitor mv, String stackDescriptor, String targetDescriptor) {
        converterService.convert(stackDescriptor, targetDescriptor, mv);
    }

    public static void insertBoxIfNecessary(MethodVisitor mv, String descriptor) {
        if (descriptor != null && descriptor.length() == 1) {
            insertBoxIfNecessary(mv, descriptor.charAt(0));
        }
    }

    public static void insertBoxIfNecessary(MethodVisitor mv, char ch) {
        switch (ch) {
            case 'Z':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                break;
            case 'B':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                break;
            case 'C':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                break;
            case 'S':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                break;
            case 'I':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                break;
            case 'J':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                break;
            case 'F':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                break;
            case 'D':
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                break;
            case 'L':
            case 'V':
            case '[':
                break;
            default:
                throw new IllegalArgumentException("cannot Boxing descriptor '" + ch + "'");

        }
    }

    public static void insertCheckCast(MethodVisitor mv, String descriptor) {
        if (descriptor != null && descriptor.length() != 1) {
            if (descriptor.charAt(0) == '[') {
                if (isPrimitiveArray(descriptor)) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, descriptor);
                } else {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, descriptor + ";");
                }
            } else {
                if (!descriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, descriptor.substring(1));
                }
            }
        }

    }

    public static boolean isPrimitiveArray(String descriptor) {
        if (descriptor == null) {
            return false;
        }
        boolean primitive = true;
        for (int i = 0, max = descriptor.length(); i < max; i++) {
            char ch = descriptor.charAt(i);
            if (ch == '[') {
                continue;
            }
            primitive = (ch != 'L');
            break;
        }
        return primitive;
    }

    public static String toDescriptor(Class<?> type) {
        String descriptor = Type.getDescriptor(type);
        if (descriptor.endsWith(";")) {
            return descriptor.substring(0, descriptor.length() - 1);
        }
        return descriptor;
    }

    public void pushDescriptor(String exitTypeDescriptor) {
        this.compilationScopes.element().add(exitTypeDescriptor);
    }

    public void enterCompilationScope() {
        this.compilationScopes.push(new ArrayList<>());
    }

    public void exitCompilationScope() {
        this.compilationScopes.pop();
    }

    public String lastDescriptor() {
        return CollectionUtils.lastElement(this.compilationScopes.peek());
    }

    public void loadTarget(MethodVisitor mv) {
        mv.visitVarInsn(Opcodes.ALOAD, 1);
    }
}
