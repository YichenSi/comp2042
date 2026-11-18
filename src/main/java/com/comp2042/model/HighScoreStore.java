package com.comp2042.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HighScoreStore {

    private final Path file;

    public HighScoreStore() {
        this(Paths.get(System.getProperty("user.home"), ".snake-highscore.txt"));
    }

    HighScoreStore(Path file) {
        this.file = file;
    }

    public int load() {
        try {
            if (Files.exists(file)) {
                return Integer.parseInt(Files.readString(file).trim());
            }
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    public boolean save(int score) {
        if (score <= load()) {
            return false;
        }
        try {
            Files.writeString(file, String.valueOf(score));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
