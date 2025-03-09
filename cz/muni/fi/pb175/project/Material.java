package cz.muni.fi.pb175.project;

/**
 * Class representing material in stock.
 *
 * @author MArtin Uhlik
 */
public class Material {
    private Integer id;
    private MaterialType materialType;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer sfProductAssigned;
    private String description;


    public static final Object[] HEADER = {"ID", "Typ", "Výška (mm)", "Šířka (mm)", "Délka (mm)", "Zarezervováno"};

    /**
     * Object constructor.
     *
     * @param id id of material
     * @param type materialType
     * @param x x size in millimeters
     * @param y y size in millimeters
     * @param z z size in millimeters
     * @param sfProductAssigned id of sfProduct it associated with
     * @param description description of material
     */
    public Material(int id, MaterialType type, int x, int y, int z, Integer sfProductAssigned, String description) {
        this.id = id;
        this.materialType = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.sfProductAssigned = sfProductAssigned;
        this.description = description;
    }

    /**
     * Returns some of object parameters as array for JTable.
     *
     * @return array of objects representing selected parameters
     */
    public Object[] asArray() {
        boolean sfProduct = !(sfProductAssigned == null);
        return new Object[]{id, materialType, x, y, z, sfProduct};
    }

    public Integer getId() {
        return id;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Integer getSfProductAssigned() {
        return sfProductAssigned;
    }

    public String getDescription() {
        return description;
    }
}
