package xyz.larkyy.aquaticutils.condition.impl;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticutils.condition.Condition;

import java.util.Arrays;
import java.util.Map;

public class PermissionCondition extends Condition {

    public PermissionCondition() {
        super(Arrays.asList("permission"));
    }

    @Override
    public boolean isMet(Player player, Map<String, String> filledArguments) {
        return (player.hasPermission(filledArguments.get("permission")));
    }
}
