package org.bvvy.yel.convert;

/**
 * @author bvvy
 * @date 2022/1/13
 */
public class TypeDescriptor {

    private final ResolvableType resolvableType;

    public TypeDescriptor(MethodParameter methodParameter) {
        this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
    }

    public static TypeDescriptor forObject(Object value) {
        return null;
    }
}
