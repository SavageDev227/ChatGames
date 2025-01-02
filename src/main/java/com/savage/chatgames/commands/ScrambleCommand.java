package com.savage.chatgames.commands;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.Scramble;
import com.savage.chatgames.games.rewards.ScrambleRewards;
import com.savage.chatgames.utils.ColorUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Logger;

public class ScrambleCommand implements CommandExecutor {

    ChatGames plugin = ChatGames.getPlugin();
    Scramble scramble = plugin.scramble;
    ScrambleRewards rewards = new ScrambleRewards();
    FileConfiguration config = plugin.getConfig();
    Logger log = ChatGames.getPlugin().getLogger();

    List<String> bannedWords = config.getStringList("blacklisted-words");

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("scramble")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ColorUtils.translateColorCodes("&cThis must be run by a player"));
                return true;
            }
            Player p = (Player) sender;
            // check if answer is correct
            if (args.length == 2 && args[0].equals("answer")) {
                if (args[1] == null) {
                    p.sendMessage(ColorUtils.translateColorCodes("&cWrong syntax!"));
                    return true;
                }
                if (scramble.answerSubmitted) {
                    p.sendMessage(ColorUtils.translateColorCodes("&cThe answer has already been submitted!\n&bPlease wait for the next puzzle!"));
                    return true;
                }

                if(config.getBoolean("enable-autoban")) {
                    for (String bannedWord : bannedWords) {
                        if(args[0].equalsIgnoreCase(bannedWord)) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban" + p.getName() + "Banned word in scramble answer");
                        }
                    }
                }

                if (args[1].equalsIgnoreCase(scramble.lastScrambledWord)) {
                    rewards.giveReward(p, scramble.lastScrambledWord);
                    scramble.setAnswerSubmitted(true);
                    String unscrambleAnnouncement = config.getString("scramble-answer");
                    String scramblePrefix = config.getString("prefix");

                    // Replace placeholders with actual values
                    if (unscrambleAnnouncement != null) {
                        unscrambleAnnouncement = unscrambleAnnouncement.replace("%prefix%", scramblePrefix != null ? scramblePrefix : "");
                        unscrambleAnnouncement = unscrambleAnnouncement.replace("%unscrambled-word%", scramble.lastScrambledWord);
                        unscrambleAnnouncement = unscrambleAnnouncement.replace("%player%", p.getName());

                        // Broadcast the message
                        Bukkit.broadcastMessage(ColorUtils.translateColorCodes(unscrambleAnnouncement));
                    }
                } else {
                    p.sendMessage(ColorUtils.translateColorCodes("&cWrong answer! Try again."));
                }
                return true;
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes("&cWrong answer or syntax"));
                return true;
            }
        }
        return false;
    }
}
