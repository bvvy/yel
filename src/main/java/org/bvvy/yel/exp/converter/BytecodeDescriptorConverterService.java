package org.bvvy.yel.exp.converter;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;

public class BytecodeDescriptorConverterService {

    Map<BytecodeDescriptorKey, BytecodeDescriptorConverter> converters;

    public BytecodeDescriptorConverterService(Map<BytecodeDescriptorKey, BytecodeDescriptorConverter> converters) {
        this.converters = converters;
    }

    public BytecodeDescriptorConverterService() {
        add(new BytecodeDescriptorKey("Z", "Ljava/lang/Object"), mv -> mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false));
        add(new BytecodeDescriptorKey("I", "D"), mv -> mv.visitInsn(Opcodes.I2D));
        add(new BytecodeDescriptorKey("I", "F"), mv -> mv.visitInsn(Opcodes.I2F));
        add(new BytecodeDescriptorKey("I", "J"), mv -> mv.visitInsn(Opcodes.I2L));
        add(new BytecodeDescriptorKey("I", "Ljava/lang/Object"), mv -> mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
        add(new BytecodeDescriptorKey("I", "Ljava/math/BigDecimal"), mv -> {
            mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// 1,ref ->
            mv.visitInsn(Opcodes.DUP_X1); //ref | 1 | ref |  ->
            mv.visitInsn(Opcodes.SWAP); // ref | ref | 1 ->
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(I)V", false);
        });

        add(new BytecodeDescriptorKey("J", "D"), mv -> mv.visitInsn(Opcodes.L2D));
        add(new BytecodeDescriptorKey("J", "F"), mv -> mv.visitInsn(Opcodes.L2F));
        add(new BytecodeDescriptorKey("J", "I"), mv -> mv.visitInsn(Opcodes.L2I));
        add(new BytecodeDescriptorKey("J", "Ljava/lang/Object"), mv -> mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false));
        add(new BytecodeDescriptorKey("J", "Ljava/math/BigDecimal"), mv -> {
            mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// _,_,ref ->
            mv.visitInsn(Opcodes.DUP_X2); //ref | _ | _ | ref |  ->
            mv.visitInsn(Opcodes.DUP_X2); //ref | ref | _ | _ | ref |  ->
            mv.visitInsn(Opcodes.POP); // ref | ref | _ | _ ->
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(J)V", false);
        });


        add(new BytecodeDescriptorKey("D", "F"), mv -> mv.visitInsn(Opcodes.D2F));
        add(new BytecodeDescriptorKey("D", "I"), mv -> mv.visitInsn(Opcodes.D2I));
        add(new BytecodeDescriptorKey("D", "J"), mv -> mv.visitInsn(Opcodes.D2L));
        add(new BytecodeDescriptorKey("D", "Ljava/lang/Object"), mv -> mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false));
        add(new BytecodeDescriptorKey("D", "Ljava/math/BigDecimal"), mv -> {
            mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// _,_,ref ->
            mv.visitInsn(Opcodes.DUP_X2); //ref | _ | _ | ref |  ->
            mv.visitInsn(Opcodes.DUP_X2); //ref | ref | _ | _ | ref |  ->
            mv.visitInsn(Opcodes.POP); // ref | ref | _ | _ ->
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(D)V", false);
        });

        add(new BytecodeDescriptorKey("F", "D"), mv -> mv.visitInsn(Opcodes.F2D));
        add(new BytecodeDescriptorKey("F", "I"), mv -> mv.visitInsn(Opcodes.F2I));
        add(new BytecodeDescriptorKey("F", "J"), mv -> mv.visitInsn(Opcodes.F2L));
        add(new BytecodeDescriptorKey("F", "Ljava/lang/Object"), mv -> mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false));
        add(new BytecodeDescriptorKey("F", "Ljava/math/BigDecimal"), mv -> {
            mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");// 1.1,ref ->
            mv.visitInsn(Opcodes.DUP_X1); //ref | 1.1 | ref |  ->
            mv.visitInsn(Opcodes.SWAP); // ref | ref | 1.1 ->
            mv.visitInsn(Opcodes.F2D);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(D)V", false);
        });

        add(new BytecodeDescriptorKey("Ljava/lang/Object", "D"), mv -> {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "doubleValue", "()D", false);
        });
        add(new BytecodeDescriptorKey("Ljava/lang/Object", "F"), mv -> {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "floatValue", "()F", false);
        });
        add(new BytecodeDescriptorKey("Ljava/lang/Object", "I"), mv -> {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "intValue", "()I", false);
        });
        add(new BytecodeDescriptorKey("Ljava/lang/Object", "J"), mv -> {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "longValue", "()J", false);
        });
        add(new BytecodeDescriptorKey("Ljava/lang/Object", "Ljava/math/BigDecimal"), mv -> {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/math/BigDecimal");
        });

    }

    public void convert(String sourceDescriptor, String targetDescriptor, MethodVisitor mv) {
        BytecodeDescriptorKey bytecodeDescriptorKey = new BytecodeDescriptorKey(sourceDescriptor, targetDescriptor);
        converters.get(bytecodeDescriptorKey).convert(mv);
    }

    public void add(BytecodeDescriptorKey bytecodeDescriptorKey, BytecodeDescriptorConverter bytecodeDescriptorConverter) {
        converters.put(bytecodeDescriptorKey, bytecodeDescriptorConverter);
    }
}
