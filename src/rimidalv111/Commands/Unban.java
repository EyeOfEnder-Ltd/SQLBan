package rimidalv111.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import rimidalv111.Database.PlayerInfoDatabase;
import rimidalv111.SQLBan.SQLBan;

public class Unban implements CommandExecutor
{
	public SQLBan plugin = SQLBan.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("unban"))
		{
			if(sender instanceof Player)
			{
				if(plugin.isUseVault())
				{
					if(!plugin.getPermissions().has((Player) sender, "sqlban.command.unban"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				} else
				{
					if(!((Player) sender).hasPermission("sqlban.command.unban"))
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
					if(pid.getPermbanned().equalsIgnoreCase("true"))
					{
						//get players permission and if his perm is bad then return saying he cant remove perm banns only higher people can
					}
					pid.setIsbanned("false");
					pid.setReason("null");
					pid.setBywhom("null");
					//pid.setPermbanned("false");
					pid.setDatebanned("null");
					pid.setDatetounban("null");
					plugin.getDatabase().save(pid);
					sender.sendMessage(ChatColor.GREEN + playerName + ChatColor.YELLOW + " was unbaned!");
					return true;
				}
			} else
			{
				sender.sendMessage(ChatColor.RED + "/unban " + ChatColor.DARK_RED + "[player]");
				return true;
			}
		}
		return false;
	}
	
}
