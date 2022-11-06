package xyz.larkyy.aquaticutils.action;


import org.bukkit.configuration.file.FileConfiguration;
import xyz.larkyy.aquaticutils.action.impl.MessageAction;
import xyz.larkyy.aquaticutils.condition.ConditionList;
import xyz.larkyy.aquaticutils.condition.Conditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actions {

    private final Map<String, Action> actionTypes;

    public Actions() {
        actionTypes = new HashMap<>();
        loadDefaultActions();
    }

    private void loadDefaultActions() {
        registerAction("message", new MessageAction());
    }

    public void registerAction(String id, Action action) {
        actionTypes.put(id.toUpperCase(),action);
    }

    public void unregisterAction(String id) {
        actionTypes.remove(id.toUpperCase());
    }

    public Action getAction(String id) {
        return actionTypes.get(id.toUpperCase());
    }

    public Map<String, Action> getActionTypes() {
        return actionTypes;
    }

    public static ActionList loadActionList(FileConfiguration cfg, String path, Actions actions, Conditions conditions) {
        if (!cfg.contains(path)) {
            return new ActionList();
        }
        List<ConfiguredAction> actionsList = new ArrayList<>();
        Object obj = cfg    .get(path);
        if (obj instanceof List<?>) {
            List<String> list = (List<String>) obj;
            for (String str : list) {
                ConfiguredAction action = translateAction(str,actions);
                if (action == null) {
                    continue;
                }
                actionsList.add(action);
            }
            return new ActionList(actionsList,new ConditionList());
        } else {
            for (String str : cfg.getConfigurationSection(path).getKeys(false)) {
                ConfiguredAction action = loadAction(cfg, path+"."+str,actions,conditions);
                if (action == null) {
                    continue;
                }
                actionsList.add(action);
            }

            ConditionList conditionList;
            if (cfg.contains(path+".conditions")) {
                conditionList = Conditions.loadConditionList(cfg, path+".conditions",conditions,actions);
            } else {
                conditionList = new ConditionList();
            }
            return new ActionList(actionsList,conditionList);
        }
    }

    public static ConfiguredAction loadAction(FileConfiguration cfg, String path, Actions actions, Conditions conditions) {

        var action = translateAction(cfg.getString(path+".value"),actions);
        if (action == null) {
            return null;
        }
        if (cfg.contains(path+".conditions")) {
            action.setConditions(Conditions.loadConditionList(cfg,path+".conditions",conditions,actions));
        }
        return action;
    }

    public static ConfiguredAction translateAction(String value,Actions actions) {
        String args;

        for (String key : actions.actionTypes.keySet()) {
            if (value.startsWith("["+key+"]")) {
                args = value.substring(key.length()+2).trim();
                var action = actions.getAction(key);
                return new ConfiguredAction(
                        new ConditionList(new ActionList(), new ActionList()),
                        action,args
                );
            }
        }
        return null;
    }
}
