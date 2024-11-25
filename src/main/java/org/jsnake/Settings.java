package org.jsnake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
    private static Map<String, Object> settings;
    private static final String SETTINGS_FILE = "src/main/resources/settings.dat";
    public static final String BOARD_COLOR = "boardColor";
    public static final String PLAYER_SNAKE_COLOR = "playerSnakeColor";
    public static final String AI_SNAKE_COLOR = "aiSnakeColor";
    


    Settings(){
        settings = new HashMap<>();
        loadSettings();
    }

    public Map<String, Object> getSettings(){
        return settings;
    }

    private void loadSettings() {
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                Map<String, Object> loadedSettings = (Map<String, Object>) ois.readObject();
                settings.putAll(loadedSettings);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveSettings() {
        try {
            File file = new File(SETTINGS_FILE);
            file.getParentFile().mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(new HashMap<>(settings));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
