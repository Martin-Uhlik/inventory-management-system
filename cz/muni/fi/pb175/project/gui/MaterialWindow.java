package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaterialWindow {
    protected final JFrame frame;
    protected JButton backButton;
    protected JButton confirmButton;
    protected JTextArea descriptionTextArea;
    private JPanel materialWindowPanel;
    protected JTextField idField;
    protected JTextField xSizeField;
    protected JTextField sfProductField;
    protected JTextField ySizeField;
    protected JTextField zSizeField;
    protected JComboBox<MaterialType> materialBox;
    protected JLabel addMaterialLabel;

    protected static final Color WRONG_COLOR = new Color(255, 104, 104);
    protected  final Color GOOD_COLOR = ySizeField.getBackground();


    public MaterialWindow(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(materialWindowPanel);

        frame.setMinimumSize(new Dimension(1000, 750));
        frame.setSize(1000, 750);

        frame.setLocationRelativeTo(null);

        materialBox.setModel(new DefaultComboBoxModel<>(MaterialType.values()));
    }

    protected void showFrame() {
        frame.pack();
        frame.setVisible(true);
    }
}
