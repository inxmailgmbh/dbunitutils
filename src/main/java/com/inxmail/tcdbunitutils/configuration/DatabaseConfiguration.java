package com.inxmail.tcdbunitutils.configuration;

/**
 * @author Bartosz Majsak
 */
public class DatabaseConfiguration
{

	private String dataSourceName;

	private final String url;

	private final String username;

	private final String password;

	private String initStatement = "";

	private String cleanupStatement = "";


	public DatabaseConfiguration( String dataSourceName )
	{
		this.dataSourceName = dataSourceName;
		this.url = null;
		this.username = null;
		this.password = null;
	}


	public DatabaseConfiguration( String url, String username, String password )
	{
		this.url = url;
		this.username = username;
		this.password = password;
	}


	public String getDataSource()
	{
		return dataSourceName;
	}


	public void setDataSourceName( String dataSource )
	{
		this.dataSourceName = dataSource;
	}


	public String getUrl()
	{
		return url;
	}


	public String getUsername()
	{
		return username;
	}


	public String getPassword()
	{
		return password;
	}


	public String getInitStatement()
	{
		return initStatement;
	}


	public void setInitStatement( String initStatement )
	{
		this.initStatement = initStatement;
	}


	public String getCleanupStatement()
	{
		return cleanupStatement;
	}


	public void setCleanupStatement( String cleanupStatement )
	{
		this.cleanupStatement = cleanupStatement;
	}

}
