//Programa compilado con Eclipse IDE 2020-03
//Nelson Tuesta Fernández

//Versión 1.3 - 04/06/2020 (20:00)
//Detalles de la versión: Funcionan los registros e inicios de sesión. También con múltiples usuarios. Falta ocultar la contraseña

package mtpa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor{
	
    public static void main(String[] args) {
        
        try {
        		int serverPort = 5000;
                // Se crea el socket del servidor
            	ServerSocket listenSocket = new ServerSocket(serverPort); 
                System.out.println ( "¡Servidor iniciado!" );
                
                // El servidor siempre escucha peticiones
                while ( true ) {
                	Socket clientSocket = listenSocket.accept();
        			Connection c = new Connection(clientSocket);
                    System.out.println( "¡Cliente conectado!" );
                }
                
        }catch (IOException ex) {
        	System.out.println( "Listen socket: " + ex.getMessage());
        }
    }
    
  
}
// Fin de la clase servidor


class Connection extends Thread{

	public static String ficheroRegistros = "Registros de usuarios.txt";
	
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	boolean cerrarConexion = false;
	
	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out = new DataOutputStream( clientSocket.getOutputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
		
	public void run(){
		while(cerrarConexion == false) {
			String username = registro(); //Recuperamos el nombre del usuario que ha iniciado sesión
			System.out.println("\nEntrando al juego...");
			
			/*REVISAR: Implementar la parte de enviar retos, aceptarlos, etc.*/
			
			System.exit(0);
			cerrarConexion = true;
		}
		
		//En esta parte del código cerrarConexion es true
		try {
			clientSocket.close();
		}catch(IOException e) {
			e.toString();
		}
	}

	public String registro() {
		String usuarioRegistrado;
		String nick = "";
		String pass;
		String passConfirm;
		String linea = "";
		String lineaRegistrada;
		
		FileReader fr = null;
		BufferedReader br = null;
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			
			/*Solo crearemos el fichero si no existe, si ya existía partiremos de ese. Con esto podemos
			 * hacer que se siga usando un fichero de registros después de cerrar el programa y volverlo
			 * a ejecutar (porque no se sobreescriben los datos)*/
		
			File existeFichero = new File(ficheroRegistros);
			if(existeFichero.exists() == false) {
				fw = new FileWriter(ficheroRegistros);
				bw = new BufferedWriter(fw);
				fw.close();
			}
			
			boolean inicioCompletado = false;
			
			do {
				System.out.print("\n¡Bienvenido al juego piedra/papel/tijera + Covid-19! ¿Tienes una cuenta en el sistema? (Si/No): ");
				Scanner sc = new Scanner(System.in);
				usuarioRegistrado = sc.nextLine();
				
				//Todas estas variables son para comprobar que el formato de la contraseña es correcto (se explicarán dicho formato y el uso de estas variables más adelante)
				char ch;
				boolean masDeOchoCaracteres = false;
				boolean hayMayuscula = false;
				boolean haySimbolo = false;
				boolean hayNumero = false;
				boolean simboloNoPermitido = false;
				
				if(usuarioRegistrado.equalsIgnoreCase("si") || usuarioRegistrado.equalsIgnoreCase("sí")){
					System.out.println("\n---Iniciar sesión---");
					System.out.print("\nNick: #"); //ERROR: Falla al poner un nick con espacio
					nick = sc.nextLine();
					/* Tengo que especificar esto dado que no pido el hashtag en el menú, sino
					 * que se lo doy automáticamente (tiene que leerse luego en el fichero)*/
					nick = "#" + nick;
					
					System.out.print("Contraseña: ");
					pass = sc.nextLine();
										
					fr = new FileReader(ficheroRegistros);
					br = new BufferedReader(fr);
					
					String lineaEncontrada = "false";
					int numTokens = 1;
					boolean errorRegistro = false;
										
					while((linea = br.readLine()) != null) { //Mientras siga habiendo líneas en el fichero
						String[] tokens = linea.split(";"); //Separamos las líneas por ;
						for(String token : tokens) {
							if(numTokens%2 != 0) { //Si el número de tokens es impar estamos en la parte izquierda del ; (es decir, la de los nicks)
								if(nick.equals(token)) { //Y si coincide el token con algún usuario del fichero significa que el usuario está bien escrito
									lineaEncontrada = linea;
								}
							}
							numTokens++;
						}
					}
						
					//Si el usuario existe ahora queda comprobar si la contraseña introducida es la que corresponde a dicho usuario
					numTokens = 1;
					if(lineaEncontrada != "false") {
						String[] tokens2 = lineaEncontrada.split(";");
						
						for(String token2 : tokens2) { //Se recorren los tokens partiendo del numero 1	
							
							/*Como sabemos que la línea del usuario y la contraseña es la misma y hay una
							 * distancia de un token entre ellas, 1 + 1 = 2. Por tanto, 2 es el valor de
							 * numTokens cuando estamos en la parte de la contraseña
							 */
							if(numTokens==2) {
								if(pass.equals(token2)) { //Si la contraseña es correcta
									inicioCompletado = true;
								}
							}
							numTokens++;
						}
					}
					
					br.close();
					
					if(!inicioCompletado) { //Si la contraña no coincide con la del usuario o si el valor de lineaEncontrada sigue en "false"
						errorRegistro = true;
					}
										
					if(errorRegistro) {
						System.out.println("\nUsuario o contraseña incorrectos. Inténtalo de nuevo");
						System.out.println("-----------------------------");
					}else {
						//En este punto el inicio de sesión es correcto y se cambia el estado del usuario a conectado (usar Enum). Después se saldrá del do-while
						
					}
					
				}else if(usuarioRegistrado.equalsIgnoreCase("no")) {
					System.out.println("\n---Darse de alta---");
					System.out.print("Introduce un nick (nombre de usuario): #");
					nick = sc.nextLine();
					nick = "#" + nick;
					
					/*Tendremos un fichero con los usuarios y contraseñas de los usuarios registrados en el sistema.
					 El fichero tendrá este formato:
					 
					 nick1;pass1;estado1
					 nick2;pass2;estado2
					 nick3;pass3;estado3
					 ...
					 
					 donde el nick es el usuario, pass la contraseña y estado el estado del usuario (conectado o desconectado)
					 */
					
					//Primero comprobamos si el nick existe (de ser así, ya hay un usuario con ese nombre y no se debe permitir continuar)
					
					fr = new FileReader(ficheroRegistros);
					br = new BufferedReader(fr);
					
					boolean usuarioExistente = false;
					int numTokens = 1;
					
					
					while((linea = br.readLine()) != null) { //Mientras siga habiendo líneas en el fichero
						String[] tokens = linea.split(";"); //Separamos las líneas por ;
						for(String token : tokens) {
							if(numTokens%2 != 0) { //Si el número de tokens es impar estamos en la parte izquierda del ; (es decir, la de los nicks)
								if(nick.equals(token)) { //Y si coincide el token con algún usuario del fichero significa que ya está registrado
									usuarioExistente = true;
								}
							}
							numTokens++;
						}
					}
					
					br.close();
					
					if(usuarioExistente == true) {
						System.out.println("Ya existe un usuario con ese nick. Intenta registrarte de nuevo con otro nombre diferente");
						System.out.println("-----------------------------");
					}else {
						/*La contraseña deberá tener un mínimo de 8 caracteres y tener al menos una mayúscula,
						 * un número y un símbolo. Todos los símbolos valen menos el punto y coma (;) y la
						 * almohadilla (#) ya que son caracteres especiales para el fichero de registros*/
						
						System.out.print("Introduce una contraseña: ");
						pass = sc.nextLine();
						
						
						if(pass.length() >= 8) {
							masDeOchoCaracteres = true;
						}
						
						for(int i=0; i<pass.length(); i++) {
							ch = pass.charAt(i);
							
							if(ch == '#' || ch == ';') {
								simboloNoPermitido = true;
							}
							
							Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
							Matcher m = p.matcher(pass);
							haySimbolo = m.find(); //Si es true es que hay simbolo especial
							
							if(Character.isDigit(ch)) {
								hayNumero = true;
							}
							
							if(Character.isUpperCase(ch)) {
								hayMayuscula = true;
							}
						}
						
						if(masDeOchoCaracteres == true && haySimbolo == true && simboloNoPermitido == false && hayNumero == true && hayMayuscula == true) {
							System.out.print("Repite la contraseña: ");
							passConfirm = sc.nextLine();
							
							if(pass.equals(passConfirm)) {
													
								fw = new FileWriter(ficheroRegistros, true); //El "true" sirve para abrir el fichero en modo append (para que no se sobreescriba todo cada vez que añada un nuevo usuario)
								bw = new BufferedWriter(fw);
								
								lineaRegistrada = nick + ";" + pass;
								bw.write(lineaRegistrada + "\n");
								bw.flush();
								bw.close();			
						
								System.out.println("\nUsuario registrado con exito! Prueba a iniciar sesion");
								System.out.println("-----------------------------");

							}else {
								System.out.println("Las contraseñas no coinciden. Intenta registrarte de nuevo");
								System.out.println("-----------------------------");
							}
						}else {
							
							System.out.println("La contraseña debe de tener más de 8 caracteres y al menos un número, una mayúscula y un símbolo (que no sea ni # ni ;). Intenta registrarte de nuevo");
							System.out.println("-----------------------------");
						}
					}
				}else {
					System.out.println("Por favor responde a la pregunta correctamente.");
				}
				
			}while(!inicioCompletado);
			
		}catch(IOException ioe) {
			ioe.toString();
		}
		
		return nick;
		
	}
}

//DUDAS
//1. ¿Hay que especificar algo para la contraseña o para los nick? (Mínimo de caracteres, mayúsculas y minúsculas, símbolos y números, ...) HECHO
//2. ¿La contraseña tiene que estar oculta al escribirla? HACERLO DESPUÉS
//3. ¿Hay que implementar la opción de que un usuario pueda cambiar su contraseña o no es necesario? SE PUEDE (NO OBLIGATORIO)
//4. He hecho los registros e inicios de sesión por consola en el servidor. ¿Así está bien o tiene que ser en el cliente? REVISAR BIEN