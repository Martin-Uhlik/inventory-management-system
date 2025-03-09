package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Machines;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Status;
import cz.muni.fi.pb175.project.database.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Class representing frame for changing status of product.
 *
 * @author Martin Uhlik
 */
public class ChangeStatus {
    private JFrame frame;
    private JButton backButton;
    private JButton confirmButton;
    private JComboBox<Status> statusComboBox;
    private JPanel statusPanel;

    /**
     * Object constructor.
     */
    public ChangeStatus(MainMenu app, SfProduct sfProduct, ShowSfProductInfo infoWindow) throws SQLException {
        frame = new JFrame("Změnit status");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(statusPanel);

        frame.setSize(400, 250);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        statusComboBox.setModel(new DefaultComboBoxModel<>(Status.values()));
        statusComboBox.setSelectedItem(sfProduct.getStatus());

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
                    changeSfProductStatus(app.getDatabase(), sfProduct);
                    frame.dispose();
                    infoWindow.setValues(sfProduct);
                    app.reloadSfProductTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                } catch (IllegalArgumentException ignore) {}

            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Gets values given by user and store them to database.
     *
     * @param database database where values should be stored
     */
    private void changeSfProductStatus(Database database, SfProduct sfProduct) throws SQLException{
        sfProduct.setStatus((Status) statusComboBox.getSelectedItem());
        database.editSfProduct(sfProduct);
    }
}
