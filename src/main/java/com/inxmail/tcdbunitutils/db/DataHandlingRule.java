package com.inxmail.tcdbunitutils.db;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import com.inxmail.tcdbunitutils.configuration.Configuration;
import com.inxmail.tcdbunitutils.configuration.DatabaseConfiguration;


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
