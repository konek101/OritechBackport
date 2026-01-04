package rearth.oritech.compat;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * Compatibility layer for MC 1.21's ByteBufCodecs.
 * Provides StreamCodec instances for common types using MC 1.20.1's FriendlyByteBuf.
 */
public final class ByteBufCodecs {
    
    public static final StreamCodec<ByteBuf, Integer> INT = StreamCodec.of(
            (buf, value) -> {
                if (buf instanceof FriendlyByteBuf fb) fb.writeInt(value);
                else buf.writeInt(value);
            },
            buf -> buf instanceof FriendlyByteBuf fb ? fb.readInt() : buf.readInt()
    );
    
    public static final StreamCodec<ByteBuf, Integer> VAR_INT = StreamCodec.of(
            (buf, value) -> {
                if (buf instanceof FriendlyByteBuf fb) fb.writeVarInt(value);
                else buf.writeInt(value);
            },
            buf -> buf instanceof FriendlyByteBuf fb ? fb.readVarInt() : buf.readInt()
    );
    
    public static final StreamCodec<ByteBuf, Long> VAR_LONG = StreamCodec.of(
            (buf, value) -> {
                if (buf instanceof FriendlyByteBuf fb) fb.writeVarLong(value);
                else buf.writeLong(value);
            },
            buf -> buf instanceof FriendlyByteBuf fb ? fb.readVarLong() : buf.readLong()
    );
    
    public static final StreamCodec<ByteBuf, Long> LONG = StreamCodec.of(
            (buf, value) -> buf.writeLong(value),
            ByteBuf::readLong
    );
    
    public static final StreamCodec<ByteBuf, Float> FLOAT = StreamCodec.of(
            (buf, value) -> buf.writeFloat(value),
            ByteBuf::readFloat
    );
    
    public static final StreamCodec<ByteBuf, Double> DOUBLE = StreamCodec.of(
            (buf, value) -> buf.writeDouble(value),
            ByteBuf::readDouble
    );
    
    public static final StreamCodec<ByteBuf, Boolean> BOOL = StreamCodec.of(
            (buf, value) -> buf.writeBoolean(value),
            ByteBuf::readBoolean
    );
    
    public static final StreamCodec<ByteBuf, Byte> BYTE = StreamCodec.of(
            (buf, value) -> buf.writeByte(value),
            ByteBuf::readByte
    );
    
    public static final StreamCodec<ByteBuf, Short> SHORT = StreamCodec.of(
            (buf, value) -> buf.writeShort(value),
            ByteBuf::readShort
    );
    
    public static final StreamCodec<ByteBuf, String> STRING_UTF8 = StreamCodec.of(
            (buf, value) -> {
                if (buf instanceof FriendlyByteBuf fb) fb.writeUtf(value);
                else {
                    byte[] bytes = value.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    buf.writeInt(bytes.length);
                    buf.writeBytes(bytes);
                }
            },
            buf -> {
                if (buf instanceof FriendlyByteBuf fb) return fb.readUtf();
                int len = buf.readInt();
                byte[] bytes = new byte[len];
                buf.readBytes(bytes);
                return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            }
    );
    
    public static final StreamCodec<ByteBuf, byte[]> BYTE_ARRAY = StreamCodec.of(
            (buf, value) -> {
                if (buf instanceof FriendlyByteBuf fb) fb.writeByteArray(value);
                else {
                    buf.writeInt(value.length);
                    buf.writeBytes(value);
                }
            },
            buf -> {
                if (buf instanceof FriendlyByteBuf fb) return fb.readByteArray();
                int len = buf.readInt();
                byte[] bytes = new byte[len];
                buf.readBytes(bytes);
                return bytes;
            }
    );
    
    public static final StreamCodec<FriendlyByteBuf, CompoundTag> COMPOUND_TAG = StreamCodec.of(
            FriendlyByteBuf::writeNbt,
            FriendlyByteBuf::readNbt
    );
    
    public static final StreamCodec<FriendlyByteBuf, ItemStack> ITEM_STACK = StreamCodec.of(
            FriendlyByteBuf::writeItem,
            FriendlyByteBuf::readItem
    );
    
    public static final StreamCodec<FriendlyByteBuf, ItemStack> OPTIONAL_ITEM_STACK = StreamCodec.of(
            (buf, value) -> buf.writeItem(value != null ? value : ItemStack.EMPTY),
            buf -> {
                ItemStack stack = buf.readItem();
                return stack.isEmpty() ? ItemStack.EMPTY : stack;
            }
    );
    
    public static final StreamCodec<FriendlyByteBuf, BlockPos> BLOCK_POS = StreamCodec.of(
            FriendlyByteBuf::writeBlockPos,
            FriendlyByteBuf::readBlockPos
    );
    
    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION = StreamCodec.of(
            FriendlyByteBuf::writeResourceLocation,
            FriendlyByteBuf::readResourceLocation
    );
    
    /**
     * Creates a list codec from an element codec.
     */
    public static <B extends ByteBuf, V> StreamCodec<B, List<V>> list(StreamCodec<? super B, V> elementCodec) {
        return collection(ArrayList::new, elementCodec);
    }
    
    /**
     * Returns a codec operation that converts a codec to a list codec.
     */
    public static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, List<V>> list() {
        return codec -> list(codec);
    }
    
    /**
     * Creates a collection codec with a custom collection factory.
     */
    public static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec<B, C> collection(
            IntFunction<C> factory, StreamCodec<? super B, V> elementCodec) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                int size = buf instanceof FriendlyByteBuf fb ? fb.readVarInt() : buf.readInt();
                C collection = factory.apply(size);
                for (int i = 0; i < size; i++) {
                    collection.add(elementCodec.decode(buf));
                }
                return collection;
            }
            
            @Override
            public void encode(B buf, C value) {
                if (buf instanceof FriendlyByteBuf fb) fb.writeVarInt(value.size());
                else buf.writeInt(value.size());
                for (V element : value) {
                    elementCodec.encode(buf, element);
                }
            }
        };
    }
    
    /**
     * Creates a map codec.
     */
    public static <B extends ByteBuf, K, V, M extends Map<K, V>> StreamCodec<B, M> map(
            Supplier<M> factory,
            StreamCodec<? super B, K> keyCodec,
            StreamCodec<? super B, V> valueCodec) {
        return new StreamCodec<>() {
            @Override
            public M decode(B buf) {
                int size = buf instanceof FriendlyByteBuf fb ? fb.readVarInt() : buf.readInt();
                M map = factory.get();
                for (int i = 0; i < size; i++) {
                    K key = keyCodec.decode(buf);
                    V value = valueCodec.decode(buf);
                    map.put(key, value);
                }
                return map;
            }
            
            @Override
            public void encode(B buf, M value) {
                if (buf instanceof FriendlyByteBuf fb) fb.writeVarInt(value.size());
                else buf.writeInt(value.size());
                for (Map.Entry<K, V> entry : value.entrySet()) {
                    keyCodec.encode(buf, entry.getKey());
                    valueCodec.encode(buf, entry.getValue());
                }
            }
        };
    }
    
    private ByteBufCodecs() {}
}
