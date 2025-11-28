package SistemaBibliotecario.vistaLector;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LibroCard extends JPanel {
    private String libroId;
    private JLabel lblPlaceholder;
    private JLabel lblTitulo;
    private JLabel lblAutor;
    private boolean isSelected = false;
    private boolean isHovered = false;

    // Colores mejorados
    private static final Color BORDER_COLOR_NORMAL = new Color(220, 220, 220);
    private static final Color BORDER_COLOR_HOVER = new Color(100, 100, 100);
    private static final Color BORDER_COLOR_SELECTED = new Color(19, 38, 76);
    private static final Color BACKGROUND_NORMAL = Color.WHITE;
    private static final Color BACKGROUND_HOVER = new Color(248, 248, 248);
    private static final Color BACKGROUND_SELECTED = new Color(240, 243, 250);
    private static final Color PLACEHOLDER_BG = new Color(230, 230, 240);
    private static final Color PLACEHOLDER_FG = new Color(180, 180, 200);

    public LibroCard(String libroId, String titulo, String autor) {
        this.libroId = libroId;
        setLayout(new BorderLayout(0, 0));
        setBorder(createCompoundBorder(BORDER_COLOR_NORMAL, 2));
        setBackground(BACKGROUND_NORMAL);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Agregar padding interno
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setOpaque(false);

        // Placeholder con diseño mejorado
        lblPlaceholder = new JLabel("📚", SwingConstants.CENTER);
        lblPlaceholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblPlaceholder.setOpaque(true);
        lblPlaceholder.setBackground(PLACEHOLDER_BG);
        lblPlaceholder.setForeground(PLACEHOLDER_FG);
        lblPlaceholder.setPreferredSize(new Dimension(180, 220));
        lblPlaceholder.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));

        // Info Panel con mejor espaciado
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Título con mejor tipografía
        lblTitulo = new JLabel("<html><div style='text-align: center;'><b>" + 
                              truncateText(titulo, 50) + "</b></div></html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Autor con estilo sutil
        lblAutor = new JLabel("<html><div style='text-align: center;'>" + 
                             truncateText(autor, 40) + "</div></html>");
        lblAutor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAutor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAutor.setHorizontalAlignment(SwingConstants.CENTER);
        lblAutor.setForeground(new Color(100, 100, 100));

        infoPanel.add(lblTitulo);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblAutor);

        contentPanel.add(lblPlaceholder, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);

        // Efectos hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    isHovered = true;
                    updateAppearance();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                updateAppearance();
            }
        });
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

    private Border createCompoundBorder(Color color, int thickness) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, thickness),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    private void updateAppearance() {
        if (isSelected) {
            setBorder(createCompoundBorder(BORDER_COLOR_SELECTED, 3));
            setBackground(BACKGROUND_SELECTED);
        } else if (isHovered) {
            setBorder(createCompoundBorder(BORDER_COLOR_HOVER, 2));
            setBackground(BACKGROUND_HOVER);
        } else {
            setBorder(createCompoundBorder(BORDER_COLOR_NORMAL, 2));
            setBackground(BACKGROUND_NORMAL);
        }
        repaint();
    }

    public String getLibroId() {
        return libroId;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        updateAppearance();
    }

    public boolean isSelected() {
        return isSelected;
    }

    // Método opcional para actualizar la imagen del libro si tienes portadas
    public void setBookCover(ImageIcon cover) {
        if (cover != null) {
            Image scaledImage = cover.getImage().getScaledInstance(
                180, 220, Image.SCALE_SMOOTH);
            lblPlaceholder.setIcon(new ImageIcon(scaledImage));
            lblPlaceholder.setText("");
        }
    }
}