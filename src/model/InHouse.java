package model;

/** Class inherits part class as in-house.*/
public class InHouse extends Part{
    private int machineId;

    /** Constructor method for in-house part.
     * @param id part ID
     * @param name part name
     * @param price part price
     * @param stock part stock
     * @param min part minimum inventory
     * @param max part maximum inventory
     * @param machineId part machine ID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return machine id for in-house part
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machine id set for in-house part
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
