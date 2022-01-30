package org.bvvy.yel.exp.converter;


import org.objectweb.asm.MethodVisitor;

public interface BytecodeDescriptorConverter {

    void convert(MethodVisitor mv);

}
