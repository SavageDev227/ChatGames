package com.savage.chatgames;

import com.savage.chatgames.commands.ReloadCommand;
import com.savage.chatgames.commands.ScrambleCommand;
import com.savage.chatgames.commands.tabCompleter.ScrambleCommandTabCompleter;
import com.savage.chatgames.games.Scramble;
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

        // Load the configuration
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Schedule the task to send messages
        scrambleTaskTimers = new ScrambleTaskTimers();

        getCommand("scramble").setExecutor(new ScrambleCommand());
        getCommand("scramble").setTabCompleter(new ScrambleCommandTabCompleter());

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