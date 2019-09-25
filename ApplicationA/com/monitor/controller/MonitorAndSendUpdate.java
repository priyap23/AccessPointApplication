package com.monitor.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MonitorAndSendUpdate {
	// initialize socket and input output streams
	private static Socket socket = null;

	// constructor to put ip address and port
	public MonitorAndSendUpdate(String address, int port) {
		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected to server..");
			
		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}
		
	}

	
	public static void monitorAndSendUpdate(String strMessage) {
		// sends output to the socket
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String line = strMessage;
			out.writeUTF(line);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}
}
