package org.bvvy.yel.context;

import org.bvvy.yel.convert.ConversionService;
import org.bvvy.yel.convert.TypeDescriptor;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class StandardTypeConverter implements TypeConverter {

    private final ConversionService conversionService;

    public StandardTypeConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }


    @Override
    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.conversionService.canConvert(sourceType, targetType);
    }


}
