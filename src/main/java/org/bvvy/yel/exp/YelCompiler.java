package org.bvvy.yel.exp;

import lombok.extern.slf4j.Slf4j;
import org.bvvy.yel.exp.ast.Node;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.atomic.AtomicInteger;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author bvvy
 */
@Slf4j
public class YelCompiler {
    private AtomicInteger suffixId = new AtomicInteger(1);

    private ChildClassLoader childClassLoader;

    public YelCompiler() {
        this.childClassLoader = new ChildClassLoader(Thread.currentThread().getContextClassLoader());
    }

    public CompiledExpression compile(Node expression) {
        Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Class<? extends CompiledExpression> createExpressionClass(Node expressionToCompile) {
        String className = "yel/Ex" + getNextSuffix();
        String contextClass = "org/bvvy/yel/context/Context";
        String parentClassName = "org/bvvy/yel/exp/CompiledExpression";
        ClassWriter cx = new ExpressionClassWriter();
        TraceClassVisitor cw = new TraceClassVisitor(cx, new PrintWriter(System.out));

        cw.visit(V1_8, ACC_PUBLIC, className, null, parentClassName, null);

        // constructor
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, parentClassName, "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // getValue method
        mv = cw.visitMethod(ACC_PUBLIC, "getValue", "(Ljava/lang/Object;L" + contextClass + ";)Ljava/lang/Object;", null, new String[]{"org/bvvy/yel/exception/YelEvaluationException"});
        mv.visitCode();

        CodeFlow cf = new CodeFlow(className, cw);
        try {
            expressionToCompile.generateCode(mv, cf);
        } catch (Exception e) {
            log.error("fail to compile: " + e.getMessage());
            return null;
        }
        CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
        if ("V".equals(cf.lastDescriptor())) {
            mv.visitInsn(ACONST_NULL);
        }
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        cw.visitEnd();

        byte[] data = cx.toByteArray();

        return loadClass(className.replaceAll("/", "."), data);

    }

    @SuppressWarnings("unchecked")
    private Class<? extends CompiledExpression> loadClass(String name, byte[] bytes) {
        ChildClassLoader ccl = this.childClassLoader;
        return (Class<? extends CompiledExpression>) ccl.defineClass(name, bytes);
    }

    private int getNextSuffix() {
        return this.suffixId.incrementAndGet();
    }


    private static class ChildClassLoader extends URLClassLoader {

        public static final URL[] NO_URLS = new URL[0];

        private final AtomicInteger classesDefinedCount = new AtomicInteger(0);

        public ChildClassLoader(ClassLoader classLoader) {
            super(NO_URLS, classLoader);
        }

        public Class<?> defineClass(String name, byte[] bytes) {
            Class<?> clazz = super.defineClass(name, bytes, 0, bytes.length);
            classesDefinedCount.incrementAndGet();
            return clazz;
        }

        public int getClassesDefinedCount() {
            return classesDefinedCount.get();
        }
    }

    private class ExpressionClassWriter extends ClassWriter {
        public ExpressionClassWriter() {
            super(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        }

        @Override
        protected ClassLoader getClassLoader() {
            return childClassLoader;
        }
    }
}
