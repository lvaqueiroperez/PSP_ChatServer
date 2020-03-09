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
    //texto
    private String txtArea;

    //EN EL CONSTRUCTOR SOLO ESTABLECEMOS EL SOCKET RECIBIDO JUNTO A SU ID
    public ChatServerHilos(Socket newSocket, int idS) {

        this.newSocket = newSocket;
        this.id = idS;

    }

    //GETTERS/SETTERS DEL NOMBRE Y TEXTO
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTxtArea(String txtArea) {
        this.txtArea = txtArea;
    }

    public String getTxtArea() {
        return txtArea;
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
        System.out.println("ACTUALMENTE HAY " + UI_ChatServer1.n + " USUARIOS CONECTADOS");

        //ANTES DE NADA, SI EL USER YA EXISTE EN LA LISTA, LE ENVIAMOS EL CONTENIDO DE SU ÚLTIMA SESIÓN (SOLO A ÉL)
        int existe = 0;
        if (UI_ChatServer1.nombres.isEmpty() == false) {

            for (ChatServerHilos z : UI_ChatServer1.nombres) {

                if (z.getNickname().equals(nickname)) {

                    System.out.println("*****USUARIO YA EXISTENTE, ENVIANDO HISTORIAL*****");
                    existe = 1;
                    
                    //enviamos el historial que tengamos
                    
                    
                    break;
                }

            }

        }

        if (existe == 0) {
            //AÑADIMOS EL NICKNAME(ahora la clase) AL ARRAYLIST
            UI_ChatServer1.nombres.add(this);
        }
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

                //COMPROBAMOS SI EL MENSAJE ES UN /BYE
                if (msg.equals("/bye")) {

                    //DISMINUIMOS EL NUMERO DE USERS CONECTADOS
                    UI_ChatServer1.n--;
                    //Y LO QUITAMOS DEL ARRAY DE CONEXIONES
                    for (ChatServerHilos cliente : UI_ChatServer1.listaClientes) {

                        if (cliente.id == id) {
                            UI_ChatServer1.listaClientes.remove(cliente);
                            //salimos de este for
                            break;
                        }

                    }

                    //INFORMAMOS A LOS CLIENTES DE QUIÓN SE HA DESCONECTADO
                    for (ChatServerHilos z : UI_ChatServer1.listaClientes) {

                        DataOutputStream dos = new DataOutputStream(z.newSocket.getOutputStream());

                        dos.writeUTF(nickname + " SE HA DESCONECTADO");

                    }

                } else {

                    System.out.println(msg);
                    //Y LO ENVIAMOS DE VUELTA A TODOS LOS CLIENTES (TODOS LOS CLIENTES DEBEN DE RECIBIR TODOS LOS MENSAJES ESCRITOS !!!)

                    for (ChatServerHilos z : UI_ChatServer1.listaClientes) {

                        DataOutputStream dos = new DataOutputStream(z.newSocket.getOutputStream());

                        dos.writeUTF(msg);

                    }
                }

            }

        } catch (IOException ex) {
            //SI UN USUARIO SE DESCONECTA DEL CHAT, SALTARÁ ESTA EXCEPCIÓN:
            System.out.println("***** " + nickname + " SE HA DESCONECTADO *****");
            
            try {
                //COMO SE HA DESCONECTADO, TENEMOS QUE GUARDAR SU TXTAREA POR SI SE VUELVE A CONECTAR
                DataInputStream dinArea = new DataInputStream(newSocket.getInputStream());
                
                this.txtArea = dinArea.readUTF();
                
                //AHORA LO PONEMOS EN EL ARRAYLIST DONDE CORRESPONDE
                
                
            } catch (IOException ex1) {
                Logger.getLogger(ChatServerHilos.class.getName()).log(Level.SEVERE, null, ex1);
            }
            

            //COMPROBAMOS N PARA SABER CUANTOS USUARIOS HAY CONECTADOS:
            if (UI_ChatServer1.n == 0) {
                System.out.println("NINGÚN USUARIO CONECTADO");
            } else {
                System.out.println("ACTUALMENTE HAY " + UI_ChatServer1.n + " USUARIOS CONECTADOS");
            }
        }

    }

}
