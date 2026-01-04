package rearth.oritech.compat;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Compatibility layer for MC 1.21's RegistryFriendlyByteBuf.
 * In MC 1.20.1, we use FriendlyByteBuf with a stored RegistryAccess.
 */
public class RegistryFriendlyByteBuf extends FriendlyByteBuf {
    
    private final RegistryAccess registryAccess;
    
    public RegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess) {
        super(source);
        this.registryAccess = registryAccess;
    }
    
    public RegistryAccess registryAccess() {
        return registryAccess;
    }
    
    /**
     * Creates a RegistryFriendlyByteBuf from a regular ByteBuf.
     * Uses the built-in registry access if available.
     */
    public static RegistryFriendlyByteBuf wrap(ByteBuf buf, RegistryAccess registryAccess) {
        if (buf instanceof RegistryFriendlyByteBuf rfb) {
            return rfb;
        }
        return new RegistryFriendlyByteBuf(buf, registryAccess);
    }
}
