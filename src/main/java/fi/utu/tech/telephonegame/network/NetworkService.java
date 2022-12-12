package fi.utu.tech.telephonegame.network;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

import javafx.scene.control.ListCell;

public class NetworkService extends Thread implements Network, Serializable {
	/*
	 * Do not change the existing class variables
	 * New variables can be added
	 */
	private TransferQueue<Object> inQueue = new LinkedTransferQueue<Object>(); // For messages incoming from network
	private TransferQueue<Serializable> outQueue = new LinkedTransferQueue<Serializable>(); // For messages outgoing to network
	public LinkedBlockingQueue<Socket> sockets = new LinkedBlockingQueue<Socket>();
	public LinkedBlockingQueue<ObjectOutputStream> sentSockets = new LinkedBlockingQueue<ObjectOutputStream>();
	// Joonaksen lista:
	public List<ClientHandler> ClientHandler = new ArrayList<ClientHandler>();


	/*
	 * No need to change the construtor
	 */
	public NetworkService() {
		this.start();
	}



	/**
	 * Creates a server instance and starts listening for new peers on specified port
	 * The port used to listen incoming connections is provided by the template
	 * 
	 * @param serverPort Which port should we start to listen to?
	 * 
	 */
	public void startListening(int serverPort) {
		try {
			try (// Create a new ServerSocket instance
			ServerSocket serverSocket = new ServerSocket(serverPort)) {
				// Start listening for incoming connections
				while (true) {
					// Accept incoming connections and create a new socket for each connection
					Socket socket = serverSocket.accept();
					
					
					// Do something with the new socket (e.g. send/receive data)
				}
			}
		} catch (IOException e) {
			// Handle exceptions
			System.out.println("start listening error");
		}
	}

	/**
	 * This method will be called when connecting to a peer (other broken telephone
	 * instance)
	 * The IP address and port will be provided by the template (by the resolver)
	 * 
	 * @param peerIP   The IP address to connect to
	 * @param peerPort The TCP port to connect to
	 */
	public void connect(String peerIP, int peerPort) throws IOException, UnknownHostException {
		try {
			// Create a new Socket instance and connect to the peer
			Socket socket = new Socket(peerIP, peerPort);
			ClientHandler ch = new ClientHandler(socket, this);
			ClientHandler.add(ch);

			// Do something with the socket (e.g. send/receive data)
		} catch (IOException e) {
			// Handle exceptions
			System.out.println("connect error");
		}
		
	}

	/**
	 * This method is used to send the message to all connected neighbours (directly connected nodes)
	 * 
	 * @param out The serializable object to be sent to all the connected nodes
	 * 
	 */

	private void send(Serializable out) {
		// Joonas (Lähde)
		for(ClientHandler i : ClientHandler) {
			i.send(out);
		}
	}
		
	/*
	 * Don't edit any methods below this comment
	 * Contains methods to move data between Network and 
	 * MessageBroker
	 * You might want to read still...
	 */

	/**
	 * Add an object to the queue for sending
	 * 
	 * @param outMessage The Serializable object to be sent
	 */
	public void postMessage(Serializable outMessage) {
		try {
			outQueue.offer(outMessage, 1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get reference to the queue containing incoming messages from the network
	 * 
	 * @return Reference to the queue incoming messages queue
	 */
	public TransferQueue<Object> getInputQueue() {
		return this.inQueue;
	}

	/**
	 * Waits for messages from the core application and forwards them to the network
	 */
	public void run() {
		while (true) {
			try {
				send(outQueue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
