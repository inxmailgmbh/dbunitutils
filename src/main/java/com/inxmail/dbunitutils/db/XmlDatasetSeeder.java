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

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import com.inxmail.dbunitutils.configuration.DatabaseConfiguration;


/**
 * @author Bartosz Majsak
 */
public class XmlDatasetSeeder implements DataSeeder
{

	private IDatabaseConnection databaseConnection;

	private final DatabaseConfiguration dbConfig;

	private final String xmlFile;

	public XmlDatasetSeeder( String xmlFile, DatabaseConfiguration dbConfig )
	{
		this.dbConfig = dbConfig;
		this.xmlFile = xmlFile;
	}


	@Override
	public void prepare()
	{
		try
		{
			setupDatabase();
			applyInitStatement();
			fillDatabase();
			close();
		}
		catch( Exception e )
		{
			throw new RuntimeException( e );
		}

	}


	@Override
	public void cleanup()
	{
		try
		{
			setupDatabase();
			cleanDatabase();
			close();
		}
		catch( Exception e )
		{
			throw new RuntimeException( e );
		}
	}


	private void close()
	{
		try
		{
			if( databaseConnection != null )
				databaseConnection.close();
		}
		catch( Exception e )
		{
			throw new RuntimeException( e );
		}
	}


	private void applyInitStatement()
	{
		try
		{
			if( dbConfig.getInitStatement() != null && dbConfig.getInitStatement().length() > 1 )
			{
				Statement initStatement = databaseConnection.getConnection().createStatement();
				initStatement.execute( dbConfig.getInitStatement() );
			}
		}
		catch( SQLException e )
		{
			throw new RuntimeException( e );
		}

	}


	private void setupDatabase() throws IOException, SQLException, DatabaseUnitException, NamingException
	{
		if( null != databaseConnection )
		{
			return;
		}

		IDatabaseConnection con;
		if( null != dbConfig.getDataSource() )
		{
			con = new DatabaseDataSourceConnection( new InitialContext(), dbConfig.getDataSource() );
		}
		else
		{
			con = new DatabaseConnection( DriverManager.getConnection( dbConfig.getUrl(), dbConfig.getUsername(),
					dbConfig.getPassword() ) );
		}
		// used to avoid problems with boolean
		con.getConfig().setProperty( DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new DefaultDataTypeFactory() );
		databaseConnection = con;
	}


	private void fillDatabase() throws IOException, SQLException, DatabaseUnitException
	{
		FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
		URL xmlUrl = getClass().getClassLoader().getResource( xmlFile );
		FlatXmlDataSet fx = flatXmlDataSetBuilder.build( xmlUrl );
		DatabaseOperation.CLEAN_INSERT.execute( databaseConnection, fx );
	}


	private void cleanDatabase() throws DatabaseUnitException, SQLException
	{
		IDataSet dataSet = databaseConnection.createDataSet();
		DatabaseOperation.DELETE_ALL.execute( databaseConnection, dataSet );
	}


}
