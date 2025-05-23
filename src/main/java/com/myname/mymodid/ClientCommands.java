package com.myname.mymodid;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class ClientCommands {
    private static boolean initialized = false;
    private static List<String> commands;

    public static boolean particles_enabled = false;

    public static void registerCommands() {
        if (initialized) {
            MyMod.LOG.warn("ClientCommands is already registered!");
            return;
        }

        initialized = true;

        commands = new ArrayList<>();
        commands.add("toggle"); // Toggles the item drop particles
    }

    public static boolean executeCommand(String command) {
        if (!initialized) {
            registerCommands();
        }

        if (!commands.contains(command)) return false;

        switch (command) {
            case "toggle":
                particles_enabled = !particles_enabled;
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("item particles " + particles_enabled));
                break;
        }

        return true;
    }
}
