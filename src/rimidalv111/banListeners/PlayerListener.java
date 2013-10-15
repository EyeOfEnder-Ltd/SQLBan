package rimidalv111.banListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;


import rimidalv111.Database.PlayerInfoDatabase;
import rimidalv111.SQLBan.SQLBan;

public class PlayerListener implements Listener
{
	SQLBan plugin;
	
	public PlayerListener(SQLBan instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		PlayerInfoDatabase pid = plugin.getDatabase().find(PlayerInfoDatabase.class).where().ieq("playerName", event.getPlayer().getName()).findUnique();
		if(pid == null)
		{
			pid = new PlayerInfoDatabase();
			pid.setPlayerName(event.getPlayer().getName());
			pid.setIp(event.getAddress().getHostAddress());
			pid.setIsbanned("false");
			pid.setReason("null");
			pid.setBywhom("null");
			pid.setPermbanned("false");
			pid.setDatebanned("null");
			pid.setDatetounban("null");
			plugin.getDatabase().save(pid);
		} else
		{
			String timeleft = plugin.timeLeft(pid);
			if(pid.getPermbanned().equalsIgnoreCase("true"))
			{
				event.disallow(Result.KICK_BANNED, ChatColor.RED + "You are permanently banned!");
			} else
			{
				if(pid.getIsbanned().equalsIgnoreCase("true"))
				{
					if(!plugin.isPlayerGoodToJoin(pid))
					{
						event.disallow(Result.KICK_BANNED, ChatColor.GRAY + "You are banned for " + ChatColor.LIGHT_PURPLE + timeleft + ChatColor.GRAY + "!");
						return;
					} else
					{
						pid.setIsbanned("false");
						pid.setDatebanned("null");
						pid.setDatetounban("null");
					}
				}
			}
			//now check if its the same player with different username
			PlayerInfoDatabase pd = plugin.getDatabase().find(PlayerInfoDatabase.class).where().ieq("ip", event.getAddress().getHostAddress()).findUnique();
			if(pd.getPermbanned().equalsIgnoreCase("true"))
			{
				event.disallow(Result.KICK_BANNED, ChatColor.RED + "You are permanently banned!");
			} else
			{
				if(pd.getIsbanned().equalsIgnoreCase("true"))
				{
					if(!plugin.isPlayerGoodToJoin(pd))
					{
						event.disallow(Result.KICK_BANNED, ChatColor.GRAY + "You are banned for " + ChatColor.LIGHT_PURPLE + timeleft + ChatColor.GRAY + "!");
						return;
					}
				}
			}
			String playerIP = event.getKickMessage();
			pid.setIp(playerIP);
			plugin.getDatabase().save(pid);
			//let through
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		//Player player = event.getPlayer();
	}
}
