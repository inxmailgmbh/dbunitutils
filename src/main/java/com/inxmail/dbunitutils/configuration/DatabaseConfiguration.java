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
