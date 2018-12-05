package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataTypes.Ingredient;
import dataTypes.Recipe;
import database.Driver;
import database.DBConstants;

public class Connector {
	
		boolean running;
		ServerSocket sSocket;
		Driver db;
		ExecutorService pool;
		

		Connector(String port) {
			running = false;
			
			try {
				sSocket = new ServerSocket(Integer.parseInt(port));
				pool = Executors.newCachedThreadPool();
				running = true;
			} catch (Exception e) {
			}
		}
		/**
		 * Instantiates new client handler (instance)
		 */
		void accept() {
			if (!running)
				return;
			while (true) {
				try {
					Socket cSocket = sSocket.accept();
					System.out.println("Client connection received.");
					Main c = new Main(cSocket.getInputStream(), cSocket.getOutputStream());
					pool.execute(c);
					
					//System.out.println("Connector: Client sent to external thread.");
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		/**
		 * creates database
		 */
		void startDB() {
			db = new Driver();
		}

		/**
		 * starts the server
		 * 
		 * @param args
		 */
		public static void main(String[] args) {
			try {
				// create port according to args, or use default value defined in communication
				String port = (args.length > 0) ? args[0] : String.format("%d", Communicate.MIN_PORT);

				int portCheck = 0;
				if ((portCheck = Integer.parseInt(port)) < Communicate.MIN_PORT || portCheck > Communicate.MAX_PORT)
					throw new NumberFormatException("Invalid Port Number: Port " + port + " out of range.");

				// I have port-forwarded ports 4200 - 4212, they are unused on IANA, but
				// unofficially used for 'vrml-multi-use'
				Connector c = new Connector(port);
				System.out.println("The server is " + ((c.running) ? "" : "not ") + "running on port " + port);
				
				c.startDB();
				//c.db.addUser("Keenan", "iLoveSunny", "funny@me.com", false);
				
//				String rName, String rIngredients, int rTime, String creator, float rating, String instructions
				///*
				c.db.addIngredient(new Ingredient("bread", 1));
				c.db.addIngredient(new Ingredient("cheese", 2));
				c.db.addIngredient(new Ingredient("butter", 4));
				
				c.db.addRecipe(new Recipe("grilled cheese", "bread,2,slices:cheese,1,slice:butter,1,dollup", 5, "Keenan", 5, 
						"cheese goes on bread, butter each side of bread, place in pan and fry until cheese melts and bread gets crispy"));
				System.out.println("INVALID: " + Communicate.INVALID);
				System.out.println("success: "+c.db.login("happy_dude123", "password123"));
				System.out.println("fail: 	 "+c.db.login("happy_dude127", "password123"));
				System.out.println("fail: 	 "+c.db.login("happy_dude123", "password126"));	//	*/
				c.accept();

			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}
		}
}
