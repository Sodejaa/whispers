package fi.utu.tech.telephonegame.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import fi.utu.tech.telephonegame.Message;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectOutputStream ulosTulo;
    private ObjectInputStream sisaanTulo;
    private NetworkService networkService;

    public ClientHandler(Socket socket, NetworkService networkService) {
        this.socket = socket;
        this.networkService = networkService;
        networkService.addToSocketList(this);
    }

    public void run() {
        try {
            InputStream iS = socket.getInputStream();
            OutputStream oS = socket.getOutputStream();
            ulosTulo = new ObjectOutputStream(oS);
            sisaanTulo = new ObjectInputStream(iS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            // Kootaan message olio envelope olion sisällöstä
            try {
                // envelopesta message olio
                Message kirjekuori = (Message) (((Serializable) sisaanTulo.readObject()));
                networkService.getInputQueue().add(kirjekuori);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void send(Object put) {
        try {
            ulosTulo.writeObject(put);
            ulosTulo.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}