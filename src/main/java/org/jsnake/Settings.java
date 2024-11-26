package org.jsnake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A játék beállításait tároló osztály.
 */
public class Settings {
    /**
     * A beállításokat tartalmazó map.
     */
    private static Map<String, Object> settings;

    /**
     * A beállítások fájlja.
     */
    private static final String SETTINGS_FILE = "src/main/resources/settings.dat";

    /**
     * A tábla színének kulcsa.
     */
    public static final String BOARD_COLOR = "boardColor";

    /**
     * A játékos kígyójának színének kulcsa.
     */
    public static final String PLAYER_SNAKE_COLOR = "playerSnakeColor";

    /**
     * Az AI kígyójának színének kulcsa.
     */
    public static final String AI_SNAKE_COLOR = "aiSnakeColor";
    
    /**
     * Konstruktor. Betölti a beállításokat a fájlból.
     */
    Settings(){
        settings = new HashMap<>();
        loadSettings();
    }

    /**
     * Visszaadja a beállításokat tartalmazó map-et.
     * @return a beállításokat tartalmazó map
     */
    public Map<String, Object> getSettings(){
        return settings;
    }

    /**
     * Betölti a beállításokat a fájlból.
     */
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

    /**
     * Elmenti a beállításokat a fájlba.
     */
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
