package com.c2.pandoraserver.proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ObjectStreamSimpleProxyServer2 {
	public static void main(String[] args) throws IOException {
		try {
			String host = args[1];
			int remoteport = Integer.parseInt(args[2]);
			int localport = Integer.parseInt(args[0]);

			System.out.println("Starting proxy for " + host + ":" + remoteport + " on port " + localport);

			runServer(host, remoteport, localport); // never returns
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void runServer(String host, int remoteport, int localport) throws IOException {
		ServerSocket ss = new ServerSocket(localport);

		while (true) {
			Socket client = null, server = null;
			try {
				client = ss.accept();
				System.out.println("connected");

				final InputStream streamFromClient = client.getInputStream();
				final OutputStream streamToClient = client.getOutputStream();

				try {
					server = new Socket(host, remoteport);
				} catch (IOException e) {
					PrintWriter out = new PrintWriter(streamToClient);
					out.print("Proxy server cannot connect to " + host + ":" + remoteport + ":\n" + e + "\n");
					out.flush();
					client.close();
					continue;
				}

				final ObjectOutputStream streamToServer = new ObjectOutputStream(server.getOutputStream());
				final ObjectInputStream streamFromServer = new ObjectInputStream(server.getInputStream());

				Thread t = new Thread() {
					public void run() {
						int bytesRead;
						try {
							byte bb[] = new byte[1024];
							while ((bytesRead = streamFromClient.read(bb)) > 0) {
								streamToServer.write(bb, 0, bytesRead);
								streamToServer.flush();
							}
						} catch (IOException e) {
						}

						try {
							streamToServer.close();
						} catch (IOException e) {
						}
					}
				};

				t.start();

				int bytesRead;
				try {
					byte bb[] = new byte[1024];
					while ((bytesRead = streamFromServer.read(bb)) > 0) {
						streamToClient.write(bb, 0, bytesRead);
						streamToClient.flush();
					}
				} catch (IOException e) {
				}
				streamToClient.close();
			} catch (IOException e) {
				System.err.println(e);
			} finally {
				try {
					if (server != null)
						server.close();
					if (client != null)
						client.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
