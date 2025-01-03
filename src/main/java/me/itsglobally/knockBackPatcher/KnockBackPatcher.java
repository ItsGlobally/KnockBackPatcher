package me.itsglobally.knockBackPatcher;

import org.bukkit.plugin.java.JavaPlugin;

import me.itsglobally.knockBackPatcher.listeners.kb;

public final class KnockBackPatcher extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new kb(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
