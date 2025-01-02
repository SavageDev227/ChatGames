package com.savage.chatgames.games.taskTimers;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.Scramble;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class TaskTimers {

    public Integer taskId1;
    public Integer taskId2;

    ChatGames plugin = ChatGames.getPlugin();
    FileConfiguration config = ChatGames.getPlugin().getConfig();

    Scramble scramble = plugin.scramble;

    public void startCountdownOne() {
        taskId1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(ChatGames.getPlugin(), new Runnable() {

            // Time in seconds in between each scramble
            int time = 600;
            @Override
            public void run() {
                if (time == 0) {
                    scramble.taskSystem();
                    startCountdownTwo();
                    Bukkit.getScheduler().cancelTask(taskId1);
                    return;
                }else {
                    time --;
                }
            }
        }, 0, 20);
    }

    public void startCountdownTwo(){
        taskId2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(ChatGames.getPlugin(), new Runnable() {
            int time = 600;
            @Override
            public void run() {
                if (time == 0){
                    scramble.taskSystem();
                    startCountdownOne();
                    Bukkit.getScheduler().cancelTask(taskId2);
                    return;
                }else {
                    time --;
                }

            }
        }, 0, 20);
    }
}
