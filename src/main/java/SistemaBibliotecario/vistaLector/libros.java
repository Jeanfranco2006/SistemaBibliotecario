package SistemaBibliotecario.vistaLector;

import SistemaBibliotecario.Dao.LibroDAO;
import SistemaBibliotecario.Dao.PrestamoDAO;
import SistemaBibliotecario.Modelos.Libro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class libros extends javax.swing.JPanel {

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

    private int currentPage = 1;
    private final int pageSize = 6; // 3 columns, 2 rows
    private int totalLibros = 0;
    private int totalPages = 0;

    private LibroCard selectedCard = null;
    private final List<LibroCard> currentCards = new ArrayList<>();

    public libros() {
        this.libroDAO = new LibroDAO();
        initComponents();
        loadBooks();
    }

    private void initComponents() {
        // Main Panel Setup
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top Bar (Title and Search)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("CATÁLOGO DE LIBROS");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        txtBuscar = new javax.swing.JTextField("Buscar....", 25);
        btnBuscar = new javax.swing.JButton("Buscar");
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        btnBuscar.addActionListener(e -> searchBooks());

        // Grid Panel for Book Cards
        panelContenedorLibros = new JPanel(new GridLayout(0, 3, 15, 15)); // 3 columns, variable rows, with gaps
        panelContenedorLibros.setBackground(Color.WHITE);
        panelContenedorLibros.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        JScrollPane scrollPane = new JScrollPane(panelContenedorLibros);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for Actions and Pagination
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(Color.WHITE);

        // Action Buttons (+Info, +Solicitar)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        btnInfo = new javax.swing.JButton("+ Info");
        btnInfo.addActionListener(e -> onInfoClicked());
        btnSolicitar = new javax.swing.JButton("+ Solicitar");
        btnSolicitar.addActionListener(e -> onSolicitarClicked());
        actionPanel.add(btnInfo);
        actionPanel.add(btnSolicitar);

        // Pagination Controls
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paginationPanel.setBackground(Color.WHITE);
        btnFirst = new JButton("<<");
        btnPrev = new JButton("<");
        btnPage1 = new JButton("1");
        btnPage2 = new JButton("2");
        btnPage3 = new JButton("3");
        btnNext = new JButton(">");
        btnLast = new JButton(">>");
        lblPaginationStatus = new JLabel("Mostrando 0-0 de 0 libros");

        btnFirst.addActionListener(e -> { currentPage = 1; loadBooks(txtBuscar.getText()); });
        btnPrev.addActionListener(e -> { if (currentPage > 1) { currentPage--; loadBooks(txtBuscar.getText()); } });
        btnNext.addActionListener(e -> { if (currentPage < totalPages) { currentPage++; loadBooks(txtBuscar.getText()); } });
        btnLast.addActionListener(e -> { currentPage = totalPages; loadBooks(txtBuscar.getText()); });
        
        btnPage1.addActionListener(e -> { currentPage = Integer.parseInt(e.getActionCommand()); loadBooks(txtBuscar.getText()); });
        btnPage2.addActionListener(e -> { currentPage = Integer.parseInt(e.getActionCommand()); loadBooks(txtBuscar.getText()); });
        btnPage3.addActionListener(e -> { currentPage = Integer.parseInt(e.getActionCommand()); loadBooks(txtBuscar.getText()); });

        paginationPanel.add(btnFirst);
        paginationPanel.add(btnPrev);
        paginationPanel.add(btnPage1);
        paginationPanel.add(btnPage2);
        paginationPanel.add(btnPage3);
        paginationPanel.add(btnNext);
        paginationPanel.add(btnLast);
        paginationPanel.add(lblPaginationStatus);

        bottomPanel.add(paginationPanel, BorderLayout.CENTER);
        bottomPanel.add(actionPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadBooks() {
        loadBooks("");
    }

    private void loadBooks(String searchTerm) {
        // 1. Clear current view
        panelContenedorLibros.removeAll();
        currentCards.clear();
        selectedCard = null;

        // 2. Get total count and calculate pages
        if (searchTerm.isEmpty()) {
            totalLibros = libroDAO.contarLibrosPorISBN();
        } else {
            totalLibros = libroDAO.contarLibros(searchTerm);
        }
        totalPages = (int) Math.ceil((double) totalLibros / pageSize);

        // 3. Fetch books for the current page
        List<Libro> libros;
        if (searchTerm.isEmpty()) {
            libros = libroDAO.getLibrosPaginados(currentPage, pageSize);
        } else {
            libros = libroDAO.buscarLibros(searchTerm, currentPage, pageSize);
        }

        // 4. Create and add book cards
        for (Libro libro : libros) {
            LibroCard card = new LibroCard(String.valueOf(libro.getIdLibro()), libro.getTitulo(), libro.getAutor());
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onCardClicked(card);
                }
            });
            panelContenedorLibros.add(card);
            currentCards.add(card);
        }
        
        // Add empty placeholders if the last page is not full
        int placeholders = pageSize - libros.size();
        for (int i = 0; i < placeholders; i++) {
            panelContenedorLibros.add(new JPanel()); // Add an empty, invisible panel
        }

        // 5. Update UI
        updatePaginationControls();
        panelContenedorLibros.revalidate();
        panelContenedorLibros.repaint();
    }

    private void searchBooks() {
        currentPage = 1;
        loadBooks(txtBuscar.getText());
    }

    private void onCardClicked(LibroCard clickedCard) {
        if (selectedCard != null) {
            selectedCard.setSelected(false);
        }
        selectedCard = clickedCard;
        selectedCard.setSelected(true);
    }

    private void updatePaginationControls() {
        // Update status label
        int start = (currentPage - 1) * pageSize + 1;
        int end = Math.min(start + pageSize - 1, totalLibros);
        lblPaginationStatus.setText("Mostrando " + start + "-" + end + " de " + totalLibros + " libros");

        // Enable/disable buttons
        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);
        
        // Update page number buttons (basic implementation)
        btnPage1.setVisible(totalPages >= 1);
        btnPage2.setVisible(totalPages >= 2);
        btnPage3.setVisible(totalPages >= 3);
    }
    
    private void onInfoClicked() {
        if (selectedCard != null) {
            // Here you would query the DB for full book details using selectedCard.getLibroId()
            // For now, we just show a dialog
            Libro fullDetails = libroDAO.obtenerLibroPorId(Integer.parseInt(selectedCard.getLibroId()));
            if (fullDetails != null) {
                 JOptionPane.showMessageDialog(this, 
                    "ID: " + fullDetails.getIdLibro() + "\n" +
                    "Título: " + fullDetails.getTitulo() + "\n" +
                    "Autor: " + fullDetails.getAutor() + "\n" +
                    "Stock: " + fullDetails.getStock() + "\n" +
                    "Disponibles: " + fullDetails.getDisponibles(),
                    "Información del Libro",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onSolicitarClicked() {
        if (selectedCard != null) {
            SeleccionarBibliotecarioDialog dialog = new SeleccionarBibliotecarioDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);

            String bibliotecarioDNI = dialog.getSelectedBibliotecarioDNI();

            if (bibliotecarioDNI != null) {
                int response = JOptionPane.showConfirmDialog(this, "¿Está seguro que quiere solicitar este libro?", "Confirmar Solicitud", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    Libro libro = libroDAO.obtenerLibroPorId(Integer.parseInt(selectedCard.getLibroId()));
                    if (libro != null) {
                        String usuarioDNI = SistemaBibliotecario.Modelos.SesionActual.dni;
                        String libroISBN = libro.getIsbn();

                        PrestamoDAO prestamoDAO = new PrestamoDAO();
                        prestamoDAO.registrarPrestamo(usuarioDNI, libroISBN, bibliotecarioDNI);
                        loadBooks(); // Recargar libros para actualizar stock
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al obtener los detalles del libro.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}