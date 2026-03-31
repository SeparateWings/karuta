package config;

import model.Card;
import model.Deck;
import model.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.txt";

    private final File baseDir;
    private final Map<String, String> rawConfig;

    private String musicFolder;
    private String imagesFolder;
    private String restMusic;
    private int minDuration;
    private int maxDuration;
    private String defaultDeck;
    private String failureMode;

    public ConfigManager(String baseDir) throws IOException {
        this.baseDir = resolveBaseDir(baseDir);
        this.rawConfig = new HashMap<>();
        loadConfigFile();
        validateConfig();
    }

    private File resolveBaseDir(String requestedBaseDir) throws IOException {
        File directPath = new File(requestedBaseDir);
        if (directPath.isDirectory()) {
            return directPath.getCanonicalFile();
        }

        File sourceResourcesPath = new File("src/main/resources", requestedBaseDir);
        if (sourceResourcesPath.isDirectory()) {
            return sourceResourcesPath.getCanonicalFile();
        }

        ClassLoader classLoader = ConfigManager.class.getClassLoader();
        if (classLoader.getResource(requestedBaseDir) != null) {
            try {
                return new File(classLoader.getResource(requestedBaseDir).toURI()).getCanonicalFile();
            } catch (Exception e) {
                throw new IOException("Failed to resolve config directory: " + requestedBaseDir, e);
            }
        }

        throw new FileNotFoundException("Config directory not found: " + requestedBaseDir);
    }

    private void loadConfigFile() throws IOException {
        File configFile = new File(baseDir, CONFIG_FILE);
        if (!configFile.exists()) {
            throw new FileNotFoundException("Config file not found: " + configFile.getAbsolutePath());
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    rawConfig.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    private void validateConfig() {
        musicFolder = resolveConfiguredPath(getConfigValue("music_folder", "config/music/")).getPath();
        imagesFolder = resolveConfiguredPath(getConfigValue("images_folder", "config/images/")).getPath();
        restMusic = getConfigValue("rest_music", "rest.mp3");
        minDuration = Integer.parseInt(getConfigValue("min_duration", "10"));
        maxDuration = Integer.parseInt(getConfigValue("max_duration", "30"));
        defaultDeck = resolveConfiguredPath(getConfigValue("default_deck", "config/decks/deck1.csv")).getPath();
        failureMode = getConfigValue("failure_mode", "PASS");

        if (minDuration <= 0 || maxDuration <= 0 || minDuration > maxDuration) {
            throw new IllegalArgumentException(
                String.format("Invalid duration config: min=%d, max=%d", minDuration, maxDuration)
            );
        }
    }

    private String getConfigValue(String key, String defaultValue) {
        return rawConfig.getOrDefault(key, defaultValue);
    }

    private File resolveConfiguredPath(String configuredPath) {
        File directPath = new File(configuredPath);
        if (directPath.exists()) {
            return directPath;
        }

        String normalized = configuredPath.replace('\\', '/');
        if (normalized.startsWith("config/")) {
            return new File(baseDir, normalized.substring("config/".length()));
        }

        return new File(baseDir, normalized);
    }

    public Deck loadDeck(String deckFilePath) throws IOException {
        File deckFile = new File(deckFilePath);
        if (!deckFile.exists()) {
            throw new FileNotFoundException("Deck file not found: " + deckFile.getAbsolutePath());
        }

        String deckName = deckFile.getName().replace(".csv", "");
        Deck deck = new Deck(deckName, deckFilePath);

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(deckFile), StandardCharsets.UTF_8))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                parseDeckLine(line, deck);
            }
        }

        return deck;
    }

    private void parseDeckLine(String line, Deck deck) {
        String[] parts = line.split(",", -1);
        if (parts.length < 3) {
            System.err.println("Invalid deck row: " + line);
            return;
        }

        String imageName = parts[0].trim();
        String workName = parts[1].trim();
        String songsList = parts[2].trim();
        String songDisplayNames = parts.length >= 4 ? parts[3].trim() : "";

        Card card = new Card(imageName, workName);

        File imageFile = new File(imagesFolder, imageName);
        card.setImageFile(imageFile);
        if (!imageFile.exists()) {
            System.err.println("Image file not found: " + imageFile.getAbsolutePath());
        }

        String[] songs = songsList.split("\\|");
        String[] displayNames = songDisplayNames.isEmpty() ? new String[0] : songDisplayNames.split("\\|");

        for (int i = 0; i < songs.length; i++) {
            String trimmedSongName = songs[i].trim();
            if (trimmedSongName.isEmpty()) {
                continue;
            }

            File musicFile = new File(musicFolder, trimmedSongName);
            if (!musicFile.exists()) {
                System.err.println("Audio file not found: " + musicFile.getAbsolutePath());
                continue;
            }

            String displayName = i < displayNames.length ? displayNames[i].trim() : "";
            card.addSong(new Song(trimmedSongName, musicFile, displayName));
        }

        if (card.getSongCount() > 0) {
            deck.addCard(card);
        }
    }

    public boolean isSupportedAudioFormat(String fileName) {
        String format = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return format.matches("mp3|wav|flac|ogg|aac");
    }

    public String[] getSupportedFormats() {
        return new String[]{"mp3", "wav", "flac", "ogg", "aac"};
    }

    public String getMusicFolder() {
        return musicFolder;
    }

    public String getImagesFolder() {
        return imagesFolder;
    }

    public String getRestMusic() {
        return restMusic;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public String getDefaultDeck() {
        return defaultDeck;
    }

    public String getFailureMode() {
        return failureMode;
    }

    public File getRestMusicFile() {
        return new File(musicFolder, restMusic);
    }

    public Map<String, String> getRawConfig() {
        return new HashMap<>(rawConfig);
    }
}
