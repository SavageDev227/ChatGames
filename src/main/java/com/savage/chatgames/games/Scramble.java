package com.savage.chatgames.games;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.utils.ColorUtils;
import com.savage.chatgames.utils.GameSystem;
import com.savage.chatgames.games.taskTimers.ScrambleTaskTimers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Scramble extends GameSystem {

    public boolean answerSubmitted = false;

    public ScrambleTaskTimers scrambleTaskTimers;

    public ChatGames plugin = ChatGames.getPlugin();

    Logger log = plugin.getLogger();

    public String lastScrambledWord;
    List<String> wordsToScramble = plugin.getConfig().getStringList("words-to-scramble");
    FileConfiguration config = ChatGames.getPlugin().getConfig();


    @Override
    public void taskSystem() {
        answerSubmitted = false;
        String wordToScramble;
        String scrambledWord;
        do {
            wordToScramble = getRandomWord();
            scrambledWord = scramble(wordToScramble);
        } while(containsBlacklistWords(scrambledWord));
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

    public boolean containsBlacklistWords(String word) {
        List<String> bannedWords = config.getStringList("blacklisted-words:");
        for(String bannedWord : bannedWords) {
            if(word.contains(bannedWord)) {
                return true;
            }
        }
        return false;
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
            if (Bukkit.getScheduler().isCurrentlyRunning(scrambleTaskTimers.taskId1)||Bukkit.getScheduler().isQueued(scrambleTaskTimers.taskId1)){
                Bukkit.getScheduler().cancelTask(scrambleTaskTimers.taskId1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(scrambleTaskTimers.taskId2)||Bukkit.getScheduler().isQueued(scrambleTaskTimers.taskId2)){
                Bukkit.getScheduler().cancelTask(scrambleTaskTimers.taskId2);
            }
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6ChatGames: &3Stopped Scramble Timers"));
        } catch (Exception e) {
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6ChatGames: &3Stopped Scramble Timers"));
        }
    }

    public void setAnswerSubmitted(boolean answerSubmitted) {
        this.answerSubmitted = answerSubmitted;
    }

    public boolean getAnswerSubmitted() {
        return answerSubmitted;
    }
}
