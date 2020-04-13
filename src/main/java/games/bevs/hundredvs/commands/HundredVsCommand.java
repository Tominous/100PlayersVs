package games.bevs.hundredvs.commands;

import com.google.common.collect.ImmutableList;
import games.bevs.hundredvs.commons.CC;
import games.bevs.hundredvs.listeners.PlayerListener;
import games.bevs.hundredvs.managers.PlayerDataManager;
import games.bevs.hundredvs.types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Allows you to
 * - remove playerdata
 * - reset all playerdata
 * - add to playerdata stats
 * - remove to playerdata stats
 *
 * /hv playedata remove <Username>
 * /hv playedata set <Username> [Skill] [value]
 * /hv playedata reset
 */
public class HundredVsCommand extends Command
{
    private PlayerDataManager playerDataManager;

    public HundredVsCommand(PlayerDataManager playerDataManager)
    {
        super("HundredVs",
                "Commands to manage the event",
                "/HundredVs", Arrays.asList("hv", "hvs"));
        this.playerDataManager = playerDataManager;
    }


    private void onHelp(CommandSender sender)
    {
        sender.sendMessage(CC.red + "/hv playedata reset");
        sender.sendMessage(CC.red + "/hv playedata set <Username> [Skill] [Value]");//I'm lazy
        sender.sendMessage(CC.red + "/hv playedata remove <Username>");
    }

    //I'm a  bit lazy, so i'll do this the simple way
    @Override
    public boolean execute(CommandSender sender, String s, String[] args)
    {
        if(args.length >= 2)
        {
            if(args[0].equalsIgnoreCase("playerdata"))
            {
                if(args[1].equalsIgnoreCase("reset"))
                {
                    this.playerDataManager.reset();
                    PlayerListener.playerDatas = new ArrayList(this.playerDataManager.getPlayerDatas());
                    sender.sendMessage(CC.gray + "reset all players.");
                    return true;
                }

                if(args[1].equalsIgnoreCase("remove") &&  args.length == 3)
                {
                    String username = args[2];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
                    if(offlinePlayer == null)
                    {
                        sender.sendMessage(CC.red + "Unkown player");
                        return false;
                    }

                    UUID uniqueId = offlinePlayer.getUniqueId();
                    PlayerData targetPlayerData = this.playerDataManager.getPlayer(uniqueId);

                    if(targetPlayerData == null)
                    {
                        sender.sendMessage(CC.red + "Unkown playerdata");
                        return false;
                    }

                    this.playerDataManager.unregisterPlayer(uniqueId);
                    if(offlinePlayer.isOnline())
                        offlinePlayer.getPlayer().kickPlayer(CC.bAqua + "Bevs" + CC.gray + "You have been kicked");
                    sender.sendMessage(CC.gray + "player " + username + " has been removed.");
                    return true;
                }

            }
        }

        this.onHelp(sender);
        return false;
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return ImmutableList.of();
    }
}
