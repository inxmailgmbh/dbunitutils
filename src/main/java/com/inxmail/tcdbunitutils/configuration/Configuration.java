package com.inxmail.tcdbunitutils.configuration;

import java.io.InputStream;
import java.util.Properties;


/**
 * @author Bartosz Majsak
 */
public final class Configuration
{

	private static final String PROPERTIES_NAME = "inxdbunit.properties";

	private static final String JDBC_URL = "db.url";

	private static final String USERNAME = "db.username";

	private static final String PASSWORD = "db.password";

	private static final String INITIAL_SQL = "db.initial_sql";

	private final Properties properties = new Properties();

	private DatabaseConfiguration databaseConfiguration;

	private static Configuration instance = null;


	public static Configuration instance()
	{
		if( instance == null )
			instance = new Configuration();
		return instance;
	}


	public Configuration()
	{
		InputStream in = null;
		try
		{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream( PROPERTIES_NAME );
			if( null != in )
			{
				properties.load( in );
			}
		}
		catch( Exception e )
		{
			throw new RuntimeException( "Unable to load required properties from file " + PROPERTIES_NAME, e );
		}
		finally
		{
			if( null != in )
			{
				try
				{
					in.close();
				}
				catch( Throwable ignore )
				{
					// do nothing
				}
			}
		}
	}


	public DatabaseConfiguration getDatabaseConfiguration()
	{
		if( null == databaseConfiguration )
		{
			createDatabaseConfiguration();
		}
		return databaseConfiguration;
	}


	private void createDatabaseConfiguration()
	{
		String dbURL = (String)properties.get( JDBC_URL );
		String username = (String)properties.get( USERNAME );
		String password = (String)properties.get( PASSWORD );
		String initialSql = (String)properties.get( INITIAL_SQL );
		databaseConfiguration = new DatabaseConfiguration( dbURL, username, password );
		databaseConfiguration.setInitStatement( initialSql );
	}

}
