package com.monitor.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveAndDisplay {

	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;

	// constructor with port
	public ReceiveAndDisplay(int port) {
		// starts server and waits for a connection
		try {
			server = new ServerSocket(port);
			System.out.println("************* APPLICATION B ************");
			System.out.println("#####Server started#####\n");

			socket = server.accept();

			// takes input from the client socket
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			String line = "";

			// reads message from client until "Over" is sent
			while (!line.equals("Over")) {
				try {
					line = in.readUTF();
					System.out.println("\nNew message received..\n");
					System.out.println(line);

				} catch (IOException i) {
					i.printStackTrace();
				}
			}
			socket.close();
			in.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static void main(String args[]) {
		int port = Integer.parseInt(args[0]);
		ReceiveAndDisplay server = new ReceiveAndDisplay(port);
	}
}
