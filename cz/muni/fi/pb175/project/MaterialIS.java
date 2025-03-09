package cz.muni.fi.pb175.project;

import cz.muni.fi.pb175.project.gui.LoginScreen;
import javax.swing.*;

/**
 * Application launcher.
 *
 * @author Martin Uhlik
 */
public class MaterialIS {
    private final static String TITLE = "Material IS";

    /**
     * Runs application logic.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen(TITLE);
            }
        });
    }
}
