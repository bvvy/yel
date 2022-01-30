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
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "floatValue", "()F", false);
                break;
            case 'J':
                if (stackDescriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "longValue", "()J", false);
                break;
            case 'I':
                if (stackDescriptor.equals("Ljava/lang/Object")) {
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "intValue", "()I", false);
                break;
            default:
                throw new IllegalArgumentException("Unboxing fail ");
        }


    }

    private static boolean isPrimitive(String descriptor) {
        return (descriptor != null && descriptor.length() == 1);
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

    public static void insertBigDecimalCoercion(MethodVisitor mv, String stackDescriptor) {
        if (stackDescriptor == null) {
            return;
        }
        switch (stackDescriptor) {
            case "I":
                mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// 1,ref ->
                mv.visitInsn(Opcodes.DUP_X1); //ref | 1 | ref |  ->
                mv.visitInsn(Opcodes.SWAP); // ref | ref | 1 ->
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(I)V", false);
                break;
            case "J":
                mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// _,_,ref ->
                mv.visitInsn(Opcodes.DUP_X2); //ref | _ | _ | ref |  ->
                mv.visitInsn(Opcodes.DUP_X2); //ref | ref | _ | _ | ref |  ->
                mv.visitInsn(Opcodes.POP); // ref | ref | _ | _ ->
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(J)V", false);
                break;
            case "D":
                mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// _,_,ref ->
                mv.visitInsn(Opcodes.DUP_X2); //ref | _ | _ | ref |  ->
                mv.visitInsn(Opcodes.DUP_X2); //ref | ref | _ | _ | ref |  ->
                mv.visitInsn(Opcodes.POP); // ref | ref | _ | _ ->
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(D)V", false);
                break;
            case "F":
                mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// 1.1,ref ->
                mv.visitInsn(Opcodes.DUP_X1); //ref | 1.1 | ref |  ->
                mv.visitInsn(Opcodes.SWAP); // ref | ref | 1.1 ->
                mv.visitInsn(Opcodes.F2D);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(D)V", false);
                break;
            case "Ljava/lang/Object":
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/math/BigDecimal");
                break;
            case "Ljava/math/BigDecimal":
                // nop
                break;
            default:
                throw new IllegalStateException("cannot cast '" + stackDescriptor + "' to BigDecimal");
        }

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
