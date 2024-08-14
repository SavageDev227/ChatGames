package com.savage.chatgames.games;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.utils.ColorUtils;
import com.savage.chatgames.utils.GameSystem;
import com.savage.chatgames.utils.TaskTimers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Scramble extends GameSystem {

    public boolean answerSubmitted = false;

    public TaskTimers taskTimers;

    public ChatGames plugin = ChatGames.getPlugin();

    Logger log = plugin.getLogger();

    public String lastScrambledWord;

    public Economy economy = ChatGames.getEconomy();
    List<String> wordsToScramble = plugin.getConfig().getStringList("words-to-scramble");

    FileConfiguration config = ChatGames.getPlugin().getConfig();


    @Override
    public void taskSystem() {
        answerSubmitted = false;
        String wordToScramble = getRandomWord();
        String scrambledWord = scramble(wordToScramble.toLowerCase());
        String scrambleAnnouncement = config.getString("scramble-announcement");
        String scramblePrefix = config.getString("announcement-prefix");

        // Replace placeholders with actual values
        scrambleAnnouncement = scrambleAnnouncement.replace("%scramble-prefix%", scramblePrefix);
        scrambleAnnouncement = scrambleAnnouncement.replace("%scrambled-word%", scrambledWord);

        // Broadcast the message
        Bukkit.broadcastMessage(ColorUtils.translateColorCodes(scrambleAnnouncement));

        lastScrambledWord = wordToScramble;
    }

    private String scramble(String word) {
        Random random = new Random();
        char[] chars = word.toCharArray();
        String scrambledWord;
        do {
            for (int i = chars.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;
            }
            scrambledWord = new String(chars);
        } while (scrambledWord.equals(word));
        return scrambledWord;
    }

    private String getRandomWord() {
        if (wordsToScramble.isEmpty()) {
            throw new RuntimeException("No words to scramble.");
        }
        int randomIndex = new Random().nextInt(wordsToScramble.size());
        return wordsToScramble.get(randomIndex);
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

}
