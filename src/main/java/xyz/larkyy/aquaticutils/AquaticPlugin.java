package xyz.larkyy.aquaticutils;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticutils.action.Actions;
import xyz.larkyy.aquaticutils.condition.Conditions;

public abstract class AquaticPlugin extends JavaPlugin {

    public static AquaticPlugin instance;
    public abstract Conditions conditions();
    public abstract Actions actions();

}
