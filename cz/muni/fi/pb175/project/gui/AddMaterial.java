package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.Users;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Class represents window where user can set values for new material and store them to database.
 *
 * @author Martin Uhlik
 */
public class AddMaterial extends MaterialWindow {


    /**
     * Object constructor.
     *
     * @param app main menu TODO
     * @param currentUser currently logged in user
     */
    public AddMaterial(MainMenu app, Users currentUser) {
        super("Přidat materiál");
        idField.setEditable(false);

        //functionality of back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(frame, "Zahodit změny?");
                if (input == 0) {
                    frame.dispose();
                }
            }
        });

        //functionality of confirm button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addMaterialToDatabase(app.getDatabase());
                    frame.dispose();
                    app.reloadMaterialTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                } catch (IllegalArgumentException ignore) {}

            }
        });

        showFrame();
    }

    /**
     * Gets values given by user and store them to database.
     *
     * @param database database where values should be stored
     */
    private void addMaterialToDatabase(Database database) throws SQLException {
        MaterialType selectedItem = (MaterialType) materialBox.getSelectedItem();
        int x = 0;
        int y = 0;
        int z = 0;
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
        if (add) {
            database.addMaterial(new Material(0, selectedItem, x, y, z, null, description));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
