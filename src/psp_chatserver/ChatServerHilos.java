package psp_chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServerHilos extends Thread {

    private Socket newSocket;
    private int id;
    //nickname de momento predeterminado
    private String nickname = "default";

    public ChatServerHilos(Socket newSocket, int idS) {

        this.newSocket = newSocket;
        this.id = id;

    }

    @Override
    public void run() {

        System.out.println("CLIENTE " + id + " CONECTADO");
        
        try {
            //BUCLE PARA SEGUIR LEYENDO SIEMPRE
            while(true){
            DataInputStream din = new DataInputStream(newSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(newSocket.getOutputStream());
            
            String msg = din.readUTF();
            System.out.println(msg);
            
            dos.writeUTF(msg);
            }
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ChatServerHilos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
