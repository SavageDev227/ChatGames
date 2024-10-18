package com.savage.chatgames.games.taskTimers;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.games.Scramble;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ScrambleTaskTimers {

    public Integer taskId1;
    public Integer taskId2;

    ChatGames wordScramblerPlugin = ChatGames.getPlugin();
    FileConfiguration config = ChatGames.getPlugin().getConfig();

    Scramble scramble = new Scramble();

    public void startCountdownOne() {
        taskId1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(ChatGames.getPlugin(), new Runnable() {
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
