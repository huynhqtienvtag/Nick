package me.hsgamer.nick;

import me.hsgamer.nick.enums.ConfigEnum;
import me.hsgamer.nick.utils.Utils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission((String) Utils.getValueFromConfig(ConfigEnum.PERMISSION))) {
                openGUI((Player) sender);
            } else {
                Utils.sendMessage(sender, ConfigEnum.NO_PERMISSION);
            }
        } else {
            Utils.sendMessage(sender, ConfigEnum.PLAYER_ONLY);
        }
        return true;
    }

    private void openGUI(Player sender) {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.addAll((List<String>) Utils.getValueFromConfig(ConfigEnum.SIGN_LINES));
        Nick.getInstance().getSignMenuFactory()
                .newMenu(sender, lines)
                .reopenIfFail()
                .response(((player, strings) -> {
                    String nick = strings[0];
                    if (nick.isEmpty()) {
                        Utils.sendMessage(player, ConfigEnum.NOT_BLANK);
                    } else {
                        player.setDisplayName(nick);
                        Utils.sendMessage(player, ConfigEnum.SUCCESSFUL);
                    }
                    return true;
                }))
                .open();
    }
}
