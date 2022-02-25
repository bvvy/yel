package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.ValueRef;
import org.bvvy.yel.exp.YelMessage;
import org.bvvy.yel.util.NumberUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Indexer extends NodeImpl {
    public Indexer(int startPos, int endPos, Node expr) {
        super(startPos, endPos, expr);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return getValueRef(state).getValue();
    }

    @Override
    public ValueRef getValueRef(ExpressionState state) {
        TypedValue context = state.getActiveContextObject();
        Object target = context.getValue();
        TypedValue indexValue;
        Object index;
        if (target == null) {
            throw new YelEvalException(getStartPosition(), YelMessage.CANNOT_INDEX_INTO_NULL_VALUE);
        }

        state.pushActiveContextObject(state.getRootContextObject());
        indexValue = this.children[0].getValueInternal(state);
        index = indexValue.getValue();
        state.popActiveContextObject();

        // ------------------------ Map index -----------------------
        if (target instanceof Map) {
            Object key = index;
            return new MapIndexingValueRef(((Map<?, ?>) target), key);
        }
        // ------------------------ Collection index -----------------------
        if (target.getClass().isArray() || target instanceof Collection) {
            int idx = NumberUtils.convertNumberToTargetClass((Number) index, Integer.class);
            if (target.getClass().isArray()) {
                return new ArrayIndexingValueRef(target, idx);
            } else {
                return new CollectionIndexingValueRef((Collection<?>) target, idx);
            }
        }
        throw new YelEvalException(YelMessage.NOT_ASSIGNABLE                                                                                                                                                                                                                                                                                                                                                 );
    }

    private class MapIndexingValueRef implements ValueRef {

        private final Map<?, ?> map;
        private final Object key;

        public MapIndexingValueRef(Map<?, ?> map, Object key) {
            this.map = map;
            this.key = key;
        }

        @Override
        public TypedValue getValue() {
            Object value = this.map.get(this.key);
            return new TypedValue(value);
        }

    }


    private class ArrayIndexingValueRef implements ValueRef {
        private final Object array;
        private final int index;

        @Override
        public TypedValue getValue() {
//            Object arrayElement = accessArrayElement(this.array, this.index);
            Object arrayElement = ((Object[]) array)[index];
            return new TypedValue(arrayElement);
        }

        public ArrayIndexingValueRef(Object array, int index) {
            this.array = array;
            this.index = index;
        }
    }

    private Object accessArrayElement(Object ctx, int idx) {
        Class<?> arrayComponentType = ctx.getClass().getComponentType();
        if (arrayComponentType == Boolean.TYPE) {
            boolean[] array = (boolean[]) ctx;
            checkAccess(array.length, idx);
            return array[idx];
        } else if (arrayComponentType == Byte.TYPE) {
            byte[] array = (byte[]) ctx;
            checkAccess(array.length, idx);
            return array[idx];
        }

        return null;
    }

    private void checkAccess(int length, int index) {
        if (index >= length) {
            throw new YelEvalException(getStartPosition(), YelMessage.ARRAY_INDEX_OUT_OF_BOUNDS, length, index);
        }

    }

    private class CollectionIndexingValueRef implements ValueRef {
        private final Collection<?> collection;
        private final int index;

        public CollectionIndexingValueRef(Collection<?> collection, int index) {
            this.collection = collection;
            this.index = index;
        }

        @Override
        public TypedValue getValue() {
            if (this.collection instanceof List) {
                Object o = ((List<?>) this.collection).get(index);
                return new TypedValue(o);
            }
            throw new IllegalStateException("index fail");
        }

    }
}
