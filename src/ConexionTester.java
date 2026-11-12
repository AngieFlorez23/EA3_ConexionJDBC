
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ConexionTester extends JFrame {

    // Componentes del formulario
    private JTextField campoUrl;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonProbar;
    private JTextArea areaResultado;

    public ConexionTester() {

        setTitle("Gestor de Conexiones JDBC");
        setSize(700, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =====================================
        // ENCABEZADO
        // =====================================

        JPanel encabezado = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );

        encabezado.setBackground(new Color(28, 55, 91));

        encabezado.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel titulo = new JLabel(
                "GESTOR DE CONEXIONES JDBC"
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 23)
        );
        titulo.setForeground(Color.WHITE);

        JLabel descripcion = new JLabel(
                "Configuración y validación de bases de datos"
        );

        descripcion.setForeground(
                new Color(205, 220, 240)
        );

        descripcion.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        encabezado.add(titulo);
        encabezado.add(descripcion);

        add(encabezado, BorderLayout.NORTH);

        // =====================================
        // FORMULARIO
        // =====================================

        JPanel formulario = new JPanel(
                new GridLayout(3, 2, 15, 18)
        );

        formulario.setBorder(
                BorderFactory.createEmptyBorder(30, 25, 20, 25)
        );

        campoUrl = new JTextField(
                "jdbc:sqlite:inventario.db"
        );

        campoUsuario = new JTextField();
        campoContrasena = new JPasswordField();

        formulario.add(new JLabel("URL de conexión:"));
        formulario.add(campoUrl);

        formulario.add(new JLabel("Usuario:"));
        formulario.add(campoUsuario);

        formulario.add(new JLabel("Contraseña:"));
        formulario.add(campoContrasena);

        // =====================================
        // RESULTADOS
        // =====================================

        JPanel panelResultado = new JPanel(
                new BorderLayout(0, 10)
        );

        panelResultado.setBorder(
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        );

        JLabel etiquetaResultado = new JLabel(
                "Resultado de la conexión"
        );

        etiquetaResultado.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);

        areaResultado.setFont(
                new Font("Consolas", Font.PLAIN, 13)
        );

        areaResultado.setBackground(
                new Color(245, 247, 250)
        );

        areaResultado.setText(
                "Esperando una prueba de conexión..."
        );

        JScrollPane scroll = new JScrollPane(
                areaResultado
        );

        panelResultado.add(
                etiquetaResultado, BorderLayout.NORTH
        );

        panelResultado.add(
                scroll, BorderLayout.CENTER
        );

        // =====================================
        // BOTON PROBAR CONEXION
        // =====================================

        botonProbar = new JButton("Probar conexión");

        botonProbar.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        botonProbar.setBackground(
                new Color(30, 100, 180)
        );

        botonProbar.setForeground(Color.WHITE);
        botonProbar.setFocusPainted(false);

        // ActionListener: ejecutar la prueba
        botonProbar.addActionListener(
                evento -> probarConexion()
        );

        JPanel panelBoton = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 25, 10)
        );

        panelBoton.add(botonProbar);

        // =====================================
        // ORGANIZACION DE LA VENTANA
        // =====================================

        JPanel contenido = new JPanel(
                new BorderLayout()
        );

        contenido.add(formulario, BorderLayout.NORTH);
        contenido.add(panelResultado, BorderLayout.CENTER);
        contenido.add(panelBoton, BorderLayout.SOUTH);

        add(contenido, BorderLayout.CENTER);
    }

    // =====================================
    // RESULTADO DE LA PRUEBA JDBC
    // =====================================

    private record ResultadoConexion(
            boolean exito,
            String mensaje
    ) {}

    // =====================================
    // METODO PARA PROBAR LA CONEXION
    // =====================================

    private void probarConexion() {

        String url = campoUrl.getText().trim();
        String usuario = campoUsuario.getText().trim();

        char[] caracteres = campoContrasena.getPassword();
        String contrasena = new String(caracteres);
        Arrays.fill(caracteres, '\0');

        // Validar el campo obligatorio
        if (url.isEmpty()) {
            mostrarResultado(
                    "FALLO DE CONEXIÓN\n\n" +
                            "Debes ingresar una URL JDBC.",
                    false
            );
            return;
        }

        botonProbar.setEnabled(false);

        areaResultado.setForeground(Color.DARK_GRAY);
        areaResultado.setText(
                "Validando conexión...\n" +
                        "Intentando acceder a la base de datos."
        );

        // Ejecutar la conexión sin bloquear la ventana
        SwingWorker<ResultadoConexion, Void> trabajador =
                new SwingWorker<>() {

                    @Override
                    protected ResultadoConexion doInBackground() {

                        try {

                            // Cargar explicitamente el driver SQLite
                            if (url.startsWith("jdbc:sqlite:")) {
                                Class.forName("org.sqlite.JDBC");
                            }

                            // Abrir y cerrar la conexion de forma segura
                            try (Connection conexion =
                                         url.startsWith("jdbc:sqlite:")
                                                 ? DriverManager.getConnection(url)
                                                 : DriverManager.getConnection(
                                                 url, usuario, contrasena
                                         )) {

                                String motor = conexion.getMetaData()
                                        .getDatabaseProductName();

                                return new ResultadoConexion(
                                        true,
                                        "CONEXIÓN EXITOSA\n\n" +
                                                "Se estableció la conexión " +
                                                "correctamente.\n\n" +
                                                "Motor: " + motor + "\n" +
                                                "URL: " + url + "\n\n" +
                                                "La conexión fue cerrada " +
                                                "de manera segura."
                                );
                            }

                        } catch (ClassNotFoundException e) {

                            return new ResultadoConexion(
                                    false,
                                    "FALLO DE CONEXIÓN\n\n" +
                                            "No se encontró el controlador JDBC.\n\n" +
                                            "Detalle técnico:\n" +
                                            e.getMessage()
                            );

                        } catch (SQLException e) {

                            return new ResultadoConexion(
                                    false,
                                    "FALLO DE CONEXIÓN\n\n" +
                                            "No fue posible conectar " +
                                            "con la base de datos.\n\n" +
                                            "Detalle técnico:\n" +
                                            e.getMessage() + "\n\n" +
                                            "SQLState: " + e.getSQLState() + "\n" +
                                            "Código de error: " + e.getErrorCode()
                            );

                        } catch (LinkageError e) {

                            return new ResultadoConexion(
                                    false,
                                    "FALLO AL CARGAR EL CONTROLADOR\n\n" +
                                            "Puede faltar una biblioteca " +
                                            "necesaria para JDBC.\n\n" +
                                            "Detalle técnico:\n" +
                                            e.toString()
                            );
                        }
                    }

                    @Override
                    protected void done() {

                        try {
                            ResultadoConexion resultado = get();

                            mostrarResultado(
                                    resultado.mensaje(),
                                    resultado.exito()
                            );

                        } catch (InterruptedException e) {

                            Thread.currentThread().interrupt();

                            mostrarResultado(
                                    "La prueba de conexión fue interrumpida.",
                                    false
                            );

                        } catch (ExecutionException e) {

                            mostrarResultado(
                                    "Error inesperado durante la prueba:\n" +
                                            e.getCause(),
                                    false
                            );

                        } finally {
                            botonProbar.setEnabled(true);
                        }
                    }
                };

        trabajador.execute();
    }

    // =====================================
    // MOSTRAR MENSAJES CON COLORES
    // =====================================

    private void mostrarResultado(
            String mensaje, boolean exito) {

        areaResultado.setForeground(
                exito
                        ? new Color(0, 130, 65)
                        : new Color(190, 30, 45)
        );

        areaResultado.setText(mensaje);
        areaResultado.setCaretPosition(0);
    }

    // =====================================
    // INICIO DE LA APLICACION
    // =====================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ConexionTester ventana =
                    new ConexionTester();

            ventana.setVisible(true);
        });
    }
}
