package practica.pkgfinal.mtpa;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Clase que sirve de apoyo a Cliente. Aquí se crean los diferentes JButton, JLabel,
 * JTextField, etc. necesarios para la interfaz de java swing. El cliente genera
 * una instancia de esta clase para usarla como un panel cambiante en función del
 * escenario en el que esté el usuario. Ver: {@link practica.pkgfinal.mtpa.Cliente}
 * @author Nelson Tuesta Fernández
 * @version 4.0
 * @since 05/09/2020
 */
public class Panel extends JFrame implements ActionListener{

    /**
     * Objeto de tipo JPanel para iniciar nuestro panel y añadir elementos al mismo
     */
    private static JPanel panel = null;
    
    /**
     * Todo esto son los JButtons, JLabels, JTextFields, JPasswordFields y JComboBoxs
     * que forman parte del panel
     */
    private JButton aceptar = null;
    private JButton salir = null;
    private JLabel usuario = null;
    private JLabel password = null;
    private JTextField putUsuario = null;
    private JPasswordField putPassword = null;
    private JLabel nuevoUsuario = null;
    private JButton registrarseAqui = null;
    private JButton actualizarLista = null;
    
    private JButton confirmarRegistro = null;
    private JButton atras = null;
    private JLabel usuario2 = null;
    private JLabel password2 = null;
    private JLabel passConfirm = null;
    private JTextField putUsuario2 = null;
    private JPasswordField putPassword2 = null;
    private JPasswordField putPassConfirm = null;
    
    private JLabel datosIncorrectos = null;
    private JLabel datosIncompletos = null;
    private JLabel datosIncompletos2 = null;
    private JLabel passNoCoincidente = null;
    private JLabel usuarioYaExiste = null;
    private JLabel restriccionesPassword = null;
    private JLabel simbolosProhibidos = null;
    private JLabel registroCompletado = null;
    
    private JLabel bienvenido = null;
    private JLabel nombreDelUsuario = null;
    private JLabel tandas = null;
    private JLabel esperandoRivales = null;
    private JLabel estableceDetalles = null;
    private JComboBox modalidadTandas = null;
    private JComboBox numeroTandas = null;
    private JLabel estableceTiempo = null;
    private JComboBox numeroSegundos = null;
    private JButton lanzarReto = null;
    private JLabel usuarioARetar = null;
    private JTextField putUsuarioARetar;
    private JLabel lanzarRetoCompletado = null;
    private JLabel ajustesIncompletos = null;
    private JLabel usuarioNoDisponible = null;
    private JLabel retoYaEnviado = null;
    private JLabel autoReto = null;
    private JLabel misRetos = null;
    private JLabel retosOtros = null;
    private JButton temporalUno = null;
    private JButton temporalDos = null;
    
    private JLabel tu = null;
    private JLabel tuContrincante = null;
    private JLabel tandasGanadas = null;
    private JLabel numTandasGanadas = null;
    private JLabel tandasRestantes = null;
    private JLabel numTandasRestantes = null;
    private JLabel tiempoRestante = null;
    private JLabel cantidadTiempoRestante = null;
    private JButton miPiedra = null;
    private JButton miPapel = null;
    private JButton miTijera = null;
    private JButton piedraRival = null;
    private JButton papelRival = null;
    private JButton tijeraRival = null;
    
    private JLabel usuariosConectados = null;
    private JLabel hashtagUsuarioARetar = null;
    private JLabel anuncioGanador = null;
    private JLabel nombreGanador = null;
    private JLabel victoria = null;
    private JLabel derrota = null;
    private JLabel queHacer = null;
    private JButton otroReto = null;
    private JButton verRanking = null;
    private JButton cerrarSesion = null;
    private JButton volverDelRanking = null;
    
    private JComboBox tipoRanking = null;
    private static JButton actualizarRankings = null;
    private JLabel tipoRankingNoElegido = null;
    private JLabel actualizarTablaCompletado = null;    
    
    /**
     * Estos son los String y boolean necesarios para recuperar los valores
     * introducidos por el usuario y para comprobar si se ha pulsado algún botón,
     * respectivamente
     */
    private String usuarioInicioSesion = null;
    private String passInicioSesion = null;
    private String usuarioRegistro = null;
    private String passRegistro = null;
    private String passConfirmRegistro = null;
    private String modalidad = null;
    private String numTandas = null;
    private String tiempo = null;
    private String oponenteReto = null;
    private String filtroRanking = null;
            
    private boolean aceptarInicioSesionPulsado = false;
    private boolean aceptarRegistroUsuarioPulsado = false;
    private boolean volverAtrasPulsado = false;
    private boolean registrateAquiPulsado = false;
    private boolean salirDelJuegoPulsado = false;
    private static boolean lanzarRetoPulsado = false;
    private static boolean actualizarListaPulsado = false;
    private boolean temporalUnoPulsado = false;
    private boolean temporalDosPulsado = false;
    private boolean verRankingPulsado = false;
    private boolean otroRetoPulsado = false;
    private boolean cerrarSesionPulsado = false;
    private boolean actualizarRankingsPulsado = false;
    private boolean volverDelRankingPulsado = false;
    
    /**
     * Esto es miscelánea. Aquí se declaran cosas como tipos de fuente, colores,
     * tipo de panel o el nick de un usuario conectado
     */
    private Font Arial = null;
    private Font ArialBold = null;
    private Font ArialBig = null;
    private Font ArialMedium = null;
    private Font ArialGiant = null;
    private Color VerdeOscuro = null;
    private int tipoPanel = 0;
    private String nickUsuarioConectado = null;
    
    /**
     * Constructor de la clase Panel. Este método empieza con la creación del mismo
     * y el método paintComponent, el cual uso para mostrar varias imágenes (piedra,
     * papel, tijeras, coronavirus, el logotipo de la UEMC y una imagen en blanco
     * que uso como fondo)
     */
    public Panel(){
        
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                crearImagenes(g);
            }
        };
                       
        panel.setLayout(null);
        Arial = new Font("Arial", Font.PLAIN, 13);
        ArialBold = new Font("Arial", Font.BOLD, 20);
        ArialBig = new Font("Arial", Font.PLAIN, 30);
        ArialMedium = new Font("Arial", Font.PLAIN, 18);
        ArialGiant = new Font("Arial", Font.BOLD, 75);
        VerdeOscuro = new Color(0, 143, 57);
        
        //Elementos "normales" del panel de inicio de sesión
                
        aceptar = new JButton();
        aceptar.setText("Aceptar");
        aceptar.setBounds(100, 220, 128, 30);
        panel.add(aceptar);

        salir = new JButton();
        salir.setText("Salir");
        salir.setBounds(260, 220, 128, 30);
        panel.add(salir);

        usuario = new JLabel();
        usuario.setText("Usuario:       #");
        usuario.setFont(ArialBold);
        usuario.setBounds(99, 101, 150, 20);
        panel.add(usuario);

        password = new JLabel();
        password.setText("Contraseña: ");
        password.setFont(ArialBold);
        password.setBounds(99, 146, 150, 20);
        panel.add(password);

        putUsuario = new JTextField();
        putUsuario.setBounds(240, 100, 150, 25);
        putUsuario.setFont(Arial);
        panel.add(putUsuario);

        putPassword = new JPasswordField();
        putPassword.setBounds(240, 145, 150, 25);
        panel.add(putPassword);

        nuevoUsuario = new JLabel();
        nuevoUsuario.setText("¿No tienes una cuenta?");
        nuevoUsuario.setFont(Arial);
        nuevoUsuario.setBounds(125, 185, 140, 20);
        panel.add(nuevoUsuario);

        registrarseAqui = new JButton();
        registrarseAqui.setText("Regístrate aquí");
        registrarseAqui.setFont(Arial);
        registrarseAqui.setForeground(Color.BLUE);
        registrarseAqui.setBounds(237, 185, 150, 20);
        registrarseAqui.setBorderPainted(false);
        registrarseAqui.setContentAreaFilled(false);
        panel.add(registrarseAqui);


        //Elementos de error del panel de inicio de sesión

        datosIncorrectos = new JLabel();
        datosIncorrectos.setText("Usuario o contraseña incorrectos");
        datosIncorrectos.setFont(Arial);
        datosIncorrectos.setForeground(Color.RED);
        datosIncorrectos.setBounds(150, 170, 300, 20);
        panel.add(datosIncorrectos);

        datosIncompletos = new JLabel();
        datosIncompletos.setText("Tienes que rellenar todos los campos");
        datosIncompletos.setFont(Arial);
        datosIncompletos.setForeground(Color.RED);
        datosIncompletos.setBounds(133, 170, 300, 20);
        panel.add(datosIncompletos);
        
        
        //Elementos "normales" del panel de registro de nuevos usuarios
        
        confirmarRegistro = new JButton();
        confirmarRegistro.setText("Registrarse");
        confirmarRegistro.setBounds(100, 250, 128, 30);
        panel.add(confirmarRegistro);

        atras = new JButton();
        atras.setText("Atrás");
        atras.setBounds(260, 250, 128, 30);
        panel.add(atras);

        usuario2 = new JLabel();
        usuario2.setText("Introduce un nick:           #");
        usuario2.setFont(ArialBold);
        usuario2.setBounds(40, 81, 300, 20);
        panel.add(usuario2);

        putUsuario2 = new JTextField();
        putUsuario2.setBounds(300, 79, 150, 25);
        putUsuario2.setFont(Arial);
        panel.add(putUsuario2);

        password2 = new JLabel();
        password2.setText("Introduce una contraseña: ");
        password2.setFont(ArialBold);
        password2.setBounds(40, 125, 300, 20);
        panel.add(password2);

        passConfirm = new JLabel();
        passConfirm.setText("Repite la contraseña: ");
        passConfirm.setFont(ArialBold);
        passConfirm.setBounds(40, 170, 300, 20);
        panel.add(passConfirm);

        putPassword2 = new JPasswordField();
        putPassword2.setBounds(300, 123, 150, 25);
        panel.add(putPassword2);

        putPassConfirm = new JPasswordField();
        putPassConfirm.setBounds(300, 169, 150, 25);
        panel.add(putPassConfirm);
        
        registroCompletado = new JLabel();
        registroCompletado.setText("Usuario registrado con éxito. Prueba a iniciar sesión");
        registroCompletado.setFont(Arial);
        registroCompletado.setForeground(VerdeOscuro);
        registroCompletado.setBounds(95, 170, 350, 20);
        panel.add(registroCompletado);

        //Elementos de error del panel de registro de nuevos usuarios

        datosIncompletos2 = new JLabel();
        datosIncompletos2.setText("Tienes que rellenar todos los campos");
        datosIncompletos2.setFont(Arial);
        datosIncompletos2.setForeground(Color.RED);
        datosIncompletos2.setBounds(133, 210, 300, 20);
        panel.add(datosIncompletos2);

        passNoCoincidente = new JLabel();
        passNoCoincidente.setText("Las contraseñas no coinciden");
        passNoCoincidente.setFont(Arial);
        passNoCoincidente.setForeground(Color.RED);
        passNoCoincidente.setBounds(150, 210, 300, 20);
        panel.add(passNoCoincidente);

        usuarioYaExiste = new JLabel();
        usuarioYaExiste.setText("Ese nombre de usuario ya está en uso");
        usuarioYaExiste.setFont(Arial);
        usuarioYaExiste.setForeground(Color.RED);
        usuarioYaExiste.setBounds(130, 210, 300, 20);
        panel.add(usuarioYaExiste);
        
        
        /*Este JLabel es demasiado largo. Utilizaré la notación de HTML para
        poder añadirle saltos de línea (<br> denota el salto de línea)*/
        restriccionesPassword = new JLabel();
        String restricciones = "<html> <body> La contraseña debe tener al menos 8 "
        + "caracte- <br> res, una mayúscula, un número y un símbolo </body> </html>";
        restriccionesPassword.setText(restricciones);
        restriccionesPassword.setFont(Arial);
        restriccionesPassword.setForeground(Color.RED);
        restriccionesPassword.setBounds(110, 200, 500, 40);
        panel.add(restriccionesPassword);

        /*Importante: respecto a los símbolos prohibidos en usuario y/o contraseña,
        si se usan en el panel de inicio de sesión marcar el error de datosIncorrectos.
        Si se usan en el panel de registro marcar el error de restriccionesPassword*/

        simbolosProhibidos = new JLabel();
        simbolosProhibidos.setText("No puedes usar ni para el usuario ni la contraseña los símbolos '#' ni ';'");
        simbolosProhibidos.setFont(Arial);
        simbolosProhibidos.setForeground(Color.RED);
        simbolosProhibidos.setBounds(40, 210, 500, 20);
        panel.add(simbolosProhibidos);
        
        
        
        //Elementos "normales" del panel de búsqueda de rivales
        
        bienvenido = new JLabel();
        bienvenido.setText("Bienvenido");
        bienvenido.setFont(ArialBig);
        bienvenido.setForeground(Color.BLACK);
        bienvenido.setBounds(25, 60, 500, 60);
        panel.add(bienvenido);
        
        nombreDelUsuario = new JLabel();
        //El texto de este JLabel lo estableceremos después para evitar el valor null
        nombreDelUsuario.setFont(ArialBig);
        nombreDelUsuario.setForeground(Color.BLUE);
        nombreDelUsuario.setBounds(190, 60, 500, 60);
        panel.add(nombreDelUsuario);
        
        estableceDetalles = new JLabel();
        estableceDetalles.setText("Establece los ajustes de la partida: ");
        estableceDetalles.setFont(ArialMedium);
        estableceDetalles.setForeground(Color.BLACK);
        estableceDetalles.setBounds(25, 125, 350, 20);
        panel.add(estableceDetalles);
        
        String modalidades[] = {"--Modalidad--", "Un total de", "Al mejor de", "El que llegue antes a"};
        modalidadTandas = new JComboBox(modalidades);
        modalidadTandas.setBackground(Color.WHITE);
        modalidadTandas.setBounds(25, 160, 150, 20);
        panel.add(modalidadTandas);
        
        String numeros[] = {"--Nº tandas--", "una", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez"};
        numeroTandas = new JComboBox(numeros);
        numeroTandas.setBounds(185, 160, 110, 20);
        numeroTandas.setBackground(Color.WHITE);
        panel.add(numeroTandas);
        
        tandas = new JLabel();
        tandas.setText("tanda(s)");
        tandas.setFont(Arial);
        tandas.setForeground(Color.BLACK);
        tandas.setBounds(300, 159, 150, 20);
        panel.add(tandas);        
        estableceTiempo = new JLabel();
        estableceTiempo.setText("Tiempo por tanda:                            segundos");
        estableceTiempo.setFont(Arial);
        estableceTiempo.setBounds(25, 190, 300, 20);
        estableceTiempo.setForeground(Color.BLACK);
        panel.add(estableceTiempo);
        
        String segundos[] = {"--Tiempo--", "cinco", "seis", "siete", "ocho", "nueve",
        "diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete",
        "dieciocho", "diecinueve", "veinte"};
        numeroSegundos = new JComboBox(segundos);
        numeroSegundos.setBounds(135, 191, 100, 20);
        numeroSegundos.setBackground(Color.WHITE);
        panel.add(numeroSegundos);

        usuarioARetar = new JLabel();
        String retar = "<html> <body> Introduce el nick del usuario al que quie-"
                + " <br> res retar (tiene que estar conectado) </body> </html>";
        usuarioARetar.setText(retar);
        usuarioARetar.setFont(Arial);
        usuarioARetar.setForeground(Color.BLACK);
        usuarioARetar.setBounds(25, 220, 250, 40);
        panel.add(usuarioARetar);
        
        putUsuarioARetar = new JTextField();
        putUsuarioARetar.setBounds(310, 230, 100, 20);
        putUsuarioARetar.setFont(Arial);
        panel.add(putUsuarioARetar);
        
        lanzarReto = new JButton();
        lanzarReto.setText("Lanzar reto");
        lanzarReto.setBounds(160, 293, 128, 30);
        panel.add(lanzarReto);
                
        lanzarRetoCompletado = new JLabel();
        lanzarRetoCompletado.setText("Reto lanzado con éxito. Esperando aceptación del rival...");
        lanzarRetoCompletado.setFont(Arial);
        lanzarRetoCompletado.setForeground(VerdeOscuro);
        lanzarRetoCompletado.setBounds(70, 263, 400, 20);
        panel.add(lanzarRetoCompletado);
        
        ajustesIncompletos = new JLabel();
        ajustesIncompletos.setText("Por favor, selecciona todos los ajustes de la partida");
        ajustesIncompletos.setFont(Arial);
        ajustesIncompletos.setForeground(Color.RED);
        ajustesIncompletos.setBounds(80, 263, 350, 20);
        panel.add(ajustesIncompletos);
                
        misRetos = new JLabel();
        misRetos.setText("Retos pendientes enviados por ti: ");
        misRetos.setFont(ArialBold);
        misRetos.setForeground(Color.BLACK);
        misRetos.setBounds(485, 71, 350, 30);
        panel.add(misRetos);
        
        retosOtros = new JLabel();
        retosOtros.setText("Retos pendientes de otros usuarios: ");
        retosOtros.setFont(ArialBold);
        retosOtros.setForeground(Color.BLACK);
        retosOtros.setBounds(485, 276, 380, 30);
        panel.add(retosOtros);
        
        usuariosConectados = new JLabel();
        usuariosConectados.setText("Lista de usuarios registrados:");
        usuariosConectados.setFont(ArialMedium);
        usuariosConectados.setBackground(Color.WHITE);
        usuariosConectados.setBounds(25, 343, 250, 20);
        panel.add(usuariosConectados);
        
        actualizarLista = new JButton();
        actualizarLista.setText("Actualizar lista");
        actualizarLista.setFont(Arial);
        actualizarLista.setBounds(307, 344, 120, 20);
        panel.add(actualizarLista);
        
        hashtagUsuarioARetar = new JLabel();
        hashtagUsuarioARetar.setText("#");
        hashtagUsuarioARetar.setFont(ArialBold);
        hashtagUsuarioARetar.setBounds(285, 220, 40, 40);
        panel.add(hashtagUsuarioARetar);
        
        temporalUno = new JButton();
        temporalUno.setText("Ir al panel 4");
        temporalUno.setBounds(320, 0, 128, 30);
        panel.add(temporalUno);
        
        temporalDos = new JButton();
        temporalDos.setText("Ir al panel 5");
        temporalDos.setBounds(320, 34, 128, 30);
        panel.add(temporalDos);
        
        //Elementos de error del panel de búsqueda de rivales
        
        usuarioNoDisponible = new JLabel();
        usuarioNoDisponible.setText("Ese usuario no existe o no está conectado");
        usuarioNoDisponible.setFont(Arial);
        usuarioNoDisponible.setForeground(Color.RED);
        usuarioNoDisponible.setBounds(100, 263, 340, 20);
        panel.add(usuarioNoDisponible);
        
        retoYaEnviado = new JLabel();
        retoYaEnviado.setText("Ya has enviado un reto a ese usuario");
        retoYaEnviado.setFont(Arial);
        retoYaEnviado.setForeground(Color.RED);
        retoYaEnviado.setBounds(115, 263, 340, 20);
        panel.add(retoYaEnviado);
        
        autoReto = new JLabel();
        autoReto.setText("No puedes enviarte un reto a ti mismo");
        autoReto.setFont(Arial);
        autoReto.setForeground(Color.RED);
        autoReto.setBounds(115, 263, 340, 20);
        panel.add(autoReto);
        
        
        //Elementos del panel del juego en sí
        
        tu = new JLabel();
        tu.setText("Tú");
        tu.setFont(ArialMedium);
        tu.setBounds(125, 80, 50, 50);
        panel.add(tu);
        
        tuContrincante = new JLabel();
        tuContrincante.setText("Tu contrincante");
        tuContrincante.setFont(ArialMedium);
        tuContrincante.setBounds(341, 80, 200, 50);
        panel.add(tuContrincante);
        
        tandasGanadas = new JLabel();
        tandasGanadas.setText("Tandas ganadas: ");
        tandasGanadas.setFont(ArialMedium);
        tandasGanadas.setBounds(20, 420, 200, 50);
        panel.add(tandasGanadas);
        
        numTandasGanadas = new JLabel();
        numTandasGanadas.setText("0");
        numTandasGanadas.setFont(ArialMedium);
        numTandasGanadas.setBounds(170, 420, 100, 50);
        panel.add(numTandasGanadas);
        
        tandasRestantes = new JLabel();
        tandasRestantes.setText("Tandas restantes: ");
        tandasRestantes.setFont(ArialMedium);
        tandasRestantes.setBounds(20, 460, 200, 50);
        panel.add(tandasRestantes);
        
        numTandasRestantes = new JLabel();
        numTandasRestantes.setText("0");
        numTandasRestantes.setFont(ArialMedium);
        numTandasRestantes.setBounds(170, 460, 100, 50);
        panel.add(numTandasRestantes);
        
        tiempoRestante = new JLabel();
        String tiempoHtml = "<html> <body> Tiempo restante:<br> &nbsp &nbsp &nbsp &nbsp segundo(s) </body> </html>";
        tiempoRestante.setText(tiempoHtml);
        tiempoRestante.setFont(ArialMedium);
        tiempoRestante.setBounds(325, 430, 500, 50);
        panel.add(tiempoRestante);
        
        cantidadTiempoRestante = new JLabel();
        cantidadTiempoRestante.setText("00");
        cantidadTiempoRestante.setFont(ArialMedium);
        cantidadTiempoRestante.setBounds(335, 442, 200, 50);
        panel.add(cantidadTiempoRestante);
        
        miPiedra = new JButton();
        miPiedra.setText("Piedra");
        miPiedra.setFont(ArialMedium);
        miPiedra.setBounds(90, 150, 100, 30);
        panel.add(miPiedra);
        
        miPapel = new JButton();
        miPapel.setText("Papel");
        miPapel.setFont(ArialMedium);
        miPapel.setBounds(90, 250, 100, 30);
        panel.add(miPapel);
        
        miTijera = new JButton();
        miTijera.setText("Tijera");
        miTijera.setFont(ArialMedium);
        miTijera.setBounds(90, 350, 100, 30);
        panel.add(miTijera);
        
        piedraRival = new JButton();
        piedraRival.setText("Piedra");
        piedraRival.setFont(ArialMedium);
        piedraRival.setBounds(355, 150, 100, 30);
        piedraRival.setEnabled(false);
        panel.add(piedraRival);
        
        papelRival = new JButton();
        papelRival.setText("Papel");
        papelRival.setFont(ArialMedium);
        papelRival.setBounds(355, 250, 100, 30);
        papelRival.setEnabled(false);
        panel.add(papelRival);
        
        tijeraRival = new JButton();
        tijeraRival.setText("Tijera");
        tijeraRival.setFont(ArialMedium);
        tijeraRival.setBounds(355, 350, 100, 30);
        tijeraRival.setEnabled(false);
        panel.add(tijeraRival);
        
        
        //Elementos del panel del resultado de la partida
        
        victoria = new JLabel();
        victoria.setText("Victoria");
        victoria.setFont(ArialGiant);
        victoria.setForeground(VerdeOscuro);
        victoria.setBounds(95, 110, 400, 100);
        panel.add(victoria);
        
        derrota = new JLabel();
        derrota.setText("Derrota");
        derrota.setFont(ArialGiant);
        derrota.setForeground(Color.RED);
        derrota.setBounds(95, 110, 400, 100);
        panel.add(derrota);
        
        anuncioGanador = new JLabel();
        anuncioGanador.setText("El ganador es ");
        anuncioGanador.setFont(ArialBig);
        anuncioGanador.setForeground(Color.BLACK);
        anuncioGanador.setBounds(95, 70, 200, 50);
        panel.add(anuncioGanador);
        
        nombreGanador = new JLabel();
        nombreGanador.setText("#nelson");
        /** @deprecated Hacer append correctamente con el usuario cuando arreglemos el problema del ArrayList*/
        //nombreGanador.setText("#");
        nombreGanador.setFont(ArialBig);
        nombreGanador.setForeground(Color.BLUE);
        nombreGanador.setBounds(290, 70, 200, 50);
        panel.add(nombreGanador);
        
        queHacer = new JLabel();
        queHacer.setText("¿Qué quieres hacer a continuación?");
        queHacer.setFont(ArialBig);
        queHacer.setBounds(45, 200, 500, 60);
        panel.add(queHacer);
        
        verRanking = new JButton();
        verRanking.setText("Ver ranking");
        verRanking.setBounds(50, 272, 138, 30);
        panel.add(verRanking);
        
        otroReto = new JButton();
        otroReto.setText("Volver a los retos");
        otroReto.setBounds(210, 272, 138, 30);
        panel.add(otroReto);
        
        cerrarSesion = new JButton();
        cerrarSesion.setText("Cerrar sesión");
        cerrarSesion.setBounds(380, 272, 138, 30);
        panel.add(cerrarSesion);
        
        
        //Elementos del panel de los rankings
        
        String tipos[] = {"--Elige filtro para el ranking--", "Ordenar por partidas ganadas", "Ordenar por tandas ganadas"};
        tipoRanking = new JComboBox(tipos);
        tipoRanking.setBounds(35, 85, 200, 25);
        tipoRanking.setBackground(Color.WHITE);
        panel.add(tipoRanking);
        
        actualizarRankings = new JButton();
        actualizarRankings.setText("Actualizar");
        actualizarRankings.setBounds(255, 85, 100, 25);
        panel.add(actualizarRankings);

        tipoRankingNoElegido = new JLabel();
        tipoRankingNoElegido.setText("Elige el tipo de ranking antes de actualizar");
        tipoRankingNoElegido.setFont(Arial);
        tipoRankingNoElegido.setForeground(Color.RED);
        tipoRankingNoElegido.setBounds(70, 115, 250, 20);
        panel.add(tipoRankingNoElegido);
        
        actualizarTablaCompletado = new JLabel();
        actualizarTablaCompletado.setText("Tipo de ranking actualizado con éxito");
        actualizarTablaCompletado.setFont(Arial);
        actualizarTablaCompletado.setForeground(VerdeOscuro);
        actualizarTablaCompletado.setBounds(85, 115, 250, 20);
        panel.add(actualizarTablaCompletado);
        
        volverDelRanking = new JButton();
        volverDelRanking.setText("Volver");
        volverDelRanking.setBounds(300, 365, 70, 30);
        panel.add(volverDelRanking);
        
        
        /**
         * En estos métodos se añade el panel al JFrame (al que extiende la clase
         * Panel) y se añaden los diferentes ActionListener a los botones del mismo
         */
        this.add(panel);
        aceptar.addActionListener(this);
        salir.addActionListener(this);
        registrarseAqui.addActionListener(this);
        atras.addActionListener(this);
        confirmarRegistro.addActionListener(this);
        lanzarReto.addActionListener(this);
        actualizarLista.addActionListener(this);
        temporalUno.addActionListener(this);
        temporalDos.addActionListener(this);
        verRanking.addActionListener(this);
        otroReto.addActionListener(this);
        cerrarSesion.addActionListener(this);
        volverDelRanking.addActionListener(this);
        actualizarRankings.addActionListener(this);
        
        tipoPanel = 1;
        tipoPanel = getTipoPanel();
        
        //Todos los elementos estarán ocultos por defecto (los activaremos/desactivaremos en un switch más adelante)
        ocultarTodosLosElementos();
        cambiaPanel(tipoPanel);
        
    }
    
    /**
     * Método que se usa para cambiar la forma y elementos del panel cuando el usuario cambie de escenario
     * @param tipoPanel El tipo de panel al que se va a cambiar
     */
    public void cambiaPanel(int tipoPanel){
        switch(tipoPanel){
            
            case 1: //Iniciar sesión
                
                ocultarTodosLosElementos();
                resetearCampos();
                
                aceptar.setVisible(true);
                salir.setVisible(true);
                usuario.setVisible(true);
                password.setVisible(true);
                putUsuario.setVisible(true);
                putPassword.setVisible(true);
                nuevoUsuario.setVisible(true);
                registrarseAqui.setVisible(true);
                
                break;
                
            case 2: //Registrarse
                
                ocultarTodosLosElementos();
                resetearCampos();
                
                confirmarRegistro.setVisible(true);
                atras.setVisible(true);
                usuario2.setVisible(true);
                password2.setVisible(true);
                passConfirm.setVisible(true);
                putUsuario2.setVisible(true);
                putPassword2.setVisible(true);
                putPassConfirm.setVisible(true);
                
                break;
                
            case 3: //Configurar la partida
                
                ocultarTodosLosElementos();
                resetearCampos();
                
                bienvenido.setVisible(true);
                tandas.setVisible(true);
                nombreDelUsuario.setVisible(true);
                nombreDelUsuario.setText("#" + getNickUsuarioConectado());
                estableceDetalles.setVisible(true);
                modalidadTandas.setVisible(true);
                numeroTandas.setVisible(true);
                estableceTiempo.setVisible(true);
                numeroSegundos.setVisible(true);
                usuarioARetar.setVisible(true);
                putUsuarioARetar.setVisible(true);
                lanzarReto.setVisible(true);
                misRetos.setVisible(true);
                retosOtros.setVisible(true);
                usuariosConectados.setVisible(true);
                hashtagUsuarioARetar.setVisible(true);
                actualizarLista.setVisible(true);
                temporalUno.setVisible(true);
                temporalDos.setVisible(true);
                
                break;
                
            case 4: //Juego en sí
                
                ocultarTodosLosElementos();
                
                tu.setVisible(true);
                tuContrincante.setVisible(true);
                tandasGanadas.setVisible(true);
                numTandasGanadas.setVisible(true);
                tandasRestantes.setVisible(true);
                numTandasRestantes.setVisible(true);
                miPiedra.setVisible(true);
                miPapel.setVisible(true);
                miTijera.setVisible(true);
                piedraRival.setVisible(true);
                papelRival.setVisible(true);
                tijeraRival.setVisible(true);
                tiempoRestante.setVisible(true);
                cantidadTiempoRestante.setVisible(true);
                
                break;
                
            case 5: //Resultado de la partida
                
                ocultarTodosLosElementos();
                
                anuncioGanador.setVisible(true);
                nombreGanador.setVisible(true);
                victoria.setVisible(true);
                //derrota.setVisible(true);
                queHacer.setVisible(true);
                verRanking.setVisible(true);
                otroReto.setVisible(true);
                cerrarSesion.setVisible(true);
                
                break;
                
            case 6: //Rankings
                
                ocultarTodosLosElementos();
                resetearCampos();
                
                tipoRanking.setVisible(true);
                actualizarRankings.setVisible(true);
                volverDelRanking.setVisible(true);
                
                break;
            
            case 7: //Salir
                
                break;
                
            default: //Error
                
                break;
        }
    }
    
    /**
     * Método que sirve para crear las imágenes que se usan en el programa
     * @param g El componente de tipo Graphics que dibuja las imágenes
     */
    public void crearImagenes(Graphics g){
        try{
                
            switch (getTipoPanel()) {
                case 1:
                    {
                        Image piedra = ImageIO.read(new File("piedra.jpg"));
                        g.drawImage(piedra, 0, 0, 64, 64, null);
                        
                        Image papel = ImageIO.read(new File("papel.jpg"));
                        g.drawImage(papel, 64, 0, 64, 64, null);
                        
                        Image tijera = ImageIO.read(new File("tijeras.jpg"));
                        g.drawImage(tijera, 128, 0, 64, 64, null);
                        
                        Image coronavirus = ImageIO.read(new File("covid-19.jpg"));
                        g.drawImage(coronavirus, 192, 0, 64, 64, null);
                        
                        Image blanco = ImageIO.read(new File("blanco.png"));
                        g.drawImage(blanco, 256, 0, 127, 64, null);
                        
                        Image uemc = ImageIO.read(new File("uemc_logo.png"));
                        g.drawImage(uemc, 383, 0, 111, 64, null);
                        break;
                    }
                    
                case 2:
                    {
                        Image piedra = ImageIO.read(new File("piedra.jpg"));
                        g.drawImage(piedra, 0, 0, 64, 64, null);
                        
                        Image papel = ImageIO.read(new File("papel.jpg"));
                        g.drawImage(papel, 64, 0, 64, 64, null);
                        
                        Image tijera = ImageIO.read(new File("tijeras.jpg"));
                        g.drawImage(tijera, 128, 0, 64, 64, null);
                        
                        Image coronavirus = ImageIO.read(new File("covid-19.jpg"));
                        g.drawImage(coronavirus, 192, 0, 64, 64, null);
                        
                        Image blanco = ImageIO.read(new File("blanco.png"));
                        g.drawImage(blanco, 256, 0, 127, 64, null);
                        
                        Image uemc = ImageIO.read(new File("uemc_logo.png"));
                        g.drawImage(uemc, 383, 0, 111, 64, null);
                        break;
                    }
                    
                case 3:
                    {
                        Image blanco = ImageIO.read(new File("blanco.png"));
                        g.drawImage(blanco, 0, 0, 900, 64, null);
                        
                        Image piedra = ImageIO.read(new File("piedra.jpg"));
                        g.drawImage(piedra, 20, 0, 64, 64, null);
                        
                        Image papel = ImageIO.read(new File("papel.jpg"));
                        g.drawImage(papel, 84, 0, 64, 64, null);
                        
                        Image tijera = ImageIO.read(new File("tijeras.jpg"));
                        g.drawImage(tijera, 148, 0, 64, 64, null);
                        
                        Image coronavirus = ImageIO.read(new File("covid-19.jpg"));
                        g.drawImage(coronavirus, 212, 0, 64, 64, null);
                        
                        Image uemc = ImageIO.read(new File("uemc_logo.png"));
                        g.drawImage(uemc, 766, 0, 111, 64, null);

                        break;
                    }
                    
                case 4:
                    {
                        Image blanco = ImageIO.read(new File("blanco.png"));
                        g.drawImage(blanco, 0, 0, 900, 64, null);
                        
                        Image piedra = ImageIO.read(new File("piedra.jpg"));
                        g.drawImage(piedra, 20, 0, 64, 64, null);
                        
                        Image papel = ImageIO.read(new File("papel.jpg"));
                        g.drawImage(papel, 84, 0, 64, 64, null);
                        
                        Image tijera = ImageIO.read(new File("tijeras.jpg"));
                        g.drawImage(tijera, 148, 0, 64, 64, null);
                        
                        Image coronavirus = ImageIO.read(new File("covid-19.jpg"));
                        g.drawImage(coronavirus, 212, 0, 64, 64, null);
                        
                        Image uemc = ImageIO.read(new File("uemc_logo.png"));
                        g.drawImage(uemc, 463, 0, 111, 64, null);
                        
                        Image reloj = ImageIO.read(new File("reloj.png"));
                        g.drawImage(reloj, 470, 425, 64, 64, null);
                        
                        break;
                    }
                    
                case 5:
                    {
                        Image piedra = ImageIO.read(new File("piedra.jpg"));
                        g.drawImage(piedra, 0, 0, 64, 64, null);
                        
                        Image papel = ImageIO.read(new File("papel.jpg"));
                        g.drawImage(papel, 64, 0, 64, 64, null);
                        
                        Image tijera = ImageIO.read(new File("tijeras.jpg"));
                        g.drawImage(tijera, 128, 0, 64, 64, null);
                        
                        Image coronavirus = ImageIO.read(new File("covid-19.jpg"));
                        g.drawImage(coronavirus, 192, 0, 64, 64, null);
                        
                        Image blanco = ImageIO.read(new File("blanco.png"));
                        g.drawImage(blanco, 256, 0, 210, 64, null);
                        
                        Image uemc = ImageIO.read(new File("uemc_logo.png"));
                        g.drawImage(uemc, 463, 0, 111, 64, null);
                        
                        Image bien = ImageIO.read(new File("bien.png"));
                        g.drawImage(bien, 365, 100, 130, 130, null);
                        
                        /**
                         * @deprecated
                         */
                        //Image mal = ImageIO.read(new File("mal.png"));
                        //g.drawImage(mal, 365, 97, 130, 130, null);
                        
                        break;
                    }
                    
                case 6:
                    {
                        Image piedra = ImageIO.read(new File("piedra.jpg"));
                        g.drawImage(piedra, 0, 0, 64, 64, null);
                        
                        Image papel = ImageIO.read(new File("papel.jpg"));
                        g.drawImage(papel, 64, 0, 64, 64, null);
                        
                        Image tijera = ImageIO.read(new File("tijeras.jpg"));
                        g.drawImage(tijera, 128, 0, 64, 64, null);
                        
                        Image coronavirus = ImageIO.read(new File("covid-19.jpg"));
                        g.drawImage(coronavirus, 192, 0, 64, 64, null);
                        
                        Image blanco = ImageIO.read(new File("blanco.png"));
                        g.drawImage(blanco, 256, 0, 27, 64, null);
                        
                        Image uemc = ImageIO.read(new File("uemc_logo.png"));
                        g.drawImage(uemc, 283, 0, 111, 64, null);
                                                
                        break;
                    }
                    
                default:
                    break;
            }

        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
    }
    
    /**
     * Método que sirve para ocultar todos los elementos del panel. Se usa justo
     * antes de un cambio de escenario para evitar que se dupliquen o se solapen
     * los elementos del mismo
     */
    public void ocultarTodosLosElementos(){
        aceptar.setVisible(false);
        salir.setVisible(false);
        usuario.setVisible(false);
        password.setVisible(false);
        nuevoUsuario.setVisible(false);
        registrarseAqui.setVisible(false);
        registroCompletado.setVisible(false);
        
        confirmarRegistro.setVisible(false);
        atras.setVisible(false);
        usuario2.setVisible(false);
        password2.setVisible(false);
        passConfirm.setVisible(false);
        
        putUsuario.setVisible(false);
        putPassword.setVisible(false);
        putUsuario2.setVisible(false);
        putPassword2.setVisible(false);
        putPassConfirm.setVisible(false);
        
        datosIncompletos.setVisible(false);
        datosIncompletos2.setVisible(false);
        datosIncorrectos.setVisible(false);
        passNoCoincidente.setVisible(false);
        usuarioYaExiste.setVisible(false);
        restriccionesPassword.setVisible(false);     
        
        getRegistroCompletado().setVisible(false);
        getDatosIncorrectos().setVisible(false);
        getDatosIncompletos().setVisible(false);
        getDatosIncompletos2().setVisible(false);
        getPassNoCoincidente().setVisible(false);
        getUsuarioYaExiste().setVisible(false);
        getRestriccionesPassword().setVisible(false);
        getSimbolosProhibidos().setVisible(false);
        
        bienvenido.setVisible(false);
        tandas.setVisible(false);
        nombreDelUsuario.setVisible(false);
        estableceDetalles.setVisible(false);
        modalidadTandas.setVisible(false);
        numeroTandas.setVisible(false);
        estableceTiempo.setVisible(false);
        numeroSegundos.setVisible(false);
        usuarioARetar.setVisible(false);
        putUsuarioARetar.setVisible(false);
        usuarioNoDisponible.setVisible(false);
        lanzarReto.setVisible(false);
        lanzarRetoCompletado.setVisible(false);
        ajustesIncompletos.setVisible(false);
        misRetos.setVisible(false);
        retosOtros.setVisible(false);
        usuariosConectados.setVisible(false);
        hashtagUsuarioARetar.setVisible(false);
        retoYaEnviado.setVisible(false);
        autoReto.setVisible(false);
        actualizarLista.setVisible(false);
        temporalUno.setVisible(false);
        temporalDos.setVisible(false);
        anuncioGanador.setVisible(false);
        nombreGanador.setVisible(false);
        victoria.setVisible(false);
        derrota.setVisible(false);
        queHacer.setVisible(false);
        verRanking.setVisible(false);
        otroReto.setVisible(false);
        cerrarSesion.setVisible(false);
        tipoRanking.setVisible(false);
        actualizarRankings.setVisible(false);
        tipoRankingNoElegido.setVisible(false);
        volverDelRanking.setVisible(false);
        actualizarTablaCompletado.setVisible(false);
        tu.setVisible(false);
        tuContrincante.setVisible(false);
        tandasGanadas.setVisible(false);
        numTandasGanadas.setVisible(false);
        tandasRestantes.setVisible(false);
        numTandasRestantes.setVisible(false);
        miPiedra.setVisible(false);
        miPapel.setVisible(false);
        miTijera.setVisible(false);
        piedraRival.setVisible(false);
        papelRival.setVisible(false);
        tijeraRival.setVisible(false);
        tiempoRestante.setVisible(false);
        cantidadTiempoRestante.setVisible(false);
        
        try{ Cliente.getDialogVerRankings().setVisible(false); }catch(Exception ex){};  
        try{ Cliente.getDialogMisRetos().setVisible(false); }catch(Exception ex){};
        try{ Cliente.getDialogUsuariosConectados().setVisible(false); }catch(Exception ex){};
    }
    
    /**
     * Este método devuelve a todos los JTextField y JComboBox sus valores por defecto
     */
    public void resetearCampos(){
        
        putUsuario.setText("");
        putPassword.setText("");
        putUsuario2.setText("");
        putPassword2.setText("");
        putPassConfirm.setText("");
        putUsuarioARetar.setText("");
        modalidadTandas.setSelectedItem("--Modalidad--");
        numeroTandas.setSelectedItem("--Nº tandas--");
        numeroSegundos.setSelectedItem("--Tiempo--");
        tipoRanking.setSelectedItem("--Elige filtro para el ranking--");
    }
    
    /**
     * Este método sirve para decidir lo que debe hacer el programa al pulsar
     * los botones
     * @param e El Objeto de tipo ActionEvent que se usa para obtener las fuentes
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == aceptar){
            
            setAceptarInicioSesionPulsado(true);
            usuarioInicioSesion = putUsuario.getText();
            passInicioSesion = String.valueOf(putPassword.getPassword());
            
            setUsuarioInicioSesion(usuarioInicioSesion);
            setPasswordInicioSesion(passInicioSesion);
            
            setAceptarInicioSesionPulsado(false);
                       
        }else if(e.getSource() == salir){
            
            //Finalizar el programa
            panel.setVisible(false);
            setSalirDelJuegoPulsado(true);
            System.exit(0);
            
        }else if(e.getSource() == registrarseAqui){
            
            setRegistrateAquiPulsado(true);
            setTipoPanel(2);
            cambiaPanel(2);
            setRegistrateAquiPulsado(false);
            
        }else if(e.getSource() == confirmarRegistro){

            setAceptarRegistroUsuarioPulsado(true);
            usuarioRegistro = putUsuario2.getText();
            passRegistro = String.valueOf(putPassword2.getPassword());
            passConfirmRegistro = String.valueOf(putPassConfirm.getPassword());

            setUsuarioRegistro(usuarioRegistro);
            setPassRegistro(passRegistro);
            setPassConfirmRegistro(passConfirmRegistro);
            setAceptarRegistroUsuarioPulsado(false);
            
        }else if(e.getSource() == atras){
            
            //Volvemos a la pantalla de inicio de sesión
            setVolverAtrasPulsado(true);
            setTipoPanel(1);
            cambiaPanel(1);
            setVolverAtrasPulsado(false);
            
        }else if(e.getSource() == lanzarReto){       
            
            setLanzarRetoPulsado(true);
            modalidad = modalidadTandas.getSelectedItem().toString();
            numTandas = numeroTandas.getSelectedItem().toString();
            tiempo = numeroSegundos.getSelectedItem().toString();
            oponenteReto = putUsuarioARetar.getText();
            
            setModalidad(modalidad);
            setNumTandas(numTandas);
            setTiempo(tiempo);
            setOponenteReto(oponenteReto);
            
            setLanzarRetoPulsado(false);
            
        }else if(e.getSource() == actualizarLista){
            
            setActualizarListaPulsado(true);
            setActualizarListaPulsado(false);
            
        }else if(e.getSource() == temporalUno){
            
            setTemporalUnoPulsado(true);
            setTipoPanel(4);
            cambiaPanel(4);
            setTemporalUnoPulsado(false);
            
        }else if(e.getSource() == temporalDos){
            
            setTemporalDosPulsado(true);
            setTipoPanel(5);
            cambiaPanel(5);
            setTemporalDosPulsado(false);
            
        }else if(e.getSource() == otroReto){
            
            setOtroRetoPulsado(true);
            setTipoPanel(3);
            cambiaPanel(3);
            setOtroRetoPulsado(false);
            
        }else if(e.getSource() == cerrarSesion){
            
            setCerrarSesionPulsado(true);
            setTipoPanel(1);
            cambiaPanel(1);
            
            /**
             * @deprecated
             */
            //Cliente cl = Cliente.buscaJugador();
            //boolean desconectar = Cliente.marcarJugadoresOffline(cl);
            
            Servidor sv = Connection.buscaJugador();
            boolean desconectar = Connection.marcarJugadoresOffline(sv);
                                        
            if(desconectar){
                System.out.println("Se ha cerrado la sesión");
            }else{
                System.out.println("Se ha cerrado la sesión, pero con errores");
            }
            
            setCerrarSesionPulsado(false);
            
        }else if(e.getSource() == verRanking){
            
            setVerRankingPulsado(true);
            setTipoPanel(6);
            cambiaPanel(6);
            setVerRankingPulsado(false);
            
        }else if(e.getSource() == volverDelRanking){
            
            setVolverDelRankingPulsado(true);
            setTipoPanel(5);
            cambiaPanel(5);
            setVolverDelRankingPulsado(false);
            
        }else if(e.getSource() == actualizarRankings){
            
            setActualizarRankingsPulsado(true);
            
            filtroRanking = tipoRanking.getSelectedItem().toString();
            setFiltroRanking(filtroRanking);
            
            setActualizarRankingsPulsado(false);
            
        }
    }
    
    //Setters y getters
    
    public int getTipoPanel() {
        return tipoPanel;
    }

    public void setTipoPanel(int tipoPanel) {
        this.tipoPanel = tipoPanel;
    }
    
    public String getUsuarioInicioSesion(){
        return usuarioInicioSesion;
    }
    
    public void setUsuarioInicioSesion(String usuarioInicioSesion){
        this.usuarioInicioSesion = usuarioInicioSesion;
    }
    
    public String getPasswordInicioSesion(){
        return passInicioSesion;
    }
    
    public void setPasswordInicioSesion(String passwordInicioSesion){
        this.passInicioSesion = passwordInicioSesion;
    }
    
    public boolean isAceptarInicioSesionPulsado(){
        return aceptarInicioSesionPulsado;
    }
    
    public void setUsuarioRegistro(String usuarioRegistro){
        this.usuarioRegistro = usuarioRegistro;
    }
    
    public String getUsuarioRegistro(){
        return usuarioRegistro;
    }
    
    public void setAceptarRegistroUsuarioPulsado(boolean aceptarRegistroUsuarioPulsado) {
        this.aceptarRegistroUsuarioPulsado = aceptarRegistroUsuarioPulsado;
    }
    
    public boolean isAceptarRegistroUsuarioPulsado(){
        return aceptarRegistroUsuarioPulsado;
    }
    
    public boolean isRegistrateAquiPulsado() {
        return registrateAquiPulsado;
    }

    public void setRegistrateAquiPulsado(boolean registrateAquiPulsado) {
        this.registrateAquiPulsado = registrateAquiPulsado;
    }
    
    public void setAceptarInicioSesionPulsado(boolean aceptarInicioSesionPulsado) {
        this.aceptarInicioSesionPulsado = aceptarInicioSesionPulsado;
    }
    
    public void setVolverAtrasPulsado(boolean volverAtrasPulsado){
        this.volverAtrasPulsado = volverAtrasPulsado;
    }
    
    public boolean isVolverAtrasPulsado(){
        return volverAtrasPulsado;
    }

    public String getPassRegistro() {
        return passRegistro;
    }

    public void setPassRegistro(String passRegistro) {
        this.passRegistro = passRegistro;
    }

    public String getPassConfirmRegistro() {
        return passConfirmRegistro;
    }

    public void setPassConfirmRegistro(String passConfirmRegistro) {
        this.passConfirmRegistro = passConfirmRegistro;
    }

    public JLabel getDatosIncompletos() {
        return datosIncompletos;
    }

    public void setDatosIncompletos(JLabel datosIncompletos) {
        this.datosIncompletos = datosIncompletos;
    }

    public JLabel getDatosIncorrectos() {
        return datosIncorrectos;
    }

    public void setDatosIncorrectos(JLabel datosIncorrectos) {
        this.datosIncorrectos = datosIncorrectos;
    }

    public JLabel getDatosIncompletos2() {
        return datosIncompletos2;
    }

    public void setDatosIncompletos2(JLabel datosIncompletos2) {
        this.datosIncompletos2 = datosIncompletos2;
    }

    public JLabel getPassNoCoincidente() {
        return passNoCoincidente;
    }

    public void setPassNoCoincidente(JLabel passNoCoincidente) {
        this.passNoCoincidente = passNoCoincidente;
    }

    public JLabel getUsuarioYaExiste() {
        return usuarioYaExiste;
    }

    public void setUsuarioYaExiste(JLabel usuarioYaExiste) {
        this.usuarioYaExiste = usuarioYaExiste;
    }

    public JLabel getRestriccionesPassword() {
        return restriccionesPassword;
    }

    public void setRestriccionesPassword(JLabel restriccionesPassword) {
        this.restriccionesPassword = restriccionesPassword;
    }

    public JLabel getSimbolosProhibidos() {
        return simbolosProhibidos;
    }

    public void setSimbolosProhibidos(JLabel simbolosProhibidos) {
        this.simbolosProhibidos = simbolosProhibidos;
    }

    public JLabel getRegistroCompletado() {
        return registroCompletado;
    }

    public void setRegistroCompletado(JLabel registroCompletado) {
        this.registroCompletado = registroCompletado;
    }

    public boolean isSalirDelJuegoPulsado() {
        return salirDelJuegoPulsado;
    }

    public void setSalirDelJuegoPulsado(boolean salirDelJuegoPulsado) {
        this.salirDelJuegoPulsado = salirDelJuegoPulsado;
    }

    public String getNickUsuarioConectado() {
        return nickUsuarioConectado;
    }

    public void setNickUsuarioConectado(String nickUsuarioConectado) {
        this.nickUsuarioConectado = nickUsuarioConectado;
    }

    public static boolean isLanzarRetoPulsado() {
        return lanzarRetoPulsado;
    }

    public static void setLanzarRetoPulsado(boolean aLanzarRetoPulsado) {
        lanzarRetoPulsado = aLanzarRetoPulsado;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getNumTandas() {
        return numTandas;
    }

    public void setNumTandas(String numTandas) {
        this.numTandas = numTandas;
    }
    
    public String getTiempo() {
        return tiempo;
    }
    
    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
    
    public String getOponenteReto() {
        return oponenteReto;
    }

    public void setOponenteReto(String oponenteReto) {
        this.oponenteReto = oponenteReto;
    }

    public JLabel getLanzarRetoCompletado() {
        return lanzarRetoCompletado;
    }

    public void setLanzarRetoCompletado(JLabel lanzarRetoCompletado) {
        this.lanzarRetoCompletado = lanzarRetoCompletado;
    }

    public JLabel getAjustesIncompletos() {
        return ajustesIncompletos;
    }

    public void setAjustesIncompletos(JLabel ajustesIncompletos) {
        this.ajustesIncompletos = ajustesIncompletos;
    }

    public JLabel getUsuarioNoDisponible() {
        return usuarioNoDisponible;
    }

    public void setUsuarioNoDisponible(JLabel usuarioNoDisponible) {
        this.usuarioNoDisponible = usuarioNoDisponible;
    }

    public JLabel getRetoYaEnviado() {
        return retoYaEnviado;
    }

    public void setRetoYaEnviado(JLabel retoYaEnviado) {
        this.retoYaEnviado = retoYaEnviado;
    }

    public static boolean isActualizarListaPulsado() {
        return actualizarListaPulsado;
    }

    public static void setActualizarListaPulsado(boolean aActualizarListaPulsado) {
        actualizarListaPulsado = aActualizarListaPulsado;
    }

    public boolean isTemporalDosPulsado() {
        return temporalDosPulsado;
    }

    public void setTemporalDosPulsado(boolean temporalDosPulsado) {
        this.temporalDosPulsado = temporalDosPulsado;
    }

    public boolean isOtroRetoPulsado() {
        return otroRetoPulsado;
    }

    public void setOtroRetoPulsado(boolean otroRetoPulsado) {
        this.otroRetoPulsado = otroRetoPulsado;
    }

    public boolean isCerrarSesionPulsado() {
        return cerrarSesionPulsado;
    }

    public void setCerrarSesionPulsado(boolean cerrarSesionPulsado) {
        this.cerrarSesionPulsado = cerrarSesionPulsado;
    }

    public boolean isVerRankingPulsado() {
        return verRankingPulsado;
    }

    public void setVerRankingPulsado(boolean verRankingPulsado) {
        this.verRankingPulsado = verRankingPulsado;
    }

    public String getFiltroRanking() {
        return filtroRanking;
    }

    public void setFiltroRanking(String filtroRanking) {
        this.filtroRanking = filtroRanking;
    }

    public boolean isActualizarRankingsPulsado() {
        return actualizarRankingsPulsado;
    }

    public void setActualizarRankingsPulsado(boolean actualizarRankingsPulsado) {
        this.actualizarRankingsPulsado = actualizarRankingsPulsado;
    }

    public boolean isVolverDelRankingPulsado() {
        return volverDelRankingPulsado;
    }

    public void setVolverDelRankingPulsado(boolean volverDelRankingPulsado) {
        this.volverDelRankingPulsado = volverDelRankingPulsado;
    }

    public JLabel getTipoRankingNoElegido() {
        return tipoRankingNoElegido;
    }

    public void setTipoRankingNoElegido(JLabel tipoRankingNoElegido) {
        this.tipoRankingNoElegido = tipoRankingNoElegido;
    }

    public JLabel getActualizarTablaCompletado() {
        return actualizarTablaCompletado;
    }

    public void setActualizarTablaCompletado(JLabel actualizarTablaCompletado) {
        this.actualizarTablaCompletado = actualizarTablaCompletado;
    }

    public JLabel getAutoReto() {
        return autoReto;
    }

    public void setAutoReto(JLabel autoReto) {
        this.autoReto = autoReto;
    }

    public boolean isTemporalUnoPulsado() {
        return temporalUnoPulsado;
    }

    public void setTemporalUnoPulsado(boolean temporalUnoPulsado) {
        this.temporalUnoPulsado = temporalUnoPulsado;
    }

    public static JButton getActualizarRankings() {
        return actualizarRankings;
    }

    public static void setActualizarRankings(JButton aActualizarRankings) {
        actualizarRankings = aActualizarRankings;
    }

}