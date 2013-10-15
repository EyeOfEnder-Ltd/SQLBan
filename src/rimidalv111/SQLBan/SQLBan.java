package rimidalv111.SQLBan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import rimidalv111.Commands.Ban;
import rimidalv111.Commands.Baninfo;
import rimidalv111.Commands.Permban;
import rimidalv111.Commands.Unban;
import rimidalv111.Database.DatabaseInstance;
import rimidalv111.Database.PlayerInfoDatabase;
import rimidalv111.banListeners.PlayerListener;

import com.avaje.ebean.EbeanServer;

public class SQLBan extends JavaPlugin
{
	private DatabaseInstance dbInstance;
	private PlayerListener playerListener = new PlayerListener(this);
	private Permission permissions;
	private boolean useVault = false;
	
	public static SQLBan instance;
	
	public void onEnable()
	{
		loadDatabase("SQLBan");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		instance = this;
		getCommand("ban").setExecutor(new Ban());
		getCommand("permban").setExecutor(new Permban());
		getCommand("unban").setExecutor(new Unban());
		getCommand("baninfo").setExecutor(new Baninfo());
		
		//hook into permissions
		if (getServer().getPluginManager().getPlugin("Vault") != null)
		{
			if (!setupPermissions())
			{
				getServer().getLogger().severe("Vault was returned NULL. (is it up to date?)");
			} else
			{
				useVault = true;
			}
		} else
		{
			getServer().getLogger().warning("Looks like you don't have vault! Defualting to superpermissions");
		}
	}
	
	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null)
		{
			permissions = (Permission) permissionProvider.getProvider();
		}
		return permissions != null;
	}
	
	@Override
	public EbeanServer getDatabase()
	{
		return dbInstance.getDatabaseInstance().getDatabase();
	}
	
	public void loadDatabase(String dbname)
	{
		dbInstance = new DatabaseInstance(this, dbname);
	}
	
	public void onDisable()
	{
		
	}
	
	public String timeLeft(PlayerInfoDatabase pid)
	{
		if(pid.getDatetounban().equalsIgnoreCase("null") || pid.getDatetounban().equalsIgnoreCase("never"))
		{
			return "null";
		}
		Date today = new Date();
		Date toUnbanOn = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm-aaa");
		try
		{
			toUnbanOn = sdf.parse(pid.getDatetounban());
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		long[] times = TimeDiff.getTimeDifference(today, toUnbanOn);
		String days = times[0] + " days";
		String hours = times[1] + " hours";
		String minutes = times[2] + " mins";
		String timeLeft = "";
		if(times[0] != 0)
		{
			timeLeft = days + ", ";
		}
		if(times[1] != 0)
		{
			timeLeft = timeLeft + hours + ", ";
		}
		if(times[2] != 0)
		{
			timeLeft = timeLeft + minutes;
		}
		return timeLeft;
	}
	
	public boolean isPlayerGoodToJoin(PlayerInfoDatabase pid)
	{
		if(pid.getDatebanned().equalsIgnoreCase("null") || pid.getDatetounban().equalsIgnoreCase("null"))
		{
			return true;
		}
		if(pid.getDatetounban().equalsIgnoreCase("never"))
		{
			return false;
		}
		Date toUnbanOn = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm-aaa");
		try
		{
			toUnbanOn = sdf.parse(pid.getDatetounban());
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		Date date = new Date();
		if (toUnbanOn.before(date)) //make sure the date the player was banned too is before current date
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	public Date addTimeToDate(Date date, int m, int w, int d, int h, int min)
	{
		long time = 0;
		if (m > 0)
		{
			time = m * 2419200;
		}
		if (w > 0)
		{
			time = time + (w * 604800);
		}
		if (d > 0)
		{
			time = time + (d * 86400);
		}
		if (h > 0)
		{
			time = time + (h * 3600);
		}
		if (min > 0)
		{
			time = time + (min * 60);
		}
		long milliseconds = time * 1000;
		long t=date.getTime();
		Date afterAdding = new Date(t + milliseconds);
		
		return afterAdding;
	}
	
	public Date getCurrentDate()
	{
		return new Date();
	}

	public String dateToString(Date date)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm-aaa");
		return dateFormat.format(date);
	}
	
	public String getCurrentDateAndTimeString()
	{
		//2013-09-22-12:04-PM
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm-aaa");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static SQLBan getInstance()
	{
		return instance;
	}

	public static void setInstance(SQLBan instance)
	{
		SQLBan.instance = instance;
	}

	public Permission getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Permission permissions)
	{
		this.permissions = permissions;
	}

	public boolean isUseVault()
	{
		return useVault;
	}

	public void setUseVault(boolean useVault)
	{
		this.useVault = useVault;
	}
}
