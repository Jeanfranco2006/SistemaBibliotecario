package SistemaBibliotecario.vistaLector;

import SistemaBibliotecario.Dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SeleccionarBibliotecarioDialog extends JDialog {

    private JTable table;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private String selectedBibliotecarioDNI;

    public SeleccionarBibliotecarioDialog(Frame parent) {
        super(parent, "Seleccionar Bibliotecario", true);
        initComponents();
        loadBibliotecarios();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 300);
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAceptar.addActionListener(e -> onAceptar());
        btnCancelar.addActionListener(e -> onCancelar());
    }

    private void loadBibliotecarios() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Object[]> bibliotecarios = usuarioDAO.getUsuariosPorRol("bibliotecario");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("DNI");
        model.addColumn("Nombre");
        model.addColumn("Rol");

        for (Object[] bibliotecario : bibliotecarios) {
            model.addRow(bibliotecario);
        }

        table.setModel(model);
    }

    private void onAceptar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            selectedBibliotecarioDNI = (String) table.getValueAt(selectedRow, 0);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un bibliotecario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancelar() {
        selectedBibliotecarioDNI = null;
        dispose();
    }

    public String getSelectedBibliotecarioDNI() {
        return selectedBibliotecarioDNI;
    }
}
