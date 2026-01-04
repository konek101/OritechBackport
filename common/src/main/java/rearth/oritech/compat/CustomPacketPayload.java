package rearth.oritech.compat;

import net.minecraft.resources.ResourceLocation;

/**
 * Compatibility layer for MC 1.21's CustomPacketPayload.
 * In MC 1.20.1, we use this as a marker interface for network packets.
 */
public interface CustomPacketPayload {
    
    /**
     * Returns the type of this payload.
     */
    Type<?> type();
    
    /**
     * Represents the type/ID of a custom packet payload.
     */
    record Type<T extends CustomPacketPayload>(ResourceLocation id) {
        public Type(ResourceLocation id) {
            this.id = id;
        }
    }
}
