package cz.muni.fi.pb175.project;

import java.util.Date;

/**
 * Class representing semi-finished products.
 *
 * @author Martin Uhlik
 */
public class SfProduct {
    private Integer id;
    private String projectName;
    private String sfProductFinishDate;
    private String projectFinishDate;
    private boolean approved;
    private Integer workBench;
    private String description;
    private Status status;
    private MaterialType material;
    private Machines machine;

    public final static String[] HEADER = {"ID", "Název projektu", "Dokončení polotovaru", "Dokončení projektu", "Status" , "Schváleno"};

    /**
     * Object constructor.
     *
     * @param id ID of material
     * @param projectName project name that is semi-finished product for
     * @param sfProductFinishDate finish date of product
     * @param projectFinishDate finish date of project
     * @param workBench table on which material should me machined
     * @param approved if production manager approved creation of semi-finished product
     * @param description description of semi-finished product
     * @param status status of sfProduct
     */
    public SfProduct(int id, String projectName, String sfProductFinishDate, String projectFinishDate, boolean approved, int workBench, String description, Status status, MaterialType material, Machines machine) {
        this.id = id;
        this.projectName = projectName;
        this.sfProductFinishDate = sfProductFinishDate;
        this.projectFinishDate = projectFinishDate;
        this.approved = approved;
        this.workBench = workBench;
        this.description = description;
        this.status = status;
        this.material = material;
        this.machine = machine;
    }

    /**
     * Returns some of object parameters as array for JTable.
     *
     * @return array of objects representing selected parameters
     */
    public Object[] asArray() {
        return new Object[]{id, projectName, sfProductFinishDate, projectFinishDate, status, approved};
    }

    public String getProjectName() {
        return projectName;
    }

    public Integer getId() {
        return id;
    }

    public String getSfProductFinishDate() {
        return sfProductFinishDate;
    }

    public String getProjectFinishDate() {
        return projectFinishDate;
    }

    public boolean isApproved() {
        return approved;
    }

    public Integer getWorkBench() {
        return workBench;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public MaterialType getMaterialType() {
        return material;
    }

    public Machines getMachine() {
        return machine;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
