package n643064.blockspawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Config
{
    public record ConfigEntry(
            String mob,
            double chance,
            boolean adjustTerrain,
            boolean targetPlayer
    ) {}

    public static class ServerConfig extends HashMap<String, ConfigEntry[]> {}


    public static ServerConfig CONFIG = new ServerConfig();
    static
    {
        CONFIG.put("minecraft:cobweb", new ConfigEntry[] { new ConfigEntry("minecraft:cave_spider", 0.05, false, true)});
        CONFIG.put("minecraft:grass", new ConfigEntry[] { new ConfigEntry("minecraft:creeper", 0.005, true, true)});
        CONFIG.put("minecraft:stone_bricks", new ConfigEntry[] { new ConfigEntry("minecraft:silverfish", 0.05, false, false), new ConfigEntry("minecraft:endermite", 0.01, false, false) });
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    static final String CONFIG_PATH = "config" + File.separator + "block_spawn.json";

    public static void create() throws IOException
    {
        final Path p = Path.of("config");
        if (Files.exists(p))
        {
            if (Files.isDirectory(p))
            {
                FileWriter writer = new FileWriter(CONFIG_PATH);
                writer.write(GSON.toJson(CONFIG));
                writer.flush();
                writer.close();
            }
        } else
        {
            Files.createDirectory(p);
            create();
        }
    }

    public static void read() throws IOException
    {
        FileReader reader = new FileReader(CONFIG_PATH);
        CONFIG = GSON.fromJson(reader, ServerConfig.class);
        reader.close();
    }

    public static void setup()
    {
        try
        {
            if (Files.exists(Path.of(CONFIG_PATH)))
            {
                read();
            } else
            {
                create();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            // CaLl tO 'pRiNtStAcKTRaCe()' ShOuLd pRoBaBlY Be rEpLaCeD WiTh mOrE RoBuSt lOgGiNg
        }
    }
}