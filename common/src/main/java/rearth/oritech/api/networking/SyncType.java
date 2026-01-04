package rearth.oritech.api.networking;

import rearth.oritech.compat.RegistryFriendlyByteBuf;
import rearth.oritech.compat.StreamCodec;

public enum SyncType {
    
    INITIAL, TICK, SPARSE_TICK, GUI_TICK, GUI_OPEN, CUSTOM;
    
    public static StreamCodec<RegistryFriendlyByteBuf, SyncType> PACKET_CODEC = new StreamCodec<>() {
        @Override
        public SyncType decode(RegistryFriendlyByteBuf buf) {
            return SyncType.values()[buf.readUnsignedShort()];
        }
        
        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncType value) {
            buf.writeShort(value.ordinal());
        }
    };
    
}
