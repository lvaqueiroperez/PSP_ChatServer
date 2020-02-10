package psp_chatserver;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerHilos extends Thread {

    private Socket newSocket;
    private int id;
    //nickname de momento predeterminado
    private String nickname = "default";

    public ServerHilos(Socket newSocket, int idS) {

        this.newSocket = newSocket;
        this.id = id;

    }

    @Override
    public void run() {

        System.out.println("CLIENTE " + id + " CONECTADO");
        
        UI_Server2.lblConectados.setText("NICKNAME SE HA CONECTADO");
        

    }

}
