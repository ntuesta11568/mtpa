//Programa compilado con Eclipse IDE 2020-03
//Nelson Tuesta Fernández

//Versión 1.3 - 04/06/2020 (20:00)

package mtpa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
*/

public class Cliente {
 
    public static void main(String[] args) {
 
        //Host del servidor
        final String HOST = "127.0.0.1";
        
        //Puerto del servidor
        final int serverPort = 5000;
        
        DataInputStream in;
        DataOutputStream out;
        int i;
        
    	try {
            
            // Se crea el socket de conexion
            // Creo el socket para conectarme con el cliente
            Socket clientSocket = new Socket(HOST, serverPort);
 
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            
            System.out.println("");
            
            clientSocket.close();
 
        } catch (IOException ex) {
            ex.toString();
        }
    }
}
// Fin de la clase cliente