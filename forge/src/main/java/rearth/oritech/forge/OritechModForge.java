package rearth.oritech.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rearth.oritech.Oritech;
import rearth.oritech.item.tools.util.ArmorEventHandler;

@Mod(Oritech.MOD_ID)
public final class OritechModForge {
    
    public OritechModForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register mod lifecycle events
        modEventBus.addListener(this::commonSetup);
        
        // Register game events
        MinecraftForge.EVENT_BUS.register(this);
        
        // Initialize the common mod code
        Oritech.initialize();
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup code here
    }
    
    @SubscribeEvent
    public void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        ArmorEventHandler.processEvent(event.getEntity(), event.getSlot(), event.getFrom(), event.getTo());
    }
}
