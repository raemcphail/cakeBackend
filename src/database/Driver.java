package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import Main.Communicate;
import dataTypes.Ingredient;
import dataTypes.Recipe;
import database.DBConstants;

/**
 * Enables connection to the database
 * @author Keenan Gaudio, Louis Johnson, Rae Mcphail
 *
 */
public class Driver {
	//Main parent;
	private final long millisMo = (60000*60*24*30);
	private final long millisYr = millisMo * (12);
	protected Connection conn;
	protected PreparedStatement statement;
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
	public Driver()//Main parent)
	{
		//this.parent = parent;
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
	//return user id for valid login or Communicate.INVALID for invalid login
	public int login(String userName, String password) {
		String sql = "SELECT User_Id FROM " + DBConstants.END_USER_TABLE + "AS s WHERE s.Screen_Name = ? AND s.Hashed_Password = SHA2(?,256);";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet ret = statement.executeQuery();
            conn.commit();
            
            if (ret.first()) 
            	return ret.getInt(1);
            else
            	return Communicate.INVALID;
        } catch (SQLException e) {
            e.printStackTrace();        // cause otherwise id have no idea what's happnin'
            return Communicate.INVALID;
        }
	}
	
	private int getRows(String table_name) {
		String sql = "SELECT COUNT(*) FROM " + table_name + ";";
		int set = 0;
		try {
			statement = conn.prepareStatement(sql);
			ResultSet setq = statement.executeQuery(sql);
			setq.next();
			set = setq.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	public boolean addUser(String usrName, String pass, String email, boolean curFlag){
		System.out.println("got values to add: " + usrName + ", " + pass + ", " + email + (curFlag?", cur":", std"));
		String sql = "INSERT INTO " + DBConstants.END_USER_TABLE + " VALUES (NULL, ?, ?, SHA2(?,256), ?)";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, usrName);
            statement.setString(3, pass);    //not hashed because..u know -> it does it upon entering the database
            statement.setBoolean(4, curFlag);
            statement.executeUpdate();
            conn.commit();   // idk why this is needed but it just is and it took me a hour and a half to figure this out :( <- either a comment from drunk-sleep-deprived sunny, or drunk-sleep-deprived keenan
        } catch (SQLException e) {
            e.printStackTrace();        // cause otherwise id have no idea what's happnin'
            return false;
        }
        return true;
	}
	public boolean addStdUser(int id, String first, String last, int num ){
		String sql = "INSERT INTO " + DBConstants.STD_USER_TABLE + " VALUES (?, ?, ?, ?);";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt	(1, id);
            statement.setString	(2, first);
            statement.setString	(3, last);
            statement.setInt	(4, num);
            statement.executeUpdate();
            conn.commit();   
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
        return true;
	}
	
	public boolean addCurator(int id, String CC, int expMon, int expYr, int cvv){
		Date exp = new Date((expMon * millisMo) + ((expYr-1970) * millisYr) );
		
		String sql = "INSERT INTO `Project_Database`.`CURATOR`(`User_Id`, `Credit_Card`, `Exp_Date`, `Sec_Num`) VALUES(?, ?, ?, ?);";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt	(1, id);
            statement.setString	(2, CC);
            statement.setDate	(3, exp);
            statement.setInt	(4, cvv);
            statement.executeUpdate();
            conn.commit();   
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
        return true;
	}
	
	public boolean addRecipe(Recipe r) {
		//ingredients should be packaged like: ing1,count1,Unit1:ing2,count2,Unit2:ing3,count3,Unit3
		ArrayList<String> ingredients = new ArrayList<String>(Arrays.asList(r.rIngredients.split(":")));
		//System.out.println(r);
		String sql1 = "INSERT INTO `Project_Database`.`RECIPE`(`Recipe_Id`, `Name`, `PrepTime`, `CookName`, `Rating`, `Instructions`) VALUES(?, ?, ?, ?, ?, ?);" ;
		
		String sql2 = "INSERT INTO `Project_Database`.`RECIPE_CONTAINS`(`Recipe_Id`, `Ingredient`, `Quantity`, `Unit`) VALUES(?, ?, ?, ?);" ;
		Integer rows = 0;
		try {
        	rows = getRows(DBConstants.RECIPE_TABLE) + 1;	//make an ID because we forgot to set up autoincrement and now it gets mad at us if we try to change it
            statement = conn.prepareStatement(sql1);
            statement.setInt	(1, rows);
            statement.setString	(2, r.rName);
            statement.setInt	(3, r.rTime);
            statement.setString	(4, r.creator);
            statement.setFloat	(5, r.rating);
            statement.setString	(6, r.instructions);
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();        // cause otherwise id have no idea what's happnin'
            return false;
        }
		if (rows == 0) 
			return false;
		
		final int rId = rows;
		
        //add ingredients
        ingredients.forEach(i -> {
        	try {
        		System.out.println("i: " + i);
            	String[] ingr_Info = i.split(",");
            	(Arrays.asList(ingr_Info)).forEach(s -> {System.out.println("\t" + s);});
            	String iName = ingr_Info[0];
            	int iCount = Integer.valueOf(ingr_Info[1]);
            	String iUnit = ingr_Info[2];
            	statement = conn.prepareStatement(sql2);
                statement.setInt	(1, rId );
                statement.setString	(2, iName );
                statement.setInt	(3, iCount );
                statement.setString	(4, iUnit );
                statement.executeUpdate();
                conn.commit();
        	}catch(Exception e){ 
        		e.printStackTrace();
        	}
        });
        return true;
	}

	public boolean addIngredient(Ingredient ingredient) {
		String sql = "INSERT INTO `Project_Database`.`INGREDIENT`(`Name`, `Cal/g`) VALUES(?, ?);";
		try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, ingredient.name);
            statement.setFloat(2, ingredient.calPerGram);
            statement.executeUpdate();
            conn.commit();   // idk why this is needed but it just is and it took me a hour and a half to figure this out :( <- either a comment from drunk-sleep-deprived sunny, or drunk-sleep-deprived keenan
        } catch (SQLException e) {
            e.printStackTrace();        // cause otherwise id have no idea what's happnin'
            return false;
        }
        return true;
	}
	//gets list of recipes but not their ingredients (ingredients can be seen after selecting a recipe?)
	public ArrayList<Recipe> getUserRecipes(int userId) {
		ArrayList<Recipe> ret = new ArrayList<Recipe>();
		String sql = "SELECT * FROM " + DBConstants.RECIPE_TABLE + " WHERE CookName IN (SELECT Screen_Name FROM " + DBConstants.END_USER_TABLE + " WHERE User_Id = ?) "
				+ "OR Recipe_Id IN (SELECT Recipe_Id FROM " + DBConstants.USER_RECIPES_TABLE + " WHERE User_Id = ?);";
		try {
			statement = conn.prepareStatement(sql);
			statement.setInt(1, userId);
			statement.setInt(2, userId);
			ResultSet set = statement.executeQuery();
			set.first();
			while(!set.wasNull()) {
				ret.add( Recipe.parse(set) );
				set.next();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return ret;
	}
	public ArrayList<Ingredient> getRecipeIngredients(int Recipe_Id) {
		ArrayList<Ingredient> ret = new ArrayList<Ingredient>();
		String sql = "SELECT * FROM " + DBConstants.RECIPE_CONTAINS_TABLE + " AS r, " + DBConstants.INGREDIENT_TABLE + "AS i WHERE r.Ingredient = i.Name AND r.Recipe_Id = ?);";
		try {
			statement = conn.prepareStatement(sql);
			statement.setInt(1, Recipe_Id);
			ResultSet set = statement.executeQuery();
			set.first();
			while(!set.wasNull()) {
				ret.add( Ingredient.parse(set) );
				set.next();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return ret;
	}
}


