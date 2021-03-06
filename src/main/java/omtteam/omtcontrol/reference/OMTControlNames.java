package omtteam.omtcontrol.reference;

import net.minecraft.util.ResourceLocation;

/**
 * Created by Keridos on 17.05.2015.
 * This Class
 */
public class OMTControlNames {
    public static class Blocks {
        public static final String manualTarget = "manual_target";
        public static final String PLAYER_DEFENSE_MODULE = "player_defense_module";
        public static final String baseAddonMain = "base_addon_main";
        public static final String HACKING_TERMINAL = "hacking_terminal";
        public static final String CABLES = "omt_cables";
    }

    public static class Items {
        public static final String laserPointer = "laser_pointer";
        public static final String REMOTE_TURRET_CONFIG = "remote_turret_config";
    }

    public static class Localizations {
        @SuppressWarnings("unused")
        public static class GUI {
            public static final String SET_YAW_PITCH = "gui.omtteam.omtcontrol:set_yaw_pitch";
            public static final String SELECTED_TURRET = "gui.omtteam.omtcontrol:selected_turret";
            public static final String SHOOT_TURRET = "gui.omtteam.omtcontrol:shoot_turret";
        }

        public static class Tooltip {
            public static final String SET_YAW_PITCH = "tooltip.omtteam.omtcontrol:set_yaw_pitch";
            public static final String SELECTED_TURRET = "tooltip.omtteam.omtcontrol:selected_turret";
            public static final String SHOOT_TURRET = "tooltip.omtteam.omtcontrol:shoot_turret";
            public static final String TOGGLE_AUTOFIRE = "tooltip.omtteam.omtcontrol:toggle_auto_fire";
            public static final String YAW = "tooltip.omtteam.omtcontrol:yaw";
            public static final String PITCH = "tooltip.omtteam.omtcontrol:pitch";
        }
    }

    public static class Textures {
        public static final String MANUAL_TARGET_BLOCK = Reference.MOD_ID + ":textures/gui/manual_target_block.png";
        public static final ResourceLocation TURRET_HACKER = new ResourceLocation(Reference.MOD_ID, "textures/gui/turret_hacking_gui.png");
    }
}
