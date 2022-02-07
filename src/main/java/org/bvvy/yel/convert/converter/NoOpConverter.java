package org.bvvy.yel.convert.converter;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class NoOpConverter implements GenericConverter {
    private String name;

    public NoOpConverter(String name) {
        this.name = name;
    }

}
