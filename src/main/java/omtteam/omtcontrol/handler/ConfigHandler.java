package omtteam.omtcontrol.handler;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static String maximumHackingLevel;

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        config.load();

        String[] acceptedAnswers = {"NONE", "OPEN_GUI", "CHANGE_SETTINGS", "ADMIN"};
        maximumHackingLevel = config.get("OMT Hackables", "maxHacking", "ADMIN", "The maximum level a hacking terminal can force. None means friend only, OPEN_GUI allows the hack into the UI, Change Settings allows for modifications to the setting panel and Admin is eqivalent to owning the block", acceptedAnswers).getDefault();

        if (config.hasChanged()) {
            config.save();
        }
    }

}
