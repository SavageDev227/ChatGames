package com.savage.chatgames;

import com.savage.chatgames.commands.ReloadCommand;
import com.savage.chatgames.commands.ScrambleCommand;
import com.savage.chatgames.commands.tabCompleter.ScrambleCommandTabCompleter;
import com.savage.chatgames.utils.ColorUtils;
import com.savage.chatgames.games.taskTimers.ScrambleTaskTimers;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ChatGames extends JavaPlugin {

    Logger log = this.getLogger();
    private static ChatGames plugin;

    public ScrambleTaskTimers scrambleTaskTimers;

    private final PluginDescriptionFile pluginInfo = getDescription();

    private final String pluginVersion = pluginInfo.getVersion();


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.20"))) {
            log.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            log.warning(ColorUtils.translateColorCodes("&6ChatGames: &4This plugin is only supported on the Minecraft versions listed below:"));
            log.warning(ColorUtils.translateColorCodes("&6ChatGames: &41.20.x"));
            log.warning(ColorUtils.translateColorCodes("&6ChatGames: &4Is now disabling!"));
            log.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else {
            log.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6ChatGames: &aA supported Minecraft version has been detected"));
            log.info(ColorUtils.translateColorCodes("&6ChatGames: &6Continuing plugin startup"));
            log.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }

        // Load the configuration
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Schedule the task to send messages
        scrambleTaskTimers = new ScrambleTaskTimers();

        getCommand("scramble").setExecutor(new ScrambleCommand());

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
                scrambleTaskTimers.startCountdownOne();
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
            if (Bukkit.getScheduler().isCurrentlyRunning(scrambleTaskTimers.taskId1) || Bukkit.getScheduler().isQueued(scrambleTaskTimers.taskId1)) {
                Bukkit.getScheduler().cancelTask(scrambleTaskTimers.taskId1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(scrambleTaskTimers.taskId2) || Bukkit.getScheduler().isQueued(scrambleTaskTimers.taskId2)) {
                Bukkit.getScheduler().cancelTask(scrambleTaskTimers.taskId2);
            }
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Stopped Scramble Timers"));
        } catch (Exception e) {
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Stopped Scramble Timers"));
        }

    }
}