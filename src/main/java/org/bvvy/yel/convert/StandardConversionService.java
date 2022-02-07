package org.bvvy.yel.convert;

import org.bvvy.yel.convert.converter.ConverterCacheKey;
import org.bvvy.yel.convert.converter.GenericConverter;
import org.bvvy.yel.convert.converter.NoOpConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class StandardConversionService implements ConversionService {

    private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
    private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
    private final Converters converters = new Converters();
    private Map<ConverterCacheKey, GenericConverter> converterCache = new HashMap<>();

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

}
