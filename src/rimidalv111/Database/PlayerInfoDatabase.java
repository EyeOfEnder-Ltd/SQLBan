package rimidalv111.Database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "SQLBan")
public class PlayerInfoDatabase
{
	@Id
	@Length(max = 20)
	@NotNull
	private String playerName;
	private String ip;
	
	private String isbanned;
	private String reason;
	private String bywhom;
	private String permbanned;
	private String datebanned;
	private String datetounban;
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public String getIp()
	{
		return ip;
	}

	public String getIsbanned()
	{
		return isbanned;
	}

	public String getReason()
	{
		return reason;
	}

	public String getBywhom()
	{
		return bywhom;
	}

	public String getPermbanned()
	{
		return permbanned;
	}

	public String getDatebanned()
	{
		return datebanned;
	}

	public String getDatetounban()
	{
		return datetounban;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public void setIsbanned(String isbanned)
	{
		this.isbanned = isbanned;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public void setBywhom(String bywhom)
	{
		this.bywhom = bywhom;
	}

	public void setPermbanned(String permbanned)
	{
		this.permbanned = permbanned;
	}

	public void setDatebanned(String datebanned)
	{
		this.datebanned = datebanned;
	}

	public void setDatetounban(String datetounban)
	{
		this.datetounban = datetounban;
	}
}
