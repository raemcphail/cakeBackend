package database;

import java.sql.*;

import Main.Communicate;
import Main.Main;

/**
 * Enables connection to the database
 * @author Keenan Gaudio, Louis Johnson, Rae Mcphail
 *
 */
public class Driver {
	Main parent;
	protected Connection conn;
	protected Statement stmt;
	public String databaseName = "Project_Database";
	
	/*
	public String connectionInfo = "jdbc:mysql://localhost:3306/pms_ensf480?useSSL=false",  
			  login          = "root",
			  password       = "HelloWorld";
	
	protected String docTable = "document", operatorTable = "operator", orderTable = "order",
			promoTable = "promotion", regTable = "registered_buyer", approveTable = "approval_documents";
	*/
	public String connectionInfo = "jdbc:mysql://cpsc471-project-instance.ceecwhryx0kc.us-east-2.rds.amazonaws.com/", 
				  dbName = "Project_Database",
				  SSLtag = "?useSSL=false",
				  login = "masterUser",
				  password = "masterPassword"; 
            
	
	/**
	 * Constructor for Driver class. Enables the connection to the database.
	 */
	public Driver(Main parent)
	{
		this.parent = parent;
		int MAXATTEMPS = 5;
		int attempts = 0;
		while(attempts <=MAXATTEMPS) {
		try{
			// If this throws an error, make sure you have added the mySQL connector JAR to the project
			Class.forName("com.mysql.jdbc.Driver");
			
			// If this fails make sure your connectionInfo and login/password are correct
			conn = DriverManager.getConnection(connectionInfo + dbName + SSLtag, login, password);
			conn.setAutoCommit(false);
			System.out.println("Connected to: " + connectionInfo + "\n");
			attempts = MAXATTEMPS + 1; 
		}
		catch(SQLException e) {
			e.printStackTrace();
			attempts++;
		}
		catch(Exception e) { e.printStackTrace(); }
		}
		
	}	
	
	
	public void addUser(String name, String pass, String mail, boolean curFlag){
		System.out.println("got values to add: " + name + ", " + pass + ", " + mail + (curFlag?", cur":", std"));
		//query stuff
	}
}


