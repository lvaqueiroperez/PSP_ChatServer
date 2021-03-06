package psp_chatserver;

import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//USAR EXCEPCIONES PARA INFORMAR QUE EL SERVER SE HA DESCONECTADO A LOS CLIENTES

public class UI_ChatServer1 extends javax.swing.JFrame {

    //VARIBLES DE CLASE
    public static int puerto = 0;
    //HACEMOS UN ARRAY DONDE ALMACENAREMOS TODAS LAS CONEXIONES QUE VAYAN ENTRANDO
    //ESTO ES DE UTILIDAD PARA, POR EJEMPLO, PODER ENVIAR UN MISMO MENSAJE A VARIOS CLIENTES
    //DESDE EL SERVER
    //TENDREMOS QUE QUITAR DEL ARRAY AQUELLOS CLIENTES QUE SE HAYAN DESCONECTADO
    public static ArrayList<ChatServerHilos> listaClientes = new ArrayList<>();
    //CONTADOR DE CLIENTES Y A LA VEZ SU ID
    public static int n = 0;
    //ARRAY DE NOMBRES DE CLIENTES PARA IDENTIFICARLOS SI YA EXISTEN
    //public static ArrayList<ChatServerHilos> nombres = new ArrayList<ChatServerHilos>();

    /**
     * Creates new form UI_Server1
     */
    public UI_ChatServer1() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("BIENVENIDO");

        btnIniciar.setText("INICIAR");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        jLabel3.setText("Server Chat");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(153, 153, 153)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(jLabel1)))
                .addContainerGap(201, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(btnIniciar)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        //USANDO ESTE TIPO DE MÉTODOS DEL JOPTIONPANE, NOS ASEGURAMOS DE QUE NO SE HAGA
        //NINGUNA OTRA ACCIÓN HASTA QUE DESAPAREZCA EL MENÚ DE JOPTIONPANE !!!
        do {
            puerto = Integer.parseInt(JOptionPane.showInputDialog("INTRODUCE EL PUERTO QUE DESEAS UTILIZAR: "));

            if ((puerto < 1000) || (puerto > 9999)) {
                System.out.println("PUERTO INCORRECTO, INTRODÚZCALO OTRA VEZ");

            }
        } while ((puerto < 1000) || (puerto > 9999));

        //EN LOS SERVER, NO SE SUELEN PONER INTERFACES DETALLADAS
        //MEJOR USAR EL TERMINAL PARA CASI TODO !!!
        System.out.println("***** CREANDO SOCKET SERVIDOR *****");

        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress("localhost", UI_ChatServer1.puerto);

            System.out.println("***** REALIZANDO BIND *****");

            serverSocket.bind(addr);

            System.out.println("***** PUERTO " + UI_ChatServer1.puerto + " *****");

            /*
        podríamos poner != null para que, cuando serverSocket deje de recibir
        conexiones, el server se apagase directamente
             */
            int serverStatus = 1;

            //CAMBIAR LA CONDICION 
            System.out.println("NINGÚN USUARIO CONECTADO");
            while (serverStatus == 1) {

                System.out.println("****** ACEPTANDO CONEXIONES ******");
                //PONER EL NUEVO SOCKET COMO PARÁMETRO DEL HILO PARA DIFERENCIARLOS Y QUE FUNCIONE
                Socket newSocket = serverSocket.accept();

                //CADA CLIENTE QUE SE CONECTE SERÁ UN HILO, CON SUS ATRIBUTOS DE LA
                //CLASE DE HILOS
                //CADA UNO TENDRÁ SU SOCKET, UN NÚMERO A MODO DE "ID" 
                //Y LUEGO SE LE PONDRÁ SU NICKNAME, QUE DE MOMENTO SERÁ PREDETERMINADO
                if (n < 2) {

                    ChatServerHilos c = new ChatServerHilos(newSocket, n);
                    //AÑADIMOS ANTES DE NADA EL OBJETO HILO A NUESTRO ARRAY, PARA TENER LA INFORMACIÓN DE SU SOCKET
                    listaClientes.add(c);
                    n++;
                    c.start();

                } else {
                    System.out.println("MÁXIMO NÚMERO DE CLIENTES ALCANZADO");

                    //ENVIAR MENSAJE INFORMATIVO AL CLIENTE DE QUE NO SE PUEDE CONECTAR:
                    DataOutputStream dosInfo = new DataOutputStream(newSocket.getOutputStream());

                    dosInfo.writeUTF("SERVER LLENO");

                }

                //MENSAJES RECIBIDOS DE LOS CLIENTES EN LOS HILOS !!!
                //(ya que el server se va a mantener abierto esperando conexiones y no va a poder hacer otra cosa)
            }

        } catch (IOException ex) {
            Logger.getLogger(UI_ChatServer1.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnIniciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UI_ChatServer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI_ChatServer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI_ChatServer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI_ChatServer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI_ChatServer1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
