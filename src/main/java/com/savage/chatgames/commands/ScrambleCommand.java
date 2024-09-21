package com.savage.chatgames.commands;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.Scramble;
import com.savage.chatgames.games.rewards.ScrambleRewards;
import com.savage.chatgames.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ScrambleCommand implements CommandExecutor {
    Scramble scramble = new Scramble();

    ChatGames plugin = ChatGames.getPlugin();
    ScrambleRewards rewards = new ScrambleRewards();
    FileConfiguration config = plugin.getConfig();
    Logger log = ChatGames.getPlugin().getLogger();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("scramble")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                // check if answer is correct
                if (args.length == 2 && args[0].equals("answer") && args[1].equals(scramble.lastScrambledWord)) {
                    if(scramble.answerSubmitted) {
                        p.sendMessage(ColorUtils.translateColorCodes("&cThe answer has already been submitted!\n&bPlease wait for the next puzzle!"));
                    } else {
                        rewards.giveReward(p, scramble.lastScrambledWord);
                        scramble.answerSubmitted = true;
                        String unscrambleAnnouncement = config.getString("scramble-answer");
                        String scramblePrefix = config.getString("announcement-prefix");

                        // Replace placeholders with actual values
                        unscrambleAnnouncement = unscrambleAnnouncement.replace("%scramble-prefix%", scramblePrefix);
                        unscrambleAnnouncement = unscrambleAnnouncement.replace("%unscrambled-word%", scramble.lastScrambledWord);
                        unscrambleAnnouncement = unscrambleAnnouncement.replace("%player%", p.getName());

                        // Broadcast the message
                        Bukkit.broadcastMessage(ColorUtils.translateColorCodes(unscrambleAnnouncement));
                    }
                    return true;
                } else if (args.length == 1 && args[0].equals("start") && p.hasPermission("scramble.staff")) {
                    scramble.stopCountdown();
                    scramble.scrambleTaskTimers.startCountdownOne();
                    log.info(String.format("[%s] - Started Scramble Timers", plugin.getDescription().getName()));
                    p.sendMessage(ColorUtils.translateColorCodes("&cScramble countdown started!"));
                } else if (args.length == 1 && args[0].equals("stop") && p.hasPermission("scramble.staff")) {
                    scramble.stopCountdown();
                    p.sendMessage(ColorUtils.translateColorCodes("&cScramble countdown stopped!"));
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes("&cWrong answer or syntax"));
                    return true;
                }
            }
        }
        return false;
    }
}
