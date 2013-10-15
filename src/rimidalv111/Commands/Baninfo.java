package rimidalv111.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import rimidalv111.Database.PlayerInfoDatabase;
import rimidalv111.SQLBan.SQLBan;

public class Baninfo implements CommandExecutor
{
	public SQLBan plugin = SQLBan.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("baninfo"))
		{
			if(sender instanceof Player)
			{
				if(plugin.isUseVault())
				{
					if(!plugin.getPermissions().has((Player) sender, "sqlban.command.baninfo"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				} else
				{
					if(!((Player) sender).hasPermission("sqlban.command.baninfo"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				}
			}
			if(args.length > 0)
			{
				String playerName = args[0];
				PlayerInfoDatabase pid = plugin.getDatabase().find(PlayerInfoDatabase.class).where().ieq("playerName", playerName).findUnique();
				if(pid == null)
				{
					sender.sendMessage(ChatColor.YELLOW + "There is no data for player " + ChatColor.GREEN + playerName);
					return true;
				} else
				{
					sender.sendMessage(ChatColor.YELLOW + "Reading " + ChatColor.GREEN + playerName + "'s" + ChatColor.YELLOW + " ban info");
					sender.sendMessage(ChatColor.GREEN + "IP: " + ChatColor.GRAY + pid.getIp());
					sender.sendMessage(ChatColor.GREEN + "Is Banned: " + ChatColor.GRAY + pid.getIsbanned());
					sender.sendMessage(ChatColor.GREEN + "Is Permabanned: " + ChatColor.GRAY + pid.getPermbanned());
					sender.sendMessage(ChatColor.GREEN + "Reason: " + ChatColor.GRAY + pid.getReason());
					sender.sendMessage(ChatColor.GREEN + "Banned By: " + ChatColor.GRAY + pid.getBywhom());
					sender.sendMessage(ChatColor.GREEN + "Banned On: " + ChatColor.GRAY + pid.getDatebanned());
					sender.sendMessage(ChatColor.GREEN + "Banned Till: " + ChatColor.GRAY + pid.getDatetounban());
					return true;
				}
			} else
			{
				sender.sendMessage(ChatColor.RED + "/baninfo " + ChatColor.DARK_RED + "[player]");
				return true;
			}
		}
		return false;
	}
	
}
