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
    private String direccionIP = "default";
    private int puerto = 0;

    //EN EL CONSTRUCTOR SOLO ESTABLECEMOS EL SOCKET RECIBIDO JUNTO A SU ID
    public ChatServerHilos(Socket newSocket, int idS) {

        this.newSocket = newSocket;
        this.id = idS;

    }

    @Override
    public void run() {

        //RECIBIMOS EL NICKNAME, IP Y PUERTO:
        DataInputStream disCliente;
        try {
            disCliente = new DataInputStream(newSocket.getInputStream());

            nickname = disCliente.readUTF();
            direccionIP = disCliente.readUTF();
            puerto = Integer.parseInt(disCliente.readUTF());
        } catch (IOException ex) {
            Logger.getLogger(ChatServerHilos.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("***** NUEVO CLIENTE CONECTADO (" + nickname + " / " + direccionIP + " / " + puerto + ") *****");
        System.out.println("***** ACTUALMENTE HAY " + UI_ChatServer1.n + " USUARIOS CONECTADOS *****");

        //INFORMAMOS AL CHAT DE QUE SE ACABA DE CONECTAR UN USER:
        for (ChatServerHilos z : UI_ChatServer1.listaClientes) {

            DataOutputStream dos;
            try {
                dos = new DataOutputStream(z.newSocket.getOutputStream());
                dos.writeUTF("***** " + nickname + " ACABA DE CONECTARSE AL CHAT *****");

            } catch (IOException ex) {
                Logger.getLogger(ChatServerHilos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            //BUCLE PARA SEGUIR LEYENDO SIEMPRE 
            while (true) {
                DataInputStream din2 = new DataInputStream(newSocket.getInputStream());

                //CUANDO UN CLIENTE ENVÍA UN MENSAJE, EN EL SERVER LO MOSTRAMOS POR EL TERMINAL
                String msg = din2.readUTF();
                System.out.println("***** " + msg + " *****");
                //Y LO ENVIAMOS DE VUELTA A TODOS LOS CLIENTES (TODOS LOS CLIENTES DEBEN DE RECIBIR TODOS LOS MENSAJES ESCRITOS !!!)

                for (ChatServerHilos z : UI_ChatServer1.listaClientes) {

                    DataOutputStream dos = new DataOutputStream(z.newSocket.getOutputStream());

                    dos.writeUTF(msg);

                }

                //COMPROBAMOS SI SE DESCONECTA:
                if (newSocket.isClosed()) {

                    System.out.println("***** CLIENTE " + nickname + "DESCONECTADO *****");

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ChatServerHilos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
