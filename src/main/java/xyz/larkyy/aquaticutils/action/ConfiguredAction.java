package xyz.larkyy.aquaticutils.action;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticutils.condition.ConditionList;

public class ConfiguredAction {

    private ConditionList conditions;
    private final Action action;
    private final String value;

    public ConfiguredAction(ConditionList conditions, Action action, String value) {
        this.conditions = conditions;
        this.action = action;
        this.value = value;
    }

    public boolean run(Player player) {
        if (conditions.areMet(player)) {
            action.run(player,value);
            return true;
        }
        return false;
    }

    public void setConditions(ConditionList conditions) {
        this.conditions = conditions;
    }
}
