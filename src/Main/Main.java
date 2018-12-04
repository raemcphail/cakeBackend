package Main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.midi.SysexMessage;

import database.Driver;

public class Main implements Runnable{
	//handles individual connections
	ObjectInputStream in;
	ObjectOutputStream out;
	boolean exit = false;
	int clientType;
	Driver db;
	
	public Main(InputStream in_, OutputStream out_/* , modelhandler */) {
		clientType = 0; // not a valid type
		try {
			// write connected
			out = new ObjectOutputStream(out_);
			// in = new ObjectInputStream(in_);
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

			db = new Driver(this);
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
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
				int tag = in.readInt();
				//System.err.println("Got command: " + tag);
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
		}
		
	}	
	
	public void disconnect() {
		exit = true;
	}
}
 