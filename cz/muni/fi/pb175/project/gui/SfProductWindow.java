package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Machines;
import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Status;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SfProductWindow {
    protected JFrame frame;
    protected JButton manageMaterialButton;
    protected JButton backButton;
    protected JButton confirmButton;
    protected JPanel sfProductPanel;
    protected JTextArea descriptionTextArea;
    protected JTable usedMaterialTable;
    protected JComboBox<MaterialType> materialComboBox;
    protected JTextField projectNameTextField;
    protected JComboBox<Machines> machineComboBox;
    protected JTextField workBenchTextField;
    protected JTextField sfProductFinishDateTextField;
    protected JTextField projectFinishDateTextField;
    protected JCheckBox approvedCheckBox;
    protected JComboBox<Status> statusBox;
    protected JLabel sddSfProductLabel;

    private DefaultTableModel assignedMaterialTableModel;
    protected static final Color WRONG_COLOR = new Color(255, 104, 104);
    protected  final Color GOOD_COLOR = usedMaterialTable.getBackground();

    public SfProductWindow(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(sfProductPanel);

        frame.setMinimumSize(new Dimension(1000, 750));
        frame.setSize(1000, 750);

        frame.setLocationRelativeTo(null);

        statusBox.setModel(new DefaultComboBoxModel<>(Status.values()));
        materialComboBox.setModel(new DefaultComboBoxModel<>(MaterialType.values()));
        machineComboBox.setModel(new DefaultComboBoxModel<>(Machines.values()));
    }

    protected void showFrame() {
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        Object[] header = {"ID", "Typ", "Výška", "Šířka", "Délka"};

        assignedMaterialTableModel = new DefaultTableModel(header, 0);
        usedMaterialTable = new JTable(assignedMaterialTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 5; i++) {
            usedMaterialTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Populates assigned material table.
     */
    protected void populateTable(Database database, SfProduct sfProduct) {
        DefaultTableModel materialTableModel = (DefaultTableModel) usedMaterialTable.getModel();
        materialTableModel.setRowCount(0);

        List<Material> assignedMaterial = database.getAssignedMaterial(sfProduct.getId());
        for (Material material : assignedMaterial) {
            materialTableModel.addRow(material.asArray());
        }
    }
}
