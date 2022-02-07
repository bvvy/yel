package org.bvvy.yel.context;

import org.bvvy.yel.convert.TypeDescriptor;

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
                        if (varargsParamType.isAssignableFrom(suppliedArg.getType())) {
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

    public static int getTypeDifferenceWeight(List<TypeDescriptor> paramDescriptors, List<TypeDescriptor> argumentTypes) {
        return 0;
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
