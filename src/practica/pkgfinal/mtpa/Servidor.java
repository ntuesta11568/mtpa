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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor {
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
    
}

class Connection extends Thread{
    
    public static String ficheroRegistros = "Registros de usuarios.txt";
    public static String ficheroRankings = "Rankings de usuarios.txt";
   
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    boolean cerrarConexion = false;
    int tipoPanel = 1;
    
    static String usuario = "";
    static String password = "";
    static String crearNick = "";
    static String crearPass = "";
    static String confirmarPass = "";
    static String indicarModalidad = "";
    static String indicarNumTandas = "";
    static String indicarTiempo = "";
    static String indicarOponenteReto = "";
    static String indicarTipoRanking = "";
    static String consultarficheroRankings = "";
    
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
    
    
    @Override
    public void run(){
        
        while(!cerrarConexion){
            try{
                
                String metodo = "";
                String buscarErroresInicioSesion = "";
                String buscarErroresRegistro = "";
                String buscarErroresLanzarReto = "";
                String buscarErroresTipoRanking = "";
                String buscarErroresConsultarFicheroRankings = "";
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
                    /*
                    case "consultarRanking":
                        consultarficheroRankings = in.readUTF();
                        
                        buscarErroresConsultarFicheroRankings = consultarRanking(consultarficheroRankings);
                        out.writeUTF(buscarErroresConsultarFicheroRankings);
                        out.flush();
                        break;
                    */
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
                //System.out.println(ioe.toString());
                System.exit(0);
            }
        }
    }
    
    public static String comprobarInicioSesion(String usuario, String password){
        
        String tipoErrorInicioSesion = "Error";
        String linea = "";
        
        FileReader fr = null;
        BufferedReader br = null;

        FileWriter fw = null;
        BufferedWriter bw = null;
        
        try{
            /*Solo crearemos el fichero si no existe, si ya existía partiremos de ese. Con esto podemos
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
                /*Ningún nombre de usuario o contraseña puede usar los símbolos # ni ;. El símbolo #
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
                    
                    /*Registramos también al usuario en el fichero de rankings
                    (nos será útil más adelante en el programa). Explicaré la
                    estructura del fichero de rankings más adelante*/
                    
                    fw = new FileWriter(ficheroRankings, true);
                    bw = new BufferedWriter(fw);
                    
                    lineaFicheroRankings = "#" + crearNick + ";0;0";
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
    
    public static String comprobarLanzarReto(String modalidad, String numTandas, String tiempo, String oponenteReto){
        String tipoErrorLanzarReto = "Error";
        
        if(modalidad.equals("--Modalidad--") || numTandas.equals("--Nº tandas--") || tiempo.equals("--Tiempo--") || oponenteReto.equals("")){
            tipoErrorLanzarReto = "ajustesIncompletos";
            
        }else if(oponenteReto.equals("error")){ //REVISAR. Cambiar esto
            tipoErrorLanzarReto = "usuarioNoDisponible";
            
        }else if(oponenteReto.equals("repetido")){ //REVISAR. Cambiar esto
            tipoErrorLanzarReto = "retoYaEnviado";
            
        }else if(oponenteReto.equals("nelson")){ //REVISAR. Cambiar esto
            tipoErrorLanzarReto = "autoReto";
            
        }else{
            tipoErrorLanzarReto = "ningunError";
        }
                
        return tipoErrorLanzarReto;
    }
    
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