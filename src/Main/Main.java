package Main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataTypes.Recipe;
import io.flutter.plugin.common.StringCodec;

import database.Driver;

public class Main implements Runnable{
	//handles individual connections
	ObjectInputStream in;
	ObjectOutputStream out;
	boolean exit = false;
	int clientType;
	Driver db;
	
	public Main(InputStream in_, OutputStream out_) {
		clientType = 0; // not a valid type
		try {
			// write connected
			out = new ObjectOutputStream(out_);
			out.writeInt(Communicate.CONNECTED);
			out.flush();

			System.out.println("Got wrote mate");

			// wait for response
			in = new ObjectInputStream(in_);
			
			System.out.println("Got input stream, waiting for input int");
			// check if response is what we're expecting
			if (in.readInt() != Communicate.CONNECTED)
				throw new IOException("Unexpected response from Client.");
			System.out.println("Connection to client established.\n" + "server instance running on "
					+ Thread.currentThread().getName());

			db = new Driver();
			
		} catch (IOException e) {
			System.err.println("something happened: " + e.getLocalizedMessage() + " : " + e.getMessage());
		}
		// set up model handling here
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (!exit) {
				int tag = in.readInt();	//0x0000ffff 
				System.err.println("Got command: " + Integer.toHexString(tag) );
				try {
					executeCommand( tag );
				} catch (Exception e) { e.printStackTrace(); }
			}
		} catch (EOFException e) {
			System.err.println("Client Disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void executeCommand(int tag) throws IOException {	//can be separated to handle gets/seraches/adds with binary ops
		switch (tag) {
			case Communicate.DISCONNECT:
				disconnect();
				break;
			case Communicate.ADD_ANY_USER:
				String name = in.readUTF();
				String pass = in.readUTF();
				String mail = in.readUTF();
				boolean cur = in.readBoolean();	
				db.addUser(name, pass, mail, cur);
				break;
			case Communicate.ADD_RECIPES:
				String rName = in.readUTF();
				String rIngredients = in.readUTF();
				int rTime = in.readInt();
				String creator = in.readUTF();	
				float rating = in.readFloat();
				String instr = in.readUTF();	
				db.addRecipe( new Recipe(0, rName, rIngredients, rTime, creator, rating, instr) );
		}
	}	
	
	private void disconnect() {
		exit = true;
	}
}
 