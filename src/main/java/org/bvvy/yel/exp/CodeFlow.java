package org.bvvy.yel.exp;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

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

    private static void insertAnyNecessaryTypeConversionBytecodes(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {

    }

    private static void insertUnboxNumberInsns(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {

    }

    private static boolean isPrimitive(String stackDescriptor) {
        return false;
    }

    public void pushDescriptor(String exitTypeDescriptor) {
        this.compilationScopes.element().add(exitTypeDescriptor);
    }

    public void enterCompilationScope() {
        this.compilationScopes.push(new ArrayList<>());
    }

    public void exitCompilationScope() {

    }
}
