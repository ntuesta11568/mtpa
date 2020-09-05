//Programa compilado con NetBeans IDE 8.2 en Windows 10 versión 1903 (compilación de SO 18362.1016)
//Versión de Java:
//java version "11.0.7" 2020-04-14 LTS
//Java(TM) SE Runtime Environment 18.9 (build 11.0.7+8-LTS)
//Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.7+8-LTS, mixed mode)

//Para compilar este programa:
//1. Hacer clic derecho en Servidor.java y hacer clic en Run File
//2. Hacer clic derecho en Cliente.java y hacer clic en Run File
//El programa empezará con un JFrame con el panel de inicio de sesión


package practica.pkgfinal.mtpa;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Por esta clase pasan todas las operaciones que se envían desde la clase cliente
 * y el servidor se encarga de validarlas y reenviar el resultado a la Clase Cliente
 * mediante Sockets TCP
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 */
public class Servidor {
    /**
     * Objeto para guardar el nick del usuario que ha iniciado sesión
     */
    private static String usuarioInicioSesion;
    
    /**
     * Método main. Se llama a la conexión por Sockets apoyándose en la clase
     * Connection.
     * @param args Argumentos del main
     */
    public static void main(String[] args) {
        
        try{
            int serverPort = 9999;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("¡Servidor iniciado!");
            
            while(true){
                Socket clientSocket = listenSocket.accept();
        	Connection c = new Connection(clientSocket);
                System.out.println("¡Cliente conectado!");
            }
        }catch(IOException ioe){
            System.out.println("No se ha podido iniciar la conexion");
            System.out.println("Listen socket: " + ioe.getMessage());
        }
    }
    
    /**
     * Constructor de la clase Servidor
     * @param aUsuarioInicioSesion El nombre del usuario que ha iniciado sesión
     */
    public Servidor(String aUsuarioInicioSesion){
        usuarioInicioSesion = aUsuarioInicioSesion;
    }
    
    /**
     * Sobrecarga del constructor
     */
    public Servidor(){
        
    }
    
}

/**
 * Clase que lanza el hilo con el que empieza el socket del Servidor y que tiene
 * el método run
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 */
class Connection extends Thread{
   
    /**
     * Se encarga de almacenar el nombre del fichero que contiene los datos del
     * registro de los usuarios
     */
    public static String ficheroRegistros = "Registros de usuarios.txt";
    
    /**
     * Se encarga de almacenar el nombre del fichero que contiene los datos de las
     * clasificaciones (palmarés) de los usuarios
     */
    public static String ficheroRankings = "Rankings de usuarios.txt";
    
    /**
     * Estos cuatro objetos nos sirven para controlar los datos que se reciben y
     * se envían a la Clase Cliente, así como controlar la conexión con el mismo
     * y el cierre de dicha conexión
    */
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    boolean cerrarConexion = false;
    
    /**
     * Este entero almacena el valor del tipo de panel, el cual se usará para indicar
     * qué tipo de panel se debe mostrar en cada momento del programa en función
     * de lo que haga el usuario
     */
    int tipoPanel = 1;
    
    /**
     * Estos objetos de tipo String nos servirán para recuperar los valores del
     * inicio de sesión y del registro introducidos por el usuario
     */
    static String usuario = "";
    static String password = "";
    static String crearNick = "";
    static String crearPass = "";
    static String confirmarPass = "";
    
    /**
     * Estos objetos de tipo String nos servirán para recuperar los valores del
     * reto que el usuario ha enviado
     */
    static String indicarModalidad = "";
    static String indicarNumTandas = "";
    static String indicarTiempo = "";
    static String indicarOponenteReto = "";
    
    /**
     * Estos objetos de tipo String nos servirán para recuperar los valores del
     * tipo y la consulta al fichero de rankings
     */
    static String indicarTipoRanking = "";
    static String consultarficheroRankings = "";
    
    /**
     * Constructor de la clase Connection
     * @param aClientSocket Socket por parte del cliente
     */
    public Connection(Socket aClientSocket){
		
        try{

            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();

        }catch(IOException e){
            System.out.println("Connection: " + e.getMessage());
        }
    }
    
    /**
     * Este método actuará como "el main" del Servidor en cuanto a la parte de
     * iniciar la conexión por Sockets
     */
    @Override
    public void run(){
        
        while(!cerrarConexion){
            try{
                /**
                 * Estos objetos de tipo String sirven para detectar los posibles
                 * errores que puedan surgir cuando el usuario realize alguna acción
                 * en los diferentes paneles que se le muestran en la interfaz de
                 * javax swing
                 */
                String buscarErroresInicioSesion = "";
                String buscarErroresRegistro = "";
                String buscarErroresLanzarReto = "";
                String buscarErroresTipoRanking = "";
                
                /**
                 * Este objeto de tipo String sirve para almacenar el método, en
                 * función del cual se realizarán las operaciones de validación
                 * de datos
                 */
                String metodo = "";
                metodo = in.readUTF();
                
                switch(metodo){
                    case "comprobarInicioSesion":
                        usuario = in.readUTF();
                        password = in.readUTF();      
                        
                        buscarErroresInicioSesion = comprobarInicioSesion(usuario, password);
                        out.writeUTF(buscarErroresInicioSesion);
                        out.flush();
                        break;
                        
                    case "comprobarRegistro":
                        crearNick = in.readUTF();
                        crearPass = in.readUTF();
                        confirmarPass = in.readUTF();
                                        
                        buscarErroresRegistro = comprobarRegistro(crearNick, crearPass, confirmarPass);
                        out.writeUTF(buscarErroresRegistro);
                        out.flush();
                        break;
                        
                    case "comprobarLanzarReto":
                        indicarModalidad = in.readUTF();
                        indicarNumTandas = in.readUTF();
                        indicarTiempo = in.readUTF();
                        indicarOponenteReto = in.readUTF();
                        
                        buscarErroresLanzarReto = comprobarLanzarReto(indicarModalidad, indicarNumTandas, indicarTiempo, indicarOponenteReto);
                        out.writeUTF(buscarErroresLanzarReto);
                        out.flush();
                        break;
                        
                    case "comprobarTipoRanking":
                        indicarTipoRanking = in.readUTF();
                        
                        buscarErroresTipoRanking = comprobarTipoRanking(indicarTipoRanking);
                        out.writeUTF(buscarErroresTipoRanking);
                        out.flush();
                        break;

                    case "salirDelJuego":
                        cerrarConexion = true;
                        
                        if(cerrarConexion){
            
                            try{
                                out.writeBoolean(cerrarConexion);
                                clientSocket.close();
                
                            }catch(IOException ioe){
                                System.out.println(ioe.toString());
                            }
                        }
                }
                
            }catch(IOException ioe){
                System.out.println(ioe.toString());
                System.exit(0);
            }
        }
    }
    
    /**
     * Este objeto ArrayList nos servirá para almacenar los jugadores que están
     * conectados en el sistema (se liberará un jugador cuando cierre la sesión
     * o salga del programa)
     */
    private static ArrayList<Servidor> jugadoresConectados = new ArrayList<>();
    
    /**
     * Este método sirve para llamar a la lista de los jugadores conectados
     * @return La lista de los jugadores conectados
     */
    public static ArrayList<Servidor> devuelveJugadores(){
        return jugadoresConectados;
    }
    
    /**
     * Este método sirve para marcar los jugadores como online en el sistema (en
     * realidad lo que hace es añadirlos al ArrayList)
     * @param sv El objeto de tipo servidor a añadir al ArrayList
     * @return El estado de éxito (1) o fracaso (-1) de la operación
     */
    public static boolean marcarJugadoresOnline(Servidor sv){
        boolean estado = false;
        estado = jugadoresConectados.add(sv);
        return estado;
    }
    
    /**
     * Este método sirve para marcar los jugadores como offline en el sistema (en
     * realidad lo que hace es quitarlos del ArrayList)
     * @param sv El objeto de tipo servidor a quitar del ArrayList
     * @return El estado de éxito (1) o fracaso (-1) de la operación
     */
    public static boolean marcarJugadoresOffline(Servidor sv){
        boolean estado = false;
        estado = jugadoresConectados.remove(sv);
        return estado;
    }
    
    /**
     * Este método sirve para buscar a un jugador en el sistema
     * @return El estado de éxito (1) o fracaso (-1) de la operación
     */
    public static Servidor buscaJugador(){
        Servidor sv = null;
        
        for(int i=0; i< jugadoresConectados.size(); i++){
            sv = jugadoresConectados.get(i);
        }
        
        return sv;
    }
    
    /**
     * Este método sirve para comprobar el inicio de sesión de un usuario en el sistema
     * @param usuario El nombre introducido por el usuario
     * @param password La contraseña introducida por el usuario
     * @return El tipo de error que se ha producido (si todo ha ido bien este valor
     * será "ningunError")
     */
    public static String comprobarInicioSesion(String usuario, String password){
        
        String tipoErrorInicioSesion = "Error";
        String linea = "";
        
        FileReader fr = null;
        BufferedReader br = null;

        FileWriter fw = null;
        BufferedWriter bw = null;
        
        try{
            /**Solo crearemos el fichero si no existe, si ya existía partiremos de ese. Con esto podemos
             * hacer que se siga usando un fichero de registros después de cerrar el programa y volverlo
             * a ejecutar (porque no se sobreescriben los datos)*/

            File existeFichero = new File(ficheroRegistros);
            if(existeFichero.exists() == false) {
                fw = new FileWriter(ficheroRegistros);
                bw = new BufferedWriter(fw);
                fw.close();
            }
            
            fr = new FileReader(ficheroRegistros);
            br = new BufferedReader(fr);
            
            String lineaEncontrada = "false";
            int numTokens = 1;
            boolean errorRegistro = false;
            boolean passwordCorrecta = false;
            
            if(usuario.contains("#") || usuario.contains(";") || password.contains("#") || password.contains(";")){
                /**Ningún nombre de usuario o contraseña puede usar los símbolos # ni ;. El símbolo #
                se añade automáticamente al nick nada más crear el usuario y el ; se usa para separar
                usuarios y contraseñas del fichero de registros. Dado que # y ; son caracteres restringidos,
                el programa directamente NO leerá del archivo de registros de usuarios si alguien intenta usar
                estos caracteres al iniciar sesión.*/
                tipoErrorInicioSesion = "datosIncorrectos";
            }else{
                usuario = "#" + usuario;
                while((linea = br.readLine()) != null){ //Mientras siga habiendo líneas en el fichero
                    String[] tokens = linea.split(";"); //Separamos las líneas por ;
                    for (String token : tokens){
                    
                        //Si el número de tokens es impar estamos en la parte izquierda del ; (es decir, la de los nicks)
                        if(numTokens %2 != 0){

                            //Y si coincide el token con algún usuario del fichero significa que el usuario está bien escrito
                            if(usuario.equals(token)){
                                lineaEncontrada = linea;
                            }
                        }
                        numTokens++;
                    }
                }
            
                //Si el usuario existe ahora queda comprobar si la contraseña introducida es la que corresponde a dicho usuario
                numTokens = 1;
                if(!(lineaEncontrada.equals("false"))){
                    String[] tokens2 = lineaEncontrada.split(";");

                    for(String token2 : tokens2){ //Se recorren los tokens partiendo del numero 1
                        /*Como sabemos que la línea del usuario y la contraseña es la misma y hay
                        una distancia de un token entre ellas, 1+1=2. Por tanto, 2 es el valor de
                        numTokens cuando estamos en la parte de la contraseña*/

                        if(numTokens == 2){
                            if(password.equals(token2)){ //Si la contraseña es correcta
                                passwordCorrecta = true;
                            }
                        }
                        numTokens++;
                    }
                }

                br.close();
            }
            
            if(usuario.equals("") || password.equals("")){
                tipoErrorInicioSesion = "datosIncompletos";
            }else if(lineaEncontrada.equals("false") || passwordCorrecta == false){
                tipoErrorInicioSesion = "datosIncorrectos";
            }else{
                tipoErrorInicioSesion = "ningunError";
            }
        
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
        
        return tipoErrorInicioSesion;
    }
    
    
    /**
     * Este método sirve para comprobar el registro de un nuevo usuario en el sistema
     * @param crearNick El nick que ha elegido el usuario
     * @param crearPass La contraseña que ha elegido el usuario
     * @param confirmarPass La confirmación de dicha contraseña
     * @return El tipo de error que se ha producido (si todo ha ido bien este valor
     * será "ningunError")
     */
    public static String comprobarRegistro(String crearNick, String crearPass, String confirmarPass){
        String tipoErrorRegistro = "Error";
        String linea = "";
        String lineaFicheroRegistros = "";
        String lineaFicheroRankings = "";
        
        char ch;
        
        boolean datosIncompletos2 = false;
        boolean usuarioYaExiste = false;
        boolean haySimbolo = false;
        boolean hayNumero = false;
        boolean hayMayuscula = false;
        boolean simboloProhibidoNick = false;
        boolean simboloProhibidoPass = false;
        boolean simboloProhibidoPassConfirm = false;
        boolean haySimboloProhibido = false;
        boolean minimoOchoCaracteres = false;
        boolean requisitosPassword = false;
        boolean passCoincide = false;
        
        FileReader fr = null;
        BufferedReader br = null;

        FileWriter fw = null;
        BufferedWriter bw = null;
        
        if(crearNick.equals("") || crearPass.equals("") || confirmarPass.equals("")){
            datosIncompletos2 = true;
            
        }else{
            try{
                fr = new FileReader(ficheroRegistros);
                br = new BufferedReader(fr);
                int numTokens = 1;
                int i;
            
                while((linea = br.readLine()) != null){ //Mientras siga habiendo líneas en el fichero
                    String[] tokens = linea.split(";"); //Separamos las líneas por ;
                    for(String token : tokens){
                        if(numTokens%2 != 0){ //Si el número de tokens es impar estamos en la parte izquierda del ; (es decir, la de los nicks)
                            if(("#" + crearNick).equals(token)){ //Y si coincide el token con algún usuario del fichero significa que alguien con ese nombre de usuario ya está registrado
                                usuarioYaExiste = true;
                            }
                        }
                        numTokens++;
                    }
                }
            
                br.close();
                
                for(i=0; i<crearNick.length(); i++){
                    ch = crearNick.charAt(i);

                    if(ch == '#' || ch == ';'){
                        simboloProhibidoNick = true;
                    }

                }

                /*La contraseña que elija un usuario debe tener al menos 8 caracteres,
                un número, una mayúscula y un símbolo que no sea ni ; ni #*/

                /*El hashtag es el identificador del usuario y el punto
                y coma se usa para separar usuario y contraseña en el fichero
                de registros. Para evitar conflictos prefiero prohibir a
                un usuario usar estos símbolos en su nick o en su contraseña
                (generaré el hashtag automáticamente al crear el usuario)*/

                if(crearPass.length() >= 8){
                    minimoOchoCaracteres = true;
                }

                for(i=0; i<crearPass.length(); i++){
                    ch = crearPass.charAt(i);

                    if(ch == '#' || ch == ';'){
                        simboloProhibidoPass = true;
                    }

                    Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(crearPass);

                    if(m.find() == true){
                        haySimbolo = true;
                    }

                    if(Character.isUpperCase(ch)){
                        hayMayuscula = true;
                    }

                    if(Character.isDigit(ch)){
                        hayNumero = true;
                    }              
                }

                if(simboloProhibidoNick || simboloProhibidoPass || simboloProhibidoPassConfirm){
                    haySimboloProhibido = true;

                }else{
                    if(confirmarPass.equals(crearPass)){
                        passCoincide = true;
                    }
                }

                if(minimoOchoCaracteres  && haySimbolo && hayMayuscula && hayNumero){
                    requisitosPassword = true;
                }

                if(!datosIncompletos2 && !usuarioYaExiste && !haySimboloProhibido && requisitosPassword && passCoincide){
                    /*El true del FileWriter sirve para abrir el fichero
                    en modo append y evitar sobreescrituras donde no se debe*/
                    tipoErrorRegistro = "ningunError";
                    fw = new FileWriter(ficheroRegistros, true);
                    bw = new BufferedWriter(fw);
                                        
                    lineaFicheroRegistros = "#" + crearNick + ";" + crearPass;
                    bw.write(lineaFicheroRegistros + "\n");
                    bw.flush();
                    bw.close();
                    
                    /**
                     * Registramos también al usuario en el fichero de rankings
                     * (nos será útil más adelante en el programa). Explicaré la
                     * estructura del fichero de rankings en esta clase:
                     * {@link practica.final.mtpa.TablaPersonalizada#MTPRankings}
                     */
                    fr = new FileReader(ficheroRankings);
                    br = new BufferedReader(fr);
                    int posiciones = 0;
                    
                    while((linea = br.readLine()) != null){
                        posiciones++;
                    }
                    
                    posiciones++;
                    fw = new FileWriter(ficheroRankings, true);
                    bw = new BufferedWriter(fw);
                    
                    lineaFicheroRankings = posiciones + ";#" + crearNick + ";0;0";
                    bw.write(lineaFicheroRankings + "\n");
                    bw.flush();
                    bw.close();
                                     
                }
            
            }catch(IOException ioe){
                System.out.println(ioe.toString());
            }
        }
        
        if(datosIncompletos2){
            tipoErrorRegistro = "datosIncompletos2";

        }else if(haySimboloProhibido){
            tipoErrorRegistro = "simboloProhibido";

        }else if(usuarioYaExiste){    
            tipoErrorRegistro = "usuarioYaExiste";

        }else if(!requisitosPassword){
            tipoErrorRegistro = "restriccionesPassword";

        }else if(!passCoincide){
            tipoErrorRegistro = "passNoCoincide";
        }

        datosIncompletos2 = false;
        usuarioYaExiste = false;
        haySimbolo = false;
        hayNumero = false;
        hayMayuscula = false;
        simboloProhibidoNick = false;
        simboloProhibidoPass = false;
        simboloProhibidoPassConfirm = false;
        haySimboloProhibido = false;
        minimoOchoCaracteres = false;
        requisitosPassword = false;
        passCoincide = false;

        return tipoErrorRegistro;
    }
    
    /**
     * Este método sirve para comprobar los valores de un reto que se ha enviado
     * a un usuario
     * @param modalidad La condición de victoria de la partida
     * @param numTandas El número de tandas de la partida
     * @param tiempo El tiempo en segundos por tanda de la partida
     * @param oponenteReto El nick del oponente al que se lanza el reto
     * @return El tipo de error que se ha producido (si todo ha ido bien este valor
     * será "ningunError")
     */
    public static String comprobarLanzarReto(String modalidad, String numTandas, String tiempo, String oponenteReto){
        String tipoErrorLanzarReto = "Error";
        
        if(modalidad.equals("--Modalidad--") || numTandas.equals("--Nº tandas--") || tiempo.equals("--Tiempo--") || oponenteReto.equals("")){
            tipoErrorLanzarReto = "ajustesIncompletos";
            
        }else if(oponenteReto.equals("error")){ /** @deprecated*/
            tipoErrorLanzarReto = "usuarioNoDisponible";
            
        }else if(oponenteReto.equals("repetido")){ /** @deprecated*/
            tipoErrorLanzarReto = "retoYaEnviado";
            
        }else if(oponenteReto.equals("nelson")){ /** @deprecated*/
            tipoErrorLanzarReto = "autoReto";
            
        }else{
            tipoErrorLanzarReto = "ningunError";
        }
                
        return tipoErrorLanzarReto;
    }
    
    /**
     * Este método sirve para comprobar el tipo de ranking que ha elegido el usuario
     * para ver
     * @param tipoRankingElegido El tipo de ranking elegido por el usuario
     * @return El tipo de error que se ha producido (si todo ha ido bien este valor
     * será "ningunError")
     */
    public static String comprobarTipoRanking(String tipoRankingElegido){
        String tipoErrorTipoRanking = "Error";
                
        if(tipoRankingElegido.equals("--Elige filtro para el ranking--")){
            tipoErrorTipoRanking = "tipoRankingNoElegido";
        }else{
            tipoErrorTipoRanking = "ningunError";
        }
                
        return tipoErrorTipoRanking;
    }
}