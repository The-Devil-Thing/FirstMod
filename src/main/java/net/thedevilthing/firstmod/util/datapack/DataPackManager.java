package net.thedevilthing.firstmod.util.datapack;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;

import java.io.File;

public class DataPackManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String MOD_ID = "firstmod";

    public static File getDataPackFolder(MinecraftServer server) {
        File worldPath = server.getWorldPath(LevelResource.DATAPACK_DIR).toFile();

        File modDataPackFolder = new File(worldPath, MOD_ID);

        if (!modDataPackFolder.exists() && !modDataPackFolder.mkdirs()) {
            LOGGER.error("Failed to create datapack directory: {}", modDataPackFolder.getAbsolutePath());
        }

        return modDataPackFolder;
    }
}
