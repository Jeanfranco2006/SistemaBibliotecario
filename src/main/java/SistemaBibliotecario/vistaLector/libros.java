package SistemaBibliotecario.vistaLector;

import SistemaBibliotecario.Dao.LibroDAO;
import SistemaBibliotecario.Dao.PrestamoDAO;
import SistemaBibliotecario.Modelos.Libro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class libros extends JPanel {

    private LibroDAO libroDAO;
    private JPanel panelContenedorLibros;
    private JPanel paginationPanel;
    private JLabel lblPaginationStatus;
    private JButton btnPrev, btnNext, btnFirst, btnLast;
    private JButton btnPage1, btnPage2, btnPage3;
    private JButton btnInfo;
    private JButton btnSolicitar;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnLimpiarBusqueda;

    private int currentPage = 1;
    private final int pageSize = 6;
    private int totalLibros = 0;
    private int totalPages = 0;

    private LibroCard selectedCard = null;
    private final List<LibroCard> currentCards = new ArrayList<>();
    
    // Colores del tema
    private static final Color PRIMARY_COLOR = new Color(19, 38, 76);
    private static final Color SECONDARY_COLOR = new Color(41, 98, 255);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 252);

    public libros() {
        this.libroDAO = new LibroDAO();
        initComponents();
        loadBooks();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Panel principal con margen
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Top Bar mejorado
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Grid Panel mejorado
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel mejorado
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.setBackground(BACKGROUND_COLOR);

        // Título con icono
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel titleLabel = new JLabel("CATÁLOGO DE LIBROS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Panel de búsqueda mejorado
        JPanel searchPanel = createSearchPanel();

        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);

        // Campo de búsqueda mejorado
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText("Buscar por título o autor...");
        
        // Placeholder behavior
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals("Buscar por título o autor...")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setForeground(Color.GRAY);
                    txtBuscar.setText("Buscar por título o autor...");
                }
            }
        });

        // Enter para buscar
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBooks();
                }
            }
        });

        btnBuscar = createStyledButton("🔍 Buscar", SECONDARY_COLOR);
        btnBuscar.addActionListener(e -> searchBooks());

        btnLimpiarBusqueda = createStyledButton("✕", new Color(150, 150, 150));
        btnLimpiarBusqueda.addActionListener(e -> {
            txtBuscar.setText("");
            txtBuscar.setForeground(Color.GRAY);
            txtBuscar.setText("Buscar por título o autor...");
            currentPage = 1;
            loadBooks("");
        });

        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnLimpiarBusqueda);

        return searchPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);

        panelContenedorLibros = new JPanel(new GridLayout(0, 3, 20, 20));
        panelContenedorLibros.setBackground(BACKGROUND_COLOR);
        panelContenedorLibros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panelContenedorLibros);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(15, 15));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        // Action Buttons mejorados
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(BACKGROUND_COLOR);
        
        btnInfo = createStyledButton("ℹ️ Más Información", SECONDARY_COLOR);
        btnInfo.addActionListener(e -> onInfoClicked());
        
        btnSolicitar = createStyledButton("📋 Solicitar Préstamo", SUCCESS_COLOR);
        btnSolicitar.addActionListener(e -> onSolicitarClicked());
        
        actionPanel.add(btnInfo);
        actionPanel.add(btnSolicitar);

        // Pagination mejorada
        paginationPanel = createPaginationPanel();

        bottomPanel.add(paginationPanel, BorderLayout.WEST);
        bottomPanel.add(actionPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private JPanel createPaginationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(BACKGROUND_COLOR);

        btnFirst = createPaginationButton("⏮");
        btnPrev = createPaginationButton("◀");
        btnPage1 = createPaginationButton("1");
        btnPage2 = createPaginationButton("2");
        btnPage3 = createPaginationButton("3");
        btnNext = createPaginationButton("▶");
        btnLast = createPaginationButton("⏭");

        btnFirst.addActionListener(e -> goToPage(1));
        btnPrev.addActionListener(e -> goToPage(currentPage - 1));
        btnNext.addActionListener(e -> goToPage(currentPage + 1));
        btnLast.addActionListener(e -> goToPage(totalPages));

        btnPage1.addActionListener(e -> goToPage(Integer.parseInt(btnPage1.getText())));
        btnPage2.addActionListener(e -> goToPage(Integer.parseInt(btnPage2.getText())));
        btnPage3.addActionListener(e -> goToPage(Integer.parseInt(btnPage3.getText())));

        lblPaginationStatus = new JLabel("Cargando...");
        lblPaginationStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPaginationStatus.setForeground(new Color(100, 100, 100));
        lblPaginationStatus.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        panel.add(btnFirst);
        panel.add(btnPrev);
        panel.add(btnPage1);
        panel.add(btnPage2);
        panel.add(btnPage3);
        panel.add(btnNext);
        panel.add(btnLast);
        panel.add(lblPaginationStatus);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }

    private JButton createPaginationButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setPreferredSize(new Dimension(40, 30));
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_COLOR);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(240, 240, 240));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
        
        return btn;
    }

    private void goToPage(int page) {
        if (page >= 1 && page <= totalPages) {
            currentPage = page;
            String searchTerm = getSearchTerm();
            loadBooks(searchTerm);
        }
    }

    private String getSearchTerm() {
        String text = txtBuscar.getText();
        if (text.equals("Buscar por título o autor...")) {
            return "";
        }
        return text;
    }

    private void loadBooks() {
        loadBooks("");
    }

    private void loadBooks(String searchTerm) {
        panelContenedorLibros.removeAll();
        currentCards.clear();
        selectedCard = null;

        if (searchTerm.isEmpty()) {
            totalLibros = libroDAO.contarLibrosPorISBN();
        } else {
            totalLibros = libroDAO.contarLibros(searchTerm);
        }
        totalPages = Math.max(1, (int) Math.ceil((double) totalLibros / pageSize));
        
        // Ajustar página actual si excede el total
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<Libro> libros;
        if (searchTerm.isEmpty()) {
            libros = libroDAO.getLibrosPaginados(currentPage, pageSize);
        } else {
            libros = libroDAO.buscarLibros(searchTerm, currentPage, pageSize);
        }

        for (Libro libro : libros) {
            LibroCard card = new LibroCard(
                String.valueOf(libro.getIdLibro()),
                libro.getTitulo(),
                libro.getAutor()
            );
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onCardClicked(card);
                }
            });
            panelContenedorLibros.add(card);
            currentCards.add(card);
        }

        // Placeholders para mantener el grid
        int placeholders = pageSize - libros.size();
        for (int i = 0; i < placeholders; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(BACKGROUND_COLOR);
            panelContenedorLibros.add(emptyPanel);
        }

        updatePaginationControls();
        panelContenedorLibros.revalidate();
        panelContenedorLibros.repaint();
    }

    private void searchBooks() {
        currentPage = 1;
        String searchTerm = getSearchTerm();
        loadBooks(searchTerm);
    }

    private void onCardClicked(LibroCard clickedCard) {
        if (selectedCard != null) {
            selectedCard.setSelected(false);
        }
        selectedCard = clickedCard;
        selectedCard.setSelected(true);
    }

    private void updatePaginationControls() {
        int start = totalLibros > 0 ? (currentPage - 1) * pageSize + 1 : 0;
        int end = Math.min(start + pageSize - 1, totalLibros);
        lblPaginationStatus.setText("Mostrando " + start + "-" + end + " de " + totalLibros + " libro(s)");

        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);

        // Sistema de paginación inteligente
        updatePageButtons();
    }

    private void updatePageButtons() {
        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(totalPages, startPage + 2);
        
        // Ajustar si estamos cerca del final
        if (endPage - startPage < 2) {
            startPage = Math.max(1, endPage - 2);
        }

        JButton[] pageButtons = {btnPage1, btnPage2, btnPage3};
        
        for (int i = 0; i < 3; i++) {
            int pageNum = startPage + i;
            if (pageNum <= totalPages) {
                pageButtons[i].setText(String.valueOf(pageNum));
                pageButtons[i].setVisible(true);
                
                // Resaltar página actual
                if (pageNum == currentPage) {
                    pageButtons[i].setBackground(PRIMARY_COLOR);
                    pageButtons[i].setForeground(Color.WHITE);
                } else {
                    pageButtons[i].setBackground(Color.WHITE);
                    pageButtons[i].setForeground(PRIMARY_COLOR);
                }
            } else {
                pageButtons[i].setVisible(false);
            }
        }
    }

    private void onInfoClicked() {
        if (selectedCard != null) {
            Libro fullDetails = libroDAO.obtenerLibroPorId(
                Integer.parseInt(selectedCard.getLibroId())
            );
            if (fullDetails != null) {
                mostrarInformacionLibro(fullDetails);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un libro de la lista.",
                "Ningún libro seleccionado",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarInformacionLibro(Libro libro) {
        String mensaje = String.format(
            "<html><body style='width: 300px; font-family: Segoe UI;'>" +
            "<h2 style='color: #132642;'>%s</h2>" +
            "<p><b>Autor:</b> %s</p>" +
            "<p><b>ISBN:</b> %s</p>" +
            "<p><b>Stock total:</b> %d ejemplares</p>" +
            "<p><b>Disponibles:</b> <span style='color: %s;'><b>%d</b></span></p>" +
            "</body></html>",
            libro.getTitulo(),
            libro.getAutor(),
            libro.getIsbn(),
            libro.getStock(),
            libro.getDisponibles() > 0 ? "#4CAF50" : "#F44336",
            libro.getDisponibles()
        );

        JOptionPane.showMessageDialog(this,
            mensaje,
            "Información del Libro",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void onSolicitarClicked() {
        if (selectedCard == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un libro de la lista.",
                "Ningún libro seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Libro libro = libroDAO.obtenerLibroPorId(
            Integer.parseInt(selectedCard.getLibroId())
        );

        if (libro == null) {
            JOptionPane.showMessageDialog(this,
                "Error al obtener los detalles del libro.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (libro.getDisponibles() <= 0) {
            JOptionPane.showMessageDialog(this,
                "Lo sentimos, no hay ejemplares disponibles de este libro en este momento.",
                "Libro no disponible",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        SeleccionarBibliotecarioDialog dialog = new SeleccionarBibliotecarioDialog(
            (Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);

        String bibliotecarioDNI = dialog.getSelectedBibliotecarioDNI();

        if (bibliotecarioDNI != null) {
            int response = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea solicitar el préstamo de:\n\n" +
                "\"" + libro.getTitulo() + "\" - " + libro.getAutor() + "?",
                "Confirmar Solicitud de Préstamo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                String usuarioDNI = SistemaBibliotecario.Modelos.SesionActual.dni;
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                prestamoDAO.registrarPrestamo(usuarioDNI, libro.getIsbn(), bibliotecarioDNI);
                
                JOptionPane.showMessageDialog(this,
                    "¡Préstamo registrado exitosamente!\n" +
                    "Puede retirar el libro en la biblioteca.",
                    "Solicitud Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadBooks(getSearchTerm());
            }
        }
    }
}