package org.bvvy.yel.exp.converter;

import java.util.Objects;

public class BytecodeDescriptorKey {
    private String sourceDescriptor;
    private String targetDescriptor;


    public BytecodeDescriptorKey(String sourceDescriptor, String targetDescriptor) {
        this.sourceDescriptor = sourceDescriptor;
        this.targetDescriptor = targetDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BytecodeDescriptorKey that = (BytecodeDescriptorKey) o;
        return Objects.equals(sourceDescriptor, that.sourceDescriptor) && Objects.equals(targetDescriptor, that.targetDescriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceDescriptor, targetDescriptor);
    }
}
