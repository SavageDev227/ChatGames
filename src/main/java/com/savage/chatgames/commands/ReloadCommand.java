package com.savage.chatgames.commands;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.taskTimers.ScrambleTaskTimers;
import com.savage.chatgames.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
   ChatGames plugin = ChatGames.getPlugin();

   public ScrambleTaskTimers taskTimers;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("reloadchatgames")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                try {
                    plugin.reloadConfig();
                    taskTimers.startCountdownOne();
                    p.sendMessage(ColorUtils.translateColorCodes("&aConfig saved and reloaded successfully!"));
                    p.sendMessage(ColorUtils.translateColorCodes("&aScramble countdown started!"));
                } catch (Exception e) {
                    sender.sendMessage(ColorUtils.translateColorCodes("&cAn error occurred while reloading the config!"));
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }
}
