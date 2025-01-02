package com.savage.chatgames.commands;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.Math;
import com.savage.chatgames.games.rewards.MathRewards;
import com.savage.chatgames.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MathCommand implements CommandExecutor {

    ChatGames plugin = ChatGames.getPlugin();
    Math math = plugin.math;

    Integer result = math.result;

    FileConfiguration config = plugin.getConfig();

    MathRewards rewards = new MathRewards();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("math")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ColorUtils.translateColorCodes("&cThis must be run by a player"));
                return true;
            }
            if(args.length == 2 && args[0].equals("answer")) {
                if(args.length == 2 && args[1] == null) {
                    p.sendMessage(ColorUtils.translateColorCodes("&cWrong Syntax!"));
                    return true;
                }
                if(math.answerSubmitted) {
                    p.sendMessage(ColorUtils.translateColorCodes("&cThe answer has already been submitted!\n&bPlease wait for the next puzzle!"));
                    return true;
                }

                if(args[1].equalsIgnoreCase(String.valueOf(result))) {
                    rewards.giveReward(p);
                    math.setAnswerSubmitted(true);
                    String solveAnnouncement = config.getString("math-answer");
                    String mathPrefix = config.getString("prefix");

                    // Replace placeholders with actual values
                    //TODO: Replace placeholders
                    if (solveAnnouncement != null) {
                        solveAnnouncement = solveAnnouncement.replace("%scramble-prefix%", mathPrefix != null ? mathPrefix : "");
                        solveAnnouncement = solveAnnouncement.replace("%unscrambled-word%", String.valueOf(result));
                        solveAnnouncement = solveAnnouncement.replace("%player%", p.getName());

                        // Broadcast the message
                        Bukkit.broadcastMessage(ColorUtils.translateColorCodes(solveAnnouncement));
                    }
                } else {
                    p.sendMessage(ColorUtils.translateColorCodes("&cWrong answer! Try again."));
                    return true;
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes("&cWrong answer or syntax"));
                return true;
            }
        }
        return false;
    }
}
