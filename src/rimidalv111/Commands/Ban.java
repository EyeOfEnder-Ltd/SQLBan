package rimidalv111.Commands;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import rimidalv111.Database.PlayerInfoDatabase;
import rimidalv111.SQLBan.SQLBan;

public class Ban implements CommandExecutor
{
	public SQLBan plugin = SQLBan.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("ban"))
		{
			if(sender instanceof Player)
			{
				if(plugin.isUseVault())
				{
					if(!plugin.getPermissions().has((Player) sender, "sqlban.command.ban"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				} else
				{
					if(!((Player) sender).hasPermission("sqlban.command.ban"))
					{
						sender.sendMessage(ChatColor.RED + "You sir don't have access to this command!");
						return true;
					}
				}
			}
			if(args.length > 1)
			{
				String player = "null";
				int m = 0;
				int w = 0;
				int d = 0;
				int h = 0;
				int min = 0;
				String reason = "";
				
				for(String s : args)
				{
					if(player.equalsIgnoreCase("null"))
					{
						player = s;
					} else
					{
						String[] spl = s.split(":");
						if(s.contains("m:"))
						{
							m = Integer.parseInt(spl[1]);
						} else
						if(s.contains("w:"))
						{
							w = Integer.parseInt(spl[1]);
						} else
						if(s.contains("d:"))
						{
							d = Integer.parseInt(spl[1]);
						} else
						if(s.contains("h:"))
						{
							h = Integer.parseInt(spl[1]);
						} else
						if(s.contains("min:"))
						{
							min = Integer.parseInt(spl[1]);
						} else
						{
							reason = reason + " " + s;
						}
						
					}
				}
				if(m == 0 && w == 0 && d == 0 && h == 0 && min == 0)
				{
					sender.sendMessage(ChatColor.RED + "/ban [player] " + ChatColor.DARK_RED + "[m,w,d,h,min] " + ChatColor.GRAY + "[reason]");
					return true;
				}
				if(player.equalsIgnoreCase("null"))
				{
					sender.sendMessage(ChatColor.RED + "/ban " + ChatColor.DARK_RED + "[player] " + ChatColor.RED + "[m,w,d,h,min] " + ChatColor.GRAY + "[reason]");
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
					pid.setPermbanned("false");
					
					Date today = plugin.getCurrentDate();
					Date bannedTill = plugin.addTimeToDate(today, m, w, d, h, min);
					
					pid.setDatebanned(plugin.dateToString(today));
					pid.setDatetounban(plugin.dateToString(bannedTill));
					plugin.getDatabase().save(pid);
					sender.sendMessage(ChatColor.RED + player + ChatColor.GREEN + " was banned even tho he didn't have any SQL data!");
					return true;
				} else
				{
					pid.setIsbanned("true");
					pid.setReason(reason);
					pid.setBywhom(sender.getName());
					Date today = plugin.getCurrentDate();
					Date bannedTill = plugin.addTimeToDate(today, m, w, d, h, min);
					
					pid.setDatebanned(plugin.dateToString(today));
					pid.setDatetounban(plugin.dateToString(bannedTill));
					plugin.getDatabase().save(pid);
					
					for(Player p : plugin.getServer().getOnlinePlayers())
					{
						if(p.getName().equalsIgnoreCase(player))
						{
							p.kickPlayer("Banned for" + reason + " by " + sender.getName());
						}
					}
					sender.sendMessage(ChatColor.RED + player + ChatColor.GREEN + " banned till " + ChatColor.YELLOW + plugin.dateToString(bannedTill));
					return true;
				}
			} else
			{
				sender.sendMessage(ChatColor.RED + "/ban " + ChatColor.DARK_RED + "[player] [m,w,d,h,min] " + ChatColor.GRAY + "[reason]");
				return true;
			}
		}
		return false;
	}
}
