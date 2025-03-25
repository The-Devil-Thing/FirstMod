package net.thedevilthing.firstmod.util.datapack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.thedevilthing.firstmod.InfusionItemsConfig;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class DynamicTagGenerator {
    private static final String TAG_NAME = "infusables";
    private static final String MOD_ID = "firstmod";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void generateTagFile(File dataPackFolder) {

        Set<String> items = InfusionItemsConfig.getAllItems();

        JsonObject tagJson = new JsonObject();
        JsonArray itemArray = new JsonArray();

        for(String item : items) {
            itemArray.add(item);
        }

        tagJson.addProperty("replace", false);
        tagJson.add("values", itemArray);

        File tagFile = new File(dataPackFolder, "data/" + MOD_ID + "/tags/item/" + TAG_NAME + ".json");

        try {
            tagFile.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(tagFile);

            writer.write(tagJson.toString());

            writer.close();

            LOGGER.debug("Successfully created the tag file at {}", tagFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.debug("Failed to create the file: {}", tagFile.getAbsolutePath(), e);
        }
    }
}
