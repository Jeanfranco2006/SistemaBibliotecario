package SistemaBibliotecario.VistaLogin;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import SistemaBibliotecario.Dao.UsuarioDAO;
import SistemaBibliotecario.Modelos.SesionActual;
import SistemaBibliotecario.Modelos.Usuario;
import SistemaBibliotecario.VistaAdmin.*;
import SistemaBibliotecario.vistaLector.*;
import SistemaBibliotecario.vistaBlibliotecario.*;

public class login extends JFrame {

    // --- Componentes ---
    private JPanel panelPrincipal, panelIzquierdo, panelDerecho;
    private JTextField txtDni;
    private JPasswordField txtContrasena;
    private JButton btnIngresar, btnRegistrarse;
    private JLabel lblMostrarPassword;

    // --- Paleta de Colores (Solo para el Login) ---
    private final Color COLOR_PRIMARY = new Color(20, 40, 80); // Azul oscuro
    private final Color COLOR_HOVER = new Color(40, 70, 120); // Hover suave
    private final Color COLOR_ACCENT = new Color(52, 152, 219); // Borde al escribir
    private final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    private final Color COLOR_BORDER_GRAY = new Color(200, 200, 200);

    // Fuentes
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public login() {
        initComponents();
        setTitle("Acceso al Sistema");
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Panel Principal
        panelPrincipal = new JPanel(new GridLayout(1, 2));
        panelPrincipal.setPreferredSize(new Dimension(950, 550));

        // Construir paneles
        crearPanelIzquierdo();
        crearPanelDerecho();

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);

        add(panelPrincipal);
        pack();
    }

    // -------------------------------------------------------------------------
    // PANEL IZQUIERDO (Decorativo)
    // -------------------------------------------------------------------------
    private void crearPanelIzquierdo() {
        panelIzquierdo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Gradiente oscuro solo para el login
                GradientPaint gp = new GradientPaint(0, 0, COLOR_PRIMARY, 0, getHeight(), new Color(10, 20, 40));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(new EmptyBorder(0, 40, 0, 40));

        panelIzquierdo.add(Box.createVerticalGlue());

        agregarTextoCentrado(panelIzquierdo, "📚", new Font("Segoe UI Emoji", Font.PLAIN, 90));
        panelIzquierdo.add(Box.createRigidArea(new Dimension(0, 20)));
        agregarTextoCentrado(panelIzquierdo, "SISTEMA", new Font("Segoe UI", Font.BOLD, 36));
        agregarTextoCentrado(panelIzquierdo, "BIBLIOTECARIO", new Font("Segoe UI", Font.PLAIN, 28));

        panelIzquierdo.add(Box.createVerticalGlue());
    }

    private void agregarTextoCentrado(JPanel panel, String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
    }

    // -------------------------------------------------------------------------
    // PANEL DERECHO (Formulario)
    // -------------------------------------------------------------------------
    private void crearPanelDerecho() {
        panelDerecho = new JPanel();
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(COLOR_TEXT_DARK);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Inicia sesión para continuar");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Inputs ---
        JLabel lblDni = new JLabel("Usuario / DNI");
        lblDni.setFont(FONT_BOLD);
        lblDni.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDni = new JTextField();
        configurarCampoTexto(txtDni);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(FONT_BOLD);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Contenedor password
        JPanel panelPassContainer = new JPanel(new BorderLayout());
        panelPassContainer.setBackground(Color.WHITE);
        panelPassContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtContrasena = new JPasswordField();
        configurarCampoTexto(txtContrasena);

        lblMostrarPassword = new JLabel("👁");
        lblMostrarPassword.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblMostrarPassword.setBorder(new EmptyBorder(0, 10, 0, 0));
        lblMostrarPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblMostrarPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                alternarVisibilidadPassword();
            }
        });

        panelPassContainer.add(txtContrasena, BorderLayout.CENTER);
        panelPassContainer.add(lblMostrarPassword, BorderLayout.EAST);
        panelPassContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // --- Botones (Ambos oscuros y mismo estilo) ---
        btnIngresar = crearBoton("INGRESAR");
        btnIngresar.addActionListener(this::btnIngresarActionPerformed);

        btnRegistrarse = crearBoton("REGISTRARME");
        btnRegistrarse.addActionListener(this::btnRegistrarseActionPerformed);

        // --- Armado ---
        panelDerecho.add(Box.createVerticalGlue());
        panelDerecho.add(lblTitulo);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(lblSub);
        panelDerecho.add(Box.createVerticalStrut(40));

        panelDerecho.add(lblDni);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(txtDni);
        panelDerecho.add(Box.createVerticalStrut(20));

        panelDerecho.add(lblPass);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(panelPassContainer);
        panelDerecho.add(Box.createVerticalStrut(40));

        panelDerecho.add(btnIngresar);
        panelDerecho.add(Box.createVerticalStrut(15));
        panelDerecho.add(btnRegistrarse);
        panelDerecho.add(Box.createVerticalGlue());
    }

    /**
     * Crea botones oscuros uniformes.
     */
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARY); // Fondo Oscuro
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFocusPainted(false);
        // Quitamos el borde pintado por defecto para que se vea plano y moderno
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hover solo para ESTOS botones del login
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
            }
        });
        return btn;
    }

    private void configurarCampoTexto(JComponent campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_ACCENT, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            }
        });
    }

    private void alternarVisibilidadPassword() {
        if (txtContrasena.getEchoChar() == '●') {
            txtContrasena.setEchoChar((char) 0);
            lblMostrarPassword.setForeground(COLOR_ACCENT);
        } else {
            txtContrasena.setEchoChar('●');
            lblMostrarPassword.setForeground(Color.BLACK);
        }
    }

    // --- LÓGICA ---
    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        String dni = txtDni.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        // 1. Validar que no estén vacíos
        if (dni.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // 2. Validar credenciales
        // Al ejecutarse esto, tu DAO ya llenó 'SesionActual.nombre' con el nombre
        // completo
        Usuario usuario = usuarioDAO.validarLogin(dni, contrasena);

        if (usuario == null) {
            mostrarMensaje("Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Actualizar acceso
        usuarioDAO.actualizarUltimoAcceso(dni);

        // 4. Guardar datos restantes en Sesión
        SesionActual.dni = dni;
        SesionActual.rol = usuario.getRol();
        // Nota: SesionActual.nombre ya fue guardado automáticamente por tu DAO

        // ✅ 5. MOSTRAR EL MENSAJE QUE QUIERES (Aquí está la magia)
        String mensaje = "¡Bienvenido al Sistema!\n\n" +
                "👤 Usuario: " + SesionActual.nombre + "\n" +
                "🔑 Rol: " + usuario.getRol();

        mostrarMensaje(mensaje, "Acceso Correcto", JOptionPane.INFORMATION_MESSAGE);

        // 6. Redirigir según rol
        switch (usuario.getRol().toLowerCase()) {
            case "administrador":
                abrirVentana(new SistemaBibliotecario.VistaAdmin.inicio(), "Administrador");
                break;
            case "bibliotecario":
                abrirVentana(new SistemaBibliotecario.vistaBlibliotecario.inicio(), "Bibliotecario");
                break;
            case "lector":
                abrirVentana(new SistemaBibliotecario.vistaLector.InicioPanel(), "Lector");
                break;
            default:
                mostrarMensaje("Rol desconocido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnRegistrarseActionPerformed(java.awt.event.ActionEvent evt) {
        // Asumiendo que existe tu clase Register
        JFrame frame = new JFrame("Registro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Register());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }

    private void abrirVentana(JPanel panelContenido, String titulo) {
        JFrame frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelContenido);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public static void main(String args[]) {
        // ⚠️ AQUÍ ESTÁ EL CAMBIO IMPORTANTE:
        // He eliminado el 'UIManager.setLookAndFeel(...)'
        // Esto evita que cambie el estilo de tus botones dentro del sistema.

        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }
}