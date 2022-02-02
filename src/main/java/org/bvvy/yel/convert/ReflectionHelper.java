package org.bvvy.yel.convert;

import org.bvvy.yel.context.TypeConverter;

import java.util.List;

public class ReflectionHelper {
    public static ArgumentsMatchInfo compareArgumentsVarargs(List<TypeDescriptor> paramDescriptors, List<TypeDescriptor> argumentTypes, TypeConverter typeConverter) {
        return null;
    }

    public static ArgumentsMatchInfo compareArguments(List<TypeDescriptor> paramDescriptors, List<TypeDescriptor> argumentTypes, TypeConverter typeConverter) {
        return null;
    }

    public static int getTypeDifferenceWeight(List<TypeDescriptor> paramDescriptors, List<TypeDescriptor> argumentTypes) {
        return 0;
    }

    public static class ArgumentsMatchInfo {
        public boolean isExactMatch() {
            return false;
        }

        public boolean isCloseMatch() {
            return false;
        }

        public boolean isMatchRequiringConversion() {
            return false;
        }
    }
}
