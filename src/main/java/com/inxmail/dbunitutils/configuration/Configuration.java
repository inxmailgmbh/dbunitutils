/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inxmail.dbunitutils.configuration;

import java.io.InputStream;
import java.util.Properties;


/**
 * @author Bartosz Majsak
 */
public final class Configuration
{

	private static final String PROPERTIES_NAME = "dbunit.properties";
	
	/*
	 * adding jndi datasource
	 * @author Luca Graf
	 */
	private static final String DATASOURCE_NAME = "db.datasource";

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

	/*
	 * adding jndi datasource to usage
	 * @author Luca Graf
	 */
	private void createDatabaseConfiguration()
	{
		String dataSourceName = (String)properties.get( DATASOURCE_NAME );
		if( null != dataSourceName && !dataSourceName.isEmpty() )
		{
			databaseConfiguration = new DatabaseConfiguration( dataSourceName );
		}
		else
		{
			String dbURL = (String)properties.get( JDBC_URL );
			String username = (String)properties.get( USERNAME );
			String password = (String)properties.get( PASSWORD );
			databaseConfiguration = new DatabaseConfiguration( dbURL, username, password );
		}
		String initialSql = (String)properties.get( INITIAL_SQL );
		databaseConfiguration.setInitStatement( initialSql );
	}

}
