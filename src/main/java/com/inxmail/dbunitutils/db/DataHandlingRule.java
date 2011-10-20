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
package com.inxmail.dbunitutils.db;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import com.inxmail.dbunitutils.configuration.Configuration;
import com.inxmail.dbunitutils.configuration.DatabaseConfiguration;


/**
 * @author Bartosz Majsak
 */
public class DataHandlingRule extends TestWatchman
{

	@Override
	public void starting( FrameworkMethod method )
	{
		PrepareData pc = method.getAnnotation( PrepareData.class );
		if( null == pc )
		{
			return;
		}

		DataSeeder dataSeeder = createDataSeeder( pc.dataSetFile(), pc.dataSourceName() );
		try
		{
			dataSeeder.prepare();
		}
		catch( Exception e )
		{
			throw new RuntimeException( e );
		}
	}


	@Override
	public void finished( FrameworkMethod method )
	{
		PrepareData pc = method.getAnnotation( PrepareData.class );
		if( null == pc )
		{
			return;
		}

		DataSeeder dataSeeder = createDataSeeder( pc.dataSetFile(), pc.dataSourceName() );

		try
		{
			dataSeeder.cleanup();
		}
		catch( Exception e )
		{
			throw new RuntimeException( e );
		}
	}


	private DataSeeder createDataSeeder( String dataSetName, String dataSourceName )
	{
		DatabaseConfiguration databaseConfiguration = Configuration.instance().getDatabaseConfiguration();
		if( !dataSourceName.isEmpty() )
		{
			databaseConfiguration.setDataSourceName( dataSourceName );
		}
		return new XmlDatasetSeeder( dataSetName, databaseConfiguration );
	}

}
