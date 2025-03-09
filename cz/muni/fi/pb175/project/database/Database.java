package cz.muni.fi.pb175.project.database;

import cz.muni.fi.pb175.project.Machines;
import cz.muni.fi.pb175.project.Material;
import cz.muni.fi.pb175.project.MaterialType;
import cz.muni.fi.pb175.project.SfProduct;
import cz.muni.fi.pb175.project.Status;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class represents SQL database of products and local copy of selected rows.
 * Local data are synchronized with SQL database.
 *
 * @author Martin Uhlik
 */
public class Database {
    private final List<Material> availableMaterial = new ArrayList<>();
    private final List<SfProduct> sfProducts = new ArrayList<>();
    private final Connection connection;
    private final Statement statement;
    private final static String DATABASE_DRIVER = "org.mariadb.jdbc.Driver";

    /**
     * Object constructor.
     * Connects to SQL database using values loaded from configuration file.
     *
     * @param configuration data for connecting to SQL database
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the class cannot be located
     */
    public Database(DatabaseConfiguration configuration) throws SQLException, ClassNotFoundException {
        Class.forName(DATABASE_DRIVER);
        connection = DriverManager.getConnection(configuration.getUrl(),
                configuration.getName(),
                configuration.getPassword());
        statement = connection.createStatement();
        loadMaterial(null);
        loadSfProducts(null);
    }

    /**
     * Explicitly closes connection to database.
     *
     * @throws SQLException if a database access error occurs
     */
    public void closeConnection() throws SQLException{
        connection.close();
    }

    /**
     * Loads material from SQL database to local objects.
     *
     * @param findId ID of material that should be found in SQL database or null for all available material
     * @throws SQLException if a database access error occurs
     */
    public void loadMaterial(Integer findId) throws SQLException {
        availableMaterial.clear();
        ResultSet resultSet;
        if (findId == null) {
            resultSet = statement.executeQuery("SELECT * FROM `material`");
        } else {
            resultSet = statement.executeQuery("SELECT * FROM `material` WHERE id=" + findId);
        }

        while (resultSet.next()) {
            Integer sfProductAssigned = resultSet.getInt(6);
            if (resultSet.wasNull()) {
                sfProductAssigned = null;
            }
            availableMaterial.add(new Material(resultSet.getInt(1),
                    MaterialType.forName(resultSet.getString(2)),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    sfProductAssigned,
                    resultSet.getString(7)));
        }
    }

    /**
     * Adds given material to SQL database
     *
     * @param material material to be added to SQL database
     * @throws SQLException if a database access error occurs
     */
    public void addMaterial(Material material) throws SQLException {
        statement.executeQuery(String.format("INSERT INTO `material` (materialType, x, y, z, sfProductAssigned, " +
                        "description) VALUES (\"%s\", %s, %s, %s, %s, \"%s\")",
                material.getMaterialType(),
                material.getX(),
                material.getY(),
                material.getZ(),
                getAssignedSfProduct(material),
                material.getDescription()));
    }

    /**
     * Change values of selected material in SQL database.
     *
     * @param material material to be changed,
     *                 material of same ID number in SQL database will be changed to these values
     * @throws SQLException if a database access error occurs
     */
    public void editMaterial(Material material) throws SQLException {
        statement.executeQuery(String.format("UPDATE `material` SET materialType = \"%s\", x = %s, y = %s, z = %s, " +
                        "sfProductAssigned = %s, description = \"%s\" WHERE id = %s",
                material.getMaterialType(),
                material.getX(),
                material.getY(),
                material.getZ(),
                getAssignedSfProduct(material),
                material.getDescription(),
                material.getId()));
    }

    /**
     * Returns value of assigned sfProduct to material. If no value is assigned, returns NULL.
     *
     * @param material material of which we want to get assigned sfProduct
     * @return number of sfProduct or "NULL" value
     */
    private static Object getAssignedSfProduct(Material material) {
        if (material.getSfProductAssigned() != null) {
            return material.getSfProductAssigned();
        }
        return "NULL";
    }

    /**
     * Removes selected material from SQL database.
     *
     * @param id id of material that should be removed
     * @throws SQLException if a database access error occurs
     */
    public void removeMaterial(int id) throws SQLException {
        statement.executeQuery(String.format("DELETE FROM `material` WHERE id=%s", id));
    }

    /**
     * Loads Semi-Finished Products that matches search criteria from SQL database.
     *
     * @param findId ID of semi-finished product that should be found in SQL database
     *               or null for all available semi-finished products
     * @throws SQLException if a database access error occurs
     */
    public void loadSfProducts(Integer findId) throws SQLException{
        sfProducts.clear();
        ResultSet resultSet;
        if (findId == null) {
            resultSet = statement.executeQuery("SELECT * FROM `sfProduct`");
        } else {
            resultSet = statement.executeQuery("SELECT * FROM `sfProduct` WHERE id=" + findId);
        }
        while (resultSet.next()) {
            sfProducts.add(new SfProduct(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5) != 0,
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    Status.forName(resultSet.getString(8)),
                    MaterialType.forName(resultSet.getString(9)),
                    Machines.forName(resultSet.getString(10))));
        }
    }

    /**
     * Adds given semi-finished product to SQL database.
     *
     * @param sfProduct semi-finished product to be added to SQL database
     * @throws SQLException if a database access error occurs
     */
    public void addSfProduct(SfProduct sfProduct) throws SQLException {
        statement.executeQuery(String.format("INSERT INTO `sfProduct` (projectName, sfProductFinishDate, " +
                        "projectFinishDate, approved, workbench, description, status, material, machine) VALUES " +
                        "(\"%s\", \"%s\", \"%s\", %s, %s, \"%s\", \"%s\", \"%s\", \"%s\")",
                sfProduct.getProjectName(),
                sfProduct.getSfProductFinishDate(),
                sfProduct.getProjectFinishDate(),
                isApproved(sfProduct),
                sfProduct.getWorkBench(),
                sfProduct.getDescription(),
                sfProduct.getStatus(),
                sfProduct.getMaterialType(),
                sfProduct.getMachine()));
    }

    /**
     * Change values of selected sfProduct in SQL database.
     *
     * @param sfProduct semi-finished product to be changed,
     *                  semi-finished product of same ID number in SQL database will be changed to these values
     * @throws SQLException if a database access error occurs
     */
    public void editSfProduct(SfProduct sfProduct) throws SQLException {
        statement.executeQuery(String.format("UPDATE `sfProduct` SET projectName = \"%s\", sfProductFinishDate = " +
                        "\"%s\", projectFinishDate = \"%s\", approved = %s, workbench = %s, description = \"%s\", " +
                        "status = \"%s\", material = \"%s\", machine = \"%s\" WHERE id = %s",
                sfProduct.getProjectName(),
                sfProduct.getSfProductFinishDate(),
                sfProduct.getProjectFinishDate(),
                isApproved(sfProduct),
                sfProduct.getWorkBench(),
                sfProduct.getDescription(),
                sfProduct.getStatus(),
                sfProduct.getMaterialType(),
                sfProduct.getMachine(),
                sfProduct.getId()));
    }

    /**
     * Returns numeric interpretation of boolean
     *
     * @param sfProduct sfProduct from which we want to get the value
     * @return numeric interpretation of boolean
     */
    private static int isApproved(SfProduct sfProduct) {
        return sfProduct.isApproved() ? 1 : 0;
    }

    /**
     * Removes selected semi-finished product from SQL database.
     *
     * @param id id of semi-finished product to be removed
     * @throws SQLException if a database access error occurs
     */
    public void removeSfProduct(int id) throws SQLException {
        statement.executeQuery(String.format("DELETE FROM `sfProduct` WHERE id=%s", id));
    }

    /**
     * Returns list of available material.
     *
     * @return list of available material
     */
    public List<Material> getAvailableMaterial() {
        return Collections.unmodifiableList(availableMaterial);
    }

    /**
     * Return list of available semi-finished products.
     *
     * @return list of semi-finished products
     */
    public List<SfProduct> getSfProducts() {
        return sfProducts;
    }

    /**
     * Returns available unassigned Material from Local database.
     *
     * @return unassigned Material from Local database
     */
    public List<Material> getUnassignedMaterial() {
        List<Material> unassignedMaterial = new ArrayList<>();
        for (Material material: availableMaterial) {
            if (material.getSfProductAssigned() == null) {
                unassignedMaterial.add(material);
            }
        }
        return unassignedMaterial;
    }

    /**
     * Returns assigned Material from Local database.
     *
     * @return assigned Material from Local database
     */
    public List<Material> getAssignedMaterial(Integer id) {
        List<Material> unassignedMaterial = new ArrayList<>();
        for (Material material: availableMaterial) {
            if (id.equals(material.getSfProductAssigned())) {
                unassignedMaterial.add(material);
            }
        }
        return unassignedMaterial;
    }

    /**
     * Gets material with selecter ID or null if not found.
     *
     * @param id
     * @return
     */
    public Material getMaterialWithId(int id) {
        for (Material material: availableMaterial) {
            if (material.getId() == id) {
                return material;
            }
        }
        return null;
    }

    public void setAssignedSfProduct(Integer materialId, Integer sfProductId) throws SQLException {
        Object sfProductStringId;
        if (sfProductId == null) {
            sfProductStringId = "NULL";
        } else {
            sfProductStringId = sfProductId;
        }

        statement.executeQuery(String.format("UPDATE `material` SET sfProductAssigned = %s WHERE id = %s",
                sfProductStringId,
                materialId));
    }
}