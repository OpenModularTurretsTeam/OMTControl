package omtteam.omtcontrol.handler;

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
