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
            double chance
    ) {}

    public record ServerConfig(
        boolean adjustTerrain,
        HashMap<String, ConfigEntry[]> entries
    ) {}


    public static ServerConfig CONFIG = new ServerConfig(
            true,
            new HashMap<>()
    );
    static
    {
        CONFIG.entries.put("minecraft:cobweb", new ConfigEntry[] { new ConfigEntry("minecraft:cave_spider", 0.5)});
        CONFIG.entries.put("minecraft:stone_bricks", new ConfigEntry[] { new ConfigEntry("minecraft:silverfish", 0.2), new ConfigEntry("minecraft:endermite", 0.1) });
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    static final String CONFIG_PATH = "config" + File.separator + "block_spawn.json";

    public static void create() throws IOException
    {
        Path p = Path.of("config");
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