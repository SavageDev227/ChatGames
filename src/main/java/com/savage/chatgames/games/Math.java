package com.savage.chatgames.games;

import com.savage.chatgames.ChatGames;
import com.savage.chatgames.utils.ColorUtils;
import com.savage.chatgames.utils.GameSystem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

public class Math extends GameSystem {

    public Integer difficulty;

    public boolean answerSubmitted = false;
    public ChatGames plugin = ChatGames.getPlugin();
    FileConfiguration config = ChatGames.getPlugin().getConfig();

    public Integer result;

    @Override
    public void taskSystem() {
        answerSubmitted = false;

        String problem = generateMathProblem();
        String mathAnnouncement = config.getString("math-announcement");
        String mathPrefix = config.getString("prefix");

        // Replace placeholders with actual values
        mathAnnouncement = mathAnnouncement.replace("%prefix%", mathPrefix);
        mathAnnouncement = mathAnnouncement.replace("%problem%", problem);

        // Broadcast the message
        Bukkit.broadcastMessage(ColorUtils.translateColorCodes(mathAnnouncement));

    }

    public String generateMathProblem() {
        Random random = new Random();

        // Generate two random numbers
        int num1 = random.nextInt(100) + 1; // Range: 1-100
        int num2 = random.nextInt(100) + 1; // Range: 1-100

        // Randomly select an operator
        char[] operators = {'+', '-', '*', '/'};
        char operator = operators[random.nextInt(operators.length)];

        // Ensure division results in an integer
        if (operator == '/') {
            num1 = num1 * num2; // Ensure num1 is divisible by num2
        }

        if(operator == '+' || operator == '-') {
            difficulty = 1;
        } else {
            difficulty = 2;
        }

        result = calculate(num1, num2, operator);

        return num1 + " " + operator + " " + num2;
    }

    public static int calculate(int num1, int num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2; // Safe because num1 is divisible by num2
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public void setAnswerSubmitted(boolean answerSubmitted) {
        this.answerSubmitted = answerSubmitted;
    }
}
