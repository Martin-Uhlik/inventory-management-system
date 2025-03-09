package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Class represents window where user can change values of existing material and update database.
 *
 * @author Martin Uhlik
 */
public class EditMaterial extends MaterialWindow {
    public EditMaterial(Material material, MainMenu app, ShowMaterialInfo showFrame) {
        super("Upravit materiál");
        idField.setEditable(false);
        sfProductField.setEditable(true);
        addMaterialLabel.setText("Upravit materiál");

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
                    Material changedMat = changeMaterialInDatabase(app.getDatabase(), material.getId());
                    frame.dispose();
                    app.reloadMaterialTable();
                    showFrame.setValues(changedMat);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                }
            }
        });

        setValues(material);

        showFrame();
    }

    /**
     * Write values from material to corresponding GUI elements.
     *
     * @param material material of which ve want to write out the values
     */
    private void setValues(Material material) {
        descriptionTextArea.setText(material.getDescription());
        idField.setText(material.getId().toString());
        xSizeField.setText(material.getX().toString());
        if (material.getSfProductAssigned() != null) {
            sfProductField.setText(material.getSfProductAssigned().toString());
        }
        ySizeField.setText(material.getY().toString());
        zSizeField.setText(material.getZ().toString());
        materialBox.setSelectedItem(material.getMaterialType());
    }

    /**
     * Gets values given by user and store them to database.
     *
     * @param database database where values should be stored
     */
    private Material changeMaterialInDatabase(Database database, int id) throws SQLException{
        MaterialType selectedItem = (MaterialType) materialBox.getSelectedItem();
        int x = 0;
        int y = 0;
        int z = 0;
        Integer sfProduct;
        boolean add = true;
        try {
            x = Integer.parseInt(xSizeField.getText());
            xSizeField.setBackground(GOOD_COLOR);
        } catch (NumberFormatException ex) {
            xSizeField.setBackground(WRONG_COLOR);
            add = false;
        }
        try {
            y = Integer.parseInt(ySizeField.getText());
            ySizeField.setBackground(GOOD_COLOR);
        } catch (NumberFormatException ex) {
            ySizeField.setBackground(WRONG_COLOR);
            add = false;
        }
        try {
            z = Integer.parseInt(zSizeField.getText());
            zSizeField.setBackground(GOOD_COLOR);
        } catch (NumberFormatException ex) {
            zSizeField.setBackground(WRONG_COLOR);
            add = false;
        }
        String description = descriptionTextArea.getText();
        if (description.length() > 255) {
            descriptionTextArea.setBackground(WRONG_COLOR);
            add = false;
        } else {
            descriptionTextArea.setBackground(GOOD_COLOR);
        }
        try {
            sfProduct = Integer.parseInt(sfProductField.getText());
        } catch (NumberFormatException ex) {
            sfProduct = null;
        }

        if (add) {
            Material changedMat = new Material(id, selectedItem, x, y, z, sfProduct, description);
            database.editMaterial(changedMat);
            return changedMat;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
