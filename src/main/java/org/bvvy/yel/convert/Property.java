package org.bvvy.yel.convert;

import org.bvvy.yel.util.StringUtils;

import java.lang.reflect.Method;

public class Property {
    private final MethodParameter methodParameter;
    private Class<?> objectType;
    private Method readMethod;
    private Method writeMethod;
    private String name;

    public Property(Class<?> objectType, Method readMethod, Method writeMethod) {
        this(objectType, readMethod, writeMethod, null);
    }

    public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.methodParameter = resolveMethodParameter();
        this.name = (name != null ? name : resolveName());
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    private MethodParameter resolveMethodParameter() {
        MethodParameter read = resolveReadMethodParameter();
        MethodParameter write = resolveWriteMethodParameter();
        if (write == null) {
            if (read == null) {
                throw new IllegalStateException("Property is neither readable nor writeable");
            }
            return read;
        }
        if (read != null) {
            Class<?> readType = read.getParameterType();
            Class<?> writeType = write.getParameterType();
            if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
                return read;
            }
        }
        return write;
    }

    private MethodParameter resolveWriteMethodParameter() {
        if (getWriteMethod() == null) {
            return null;
        }
        return new MethodParameter(getWriteMethod(), 0).withContainingClass(getObjectType());
    }

    private MethodParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }
        return new MethodParameter(getReadMethod(), -1).withContainingClass(getObjectType());
    }

    private Class<?> getObjectType() {
        return objectType;
    }

    private String resolveName() {
        if (this.readMethod != null) {
            int index = this.readMethod.getName().indexOf("get");
            if (index != -1) {
                index += 3;
            }
            else {
                index = this.readMethod.getName().indexOf("is");
                if (index != -1) {
                    index += 2;
                }
                else {
                    // Record-style plain accessor method, e.g. name()
                    index = 0;
                }
            }
            return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
        }
        else if (this.writeMethod != null) {
            int index = this.writeMethod.getName().indexOf("set");
            if (index == -1) {
                throw new IllegalArgumentException("Not a setter method");
            }
            index += 3;
            return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
        }
        else {
            throw new IllegalStateException("Property is neither readable nor writeable");
        }
    }

    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    public Class<?> getType() {
        return this.objectType;
    }
}
