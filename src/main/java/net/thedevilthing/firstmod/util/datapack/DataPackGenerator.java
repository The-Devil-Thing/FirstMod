package net.thedevilthing.firstmod.util.datapack;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataPackGenerator {
    private static final String MOD_ID = "firstmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void generateDataPack(File dataPackFolder) {

        File mcmetaFile = new File(dataPackFolder, "pack.mcmeta");

        try {
            if (!mcmetaFile.exists()) {
                FileWriter mcmetaWriter = new FileWriter(mcmetaFile);

                mcmetaWriter.write("{ \"pack\": { \"pack_format\": 15, \"description\": \"FirstMod's Dynamic Datapack\", \"forced\": true } }");
                mcmetaWriter.close();

                LOGGER.debug("pack.mcmeta file created: {}", mcmetaFile.getAbsolutePath());
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create pack.mcmeta file: {}", mcmetaFile.getAbsolutePath(), e);
        }
    }
}
