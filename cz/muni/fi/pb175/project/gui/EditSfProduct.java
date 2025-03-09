package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Machines;
import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Status;
import cz.muni.fi.pb175.project.Users;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Class represents window where user can change values of existing material and update database.
 *
 * @author Martin Uhlik
 */
public class EditSfProduct extends SfProductWindow {

    public EditSfProduct(SfProduct sfProduct, MainMenu app, ShowSfProductInfo showFrame) {
        super("Upravit požadavek na materiál");
        SfProductWindow self = this;
        sddSfProductLabel.setText("Upravit materiál");

        statusBox.setEnabled(true);
        if (app.getCurrentUser() == Users.MANAGER) {
            approvedCheckBox.setEnabled(true);
        }

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
                try {
                    SfProduct changedSfProduct = changeSfProductInDatabase(app.getDatabase(), sfProduct.getId());
                    frame.dispose();
                    app.reloadSfProductTable();
                    showFrame.setValues(changedSfProduct);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                } catch (IllegalArgumentException ignored) {}
            }
        });

        manageMaterialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AssignMaterial(app.getDatabase(), sfProduct, self);
            }
        });

        setValues(sfProduct);
        populateTable(app.getDatabase(), sfProduct);

        showFrame();
    }


    /**
     *
     * @param sfProduct
     */
    public void setValues(SfProduct sfProduct) {
        descriptionTextArea.setText(sfProduct.getDescription());
        materialComboBox.setSelectedItem(sfProduct.getMaterialType());
        //idField.setText(material.getId().toString()); TODO no id?
        machineComboBox.setSelectedItem(sfProduct.getMachine());
        workBenchTextField.setText(sfProduct.getWorkBench().toString());
        projectNameTextField.setText(sfProduct.getProjectName());
        sfProductFinishDateTextField.setText(sfProduct.getSfProductFinishDate());
        projectFinishDateTextField.setText(sfProduct.getProjectFinishDate());
        statusBox.setSelectedItem(sfProduct.getStatus());
        approvedCheckBox.setSelected(sfProduct.isApproved());
    }

    /**
     * Gets values given by user and store them to database.
     *
     * @param database database where values should be stored
     */
    private SfProduct changeSfProductInDatabase(Database database, int id) throws SQLException{//TODO probably some fixes
        boolean add = true;
        int workBench = 0;
        String description = descriptionTextArea.getText();
        String projectName = projectNameTextField.getText();
        if (projectName.length() == 0) {
            add = false;
            projectNameTextField.setBackground(WRONG_COLOR);
        } else {
            projectNameTextField.setBackground(GOOD_COLOR);
        }
        String sfProductFinishDate = sfProductFinishDateTextField.getText();
        if (sfProductFinishDate.length() == 0) {
            add = false;
            sfProductFinishDateTextField.setBackground(WRONG_COLOR);
        } else {
            sfProductFinishDateTextField.setBackground(GOOD_COLOR);
        }
        String projectFinishDate = projectFinishDateTextField.getText();
        if (projectFinishDate.length() == 0) {
            add = false;
            projectFinishDateTextField.setBackground(WRONG_COLOR);
        } else {
            projectFinishDateTextField.setBackground(GOOD_COLOR);

        }
        try {
            workBench = Integer.parseInt(workBenchTextField.getText());
            workBenchTextField.setBackground(GOOD_COLOR);
        } catch (NumberFormatException ex) {
            workBenchTextField.setBackground(WRONG_COLOR);
            add = false;
        }
        if (description.length() > 255) {
            descriptionTextArea.setBackground(WRONG_COLOR);
            add = false;
        } else {
            descriptionTextArea.setBackground(GOOD_COLOR);
        }
        if (add) {
            SfProduct changedSfProduct = new SfProduct(0,
                    projectName,
                    sfProductFinishDate,
                    projectFinishDate,
                    approvedCheckBox.isSelected(),
                    workBench,
                    description,
                    (Status) statusBox.getSelectedItem(),
                    (MaterialType) materialComboBox.getSelectedItem(),
                    (Machines) machineComboBox.getSelectedItem());
            database.editSfProduct(changedSfProduct);
            return changedSfProduct;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
