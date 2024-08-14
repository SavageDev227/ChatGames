package com.savage.chatgames;

import com.savage.chatgames.utils.ColorUtils;
import com.savage.chatgames.utils.TaskTimers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ChatGames extends JavaPlugin {

    Logger log = this.getLogger();
    private static ChatGames plugin;

    public TaskTimers taskTimers;

    private final PluginDescriptionFile pluginInfo = getDescription();

    private final String pluginVersion = pluginInfo.getVersion();

    public static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.20"))){
            log.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            log.warning(ColorUtils.translateColorCodes("&6WordScrambler: &4This plugin is only supported on the Minecraft versions listed below:"));
            log.warning(ColorUtils.translateColorCodes("&6WordScrambler: &41.20.x"));
            log.warning(ColorUtils.translateColorCodes("&6WordScrambler: &4Is now disabling!"));
            log.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else {
            log.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &aA supported Minecraft version has been detected"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &6Continuing plugin startup"));
            log.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }
        //Check for vault
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", pluginInfo.getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load the configuration
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Schedule the task to send messages
        taskTimers = new TaskTimers();

        //TODO: Register the tab completer

        // Plugin startup message
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin by: &b&lSavageDev & Loving11ish"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3has been loaded successfully"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin Version: &d&l" + pluginVersion));
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));

        // Schedule first word
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                taskTimers.startCountdownOne();
            }
        }, 100L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stopCountdown();

        // Plugin shutdown message
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin by: &b&lSavageDev"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3has been disabled successfully"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin Version: &d&l" + pluginVersion));
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
    }

    public static ChatGames getPlugin() {
        return plugin;
    }

    public void stopCountdown() {
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(taskTimers.taskId1)||Bukkit.getScheduler().isQueued(taskTimers.taskId1)){
                Bukkit.getScheduler().cancelTask(taskTimers.taskId1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(taskTimers.taskId2)||Bukkit.getScheduler().isQueued(taskTimers.taskId2)){
                Bukkit.getScheduler().cancelTask(taskTimers.taskId2);
            }
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Stopped Scramble Timers"));
        } catch (Exception e) {
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Stopped Scramble Timers"));
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null || !getServer().getPluginManager().isPluginEnabled("Vault")) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() { return econ; }
}
