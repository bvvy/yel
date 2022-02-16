package org.bvvy.yel.context.method;

import org.bvvy.yel.convert.MethodParameter;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.util.ClassUtils;

import java.lang.reflect.Executable;
import java.util.List;

public class ReflectionHelper {
    public static ArgumentsMatchInfo compareArgumentsVarargs(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter) {

        ArgumentsMatchKind match = ArgumentsMatchKind.EXACT;
        int argCountUpToVarargs = expectedArgTypes.size() - 1;
        for (int i = 0; i < argCountUpToVarargs && match != null; i++) {
            TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
            TypeDescriptor expectedArg = expectedArgTypes.get(i);
            if (suppliedArg == null) {
                if (expectedArg.isPrimitive()) {
                    match = null;
                }
            } else {
                if (!expectedArg.equals(suppliedArg)) {
                    if (suppliedArg.isAssignableTo(expectedArg)) {
                        if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
                            match = ArgumentsMatchKind.CLOSE;
                        }
                    } else if (typeConverter.canConvert(suppliedArg, expectedArg)) {
                        match = ArgumentsMatchKind.REQUIRES_CONVERSION;
                    } else {
                        match = null;
                    }
                }
            }
        }
        if (match == null) {
            return null;
        }
        if (suppliedArgTypes.size() == expectedArgTypes.size()
                && expectedArgTypes.get(expectedArgTypes.size() - 1).equals(suppliedArgTypes.get(suppliedArgTypes.size() - 1))) {

        } else {
            TypeDescriptor varargsDesc = expectedArgTypes.get(expectedArgTypes.size() - 1);
            TypeDescriptor elementDesc = varargsDesc.getElementTypeDescriptor();
            Class<?> varargsParamType = elementDesc.getType();

            for (int i = expectedArgTypes.size() - 1; i < suppliedArgTypes.size(); i++) {
                TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
                if (suppliedArg == null) {
                    if (varargsParamType.isPrimitive()) {
                        match = null;
                    }
                } else {
                    if (varargsParamType != suppliedArg.getType()) {
                        if (ClassUtils.isAssignable(varargsParamType, suppliedArg.getType())) {
                            if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
                                match = ArgumentsMatchKind.CLOSE;
                            }
                        } else if (typeConverter.canConvert(suppliedArg, TypeDescriptor.valueOf(varargsParamType))) {
                            match = ArgumentsMatchKind.REQUIRES_CONVERSION;
                        } else {
                            match = null;
                        }
                    }
                }
            }
        }
        return (match != null ? new ArgumentsMatchInfo(match) : null);
    }

    public static ArgumentsMatchInfo compareArguments(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter) {
        ArgumentsMatchKind match = ArgumentsMatchKind.EXACT;
        for (int i = 0; i < expectedArgTypes.size() && match != null; i++) {
            TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
            TypeDescriptor expectedArg = expectedArgTypes.get(i);
            if (suppliedArg == null) {
                if (expectedArg.isPrimitive()) {
                    return null;
                }
            } else if (!expectedArg.equals(suppliedArg)) {
                if (suppliedArg.isAssignableTo(expectedArg)) {
                    if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
                        match = ArgumentsMatchKind.CLOSE;
                    }
                } else if (typeConverter.canConvert(suppliedArg, expectedArg)) {
                    match = ArgumentsMatchKind.REQUIRES_CONVERSION;
                } else {
                    match = null;
                }
            }
        }
        return match != null ? new ArgumentsMatchInfo(match) : null;
    }

    public static int getTypeDifferenceWeight(List<TypeDescriptor> paramTypes, List<TypeDescriptor> argTypes) {
        int result = 0;
        for (int i = 0; i < paramTypes.size(); i++) {
            TypeDescriptor paramType = paramTypes.get(i);
            TypeDescriptor argType = i < argTypes.size() ? argTypes.get(i) : null;
            if (argType == null) {
                if (paramType.isPrimitive()) {
                    return Integer.MAX_VALUE;
                }
            } else {
                Class<?> paramTypeClazz = paramType.getType();
                if (!ClassUtils.isAssignable(paramTypeClazz, argType.getType())) {
                    return Integer.MAX_VALUE;
                }
                if (paramTypeClazz.isPrimitive()) {
                    paramTypeClazz = Object.class;
                }
                Class<?> superClass = argType.getType().getSuperclass();
                while (superClass != null) {
                    if (paramTypeClazz.equals(superClass)) {
                        result = result + 2;
                        superClass = null;
                    } else if (ClassUtils.isAssignable(paramTypeClazz, superClass)) {
                        result = result + 2;
                        superClass = superClass.getSuperclass();
                    } else {
                        superClass = null;
                    }
                }
                if (paramTypeClazz.isInterface()) {
                    result = result + 1;
                }
            }

        }
        return result;
    }

    public static boolean convertArguments(TypeConverter converter, Object[] arguments, Executable executable, Integer varargsPosition) {
        boolean conversionOccurred = false;
        if (varargsPosition == null) {
            for (int i = 0; i < arguments.length; i++) {
                TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forExecutable(executable, i));
                Object argument = arguments[i];
                arguments[i] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
                conversionOccurred |= (argument != arguments[i]);
            }
        } else {
            for (int i = 0; i < varargsPosition; i++) {
                TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forExecutable(executable, i));
                Object argument = arguments[i];
                arguments[i] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
                conversionOccurred |= (argument != arguments[i]);
            }
            MethodParameter methodParam = MethodParameter.forExecutable(executable, varargsPosition);
            if (varargsPosition == arguments.length - 1) {
                Object argument = arguments[varargsPosition];
                TypeDescriptor targetType = new TypeDescriptor(methodParam);
                TypeDescriptor sourceType = TypeDescriptor.forObject(argument);

                if (!sourceType.equals(targetType.getElementTypeDescriptor())) {
                    arguments[varargsPosition] = converter.convertValue(argument, sourceType, targetType);
                }

                if (argument != arguments[varargsPosition]
                        && !isFirstEntryInArray(argument, arguments[varargsPosition])) {
                    conversionOccurred = true;
                }
            } else {
                TypeDescriptor targetType = new TypeDescriptor(methodParam).getElementTypeDescriptor();

                for (int i = varargsPosition; i < arguments.length; i++) {
                    Object argument = arguments[i];
                    arguments[i] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
                    conversionOccurred |= (argument != arguments[i]);
                }
            }
        }
        return conversionOccurred;
    }

    private static boolean isFirstEntryInArray(Object argument, Object argument1) {
        return false;
    }

    public static Object[] setupArgumentsForVarargsInvocation(Class<?>[] parameterTypes, Object[] arguments) {
        return new Object[0];
    }

    public enum ArgumentsMatchKind {
        EXACT, REQUIRES_CONVERSION, CLOSE;
    }

    public static class ArgumentsMatchInfo {
        private final ArgumentsMatchKind kind;

        public ArgumentsMatchInfo(ArgumentsMatchKind kind) {
            this.kind = kind;
        }

        public boolean isExactMatch() {
            return this.kind == ArgumentsMatchKind.EXACT;
        }

        public boolean isCloseMatch() {
            return this.kind == ArgumentsMatchKind.CLOSE;
        }

        public boolean isMatchRequiringConversion() {
            return this.kind == ArgumentsMatchKind.REQUIRES_CONVERSION;
        }


    }

}
