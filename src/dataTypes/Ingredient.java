package dataTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ingredient {
	public float calPerGram;
	public String name, unit;
	public int quantity;

	
	public Ingredient(String name, float calPerGram) {
		this.name = name.toLowerCase();
		this.calPerGram = calPerGram;
	}
	public Ingredient(String name, float calPerGram, int quantity, String unit) {
		this.name = name.toLowerCase();
		this.calPerGram = calPerGram;
		this.quantity = quantity;
		this.unit = unit;
	}
	public static Ingredient parse(ResultSet set) throws SQLException {
		/* 
		`Name`
		`Cal/g`
		*/
		String ingName, unit;
		float calGram;
		int quantity;
		ingName 	= set.getString	(1);
		calGram 	= set.getFloat	(2);
		quantity 	= set.getInt	(3);
		unit		= set.getString	(4);
		return new Ingredient(ingName, calGram, quantity, unit);
	}
	
}
