package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Martin Uhlik
 */
public class AssignMaterial {//TODO
    protected final JFrame frame;
    private JPanel assignMaterilPanel;
    private JTable assignedMaterialtable;
    private JTable availableMaterialtable;
    private JButton backButton;
    private JButton confirmButton;
    private JButton addButton;
    private JButton removeButton;

    private DefaultTableModel assignedMaterialTableModel;
    private DefaultTableModel availableMaterialtableModel;

    public AssignMaterial(Database database, SfProduct sfProduct, SfProductWindow parent) {
        frame = new JFrame("Přiřadit materiál k polotovaru");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(assignMaterilPanel);

        frame.setMinimumSize(new Dimension(1000, 750));
        frame.setSize(1000, 750);

        frame.setLocationRelativeTo(null);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(frame, "Zahodit změny?");
                if (input == 0) {
                    frame.dispose();
                }
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO confirm changes
                try {
                    for (int i = 0; i < assignedMaterialtable.getRowCount(); i++) {
                        database.setAssignedSfProduct((Integer) assignedMaterialtable.getValueAt(i,
                                0), sfProduct.getId());
                    }
                    for (int i = 0; i < availableMaterialtable.getRowCount(); i++) {
                        database.setAssignedSfProduct((Integer) availableMaterialtable.getValueAt(i,
                                0), null);
                    }
                    database.loadMaterial(null);
                    parent.populateTable(database, sfProduct);
                    frame.dispose();
                } catch (SQLException ex) {
                    //TODO
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = availableMaterialtable.getSelectedRow();
                if (selectedRow != -1) {
                    Integer id = (Integer) availableMaterialtable.getValueAt(selectedRow, 0);
                    Material currentMaterial = database.getMaterialWithId(id);
                    assignedMaterialTableModel.addRow(currentMaterial.asArray());
                    availableMaterialtableModel.removeRow(selectedRow);
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = assignedMaterialtable.getSelectedRow();
                if (selectedRow != -1) {
                    Integer id = (Integer) assignedMaterialtable.getValueAt(selectedRow, 0);
                    Material currentMaterial = database.getMaterialWithId(id);
                    availableMaterialtableModel.addRow(currentMaterial.asArray());
                    assignedMaterialTableModel.removeRow(selectedRow);
                }
            }
        });

        populateAssignedTable(database, sfProduct);
        populateUnassignedTable(database);

        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        Object[] header = {"ID", "Typ", "Výška", "Šířka", "Délka"};

        assignedMaterialTableModel = new DefaultTableModel(header, 0);
        assignedMaterialtable = new JTable(assignedMaterialTableModel) {
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
            assignedMaterialtable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        availableMaterialtableModel = new DefaultTableModel(header, 0);
        availableMaterialtable = new JTable(availableMaterialtableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 5; i++) {
            availableMaterialtable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Populates assigned material table.
     */
    private void populateUnassignedTable(Database database) {
        DefaultTableModel materialTableModel = (DefaultTableModel) availableMaterialtable.getModel();
        materialTableModel.setRowCount(0);

        List<Material> unassignedMaterial = database.getUnassignedMaterial();
        for (Material material : unassignedMaterial) {
            materialTableModel.addRow(material.asArray());
        }
    }

    /**
     * Populates assigned material table.
     */
    private void populateAssignedTable(Database database, SfProduct sfProduct) {
        DefaultTableModel materialTableModel = (DefaultTableModel) assignedMaterialtable.getModel();
        materialTableModel.setRowCount(0);

        List<Material> unassignedMaterial = database.getAssignedMaterial(sfProduct.getId());
        for (Material material : unassignedMaterial) {
            materialTableModel.addRow(material.asArray());
        }
    }
}
