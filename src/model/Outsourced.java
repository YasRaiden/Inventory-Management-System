package model;

/** Class inherits part class as outsourced parts.*/
public class Outsourced extends Part{

    private String companyName;

    /** Constructor method for outsourced part.
     * @param id part ID
     * @param name part name
     * @param price part price
     * @param stock part stock
     * @param min part minimum inventory
     * @param max part maximum inventory
     * @param companyName part machine ID
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return company name for outsourced part
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the company name set for outsourced part
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
