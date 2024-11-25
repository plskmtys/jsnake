package org.jsnake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

public class SettingsTest {
    
    private Settings settings;

    @BeforeEach
    public void setUp() {
        settings = new Settings();
    }

    @Test
    public void testInitialSettingsNotNull() {
        assertNotNull(settings.getSettings());
    }

    @Test
    public void testLoadSettings() {
        settings.getSettings().put(Settings.BOARD_COLOR, Color.BLACK);
        settings.saveSettings();
        
        Settings newSettings = new Settings();
        assertEquals(Color.BLACK, newSettings.getSettings().get(Settings.BOARD_COLOR));
    }

    @Test
    public void testSaveSettings() {
        settings.getSettings().put(Settings.PLAYER_SNAKE_COLOR, Color.GREEN);
        settings.saveSettings();
        
        Settings newSettings = new Settings();
        assertEquals(Color.GREEN, newSettings.getSettings().get(Settings.PLAYER_SNAKE_COLOR));
    }

    
}
