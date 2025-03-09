package cz.muni.fi.pb175.project.gui;

import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Users;
import cz.muni.fi.pb175.project.database.Database;
import cz.muni.fi.pb175.project.database.DatabaseConfiguration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Class represents Main window of application.
 *
 * @author Martin Uhlik
 */
public class MainMenu {
    private final MainMenu self;
    private final JFrame frame;
    private JPanel menuPanel;
    private Database database;

    private JTabbedPane menuPane;
    private JButton addMaterialButton;
    private JPanel materialPanel;
    private JPanel sfProductPanel;
    private JPanel changeUserPanel;
    private JButton removeMaterialButton;
    private JButton showMaterialInfoButton;
    private JTextField findMaterialIdTextField;
    private JTable materialTable;
    private JButton addSfProductButton;
    private JButton removeSfProductButton;
    private JButton showSfProductInfoButton;
    private JTextField findSfProductIdTextField;
    private JTable sfProductTable;
    private final CardLayout cl = new CardLayout();

    private DefaultTableModel materialTableModel;
    private DefaultTableModel sfProductTableModel;
    private Integer findMaterialId = null;
    private Integer findSfProductId = null;
    private final Users currentUser;


    /**
     * Object constructor. Creates new window with two cards.
     *
     * @param title title of window frame
     * @param login original login screen
     * @param currentUser currently logged in user
     */
    public MainMenu(String title, LoginScreen login, Users currentUser) {
        self = this;
        frame = new JFrame(title);

        frame.setContentPane(menuPanel);

        frame.setMinimumSize(new Dimension(1000, 750));
        frame.setSize(1000, 750);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.currentUser = currentUser;
        materialTab();
        sfProductTab();

        changeUserPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                frame.dispose();
                login.showLogin();
            }
        });

        try {
            database = new Database(new DatabaseConfiguration());
            reloadMaterialTable();
            frame.pack();
            frame.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
            frame.dispose();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Problém s ovladačem databáze!");
            frame.dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Soubor database.conf je poškozen!");
            frame.dispose();
        }
    }

    /**
     * Gui logic in material tab.
     *
     * @param currentUser currently logged in user
     */
    private void materialTab() {
        materialPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                reloadMaterialTable();
            }
        });

        addMaterialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMaterial(self, currentUser);
            }
        });

        showMaterialInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Material currentMaterial = database.getAvailableMaterial().get(materialTable.getSelectedRow());
                new ShowMaterialInfo(currentMaterial, self);
            }
        });

        //remove material button
        removeMaterialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = materialTable.getSelectedRow();
                Integer currentId = (Integer) materialTable.getValueAt(currentRow,0);
                try {
                    database.removeMaterial(currentId);
                    reloadMaterialTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                }
            }
        });

        materialTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (currentUser != Users.OPERATOR) {
                    removeMaterialButton.setEnabled(materialTable.getSelectedRow() != -1);
                }
                showMaterialInfoButton.setEnabled(materialTable.getSelectedRow() != -1);
            }
        });

        // Listen for changes in the material search field
        findMaterialIdTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    findMaterialId = Integer.parseInt(findMaterialIdTextField.getText());
                } catch (NumberFormatException ex) {
                    if (findMaterialIdTextField.getText().length() == 0) {
                        findMaterialId = null;
                    } else {
                        findMaterialId = -1;
                    }
                }
                reloadMaterialTable();
            }
        });
    }

    /**
     * Gui logic in sfProduct tab.
     *
     * @param currentUser currently logged in user
     */
    private void sfProductTab() {
        if (currentUser == Users.OPERATOR) {
            addSfProductButton.setEnabled(false);
        }
        sfProductPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                reloadSfProductTable();
            }
        });

        addSfProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddSfProduct(self, currentUser);
            }
        });

        showSfProductInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SfProduct currentSfProduct = database.getSfProducts().get(sfProductTable.getSelectedRow());
                new ShowSfProductInfo(currentSfProduct, self);
            }
        });

        //remove sfProduct button
        removeSfProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = sfProductTable.getSelectedRow();
                Integer currentId = (Integer) sfProductTable.getValueAt(currentRow,0);
                try {
                    database.removeSfProduct(currentId);
                    reloadSfProductTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
                }
            }
        });

        sfProductTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (currentUser != Users.OPERATOR) {
                    removeSfProductButton.setEnabled(sfProductTable.getSelectedRow() != -1);
                }
                showSfProductInfoButton.setEnabled(sfProductTable.getSelectedRow() != -1);
            }
        });

        // Listen for changes in the sfProduct search field
        findSfProductIdTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    findSfProductId = Integer.parseInt(findSfProductIdTextField.getText());
                } catch (NumberFormatException ex) {
                    if (findSfProductIdTextField.getText().length() == 0) {
                        findSfProductId = null;
                    } else {
                        findSfProductId = -1;
                    }
                }
                reloadSfProductTable();
            }
        });
    }

    /**
     * Reloads available material from SQL database and JTable which is showing them.
     */
    public void reloadMaterialTable() {
        DefaultTableModel materialTableModel = (DefaultTableModel) materialTable.getModel();
        materialTableModel.setRowCount(0);

        try {
            database.loadMaterial(findMaterialId);
            List<Material> availableMaterial = database.getAvailableMaterial();
            for (Material material : availableMaterial) {
                materialTableModel.addRow(material.asArray());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
        }
    }

    /**
     * Reload sfProducts from SQL database and JTable which is showing them.
     */
    public void reloadSfProductTable() {
        DefaultTableModel sfProductTableModel = (DefaultTableModel) sfProductTable.getModel();
        sfProductTableModel.setRowCount(0);

        try {
            database.loadSfProducts(findSfProductId);
            List<SfProduct> availableSfProduct = database.getSfProducts();
            for (SfProduct sfProduct : availableSfProduct) {
                sfProductTableModel.addRow(sfProduct.asArray());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Problém při komunikaci s databází!");
        }
    }

    /**
     * Creates custom GUI elements.
     * used for Intellij Forms.
     */
    private void createUIComponents() {
        materialTableModel = new DefaultTableModel(Material.HEADER, 0);
        materialTable = new JTable(materialTableModel) {
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
            materialTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        sfProductTableModel = new DefaultTableModel(SfProduct.HEADER, 0);
        sfProductTable = new JTable(sfProductTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        for (int i = 0; i < 5; i++) {
            sfProductTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Returns SQL database interface
     *
     * @return SQL database interface
     */
    public Database getDatabase() {
        return database;
    }

    public Users getCurrentUser() {
        return currentUser;
    }
}
