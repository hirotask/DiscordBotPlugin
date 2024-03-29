package tech.erudo.mc.discord.discordbotplugin.util.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class CustomConfig {

    private final JavaPlugin plugin;
    private final String PATH;

    @Getter
    private FileConfiguration config;

    private File configFile;

    protected CustomConfig(JavaPlugin plugin, String path) {
        this.plugin = plugin;
        this.PATH = path;

        this.configFile = new File(plugin.getDataFolder(), this.PATH);

        saveDefaultConfig();
        this.reload();
    }

    protected CustomConfig(JavaPlugin plugin) {
        this(plugin, "config.yml");
    }

    protected void saveDefaultConfig() {
        if(!configFile.exists()) {
            plugin.saveResource(this.PATH, false);
        }
    }

    protected void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream configStream = plugin.getResource(this.PATH);

        if(configStream == null) return;
        if(config == null) return;

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configStream, StandardCharsets.UTF_8)));
    }
}
