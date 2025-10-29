package SistemaBibliotecario.vistaLector;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LibroCard extends JPanel {
    private String libroId;
    private JLabel lblPlaceholder;
    private JLabel lblTitulo;
    private JLabel lblAutor;
    private boolean isSelected = false;

    private static final Color BORDER_COLOR_NORMAL = Color.GRAY;
    private static final Color BORDER_COLOR_SELECTED = new Color(19, 38, 76); // Dark blue
    private static final Border BORDER_NORMAL = BorderFactory.createLineBorder(BORDER_COLOR_NORMAL, 1);
    private static final Border BORDER_SELECTED = BorderFactory.createLineBorder(BORDER_COLOR_SELECTED, 2);

    public LibroCard(String libroId, String titulo, String autor) {
        this.libroId = libroId;
        setLayout(new BorderLayout(5, 5));
        setBorder(BORDER_NORMAL);
        setBackground(Color.WHITE);

        // Placeholder
        lblPlaceholder = new JLabel("X", SwingConstants.CENTER);
        lblPlaceholder.setFont(new Font("Arial", Font.BOLD, 80));
        lblPlaceholder.setOpaque(true);
        lblPlaceholder.setBackground(Color.LIGHT_GRAY);
        lblPlaceholder.setPreferredSize(new Dimension(150, 200)); // Placeholder size

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);

        lblTitulo = new JLabel("<html><b>" + titulo + "</b></html>");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblAutor = new JLabel(autor);
        lblAutor.setHorizontalAlignment(SwingConstants.CENTER);
        lblAutor.setForeground(Color.DARK_GRAY);

        infoPanel.add(lblTitulo);
        infoPanel.add(lblAutor);

        add(lblPlaceholder, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    public String getLibroId() {
        return libroId;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (isSelected) {
            setBorder(BORDER_SELECTED);
        } else {
            setBorder(BORDER_NORMAL);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }
}
