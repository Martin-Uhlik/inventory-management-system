package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Machines;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Status;
import cz.muni.fi.pb175.project.Users;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Class represents window where user can set values for new sfProduct and store them to database.
 *
 * @author Martin Uhlik
 */
public class AddSfProduct extends SfProductWindow {
    public AddSfProduct(MainMenu app, Users currentUser) {
        super("Přidat požadavek na výrobu polotovaru");
        manageMaterialButton.setEnabled(false);

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
                    addSfProductToDatabase(app.getDatabase());
                    frame.dispose();
                    app.reloadSfProductTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                } catch (IllegalArgumentException ignored) {}

            }
        });

        showFrame();
    }

    /**
     * Gets values given by user and store them to database.
     *
     * @param database database where values should be stored
     */
    private void addSfProductToDatabase(Database database) throws SQLException {
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
            database.addSfProduct(new SfProduct(0,
                    projectName,
                    sfProductFinishDate,
                    projectFinishDate,
                    false,
                    workBench,
                    description,
                    Status.NOT_READY,
                    (MaterialType) materialComboBox.getSelectedItem(),
                    (Machines) machineComboBox.getSelectedItem()));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
