package rimidalv111.Commands;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import rimidalv111.Database.PlayerInfoDatabase;
import rimidalv111.SQLBan.SQLBan;

public class Permban implements CommandExecutor
{
	public SQLBan plugin = SQLBan.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("permban"))
		{
			if(sender instanceof Player)
			{
				if(plugin.isUseVault())
				{
					if(!plugin.getPermissions().has((Player) sender, "sqlban.command.permban"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				} else
				{
					if(!((Player) sender).hasPermission("sqlban.command.permban"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				}
			}
			if(args.length > 1)
			{
				String player = "null";
				String reason = "";
				
				for(String s : args)
				{
					if(player.equalsIgnoreCase("null"))
					{
						player = s;
					} else
					{
						reason = reason + " " + s;
					}
				}
				if(player.equalsIgnoreCase("null"))
				{
					sender.sendMessage(ChatColor.RED + "/permban " + ChatColor.DARK_RED + "[player] " + ChatColor.GRAY + "[reason]");
					return true;
				}
				PlayerInfoDatabase pid = plugin.getDatabase().find(PlayerInfoDatabase.class).where().ieq("playerName", player).findUnique();
				if(pid == null)
				{
					pid = new PlayerInfoDatabase();
					pid.setPlayerName(player);
					pid.setIp("null");
					pid.setIsbanned("true");
					pid.setReason(reason);
					pid.setBywhom(sender.getName());;
					pid.setPermbanned("true");
					
					Date today = plugin.getCurrentDate();
					
					pid.setDatebanned(plugin.dateToString(today));
					pid.setDatetounban("never");
					plugin.getDatabase().save(pid);
					sender.sendMessage(ChatColor.RED + player + ChatColor.GREEN + " was permanently banned even tho he didn't have any SQL data!");
					return true;
				} else
				{
					pid.setIsbanned("true");
					pid.setReason(reason);
					pid.setBywhom(sender.getName());
					pid.setPermbanned("true");
					
					Date today = plugin.getCurrentDate();
					
					pid.setDatebanned(plugin.dateToString(today));
					pid.setDatetounban("never");
					plugin.getDatabase().save(pid);
					
					for(Player p : plugin.getServer().getOnlinePlayers())
					{
						if(p.getName().equalsIgnoreCase(player))
						{
							p.kickPlayer("Permanently Banned for" + reason + " by " + sender.getName());
						}
					}
					sender.sendMessage(ChatColor.RED + player + ChatColor.GREEN + " permanently banned till " + ChatColor.YELLOW + "NEVER");
					return true;
				}
			} else
			{
				sender.sendMessage(ChatColor.RED + "/permban " + ChatColor.DARK_RED + "[player] " + ChatColor.GRAY + "[reason]");
				return true;
			}
		}
		return false;
	}
	
}
