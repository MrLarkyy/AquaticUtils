package xyz.larkyy.aquaticutils.action.impl;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticutils.action.Action;

public class MessageAction extends Action {

    @Override
    public void run(Player player, String value) {
        player.sendMessage(value);
    }
}
