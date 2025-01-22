package com.savage.chatgames.games.rewards;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.Math;
import com.savage.chatgames.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MathRewards {

    ChatGames plugin = ChatGames.getPlugin();
    Math math = plugin.math;
    FileConfiguration config = ChatGames.getPlugin().getConfig();
    public void giveReward(Player player, int diff) {
        int reward = 0;
        String easycmd = "eco give " + player.getName() + " " + config.getInt("reward-short");
        String hardcmd = "eco give " + player.getName() + " " + config.getInt("reward-medium");
        if (diff == 1) {
            reward = config.getInt("reward-1");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), easycmd);
        } else if (diff == 2) {
            reward = config.getInt("reward-2");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), hardcmd);
        }
        // Give the player the defined reward
        player.sendMessage(ColorUtils.translateColorCodes("&aYou unscrambled the word and got $" + reward));
        // Code to add reward to player's account
    }
}
