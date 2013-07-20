package com.titanserver.proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleProxyServer2 {
	public static void main(String[] args) {
		try {
			new Thread(new SimplePS(1234)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class SimplePS implements Runnable {
	private ServerSocket listenSocket;

	public SimplePS(int port) throws IOException {
		this.listenSocket = new ServerSocket(port);
	}

	public void run() {
		for (;;) {
			try {
				Socket clientSocket = listenSocket.accept();
				System.out.println("Create a new Thread to handle this connection");
				new Thread(new ConnectionHandler(clientSocket)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ProxyCounter {
	static int sendLen = 0;
	static int recvLen = 0;

	public static void showStatistics() {
		System.out.println("sendLen = " + sendLen);
		System.out.println("recvLen = " + recvLen);
	}
}

// must close sockets after a transaction                                                                                                                               
class ConnectionHandler extends ProxyCounter implements Runnable {
	private Socket clientSocket;
	private Socket serverSocket;

	private static final int bufferlen = 8192;

	public ConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		// receive request from clientSocket,                                                                                                                           
		//extract hostname,                                                                                                                                             
		//create a serverSocket to communicate with the host                                                                                                            
		// count the bytes sent and received                                                                                                                            
		try {
			byte[] buffer = new byte[bufferlen];
			int count = 0;

			InputStream inFromClient = clientSocket.getInputStream();
//			count = inFromClient.read(buffer);
//			String request = new String(buffer, 0, count);
			String host = "210.5.164.14";//extractHost(request);
			// create serverSocket   
			Socket serverSocket = new Socket(host, 9401);
			// forward request to internet host                                                                                                                         
			OutputStream outToHost = serverSocket.getOutputStream();
//			outToHost.write(buffer, 0, count);
//			outToHost.flush();
			sendLen += count;
			showStatistics();
			// forward response from internet host to client                                                                                                            
			InputStream inFromHost = serverSocket.getInputStream();
			OutputStream outToClient = clientSocket.getOutputStream();
			while (true) {
				count = inFromHost.read(buffer);
				if (count < 0)
					break;
				outToClient.write(buffer, 0, count);
				outToClient.flush();
				recvLen += count;
				showStatistics();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String extractHost(String request) {
		int start = request.indexOf("Host: ") + 6;
		int end = request.indexOf('\n', start);
		String host = request.substring(start, end - 1);
		return host;
	}
}
