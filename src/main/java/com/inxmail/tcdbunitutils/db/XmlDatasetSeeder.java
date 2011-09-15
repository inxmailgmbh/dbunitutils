package com.inxmail.tcdbunitutils.db;

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

import com.inxmail.tcdbunitutils.configuration.DatabaseConfiguration;


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
