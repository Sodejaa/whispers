package fi.utu.tech.telephonegame.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket s;
    private NetworkService t;

    public ClientHandler(Socket s, NetworkService t) {
        this.s = s;
        this.t = t;
    }


public void send(Serializable out) {

    

}

    public void run() {
        try{
            DataInputStream DataInput = new DataInputStream(s.getInputStream());
            DataOutputStream DataOut = new DataOutputStream(s.getOutputStream());
            String message = (String)DataInput.readUTF();
            System.out.println(message);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}    