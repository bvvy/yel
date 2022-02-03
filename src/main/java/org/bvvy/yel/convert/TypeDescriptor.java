package org.bvvy.yel.convert;

/**
 * @author bvvy
 * @date 2022/1/13
 */
public class TypeDescriptor {

    private final ResolvableType resolvableType;
    private final Class<?> type;

    public TypeDescriptor(MethodParameter methodParameter) {
        this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
        this.type = this.resolvableType.resolve(methodParameter.getNestedParameterType());
    }

    public static TypeDescriptor forObject(Object value) {
        return null;
    }
}
