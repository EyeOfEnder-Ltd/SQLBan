package rimidalv111.Database;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import rimidalv111.SQLBan.SQLBan;



public class DatabaseInstance
{
	private MyDatabase database;

	public FileConfiguration sqlConfig = null;
	private File customConfigurationFile = null;
	private SQLBan plugin;
	
	private String driver;
	private String url;
	private String isolation;
	private String username;
	private String password;
	private boolean logging;
	private boolean rebuild;
	
	private String searchName;
	
	public DatabaseInstance(SQLBan  instance, String dbsearchname)
	{
		plugin = instance;
		searchName = dbsearchname;
		loadConfig();
		loadSQLSettings();
		initializeDatabase();
	}
	
	public void loadSQLSettings()
	{
		driver = sqlConfig.getString(searchName + ".driver");
		url = sqlConfig.getString(searchName + ".url");
		username = sqlConfig.getString(searchName + ".username");
		password = sqlConfig.getString(searchName + ".password");
		isolation = sqlConfig.getString(searchName + ".isolation");
		logging = sqlConfig.getBoolean(searchName + ".logging");
		rebuild = sqlConfig.getBoolean(searchName + ".rebuild");
	}
	
	public void loadConfig() 
	{ 
	    if (customConfigurationFile == null) 
	    {
	    	customConfigurationFile = new File(plugin.getDataFolder(), "mysqlconfig.yml");
	    }
	    sqlConfig = YamlConfiguration.loadConfiguration(customConfigurationFile);
		File configFile = new File(plugin.getDataFolder(), "mysqlconfig.yml");
		if (!configFile.exists() || sqlConfig.get(searchName + ".driver") == null)
		{
			sqlConfig.addDefault(searchName + ".driver", "org.sqlite.JDBC");
			sqlConfig.addDefault(searchName + ".url", "jdbc:sqlite:{DIR}{NAME}.db");
			sqlConfig.addDefault(searchName + ".username", "root");
			sqlConfig.addDefault(searchName + ".password", "");
			sqlConfig.addDefault(searchName + ".isolation", "SERIALIZABLE");
			sqlConfig.addDefault(searchName + ".logging", false);
			sqlConfig.addDefault(searchName + ".rebuild", true);
			sqlConfig.options().copyDefaults(true);
			try
			{
				sqlConfig.save(new File(plugin.getDataFolder(), "mysqlconfig.yml"));
			} catch (Exception e)
			{
				//plugin.getUtil().logSevere("Error while trying to save SQL Config!!");
			}
		}
	}
	
	private void initializeDatabase()
	{
		database = new MyDatabase(plugin)
		{
			protected java.util.List<Class<?>> getDatabaseClasses()
			{
				List<Class<?>> list = new ArrayList<Class<?>>();
				list.add(PlayerInfoDatabase.class);
				return list;
			};
		};
		
		database.initializeDatabase(driver, url, username,password, isolation, logging, rebuild);
		
		sqlConfig.set(searchName + ".rebuild", false);
		try
		{
			sqlConfig.save(new File(plugin.getDataFolder(), "mysqlconfig.yml"));
		} catch (Exception e)
		{
			//plugin.getUtil().logSevere("Error while trying to save SQL Config!!");
		}
	}
	
	public MyDatabase getDatabaseInstance()
	{
		return database;
	}

	public void setDatabase(MyDatabase database)
	{
		this.database = database;
	}
}
