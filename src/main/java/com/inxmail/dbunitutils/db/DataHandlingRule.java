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


import com.inxmail.dbunitutils.configuration.Configuration;
import com.inxmail.dbunitutils.configuration.DatabaseConfiguration;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


/**
 * Original class from Bartosz Majsak uses <code>TestWatchman</code> which is depracted since JUnit 4.9.
 * Changed to TestWatcher as recommended by JUnit.
 * 
 * @author Stefan Biermann
 */
public class DataHandlingRule extends TestWatcher
{
	@Override
	protected void finished( Description description )
	{
		super.finished( description );
		PrepareData pc = description.getAnnotation( PrepareData.class );
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


	@Override
	protected void starting( Description description )
	{
		super.starting( description );
		PrepareData pc = description.getAnnotation( PrepareData.class );
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
