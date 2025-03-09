package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.Users;
import cz.muni.fi.pb175.project.database.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Martin Uhlik
 */
public class ShowMaterialInfo extends MaterialWindow {

    public ShowMaterialInfo(Material material, MainMenu app) {
        super("Podrobnosti o materiálu");
        ShowMaterialInfo self = this;

        addMaterialLabel.setText("Zvolený materiál");

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
                new EditMaterial(material, app, self);
            }
        });

        backButton.setText("Zpět");
        confirmButton.setText("Upravit");

        if (app.getCurrentUser() == Users.OPERATOR) {
            confirmButton.setEnabled(false);
        }

        descriptionTextArea.setEditable(false);
        idField.setEditable(false);
        xSizeField.setEditable(false);
        sfProductField.setEditable(false);
        ySizeField.setEditable(false);
        zSizeField.setEditable(false);
        materialBox.setEditable(false);
        setValues(material);

        showFrame();
    }

    public void setValues(Material material) {
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
}
