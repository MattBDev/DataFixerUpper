package com.mojang.datafixers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.types.DynamicOps;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public abstract class DynamicLike<T> {
    protected final DynamicOps<T> ops;

    public DynamicLike(final DynamicOps<T> ops) {
        this.ops = ops;
    }

    public DynamicOps<T> getOps() {
        return ops;
    }

    public abstract Optional<Number> asNumber();
    public abstract Optional<String> asString();
    public abstract Optional<Stream<Dynamic<T>>> asStreamOpt();
    public abstract Optional<ByteBuffer> asByteBufferOpt();
    public abstract Optional<IntStream> asIntStreamOpt();
    public abstract Optional<LongStream> asLongStreamOpt();
    public abstract OptionalDynamic<T> get(String key);
    public abstract Optional<T> getGeneric(T key);
    public abstract Optional<T> getElement(String key);
    public abstract Optional<T> getElementGeneric(T key);
    public abstract <U> Optional<List<U>> asListOpt(Function<Dynamic<T>, U> deserializer);
    public abstract <K, V> Optional<Map<K, V>> asMapOpt(Function<Dynamic<T>, K> keyDeserializer, Function<Dynamic<T>, V> valueDeserializer);

    public Number asNumber(final Number defaultValue) {
        return asNumber().orElse(defaultValue);
    }

    public int asInt(final int defaultValue) {
        return asNumber(defaultValue).intValue();
    }

    public long asLong(final long defaultValue) {
        return asNumber(defaultValue).longValue();
    }

    public float asFloat(final float defaultValue) {
        return asNumber(defaultValue).floatValue();
    }

    public double asDouble(final double defaultValue) {
        return asNumber(defaultValue).doubleValue();
    }

    public byte asByte(final byte defaultValue) {
        return asNumber(defaultValue).byteValue();
    }

    public short asShort(final short defaultValue) {
        return asNumber(defaultValue).shortValue();
    }

    public boolean asBoolean(final boolean defaultValue) {
        return asNumber(defaultValue ? 1 : 0).intValue() != 0;
    }

    public String asString(final String defaultValue) {
        return asString().orElse(defaultValue);
    }

    public Stream<Dynamic<T>> asStream() {
        return asStreamOpt().orElseGet(Stream::empty);
    }

    public ByteBuffer asByteBuffer() {
        return asByteBufferOpt().orElseGet(() -> ByteBuffer.wrap(new byte[0]));
    }

    public IntStream asIntStream() {
        return asIntStreamOpt().orElseGet(IntStream::empty);
    }

    public LongStream asLongStream() {
        return asLongStreamOpt().orElseGet(LongStream::empty);
    }

    public <U> List<U> asList(final Function<Dynamic<T>, U> deserializer) {
        return asListOpt(deserializer).orElseGet(ImmutableList::of);
    }

    public <K, V> Map<K, V> asMap(final Function<Dynamic<T>, K> keyDeserializer, final Function<Dynamic<T>, V> valueDeserializer) {
        return asMapOpt(keyDeserializer, valueDeserializer).orElseGet(ImmutableMap::of);
    }

    public T getElement(final String key, final T defaultValue) {
        return getElement(key).orElse(defaultValue);
    }

    public T getElementGeneric(final T key, final T defaultValue) {
        return getElementGeneric(key).orElse(defaultValue);
    }

    public Dynamic<T> emptyList() {
        return new Dynamic<>(ops, ops.emptyList());
    }

    public Dynamic<T> emptyMap() {
        return new Dynamic<>(ops, ops.emptyMap());
    }

    public Dynamic<T> createNumeric(final Number i) {
        return new Dynamic<>(ops, ops.createNumeric(i));
    }

    public Dynamic<T> createByte(final byte value) {
        return new Dynamic<>(ops, ops.createByte(value));
    }

    public Dynamic<T> createShort(final short value) {
        return new Dynamic<>(ops, ops.createShort(value));
    }

    public Dynamic<T> createInt(final int value) {
        return new Dynamic<>(ops, ops.createInt(value));
    }

    public Dynamic<T> createLong(final long value) {
        return new Dynamic<>(ops, ops.createLong(value));
    }

    public Dynamic<T> createFloat(final float value) {
        return new Dynamic<>(ops, ops.createFloat(value));
    }

    public Dynamic<T> createDouble(final double value) {
        return new Dynamic<>(ops, ops.createDouble(value));
    }

    public Dynamic<T> createBoolean(final boolean value) {
        return new Dynamic<>(ops, ops.createBoolean(value));
    }

    public Dynamic<T> createString(final String value) {
        return new Dynamic<>(ops, ops.createString(value));
    }

    public Dynamic<T> createList(final Stream<? extends Dynamic<?>> input) {
        return new Dynamic<>(ops, ops.createList(input.map(element -> element.cast(ops))));
    }

    public Dynamic<T> createMap(final Map<? extends Dynamic<?>, ? extends Dynamic<?>> map) {
        final ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        for (final Map.Entry<? extends Dynamic<?>, ? extends Dynamic<?>> entry : map.entrySet()) {
            builder.put(entry.getKey().cast(ops), entry.getValue().cast(ops));
        }
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    public Dynamic<?> createByteList(final ByteBuffer input) {
        return new Dynamic<>(ops, ops.createByteList(input));
    }

    public Dynamic<?> createIntList(final IntStream input) {
        return new Dynamic<>(ops, ops.createIntList(input));
    }

    public Dynamic<?> createLongList(final LongStream input) {
        return new Dynamic<>(ops, ops.createLongList(input));
    }
}
