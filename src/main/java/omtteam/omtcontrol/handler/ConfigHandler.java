package omtteam.omtcontrol.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {


    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        config.load();



        if (config.hasChanged()) {
            config.save();
        }
    }

}
