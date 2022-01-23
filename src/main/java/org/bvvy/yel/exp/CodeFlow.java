package org.bvvy.yel.exp;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author bvvy
 */
public class CodeFlow {

    private final Deque<List<String>> compilationScopes;

    public CodeFlow(String className, ClassWriter cw) {
        this.compilationScopes = new ArrayDeque<>();
    }

    public static void insertNumericUnboxOrPrimitiveTypeCoercion(MethodVisitor mv, String stackDescriptor, char targetDescriptor) {
        if (!CodeFlow.isPrimitive(stackDescriptor)) {
            CodeFlow.insertUnboxNumberInsns(mv, targetDescriptor, stackDescriptor);
        } else {
            CodeFlow.insertAnyNecessaryTypeConversionBytecodes(mv, targetDescriptor, stackDescriptor);
        }
    }

    public static void insertAnyNecessaryTypeConversionBytecodes(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {
        if (CodeFlow.isPrimitive(stackDescriptor)) {
            char stackTop = stackDescriptor.charAt(0);
            if (stackTop == 'I' || stackTop == 'B' || stackTop == 'S' || stackTop == 'C') {
                if (targetDescriptor == 'D') {
                    mv.visitInsn(Opcodes.I2D);
                } else if (targetDescriptor == 'F') {
                    mv.visitInsn(Opcodes.I2F);
                } else if (targetDescriptor == 'J') {
                    mv.visitInsn(Opcodes.I2L);
                } else if (targetDescriptor == 'I') {
                    // nop
                } else {
                    throw new IllegalStateException("cannot cast");
                }
            } else if (stackTop == 'J') {
                if (targetDescriptor == 'D') {
                    mv.visitInsn(Opcodes.L2D);
                } else if (targetDescriptor == 'F') {
                    mv.visitInsn(Opcodes.L2F);
                } else if (targetDescriptor == 'J') {
                    // nop
                } else if (targetDescriptor == 'I') {
                    mv.visitInsn(Opcodes.L2I);
                } else {
                    throw new IllegalStateException("cannot cast");
                }
            } else if (stackTop == 'D') {
                if (targetDescriptor == 'D') {
                    // nop
                } else if (targetDescriptor == 'F') {
                    mv.visitInsn(Opcodes.D2F);
                } else if (targetDescriptor == 'J') {
                    mv.visitInsn(Opcodes.D2L);
                } else if (targetDescriptor == 'I') {
                    mv.visitInsn(Opcodes.D2I);
                } else {
                    throw new IllegalStateException("cannot cast");
                }
            }
        }

    }

    private static void insertUnboxNumberInsns(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {
        if (stackDescriptor == null) {
            return;
        }
        switch (targetDescriptor) {
            case 'D':
                if (stackDescriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "doubleValue", "()D", false);
                break;
            case 'F':
                if (stackDescriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "floatValue", "()D", false);
                break;
            case 'J':
                if (stackDescriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "longValue", "()D", false);
                break;
            case 'I':
                if (stackDescriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "intValue", "()D", false);
                break;
            default:
                throw new IllegalArgumentException("Unboxing fail ");
        }


    }

    private static boolean isPrimitive(String descriptor) {
        return (descriptor != null && descriptor.length() == 1);
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
}
