package practica.pkgfinal.mtpa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import static practica.pkgfinal.mtpa.Connection.ficheroRankings;
import static practica.pkgfinal.mtpa.Connection.ficheroUsuariosRetados;

/**
 * Clase que permite crear una tabla personalizada mediante AbstractTableModel.
 * Se usa para crear las diferentes tablas de mi programa
 * @author Nelson Tuesta Fernández
 * @version 6.0
 * @since 05/09/2020
 */
public class TablaPersonalizada extends JDialog{
    
    private static JTable tablaMisRetos = null;
    private static TableModel modeloMisRetos = null;
    private static JTable tablaRetosRivales = null;
    private static TableModel modeloRetosRivales = null;
    private static JTable tablaUsuariosConectados = null;
    private static TableModel modeloUsuariosConectados = null;
    private static JTable tablaRankings = null;
    private static TableModel modeloRankings = null;
    
    /**
     * Constructor de la clase
     * @param tipoTabla El tipo de tabla a crear
     */
    public TablaPersonalizada(int tipoTabla){
        
        if(tipoTabla == 1){ //Tabla de mis retos
            modeloMisRetos = new MTPMisRetos();
            tablaMisRetos = new JTable(modeloMisRetos);
        
            add(new JScrollPane(tablaMisRetos));
            tablaMisRetos.getColumnModel().getColumn(0).setPreferredWidth(125);
            tablaMisRetos.getColumnModel().getColumn(1).setPreferredWidth(165);
            tablaMisRetos.getTableHeader().setReorderingAllowed(false);
            tablaMisRetos.getTableHeader().setResizingAllowed(false);
            tablaMisRetos.setEnabled(false);
            tablaMisRetos.setVisible(true);
        }
        
        if(tipoTabla == 2){ //Tabla de retos del rival
            
        }
        
        if(tipoTabla == 3){ //Tabla de usuarios conectados
            
            modeloUsuariosConectados = new MTPUsuariosConectados();
            tablaUsuariosConectados = new JTable(modeloUsuariosConectados);
            
            add(new JScrollPane(tablaUsuariosConectados));
            
            tablaUsuariosConectados.getTableHeader().setReorderingAllowed(false);
            tablaUsuariosConectados.getTableHeader().setResizingAllowed(false);
            tablaUsuariosConectados.setEnabled(false);
            tablaUsuariosConectados.setVisible(true);
            
        }
        
        if(tipoTabla == 4){ //Tabla de los rankings
            modeloRankings = new MTPRankings();
            tablaRankings = new JTable(modeloRankings);

            add(new JScrollPane(tablaRankings));

            tablaRankings.getColumnModel().getColumn(0).setPreferredWidth(5);
            tablaRankings.getColumnModel().getColumn(2).setPreferredWidth(40);
            tablaRankings.getColumnModel().getColumn(3).setPreferredWidth(40);
            tablaRankings.getTableHeader().setReorderingAllowed(false);
            tablaRankings.getTableHeader().setResizingAllowed(false);
            tablaRankings.setEnabled(false);
            
            tablaRankings.setVisible(false);
            
            /*En este punto tenemos los datos del fichero en la tabla. Ahora hay
            que recuperar de la misma un array con las tandas ganadas y otro con
            las partidas ganadas para después reorganizar la tabla en función del
            filtro que haya elegido el usuario. Usaré el método de ordenación
            burbuja para ello*/
            
            int numFilasTablaRankings = tablaRankings.getModel().getRowCount();
            int[] arrayPartidasGanadas = new int[numFilasTablaRankings];
            int[] arrayTandasGanadas = new int[numFilasTablaRankings];
            int i;
            
            //Creamos los arrays con las partidas y las tandas ganadas:
            for(i=0; i<numFilasTablaRankings; i++){
                String valorPartidaString = tablaRankings.getModel().getValueAt(i, 2).toString();
                String valorTandaString = tablaRankings.getModel().getValueAt(i, 3).toString();
                int valorPartidaInt = Integer.parseInt(valorPartidaString);
                int valorTandaInt = Integer.parseInt(valorTandaString);
                arrayPartidasGanadas[i] = valorPartidaInt;
                arrayTandasGanadas[i] = valorTandaInt;
            }            
            
            //En función del filtro del cliente ordenamos por partidas o por tandas 
            String modo = "";
            if(Cliente.getTipoRankingElegido().equals("Ordenar por partidas ganadas")){
                modo = "partidas";
                ordenarBurbuja(arrayPartidasGanadas);
                System.out.println("Ordenación por partidas:");
                mostrarArrayburbuja(arrayPartidasGanadas);
                organizarTablaBurbuja(arrayPartidasGanadas, modo);
                
            }else if(Cliente.getTipoRankingElegido().equals("Ordenar por tandas ganadas")){
                modo = "tandas";
                ordenarBurbuja(arrayTandasGanadas);
                System.out.println("Ordenación por tandas:");
                mostrarArrayburbuja(arrayTandasGanadas);
                organizarTablaBurbuja(arrayTandasGanadas, modo);
            }
            
            //Añadimos un listener a nuestro TableModel para que se actualize la tabla cuando se ordene
            tablaRankings.getModel().addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if(e.getType() == TableModelEvent.UPDATE){
                        final int columna = 2;
                        
                        for(int i=(arrayPartidasGanadas.length)-1; i>=0; i--){
                            if(Cliente.getTipoRankingElegido().equals("Ordenar por partidas ganadas")){
                                tablaRankings.setValueAt(arrayPartidasGanadas[i], i, columna);
                            }else if(Cliente.getTipoRankingElegido().equals("Ordenar por tandas ganadas")){
                                tablaRankings.setValueAt(arrayPartidasGanadas[i], i, columna+1);
                            }
                        }
                    }
                }
            });
            
            tablaRankings.setVisible(true);
        }

    }

    /**
     * Método que ordena un array por el método de ordenación de la burbuja
     * @param array El array a ordenar
     */
    public static void ordenarBurbuja(int array[]){
        int n = array.length;
        
        for(int i=0; i<n-1; i++){
            for(int j=0; j< n-i-1; j++){
                if(array[j] > array[j+1]){
                    int tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                }
            }
        }
    }
    
    /**
     * Método que organiza la tabla en función del array ordenado y el modo indicado
     * @param array El array base organizado
     * @param modo El modo de ordenación (por partidas o por tandas)
     */
    public static void organizarTablaBurbuja(int array[], String modo){
        int n = array.length;
        int columnaAOrganizar = 1;
        
        if(modo.equals("partidas")){
            columnaAOrganizar = 2;
        }else if(modo.equals("tandas")){
            columnaAOrganizar = 3;
        }
        
        for(int i=n-1; i>=0; i--){
            tablaRankings.getModel().setValueAt(array[i], i, columnaAOrganizar);
        }
    }
    
    /**
     * Método que muestra un array que previamente ha sido ordenado por el método burbuja
     * @param array El array a mostrar
     */
    public static void mostrarArrayburbuja(int array[]){
        int n = array.length;
        
        /*El método de ordenación burbuja ordena de menor a mayor. Lo que queremos
        es que se muestren primero los valores más altos en la tabla de los rankings,
        así que deberemos imprimir los valores al revés de lo habitual*/
        
        for(int i=n-1; i>=0; i--){
            System.out.println(array[i] + " ");
        }
        System.out.println("");
    }
    
    //Setters y getters

    public static TableModel getModeloMisRetos() {
        return modeloMisRetos;
    }

    public static void setModeloMisRetos(TableModel aModeloMisRetos) {
        modeloMisRetos = aModeloMisRetos;
    }

    public static JTable getTablaMisRetos() {
        return tablaMisRetos;
    }

    public static void setTablaMisRetos(JTable aTablaMisRetos) {
        tablaMisRetos = aTablaMisRetos;
    }
    
}

//MTP = ModeloTablaPersonalizada

/**
 * Clase que crea una tabla con los rankings
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 */
class MTPRankings extends AbstractTableModel{
    /*
        La estructura del fichero de rankings es la siguiente: cuatro campos (posición,
        nick, partidas ganadas y tandas ganadas) separados cada uno por un punto y coma.

        Ejemplo: 1;#nelson;20;100

        Esto quiere decir que #nelson ha ganado un total de 20 partidas y 100 tandas
    */
    
    Vector datos;
    int posicion = 0;
    
    /**
     * Constructor
     */
    public MTPRankings(){
        
        String linea;
        datos = new Vector();
        
        FileReader fr = null;
        BufferedReader br = null;
        StringTokenizer st = null;
        
        try{
            
            fr = new FileReader(ficheroRankings);
            br = new BufferedReader(fr);

            while((linea = br.readLine()) != null){
                st = new StringTokenizer(linea, ";");
                
                while(st.hasMoreTokens()){
                    datos.addElement(st.nextToken());
                }
                
            }
            
            br.close();
            
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
    }
    
    @Override
    public String getColumnName(int i){
        
        switch(i){
            case 0:
                return "Pos.";
            case 1:
                return "Nick";
            case 2:
                return "P. Ganadas";
            case 3:
                return "T. Ganadas";                  
        }
        return "";
    }
    
    @Override
    public int getRowCount() {
        return datos.size() / getColumnCount();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return (String) datos.elementAt((rowIndex * getColumnCount()) + columnIndex);
    }
    
}


/**
 * Clase que crea una tabla con los retos enviados por el usuario
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 */

class MTPMisRetos extends AbstractTableModel{
    Vector datosFila;
    Vector datosColumna;
    
    public MTPMisRetos(){
        
        String linea = "";
        datosFila = new Vector();
        datosColumna = new Vector();
        
        try{
            
            FileInputStream fis = new FileInputStream(ficheroUsuariosRetados);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringTokenizer st1 = new StringTokenizer(br.readLine(), ";");
            
            while(st1.hasMoreTokens()){
                datosColumna.addElement(st1.nextToken());
            }
            
            while((linea = br.readLine()) != null){
                StringTokenizer st2 = new StringTokenizer(linea, ";");
                while(st2.hasMoreTokens()){
                    datosFila.addElement(st2.nextToken());
                }
            }
            
            br.close();
            
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }

    }

    @Override
    public int getRowCount() {
        return datosFila.size() / getColumnCount();
    }

    @Override
    public int getColumnCount() {
        return datosColumna.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return (String) datosFila.elementAt((rowIndex * getColumnCount()) + columnIndex);
    }
    
    @Override
    public String getColumnName(int i){
        return (String)datosColumna.get(i);
    }
}

/**
 * Clase que crea una tabla con los retos que recibe el usuario
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 * @deprecated Falta implementación
 */
class MTPRetosRival extends AbstractTableModel{

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return "";
    }
    
}


/**
 * Clase que crea una tabla con los usuarios conectados
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 */
class MTPUsuariosConectados extends AbstractTableModel{
    private static Vector nicks = new Vector();
    //private static Vector nicks;
    
    /**
     * Constructor
     */
    public MTPUsuariosConectados(){
        
        FileReader fr = null;
        BufferedReader br = null;
        String linea = "";
        
        try{
            fr = new FileReader("Usuarios conectados.txt");
            br = new BufferedReader(fr);
            
            while((linea = br.readLine()) != null){
                nicks.addElement(linea);
            }
            br.close();
            
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
    }
    
    @Override
    public int getRowCount() {
        int numFilas = Cliente.getNumJugadoresInt();
        return numFilas;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        if(columnIndex == 0){
            return nicks.get(rowIndex);
        }
        
        if(columnIndex == 1){
            return "Conectado";
        }
        
        return "";
    }
    
    @Override
    public String getColumnName(int i){
        if(i == 0){
            return "Nick";
        }
        
        if(i == 1){
            return "Estado";
        }
        
        return "";
    }
    
}