package org.bvvy.yel.convert;

import org.bvvy.yel.convert.converter.*;
import org.bvvy.yel.convert.support.NumberToNumberConverterFactory;

import java.util.*;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class StandardConversionService implements ConversionService {

    private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
    private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
    private final Converters converters = new Converters();
    private Map<ConverterCacheKey, GenericConverter> converterCache = new HashMap<>();

    public StandardConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(StandardConversionService converterRegistry) {
        converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());
    }

    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
        if (typeInfo == null) {
            throw new IllegalArgumentException();
        }
        addConverter(new ConverterFactoryAdapter(factory, new ConvertiblePair(typeInfo[0].toClass(), typeInfo[1].toClass())));
    }

    public void addConverter(GenericConverter converter) {
        this.converters.add(converter);
    }

    private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
        ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
        ResolvableType[] generics = resolvableType.getGenerics();
        if (generics.length < 2) {
            return null;
        }
        Class<?> sourceType = generics[0].resolve();
        Class<?> targetType = generics[1].resolve();
        if (sourceType == null || targetType == null) {
            return null;
        }
        return generics;
    }

    @Override
    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType == null) {
            return true;
        }
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
        ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
        GenericConverter converter = this.converterCache.get(key);
        if (converter != null) {
            return (converter != NO_MATCH ? converter : null);
        }
        converter = this.converters.find(sourceType, targetType);
        if (converter == null) {
            converter = getDefaultConverter(sourceType, targetType);
        }
        if (converter != null) {
            this.converterCache.put(key, converter);
            return converter;
        }
        this.converterCache.put(key, NO_MATCH);
        return null;
    }

    private GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return (sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        if (sourceType == null) {
            return handleResult(null, targetType, convertNullSource(null, targetType));
        }
        if (source != null && !sourceType.getObjectType().isInstance(source)) {
            throw new IllegalArgumentException("Source to convert from must be an instance of [" +
                    sourceType + "]; instead it was a [" + source.getClass().getName() + "]");
        }
        GenericConverter converter = getConverter(sourceType, targetType);
        if (converter != null) {
            Object result = converter.convert(source, sourceType, targetType);
            return handleResult(sourceType, targetType, result);
        }
        return handleConverterNotFound(source, sourceType, targetType);
    }

    private Object handleConverterNotFound(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return null;
    }

    private Object handleResult(TypeDescriptor sourceType, TypeDescriptor targetType, Object result) {
        if (result == null) {
//            assertNotPrimitiveTargetType(sourceType, targetType);
        }
        return result;
    }

    private Object convertNullSource(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (targetType.getObjectType() == Optional.class) {
            return Optional.empty();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public final class ConverterFactoryAdapter implements ConditionalGenericConverter {
        private ConverterFactory<Object, Object> converterFactory;
        private ConvertiblePair typeInfo;

        public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, ConvertiblePair typeInfo) {
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
            this.typeInfo = typeInfo;
        }

        @Override
        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            boolean matches = false;
            if (this.converterFactory instanceof ConditionalConverter) {
                matches = ((ConditionalConverter) this.converterFactory).matches(sourceType, targetType);
            }
            if (matches) {
                Converter<Object, ?> converter = this.converterFactory.getConverter(targetType.getType());
                if (converter instanceof ConditionalConverter) {
                    matches = ((ConditionalConverter) converter).matches(sourceType, targetType);
                }
            }
            return matches;
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null) {
                return convertNullSource(sourceType, targetType);
            }
            return this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }
    }

}
