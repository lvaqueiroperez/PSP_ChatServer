package psp_chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServerHilos extends Thread {

    //SOCKET PARA CADA CLIENTE
    private Socket newSocket;
    private int id;
    //nickname de momento predeterminado
    private String nickname = "default";

    //EN EL CONSTRUCTOR SOLO ESTABLECEMOS EL SOCKET RECIBIDO JUNTO A SU ID
    public ChatServerHilos(Socket newSocket, int idS) {

        this.newSocket = newSocket;
        this.id = idS;

    }

    @Override
    public void run() {
        //siempre muestra 0 si lo ponemos como id, pero no al acceder a la variable n?
        System.out.println("CLIENTE " + id + " CONECTADO");

        try {
            //BUCLE PARA SEGUIR LEYENDO SIEMPRE 
            while (true) {
                DataInputStream din = new DataInputStream(newSocket.getInputStream());
                
                //CUANDO UN CLIENTE ENVÍA UN MENSAJE, EN EL SERVER LO MOSTRAMOS POR EL TERMINAL
                String msg = din.readUTF();
                System.out.println(msg);
                //Y LO ENVIAMOS DE VUELTA A TODOS LOS CLIENTES (TODOS LOS CLIENTES DEBEN DE RECIBIR TODOS LOS MENSAJES ESCRITOS !!!)
                
                for(ChatServerHilos z : UI_ChatServer1.listaClientes){
                    
                DataOutputStream dos = new DataOutputStream(z.newSocket.getOutputStream());
                    
                dos.writeUTF(msg);
                
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ChatServerHilos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
