package xyz.larkyy.aquaticutils.condition;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.larkyy.aquaticutils.AquaticPlugin;
import xyz.larkyy.aquaticutils.action.ActionList;
import xyz.larkyy.aquaticutils.action.Actions;
import xyz.larkyy.aquaticutils.condition.impl.PermissionCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conditions {

    private final Map<String,Condition> conditionTypes;

    public Conditions() {
        conditionTypes = new HashMap<>();
        loadDefaultConditions();
    }

    private void loadDefaultConditions() {
        registerCondition("hasPermission", new PermissionCondition());
    }

    public void registerCondition(String id, Condition condition) {
        conditionTypes.put(id.toUpperCase(),condition);
    }

    public void unregisterCondition(String id) {
        conditionTypes.remove(id.toUpperCase());
    }

    public Condition getCondition(String id) {
        return conditionTypes.get(id.toUpperCase());
    }

    public static ConfiguredCondition loadCondition(FileConfiguration cfg, String path) {
        String type = cfg.getString(path+".type");
        if (type == null) {
            return null;
        }
        var condition = Conditions.inst().getCondition(type);
        if (condition == null) {
            Bukkit.broadcastMessage("Unknown condition");
            return null;
        }

        Map<String,String> filledArgs = new HashMap<>();
        for (String arg : condition.getArguments()) {
            filledArgs.put(arg,cfg.getString(path+"."+arg));
        }

        ActionList successActions = Actions.loadActionList(cfg,path+".success-actions");
        ActionList failActions = Actions.loadActionList(cfg,path+".fail-actions");

        return new ConfiguredCondition(condition,filledArgs,successActions,failActions);
    }

    public static ConditionList loadConditionList(FileConfiguration cfg, String path) {
        if (!cfg.contains(path)) {
            return new ConditionList();
        }
        List<ConfiguredCondition> conditions = new ArrayList<>();
        for (String str : cfg.getConfigurationSection(path).getKeys(false)) {

            ConfiguredCondition condition = loadCondition(cfg,path+"."+str);
            if (condition != null) {
                conditions.add(condition);
            }
        }
        if (conditions.isEmpty()) {
            return new ConditionList(new ActionList(), new ActionList(),conditions);
        }
        ActionList successActions = Actions.loadActionList(cfg,path+".success-actions");
        ActionList failActions = Actions.loadActionList(cfg,path+".fail-actions");

        return new ConditionList(failActions,successActions,conditions);
    }

    public static Conditions inst() {
        return AquaticPlugin.instance.conditions();
    }
}
