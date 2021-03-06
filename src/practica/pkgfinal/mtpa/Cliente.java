package practica.pkgfinal.mtpa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import java.net.Socket;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.WindowConstants;

import static practica.pkgfinal.mtpa.Connection.ficheroUsuariosConectados;

/**
 * Esta clase representa a todo usuario que se conecta al sistema. Esta clase es
 * la que se encarga de controlar los elementos de Java Swing que le llegan desde
 * la Clase Panel mediante setters y getters
 * @author Nelson Tuesta Fernández
 * @version 6.0
 * @since 05/09/2020
 */
public class Cliente{
    
    /**
     * Estos JLabel muestran errores/mensajes de éxito cuando el usuario realiza
     * alguna acción en alguno de los paneles
     */
    private static JLabel errorDatosIncompletos;
    private static JLabel errorDatosIncorrectos;
    private static JLabel errorDatosIncompletos2;
    private static JLabel errorPassNoCoincidente;
    private static JLabel errorUsuarioYaExiste;
    private static JLabel errorRestriccionesPassword;
    private static JLabel errorSimbolosProhibidos;
    private static JLabel errorInicioSesionDoble;
    private static JLabel registroCompletado;
    private static JLabel errorAjustesIncompletos;
    private static JLabel errorUsuarioNoDisponible;
    private static JLabel errorRetoYaEnviado;
    private static JLabel errorAutoReto;
    private static JLabel lanzarRetoCompletado;
    private static JLabel errorTipoRankingNoElegido;
    private static JLabel elegirTipoRankingCompletado;
    
    /**
     * Estos cuatro objetos son el panel y los tres JDialog que usa mi programa.
     * Los JDialog los uso para poder mostrar las tablas de manera correcta
     */
    private static Panel miPanel = new Panel();
    private static JDialog dialogUsuariosConectados = new JDialog(miPanel, false);
    private static JDialog dialogMisRetos = new JDialog(miPanel, false);
    private static JDialog dialogVerRankings = new JDialog(miPanel, false);
    private static JDialog dialogTiempoRestante = new JDialog(miPanel, false);
    
    
    /**
     * Todos estos objetos son variables de tipo String y boolean que se usan
     * para recoger la información que ha indicado el usuario en la interfaz,
     * para indicar qué botones han sido pulsados o qué opciones ha elegido
     */
    private static String usuarioInicioSesion;
    private static String passwordInicioSesion;
    private static String usuarioRegistro;
    private static String passRegistro;
    private static String passConfirmRegistro;
    private static String usuarioConectado;
    
    private static String modalidad;
    private static String numTandas;
    private static String tiempo;
    private static String oponenteReto;
    private static String nombreDelPanel;
    private static String tipoRankingElegido;
    private static String numJugadoresString;
    private static int numJugadoresInt;
    private static int numMisRetos;
    private static int numConexiones;
    private static int numRankings;
    private static String retador;
    
    private static boolean retoLanzadoConExito;
    private static boolean irAlPanelCuatro;
    private static boolean irAlPanelSeis;
    private static boolean volverALosRetos;
    private static boolean cerrarSesionJugador;
    private static boolean verLosRankings;
    private static boolean rankingActualizadoConExito;
    private static boolean atrasRankings;
    private static boolean actualizarRanking;
    
    
    /**
     * Método main de la clase Cliente
     * @param args Argumentos del main
     */
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int serverPort = 9999;
        
        boolean aceptarInicioSesion = false;
        boolean aceptarRegistroNuevoUsuario = false;
        boolean registrateAqui = false;
        boolean volverAtras = false;
        boolean lanzarUnReto = false;
        boolean actualizarJugadoresConectados = false;
        boolean retoRecibido = false;
        boolean conectar = false;
        boolean desconectar = false;
        
        String tipoErrorInicioSesion = "Error";
        String tipoErrorRegistro = "Error";
        String tipoErrorLanzarReto = "Error";
        String tipoErrorTipoRanking = "Error";
        
        DataInputStream in;
        DataOutputStream out;
                      
        try{
            Socket clientSocket = new Socket(HOST, serverPort);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            
            int tipoPanel;
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            dialogMisRetos.setSize(380, 120);
            dialogUsuariosConectados.setSize(321, 200);
                        
            miPanel.addComponentListener(new ComponentAdapter() {
                Point ultimaUbicacion;
                
                @Override
                public void componentMoved(ComponentEvent e){
                    try{

                        dialogMisRetos.toFront();
                        getDialogUsuariosConectados().toFront();
                        dialogVerRankings.toFront();
                        dialogTiempoRestante.toFront();

                        if(ultimaUbicacion == null && miPanel.isVisible()){

                            ultimaUbicacion = miPanel.getLocation();

                        }else{
                            Point nuevaUbicacion = miPanel.getLocation();
                            int dx = nuevaUbicacion.x - ultimaUbicacion.x;
                            int dy = nuevaUbicacion.y - ultimaUbicacion.y;
                            
                            dialogMisRetos.setLocation(dialogMisRetos.getX() + dx, dialogMisRetos.getY() + dy);
                            getDialogUsuariosConectados().setLocation(getDialogUsuariosConectados().getX() + dx, getDialogUsuariosConectados().getY() + dy);
                            dialogVerRankings.setLocation(dialogVerRankings.getX() + dx, dialogVerRankings.getY() + dy);
                            dialogTiempoRestante.setLocation(dialogTiempoRestante.getX() + dx, dialogTiempoRestante.getY() + dy);

                            ultimaUbicacion = nuevaUbicacion;

                        }
                    }catch(NullPointerException npe){}
                }
            });
            
            miPanel.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent e){
                    
                    dialogMisRetos.toBack();
                    getDialogUsuariosConectados().toBack();
                    dialogVerRankings.toBack();
                    dialogTiempoRestante.toBack();
                }
                
            });
                        
            dialogMisRetos.setLocationRelativeTo(miPanel);
            dialogUsuariosConectados.setLocationRelativeTo(miPanel);
            dialogVerRankings.setLocationRelativeTo(miPanel);
            dialogTiempoRestante.setLocationRelativeTo(miPanel);
            
            do{

                tipoPanel = miPanel.getTipoPanel();
                System.out.println("Tipo de panel: " + tipoPanel);
                
                switch(tipoPanel){
                    
                    case 1: //Panel de inicio de sesión
                        
                        System.out.println("Lanzando el panel de inicio de sesión...\n");
                        miPanel.setTitle("Iniciar sesión");
                        miPanel.setSize(500, 310);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        
                        while(miPanel.getTipoPanel() == 1){
                            aceptarInicioSesion = miPanel.isAceptarInicioSesionPulsado();
                            registrateAqui = miPanel.isRegistrateAquiPulsado();
                            
                            if(registrateAqui){
                                miPanel.setTipoPanel(2);
                                break;
                            }
                                                        
                            if(aceptarInicioSesion){
                                
                                /*Debido a problemas de memoria, me salta una
                                NullPointerException al hacer la comprobación de
                                Inicio de Sesión. Una forma que he descubierto
                                para que no me salte es usar un System.out.print
                                con un espacio. Para que no se sature la consola,
                                uso el comando \b para limpiar el espacio que se
                                ha escrito*/
                                
                                System.out.print(" ");
                                System.out.print("\b");
                                
                                usuarioInicioSesion = miPanel.getUsuarioInicioSesion();
                                passwordInicioSesion = miPanel.getPasswordInicioSesion();
                                
                                tipoErrorInicioSesion = comprobarInicioSesion(in, out, usuarioInicioSesion, passwordInicioSesion);
                                
                                switch(tipoErrorInicioSesion){
                                    case "ningunError":
                                        ocultarErrores();
                                        setUsuarioConectado(usuarioInicioSesion);
                                        
                                        //Conectar con el tercer panel
                                        miPanel.setTipoPanel(3);
                                        miPanel.cambiaPanel(3);
                                        
                                        FileWriter fw = null;
                                        BufferedWriter bw = null;
                                        
                                        FileReader fr = null;
                                        BufferedReader br = null;
                                        String linea = "";
                                                                                
                                        Cliente.setUsuarioInicioSesion(usuarioInicioSesion);
                                        
                                        try{

                                            fw = new FileWriter(ficheroUsuariosConectados, true);
                                            bw = new BufferedWriter(fw);
                                            
                                            numJugadoresString = comprobarNumeroJugadoresConectados(in, out);
                                            
                                            Servidor.getUsuariosConectados().add(new Connection(Integer.parseInt(numJugadoresString), usuarioInicioSesion));
                                            
                                            bw.write("#" + usuarioInicioSesion + "\n");
                                            bw.flush();
                                            bw.close();
                                            
                                            Cliente.setNumJugadoresString(numJugadoresString);
                                            //Cliente.setNumJugadoresInt(Integer.parseInt(numJugadoresString));
                                            
                                        }catch(IOException ioe){
                                            System.out.println(ioe.toString());
                                        }

                                        break;
                                        
                                    case "datosIncompletos":
                                        ocultarErrores();
                                        errorDatosIncompletos = miPanel.getDatosIncompletos();
                                        errorDatosIncompletos.setVisible(true);
                                        
                                        /*Si un usuario se registra en el panel de registro,
                                        se vuelve automáticamente al panel de inicio de sesión
                                        y aparece un mensaje de usuario registrado con éxito.
                                        Si a continuación de eso el usuario falla al poner sus
                                        credenciales el error correspondiente se solaparía con
                                        el de registro completado. Para evitar esto, pongo en
                                        blanco el mensaje de registro completado*/
                                        
                                        miPanel.getRegistroCompletado().setText("");
                                        break;
                                        
                                    case "datosIncorrectos":
                                        ocultarErrores();
                                        errorDatosIncorrectos = miPanel.getDatosIncorrectos();
                                        errorDatosIncorrectos.setVisible(true);
                                        
                                        //Por el mismo motivo que lo anterior:
                                        miPanel.getRegistroCompletado().setText("");
                                        break;
                                        
                                    case "inicioSesionDoble":
                                        ocultarErrores();
                                        errorInicioSesionDoble = miPanel.getInicioSesionDoble();
                                        errorInicioSesionDoble.setVisible(true);
                                        break;
                                        
                                    default:
                                        System.out.println("Se ha producido un error al iniciar sesión");
                                        break;
                                }
                            }
                        }
                        
                        break;
                        
                    case 2: //Panel de registro de nuevos usuarios
                        
                        ocultarErrores();
                        System.out.println("Lanzando el panel de registro de nuevos usuarios...\n");
                        miPanel.setTitle("Registro de nuevos usuarios");
                        miPanel.setSize(500, 340);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                                                
                        while(miPanel.getTipoPanel() == 2){
      
                            aceptarRegistroNuevoUsuario = miPanel.isAceptarRegistroUsuarioPulsado();
                            volverAtras = miPanel.isVolverAtrasPulsado();

                            if(volverAtras){
                                miPanel.setTipoPanel(1);
                                break;
                            }
                            
                            if(aceptarRegistroNuevoUsuario){
                                
                                /*Uso este System.out.print para solucionar el
                                mismo problema que tenía con el inicio de sesión*/
                                
                                System.out.print(" ");
                                System.out.print("\b");
                                
                                usuarioRegistro = miPanel.getUsuarioRegistro();
                                passRegistro = miPanel.getPassRegistro();
                                passConfirmRegistro = miPanel.getPassConfirmRegistro();

                                tipoErrorRegistro = comprobarRegistro(in, out, usuarioRegistro, passRegistro, passConfirmRegistro);
                                                                
                                switch(tipoErrorRegistro){
                                    case "ningunError":
                                        ocultarErrores();
                                        miPanel.setTipoPanel(1);
                                        miPanel.cambiaPanel(1);
                                        registroCompletado = miPanel.getRegistroCompletado();
                                        registroCompletado.setVisible(true);
                                        
                                        /*Nada más crear la cuenta el sistema se piensa que
                                        se ha intentado crear un usuario que ya existía así
                                        que lo "camuflo" poniendo el aviso vacío si se acaba de
                                        crear la cuenta*/
                                        miPanel.getUsuarioYaExiste().setText("");
                                        
                                        break;
                                        
                                    case "datosIncompletos2":
                                        ocultarErrores();
                                        errorDatosIncompletos2 = miPanel.getDatosIncompletos2();
                                        errorDatosIncompletos2.setVisible(true);
                                        break;
                            
                                    case "usuarioYaExiste":
                                        ocultarErrores();
                                        errorUsuarioYaExiste = miPanel.getUsuarioYaExiste();
                                        errorUsuarioYaExiste.setVisible(true);
                                        break;

                                    case "simboloProhibido":
                                        ocultarErrores();
                                        errorSimbolosProhibidos = miPanel.getSimbolosProhibidos();
                                        errorSimbolosProhibidos.setVisible(true);
                                        break;

                                    case "restriccionesPassword":
                                        ocultarErrores();
                                        errorRestriccionesPassword = miPanel.getRestriccionesPassword();
                                        errorRestriccionesPassword.setVisible(true);
                                        break;
                                        
                                    case "passNoCoincide":
                                        ocultarErrores();
                                        errorPassNoCoincidente = miPanel.getPassNoCoincidente();
                                        errorPassNoCoincidente.setVisible(true);
                                        break;

                                    default:
                                        System.out.println("Se ha producido un error al registrarse");
                                        break;
                                }
                            }
                        }
                        
                        break;
                        
                    case 3: //Panel de configuración de la partida
                        
                        ocultarErrores();
                        System.out.println("Lanzando el panel de configuración de la partida...\n");
                        miPanel.setTitle("Buscando oponentes (" + usuarioInicioSesion + ")");
                        miPanel.setSize(900, 520);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        setRetoLanzadoConExito(false);
                        
                        while(miPanel.getTipoPanel() == 3 || !retoRecibido){
                            
                            setIrAlPanelCuatro(false);
                            setIrAlPanelSeis(false);
                            
                            lanzarUnReto = miPanel.isLanzarRetoPulsado();
                            actualizarJugadoresConectados = miPanel.isActualizarListaPulsado();
                            miPanel.getAvisoErrorPosicion().setVisible(true);
                            
                            miPanel.getBienvenido().setText("<html> <body>"
                            + "Bienvenido <font color=blue>" + "#" + Cliente.getUsuarioInicioSesion()
                            + "</font> </body> </html>");
                            
                            if(lanzarUnReto){
                                
                                System.out.print(" ");
                                System.out.print("\b");
                                
                                modalidad = miPanel.getModalidad();
                                numTandas = miPanel.getNumTandas();
                                tiempo = miPanel.getTiempo();
                                oponenteReto = miPanel.getOponenteReto();
                                nombreDelPanel = miPanel.getTitle();
                                
                                tipoErrorLanzarReto = comprobarLanzarReto(in, out, modalidad, numTandas, tiempo, oponenteReto, nombreDelPanel);
                                
                                switch(tipoErrorLanzarReto){
                                    case "ningunError":
                                        ocultarErrores();
                                        setRetoLanzadoConExito(true);
                                        lanzarRetoCompletado = miPanel.getLanzarRetoCompletado();
                                        lanzarRetoCompletado.setVisible(true);
                                        numMisRetos++;
                                        
                                        setRetador(usuarioInicioSesion);
                                        
                                        int tmp = numMisRetos-1;
                                        String tituloInstancia = "Instancia " + tmp;
                                        
                                        if(numMisRetos!=1 && dialogMisRetos.getTitle().equals(tituloInstancia)){
                                            dialogMisRetos.dispose();
                                        }
                                        
                                        dialogMisRetos = new TablaPersonalizada(1);
                                        dialogMisRetos.setSize(380, 160);
                                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                               
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex.toString());
                                        }
                                
                                        dialogMisRetos.setLocation((dim.width/2 - dialogMisRetos.getSize().width/2) + 229, (dim.height/2 - dialogMisRetos.getSize().height/2) - 45);
                                        dialogMisRetos.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                                        dialogMisRetos.setTitle("Instancia " + numMisRetos);
                                        dialogMisRetos.setUndecorated(true);
                                        dialogMisRetos.setType(JDialog.Type.UTILITY);
                                        dialogMisRetos.setVisible(true);
                                        miPanel.resetearCampos();

                                        System.out.println("Hola #" + usuarioInicioSesion + " Te han retado");
                                        retoRecibido = true;
                                        miPanel.setTipoPanel(4);
                                        miPanel.cambiaPanel(4);
                                            
                                        break;
                                        
                                    case "ajustesIncompletos":
                                        ocultarErrores();
                                        errorAjustesIncompletos = miPanel.getAjustesIncompletos();
                                        errorAjustesIncompletos.setVisible(true);
                                        break;
                                        
                                    case "usuarioNoDisponible":
                                        ocultarErrores();
                                        errorUsuarioNoDisponible = miPanel.getUsuarioNoDisponible();
                                        errorUsuarioNoDisponible.setVisible(true);
                                        break;
                                        
                                    case "retoYaEnviado":
                                        ocultarErrores();
                                        errorRetoYaEnviado = miPanel.getRetoYaEnviado();
                                        errorRetoYaEnviado.setVisible(true);
                                        break;
                                        
                                    case "autoReto":
                                        ocultarErrores();
                                        errorAutoReto = miPanel.getAutoReto();
                                        errorAutoReto.setVisible(true);
                                        break;
                                        
                                    default:
                                        System.out.println("Se ha producido un error al lanzar el reto");
                                        break;
                                        
                                }
                            }
                            
                            if(actualizarJugadoresConectados){
                                
                                System.out.print(" ");
                                System.out.print("\b");
                                
                                numJugadoresString = comprobarNumeroJugadoresConectados(in, out);
                                setNumJugadoresInt(Integer.parseInt(numJugadoresString));
                                numConexiones++;
                                
                                int tmp = numConexiones-1;
                                String tituloInstancia = "Instancia " + tmp;

                                if(numConexiones!=1 && dialogUsuariosConectados.getTitle().equals(tituloInstancia)){
                                    dialogUsuariosConectados.dispose();
                                }
                                
                                dialogUsuariosConectados = new TablaPersonalizada(3);
                                dialogUsuariosConectados.setSize(403, 100);
                                miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                                
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.toString());
                                }
                                
                                dialogUsuariosConectados.setLocation((dim.width/2 - dialogUsuariosConectados.getSize().width/2) - 220,
                                                                (dim.height/2 - dialogUsuariosConectados.getSize().height/2) + 190);
                                dialogUsuariosConectados.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                                dialogUsuariosConectados.setTitle("Instancia " + numConexiones);
                                dialogUsuariosConectados.setUndecorated(true);
                                dialogUsuariosConectados.setType(JDialog.Type.UTILITY);
                                dialogUsuariosConectados.setVisible(true);

                            }
                        }
                        
                        break;
                        
                    case 4: //Panel previo al juego
                        
                        ocultarErrores();
                        /*
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                            System.out.println(ie.toString());
                        }*/
                
                        System.out.println("Lanzando el panel previo al juego...\n");
                        miPanel.setTitle("Preparando la partida (" + usuarioInicioSesion + ")");
                        miPanel.setSize(600, 570);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        
                        int i;
                        while(miPanel.getTipoPanel() == 4){
                            miPanel.getCondiciones().setText("<html> <body>"
                            + "Éstas son las condiciones del reto. ¿Aceptas?"
                            + "<br>"
                            + "<br>"
                            + "<font color=blue> Modalidad: </font>" + modalidad + "<br>"
                            + "<font color=blue> Número de tandas: </font>" + numTandas + "<br>"
                            + "<font color=blue> Tiempo por tanda: </font>" + tiempo + "<font color=blue> segundos </font>"
                            + "</body> </html>");

                            if(retoRecibido){
                                System.out.print(" ");
                                System.out.print("\b");

                                miPanel.getRetoRecibido().setText("<html> <body> Has recibido un reto del usuario <font color=blue> #" + getRetador() + "</font> </body> </html>");
                                miPanel.getRetoRecibido().setVisible(true);
                                miPanel.getCondiciones().setVisible(true);
                                miPanel.getAceptarReto().setVisible(true);
                                miPanel.getRechazarReto().setVisible(true);

                                if(miPanel.isAceptarRetoPulsado()){

                                    miPanel.getRetoAceptado().setVisible(true);

                                    for(i=5; i>=0; i--){
                                        miPanel.getRetoAceptado().setText("<html> <body>"
                                        + "El juego comenzará<br>"
                                        + "automáticamente en <font color=blue> <br>"
                                        + "&nbsp &nbsp &nbsp " + i + " </font> segundo(s)"
                                        + "</body> </html>");

                                        try{
                                            Thread.sleep(1000);
                                        }catch(InterruptedException ie){
                                            System.out.println(ie.toString());
                                        }
                                    }

                                    miPanel.getRetoAceptado().setVisible(false);
                                    miPanel.setTipoPanel(5);
                                    miPanel.cambiaPanel(5);
                                }
                            }
                        }

                        break;
                        
                    case 5: //Panel del juego
                        
                        ocultarErrores();
                        System.out.println("Lanzando el panel del juego...\n");
                        miPanel.setTitle("Piedra/Papel/Tijera/Covid-19");
                        miPanel.setSize(600, 550);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                        tiempo = transformarTiempoANumero(tiempo);
                        int tiempoInt = Integer.parseInt(tiempo);
                        
                        numTandas = transformarTandasANumero(numTandas);
                        int tandasInt = Integer.parseInt(numTandas);
                        miPanel.getNumTandasRestantes().setText(numTandas);
                        
                        dialogTiempoRestante.setSize(130, 30);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        try {Thread.sleep(300);}catch (InterruptedException ex){System.out.println(ex.toString());}
                        dialogTiempoRestante.setLocation((dim.width/2 - dialogTiempoRestante.getSize().width/2) + 95,(dim.height/2 - dialogTiempoRestante.getSize().height/2) + 227);
                        dialogTiempoRestante.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        
                        try{
                            dialogTiempoRestante.setUndecorated(true);
                            dialogTiempoRestante.setType(JDialog.Type.UTILITY);
                        }catch(Exception e){}
                        
                        dialogTiempoRestante.setVisible(true);
                        dialogTiempoRestante.add(miPanel.getBarraTiempoRestante());
                        
                        //Inicializamos 
                        Seleccion yo = Seleccion.NADA;
                        Seleccion rival = Seleccion.NADA;
                        int tandasGanadas = 0;
                                                    
                        if(modalidad.equals("Un total de")){
                            tandasInt++;
                            
                            miPanel.getTipoVictoria().setText("<html> <body>"
                                    + "Se jugarán un total de " + numTandas + " tandas. ¡Gana más<br>"
                                    + "tandas que tu rival para ganar la partida!"
                                    + "</html> <body>");
                            miPanel.getTipoVictoria().setBounds(100, 362, 500, 45);
                            miPanel.getTipoVictoria().setVisible(true);
                            
                            while(tandasInt > 1){
                                if(miPanel.isMiPiedraPulsado()){
                                    yo = Seleccion.PIEDRA;
                                }else if(miPanel.isMiPapelPulsado()){
                                    yo = Seleccion.PAPEL;
                                }else if(miPanel.isMiTijeraPulsado()){
                                    yo = Seleccion.TIJERAS;
                                }
                                
                                if((yo.equals(Seleccion.PIEDRA) && rival.equals(Seleccion.TIJERAS)) ||
                                (yo.equals(Seleccion.PAPEL) && rival.equals(Seleccion.PIEDRA)) ||
                                (yo.equals(Seleccion.TIJERAS) && rival.equals(Seleccion.PAPEL))){
                                    tandasGanadas++;
                                    miPanel.getNumTandasGanadas().setText("" + tandasGanadas);
                                }
                                
                                miPanel.getNumTandasRestantes().setText("" + (tandasInt-1));
                                int j=-1;
                                miPanel.getBarraTiempoRestante().setMinimum(0);
                                miPanel.getBarraTiempoRestante().setMaximum(tiempoInt);
                                miPanel.getBarraTiempoRestante().setVisible(true);
                                
                                for(i=tiempoInt; i>=0; i--){
                                        j++;
                                        miPanel.getBarraTiempoRestante().setValue(j);
                                        try{Thread.sleep(1000);}catch(InterruptedException ie){System.out.println(ie.toString());}
                                }
                                tandasInt--;
                            }
                            
                            
                        } else if(modalidad.equals("Al mejor de")){
                            tandasInt++;
                            miPanel.getTipoVictoria().setText("<html> <body>"
                                    + "Se jugará al mejor de " + numTandas + " tandas ganadas. ¡Ga-<br>"
                                    + "na " + ((tandasInt+1)/2) + " de las " + numTandas + " tandas para ganar la partida!"
                                    + "</html> <body>");
                            miPanel.getTipoVictoria().setBounds(90, 362, 500, 45);
                            miPanel.getTipoVictoria().setVisible(true);
                            
                            while(tandasInt > 1){
                                
                                if(miPanel.isMiPiedraPulsado()){
                                    yo = Seleccion.PIEDRA;
                                }else if(miPanel.isMiPapelPulsado()){
                                    yo = Seleccion.PAPEL;
                                }else if(miPanel.isMiTijeraPulsado()){
                                    yo = Seleccion.TIJERAS;
                                }
                                
                                if((yo.equals(Seleccion.PIEDRA) && rival.equals(Seleccion.TIJERAS)) ||
                                (yo.equals(Seleccion.PAPEL) && rival.equals(Seleccion.PIEDRA)) ||
                                (yo.equals(Seleccion.TIJERAS) && rival.equals(Seleccion.PAPEL))){
                                    tandasGanadas++;
                                    miPanel.getNumTandasGanadas().setText("" + tandasGanadas);
                                }
                                
                                miPanel.getNumTandasRestantes().setText("" + (tandasInt-1) + " (como máximo)");
                                
                                int j=-1;
                                miPanel.getBarraTiempoRestante().setMinimum(0);
                                miPanel.getBarraTiempoRestante().setMaximum(tiempoInt);
                                miPanel.getBarraTiempoRestante().setVisible(true);
                                
                                for(i=tiempoInt; i>=0; i--){
                                        j++;
                                        miPanel.getBarraTiempoRestante().setValue(j);
                                        try{Thread.sleep(1000);}catch(InterruptedException ie){System.out.println(ie.toString());}
                                }
                                tandasInt--;
                            }
                            
                        }else if(modalidad.equals("El que llegue antes a")){
                            
                            miPanel.getTipoVictoria().setText("<html> <body>"
                                    + "¡Consigue ganar " + numTandas + " tandas antes que tu rival<br>"
                                    + "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
                                    + "para ganar la partida! </html> </body>");
                            miPanel.getTipoVictoria().setBounds(95, 362, 500, 45);
                            miPanel.getTipoVictoria().setVisible(true);
                            
                            int tmp = (tandasInt*2)-1;
                            miPanel.getNumTandasRestantes().setText("" + tmp + " (como máximo)");
                            tandasInt++;
                            tmp++;
                            
                            while(tmp > 1){
                                
                                if(miPanel.isMiPiedraPulsado()){
                                    yo = Seleccion.PIEDRA;
                                }else if(miPanel.isMiPapelPulsado()){
                                    yo = Seleccion.PAPEL;
                                }else if(miPanel.isMiTijeraPulsado()){
                                    yo = Seleccion.TIJERAS;
                                }
                                
                                if((yo.equals(Seleccion.PIEDRA) && rival.equals(Seleccion.TIJERAS)) ||
                                (yo.equals(Seleccion.PAPEL) && rival.equals(Seleccion.PIEDRA)) ||
                                (yo.equals(Seleccion.TIJERAS) && rival.equals(Seleccion.PAPEL))){
                                    tandasGanadas++;
                                    miPanel.getNumTandasGanadas().setText("" + tandasGanadas);
                                }
                                
                                miPanel.getNumTandasRestantes().setText("" + (tmp-1) + " (como máximo)");
                                
                                int j=-1;
                                miPanel.getBarraTiempoRestante().setMinimum(0);
                                miPanel.getBarraTiempoRestante().setMaximum(tiempoInt);
                                miPanel.getBarraTiempoRestante().setVisible(true);
                                
                                for(i=tiempoInt; i>=0; i--){
                                        j++;
                                        miPanel.getBarraTiempoRestante().setValue(j);
                                        try{Thread.sleep(1000);}catch(InterruptedException ie){System.out.println(ie.toString());}
                                }
                                tmp--;
                            }
                            
                        }
                        
                        dialogTiempoRestante.setVisible(false);
                        miPanel.getBarraTiempoRestante().setVisible(false);
                        miPanel.setTipoPanel(6);
                        miPanel.cambiaPanel(6);
                        break;

                        
                    case 6: //Panel del resultado de la partida
                        
                        ocultarErrores();
                        System.out.println("Lanzando el panel del resultado de la partida...\n");
                        miPanel.setTitle("Resultado de la partida");
                        miPanel.setSize(580, 370);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        
                        while(miPanel.getTipoPanel() == 6){
                            
                            setVerLosRankings(false);
                            setVolverALosRetos(false);
                            setCerrarSesionJugador(false);
                            
                            verLosRankings = miPanel.isVerRankingPulsado();
                            volverALosRetos = miPanel.isOtroRetoPulsado();
                            cerrarSesionJugador = miPanel.isCerrarSesionPulsado();
                            
                            if(volverALosRetos){
                                miPanel.setTipoPanel(3);
                                break;
                            }
                            
                            if(cerrarSesionJugador){
                                miPanel.setTipoPanel(1);
                                break;
                            }

                            if(verLosRankings){
                                miPanel.setTipoPanel(7);
                                break;
                            }
                        }
                        
                        break;
                    
                    case 7: //Panel de los rankings
                        
                        ocultarErrores();
                        System.out.println("Lanzando el panel de los rankings...\n");
                        miPanel.setTitle("Rankings de los mejores jugadores");
                        miPanel.setSize(400, 440);
                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                        miPanel.setVisible(true);
                        miPanel.setResizable(false);
                        miPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        
                        while(miPanel.getTipoPanel() == 7){
                            setAtrasRankings(false);
                            setRankingActualizadoConExito(false);
                            
                            atrasRankings = miPanel.isVolverDelRankingPulsado();
                            actualizarRanking = miPanel.isActualizarRankingsPulsado();
                                                        
                            if(atrasRankings){
                                miPanel.setTipoPanel(6);
                                break;
                            }
                            
                            if(actualizarRanking){
                                
                                System.out.print(" ");
                                System.out.print("\b");
                                
                                tipoRankingElegido = miPanel.getFiltroRanking();
                                
                                tipoErrorTipoRanking = comprobarTipoRanking(in, out, tipoRankingElegido);
                                
                                switch(tipoErrorTipoRanking){
                                    case "ningunError":
                                        ocultarErrores();
                                        elegirTipoRankingCompletado = miPanel.getActualizarTablaCompletado();
                                        elegirTipoRankingCompletado.setVisible(true);
                                        numRankings++;
                                        
                                        int tmp = numRankings-1;
                                        String tituloInstancia = "Instancia " + tmp;
                                                                                
                                        if(numRankings!=1 && dialogVerRankings.getTitle().equals(tituloInstancia)){
                                            dialogVerRankings.dispose();
                                        }
                                                                                
                                        dialogVerRankings = new TablaPersonalizada(4);
                                        dialogVerRankings.setSize(321, 180);
                                        miPanel.setLocation(dim.width/2 - miPanel.getSize().width/2, dim.height/2 - miPanel.getSize().height/2);
                                        
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex.toString());
                                        }
                                        
                                        dialogVerRankings.setLocation((dim.width/2 - dialogVerRankings.getSize().width/2) - 2,(dim.height/2 - dialogVerRankings.getSize().height/2) + 70);
                                        dialogVerRankings.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                                        dialogVerRankings.setTitle("Instancia " + numRankings);
                                        dialogVerRankings.setUndecorated(true);
                                        dialogVerRankings.setType(JDialog.Type.UTILITY);
                                        dialogVerRankings.setVisible(true);
                                        
                                        setRankingActualizadoConExito(true);
                                        break;
                                        
                                    case "tipoRankingNoElegido":
                                        ocultarErrores();
                                        errorTipoRankingNoElegido = miPanel.getTipoRankingNoElegido();
                                        errorTipoRankingNoElegido.setVisible(true);
                                        break;
                                        
                                    default:
                                        System.out.println("Se ha producido un error al elegir el tipo de ranking");
                                        break;
                                }
                            }
                            
                        }
                        
                        break;
                        
                    case 8: //Salir
                        System.out.println("Saliendo del juego...");
                        salirDelJuego(in, out);
                        break;
                        
                    default: //Error
                        System.out.println("Error al recibir el tipo de panel");
                        System.out.println("Abortando el programa...");
                        salirDelJuego(in, out);
                        break;
                }
            }while(tipoPanel != 8);
            clientSocket.close();
            
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
    }
    
    
    public static String transformarTiempoANumero(String tiempo){
        switch(tiempo){
            case "cinco": tiempo = "5"; break;
            case "seis": tiempo = "6"; break;
            case "siete": tiempo = "7"; break;
            case "ocho": tiempo = "8"; break;
            case "nueve": tiempo = "9"; break;
            case "diez": tiempo = "10"; break;
            case "once": tiempo = "11"; break;
            case "doce": tiempo = "12"; break;   
            case "trece": tiempo = "13"; break;
            case "catorce": tiempo = "14"; break;
            case "quince": tiempo = "15"; break;
            case "dieciséis": tiempo = "16"; break;  
            case "diecisiete": tiempo = "17"; break;
            case "dieciocho": tiempo = "18"; break;
            case "diecinueve": tiempo = "19"; break;
            case "veinte": tiempo = "20"; break;
        }
        return tiempo;
    }
    
    public static String transformarTandasANumero(String numTandas){
        switch (numTandas) {
            case "tres": numTandas = "3"; break; 
            case "cinco": numTandas = "5"; break;
            case "siete": numTandas = "7"; break;
            case "nueve": numTandas = "9"; break; 
            case "once": numTandas = "11"; break;
        }
        return numTandas;
    }
    
    /**
     * Método para salir del juego y cerrar la conexión con el servidor
     * @param in El DataInputStream que conecta con la clase Servidor
     * @param out El DataOutputStream que conecta con la clase Servidor
     * @throws IOException Cuando se produzca un error en la llamada a los InputStream
     */
    public static void salirDelJuego(DataInputStream in, DataOutputStream out) throws IOException{
        
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("salirDelJuego");
        
        boolean cerrarConexion = in2.readBoolean();
    }
    
    /**
     * Método para ocultar todos los JLabel de los errores. Cada vez que se vaya
     * a cambiar de panel o se vaya a mostrar un error hay que llamar a este método
     * para evitar que se "amontonen" los errores o que se muestren en un panel
     * incorrecto
     */
    public static void ocultarErrores(){
         
        if(miPanel.getDatosIncorrectos().isVisible()){
            miPanel.getDatosIncorrectos().setVisible(false);
            
        }else if(miPanel.getDatosIncompletos().isVisible()){
            miPanel.getDatosIncompletos().setVisible(false);
            
        }else if(miPanel.getDatosIncompletos2().isVisible()){
            miPanel.getDatosIncompletos2().setVisible(false);
            
        }else if(miPanel.getSimbolosProhibidos().isVisible()){
            miPanel.getSimbolosProhibidos().setVisible(false);
            
        }else if(miPanel.getRestriccionesPassword().isVisible()){
            miPanel.getRestriccionesPassword().setVisible(false);
            
        }else if(miPanel.getUsuarioYaExiste().isVisible()){
            miPanel.getUsuarioYaExiste().setVisible(false);
        
        /*El aviso de registro completado se muestra directamente en la pantalla de
        inicio de sesión (tiene que estar oculto en el panel 2, el de registro)*/
        }else if(miPanel.getTipoPanel() == 2){
            if(miPanel.getRegistroCompletado().isVisible()){
                miPanel.getRegistroCompletado().setVisible(false);
            }
            
        }else if(miPanel.getAjustesIncompletos().isVisible()){
            miPanel.getAjustesIncompletos().setVisible(false);
            
        }else if(miPanel.getUsuarioNoDisponible().isVisible()){
            miPanel.getUsuarioNoDisponible().setVisible(false);
            
        }else if(miPanel.getAutoReto().isVisible()){
            miPanel.getAutoReto().setVisible(false);
            
        }else if(miPanel.getLanzarRetoCompletado().isVisible()){
            miPanel.getLanzarRetoCompletado().setVisible(false);
        
        }else if (miPanel.getRetoYaEnviado().isVisible()){
            miPanel.getRetoYaEnviado().setVisible(false);
            
        }else if (miPanel.getTipoRankingNoElegido().isVisible()){
            miPanel.getTipoRankingNoElegido().setVisible(false);
            
        }else if (miPanel.getActualizarTablaCompletado().isVisible()){
            miPanel.getActualizarTablaCompletado().setVisible(false);
        }
    }
    
    /**
     * Método para notificar al servidor que compruebe el inicio de sesión
     * @param in El DataInputStream que conecta con la clase Servidor
     * @param out El DataOutputSream que conecta con la clase Servidor
     * @param usuarioInicioSesion El nombre que el usuario ha introducido para iniciar sesión
     * @param passwordInicioSesion La contraseña que el usuario ha introducido para iniciar sesión
     * @return El tipo de error que se ha producido (si todo ha ido bien este valor
     * será "ningunError")
     * @throws IOException Cuando se produzca un error en la llamada a los InputStream
     */
    public static String comprobarInicioSesion(DataInputStream in, DataOutputStream out, String usuarioInicioSesion, String passwordInicioSesion) throws IOException{
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("comprobarInicioSesion");
        out2.flush();
        
        out2.writeUTF(usuarioInicioSesion);
        out2.flush();
        out2.writeUTF(passwordInicioSesion);
        out2.flush();
        
        String errorStringInicioSesion = in2.readUTF();        
        return errorStringInicioSesion;
    }
    
    /**
     * Método para notificar al servidor que compruebe el registro de un nuevo usuario en el sistema
     * @param in El DataInputStream que conecta con la clase Servidor
     * @param out El DataOutputSream que conecta con la clase Servidor
     * @param usuarioRegistro El nombre que el usuario ha introducido para registrarse
     * @param passRegistro La contraseña que el usuario ha introducido para registrarse
     * @param passConfirmRegistro La confirmación de la contraseña que el usuario ha introducido para registrarse
     * @return El tipo de error que se ha producido (si todo va bien este valor
     * será "ningunError")
     * @throws IOException Cuando se produzca un error en la llamada a los InputStream
     */
    public static String comprobarRegistro(DataInputStream in, DataOutputStream out, String usuarioRegistro, String passRegistro, String passConfirmRegistro) throws IOException{
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("comprobarRegistro");
        out2.flush();
        
        out2.writeUTF(usuarioRegistro);
        out2.flush();
        out2.writeUTF(passRegistro);
        out2.flush();
        out2.writeUTF(passConfirmRegistro);
        out2.flush();
        
        String errorStringRegistro = in2.readUTF();
        return errorStringRegistro;
    }
    
    
    /**
     * Método para notificar al servidor que compruebe las condiciones de un reto cuando es lanzado por un usuario
     * @param in El DataInputStream que conecta con la clase Servidor
     * @param out El DataOutputStream que conecta con la clase Servidor
     * @param modalidad La condición de victoria de la partida
     * @param numTandas El número de tandas de la partida
     * @param tiempo El tiempo en segundos por tanda de la partida
     * @param oponenteReto El nick oponente con el que el usuario quiere enfrentarse
     * @param nombreDelPanel El título del panel con el que se llama a este método (útil para detectar autoRetos)
     * @return El tipo de error que se ha producido (si todo va bien este valor
     * será "ningunError")
     * @throws IOException Cuando se produzca un error en la llamada a los InputStream
     */
    public static String comprobarLanzarReto(DataInputStream in, DataOutputStream out, String modalidad, String numTandas, String tiempo, String oponenteReto, String nombreDelPanel) throws IOException{
        
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("comprobarLanzarReto");
        out2.flush();
        
        out2.writeUTF(modalidad);
        out2.flush();
        out2.writeUTF(numTandas);
        out2.flush();
        out2.writeUTF(tiempo);
        out2.flush();
        out2.writeUTF(oponenteReto);
        out2.flush();
        out2.writeUTF(nombreDelPanel);
        out2.flush();
                
        String errorStringLanzarReto = in2.readUTF();
        return errorStringLanzarReto;
    }
    
    
    /**
     * Método que sirve para comprobar el filtro de ordenación del ranking elegido por el usuario
     * @param in El DataInputStream que conecta con la clase Servidor
     * @param out El DataOutputStream que conecta con la clase Servidor
     * @param tipoRankingElegido El filtro elegido para el ranking
     * @return El tipo de error que se ha producido (si todo va bien este valor
     * será "ningunError")
     * @throws IOException Cuando se produzca un error en la llamada a los InputStream
     */
    public static String comprobarTipoRanking(DataInputStream in, DataOutputStream out, String tipoRankingElegido) throws IOException{
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("comprobarTipoRanking");
        out2.flush();
        
        out2.writeUTF(tipoRankingElegido);
        out2.flush();
        
        String errorStringTipoRanking = in2.readUTF();
        return errorStringTipoRanking;
    }
    
    public static String comprobarNumeroJugadoresConectados(DataInputStream in, DataOutputStream out) throws IOException{
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("comprobarNumeroJugadoresConectados");
        out2.flush();
                
        String numJugadores = in2.readUTF();
        return numJugadores;
    }
    
    public static String comprobarNickJugadoresConectados(DataInputStream in, DataOutputStream out) throws IOException{
        DataInputStream in2 = in;
        DataOutputStream out2 = out;
        
        out2.writeUTF("comprobarNickJugadoresConectados");
        out2.flush();
        
        String nickJugadores = in2.readUTF();
        return nickJugadores;
    }
    
    //Todos los métodos que hay a partir de aquí son getters y setters
    
    public static void setNumJugadoresString(String aNumJugadoresString) {
        numJugadoresString = aNumJugadoresString;
    }
    
    public static String getNumJugadoresString(){
        return numJugadoresString;
    }
    
    public static String getUsuarioInicioSesion() {
        return usuarioInicioSesion;
    }

    public static void setUsuarioInicioSesion(String aUsuarioInicioSesion) {
        usuarioInicioSesion = aUsuarioInicioSesion;
    }

    public static boolean isRetoLanzadoConExito() {
        return retoLanzadoConExito;
    }

    public static void setRetoLanzadoConExito(boolean aRetoLanzadoConExito) {
        retoLanzadoConExito = aRetoLanzadoConExito;
    }

    public static String getModalidad() {
        return modalidad;
    }

    public static void setModalidad(String aModalidad) {
        modalidad = aModalidad;
    }

    public static String getNumTandas() {
        return numTandas;
    }

    public static void setNumTandas(String aNumTandas) {
        numTandas = aNumTandas;
    }

    public static String getTiempo() {
        return tiempo;
    }

    public static void setTiempo(String aTiempo) {
        tiempo = aTiempo;
    }

    public static String getOponenteReto() {
        return oponenteReto;
    }

    public static void setOponenteReto(String aOponenteReto) {
        oponenteReto = aOponenteReto;
    }
    
    public static boolean isIrAlPanelCuatro() {
        return irAlPanelCuatro;
    }
    
    public static void setIrAlPanelCuatro(boolean aIrAlPanelCuatro) {
        irAlPanelCuatro = aIrAlPanelCuatro;
    }
    
    public static boolean isIrAlPanelSeis() {
        return irAlPanelSeis;
    }

    public static void setIrAlPanelSeis(boolean aIrAlPanelSeis) {
        irAlPanelSeis = aIrAlPanelSeis;
    }

    public static boolean isVolverALosRetos() {
        return volverALosRetos;
    }

    public static void setVolverALosRetos(boolean aVolverALosRetos) {
        volverALosRetos = aVolverALosRetos;
    }

    public static boolean isCerrarSesionJugador() {
        return cerrarSesionJugador;
    }

    public static void setCerrarSesionJugador(boolean aCerrarSesionJugador) {
        cerrarSesionJugador = aCerrarSesionJugador;
    }

    public static boolean isVerLosRankings() {
        return verLosRankings;
    }

    public static void setVerLosRankings(boolean aVerLosRankings) {
        verLosRankings = aVerLosRankings;
    }

    public static boolean isAtrasRankings() {
        return atrasRankings;
    }

    public static void setAtrasRankings(boolean aAtrasRankings) {
        atrasRankings = aAtrasRankings;
    }

    public static boolean isRankingActualizadoConExito() {
        return rankingActualizadoConExito;
    }

    public static void setRankingActualizadoConExito(boolean aRankingActualizadoConExito) {
        rankingActualizadoConExito = aRankingActualizadoConExito;
    }
    
    public static JDialog getDialogMisRetos() {
        return dialogMisRetos;
    }

    public static void setDialogMisRetos(JDialog aDialogMisRetos) {
        dialogMisRetos = aDialogMisRetos;
    }
    
    public static JDialog getDialogVerRankings() {
        return dialogVerRankings;
    }

    public static void setDialogVerRankings(JDialog aDialogVerRankings) {
        dialogVerRankings = aDialogVerRankings;
    }

    public static String getTipoRankingElegido() {
        return tipoRankingElegido;
    }

    public static void setTipoRankingElegido(String aTipoRankingElegido) {
        tipoRankingElegido = aTipoRankingElegido;
    }

    public static JDialog getDialogUsuariosConectados() {
        return dialogUsuariosConectados;
    }

    public static void setDialogUsuariosConectados(JDialog aDialogUsuariosConectados) {
        dialogUsuariosConectados = aDialogUsuariosConectados;
    }

    public static int getNumJugadoresInt() {
        return numJugadoresInt;
    }

    public static void setNumJugadoresInt(int aNumJugadoresInt) {
        numJugadoresInt = aNumJugadoresInt;
    }

    public static String getUsuarioConectado() {
        return usuarioConectado;
    }

    public static void setUsuarioConectado(String aUsuarioConectado) {
        usuarioConectado = aUsuarioConectado;
    }

    public static int getNumMisRetos() {
        return numMisRetos;
    }

    public static void setNumMisRetos(int aNumMisRetos) {
        numMisRetos = aNumMisRetos;
    }

    public static String getRetador() {
        return retador;
    }

    public static void setRetador(String aRetador) {
        retador = aRetador;
    }
    
}