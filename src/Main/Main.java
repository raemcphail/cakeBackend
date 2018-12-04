package Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.flutter.plugin.common.StringCodec;

import database.Driver;

public class Main implements Runnable{
	//handles individual connections
	DataInputStream in;
	DataOutputStream out;
	boolean exit = false;
	int clientType;
	Driver db;
	
	public Main(InputStream in_, OutputStream out_) {
		clientType = 0; // not a valid type
		try {
			// write connected
			out = new DataOutputStream(out_);
			//out.writeInt(Communicate.CONNECTED);
			//out.flush();

			System.out.println("Got wrote mate");

			// wait for response
			in = new DataInputStream(in_);
			System.out.println("Got:  " + readString());
			
			System.out.println("Got input stream, waiting for input int");
			// check if response is what we're expecting
			if (in.readInt() != Communicate.CONNECTED)
				throw new IOException("Unexpected response from Client.");
			System.out.println("Connection to client established.\n" + "server instance running on "
					+ Thread.currentThread().getName());

			db = new Driver(this);
			
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
				String name = readString();
				String pass = readString();
				String mail = readString();
				boolean cur = in.readBoolean();	//yikes maybe not
				db.addUser(name, pass, mail, cur);
		}
	}	
	private String readString() throws IOException {
		String out = "";
		char next = in.readChar();	//maybe do bytes
		while (next != '\0') {
			out += next;
			next = in.readChar();
		}
		return out;
	}
	private void disconnect() {
		exit = true;
	}
}
 