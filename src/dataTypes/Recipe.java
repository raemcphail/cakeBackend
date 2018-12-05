package dataTypes;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConstants;

public class Recipe implements Serializable{
	//ingredients should be packaged like: ing1,count1,Unit1:ing2,count2,Unit2:ing3,count3,Unit3
	public String rName, rIngredients, creator, instructions;
	public int rTime, recipe_Id;
	public float rating;

	/**
	 * @param rName
	 * @param rIngredients
	 * @param rTime
	 * @param creator
	 * @param rating
	 * @param instructions
	 */
	public Recipe(int recipe_Id, String rName, String rIngredients, int rTime, String creator, float rating, String instructions) {
		this.recipe_Id = recipe_Id;
		this.rName = rName;
		this.rIngredients = rIngredients;
		this.rTime = rTime;
		this.creator = creator;
		this.rating = rating;
		this.instructions = instructions;
	}

	public Recipe(String rName, String rIngredients, int rTime, String creator, float rating, String instructions) {
		this.rName = rName;
		this.rIngredients = rIngredients;
		this.rTime = rTime;
		this.creator = creator;
		this.rating = rating;
		this.instructions = instructions;
	}

	public static Recipe parse(ResultSet set) throws SQLException {
		/* 
		`Recipe_Id` = int
		`Name` = string
		`PrepTime` string
		`CookName` string
		`Rating` = float
		`Instructions` = string
		*/
		String rName, creator, instructions;
		int rTime, id;
		float rating;
		id 				= set.getInt	(1);
		rName 			= set.getString	(2);
		rTime 			= Integer.valueOf(set.getString(3)); 
		creator 		= set.getString	(4);
		rating 			= set.getFloat	(5);
		instructions 	= set.getString	(6);
		return new Recipe(id, rName, "", rTime, creator, rating, instructions);
	}
}
