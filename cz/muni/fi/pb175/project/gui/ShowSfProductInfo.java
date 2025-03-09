package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Users;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author Martin Uhlik
 */
public class ShowSfProductInfo extends SfProductWindow {

    public ShowSfProductInfo(SfProduct sfProduct, MainMenu app) {
        super("Podrobnosti o materiálu");
        ShowSfProductInfo self = this;

        sddSfProductLabel.setText("Zvolený požadavek na výrobu polotovaru");

        //back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //change material button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditSfProduct(sfProduct, app, self);
            }
        });

        backButton.setText("Zpět");
        confirmButton.setText("Upravit");
        manageMaterialButton.setText("Změnit status");

        if (app.getCurrentUser() == Users.OPERATOR) {
            confirmButton.setEnabled(false);
        }

        descriptionTextArea.setEditable(false);
        materialComboBox.setEditable(false);
        machineComboBox.setEditable(false);
        workBenchTextField.setEditable(false);
        projectNameTextField.setEditable(false);
        sfProductFinishDateTextField.setEditable(false);
        projectFinishDateTextField.setEditable(false);
        statusBox.setEditable(false);
        approvedCheckBox.setEnabled(false);

        setValues(sfProduct);
        populateTable(app.getDatabase(), sfProduct);

        //cahnge status button
        manageMaterialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ChangeStatus(app, sfProduct, self);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                }

            }
        });

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
}
