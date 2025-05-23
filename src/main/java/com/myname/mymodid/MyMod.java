package com.myname.mymodid;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MyMod.MODID, version = Tags.VERSION, name = "MyMod", acceptedMinecraftVersions = "[1.7.10]")
public class MyMod {

    public static final String MODID = "mymodid";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "com.myname.mymodid.ClientProxy", serverSide = "com.myname.mymodid.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void itemDrop(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (!ClientCommands.particles_enabled) return;

        if (event.phase == TickEvent.Phase.END) {
            var client = Minecraft.getMinecraft();
            var player = client.thePlayer;
            var itemEntities = client.theWorld.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(10.0, 5.0, 10.0));

            for (var item : itemEntities) {
                client.theWorld.spawnParticle("smoke", item.posX, item.posY, item.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void chatCommands(ClientChatReceivedEvent event) {
        var message = event.message.getUnformattedText();
        var split = message.split(";");
        if (split.length > 1) {
            var command = split[1];
            var flag = ClientCommands.executeCommand(command);

            if (flag) {
                event.setCanceled(true);
            }

            LOG.info("Command completed successfully? {}", flag);
        }
    }
}
