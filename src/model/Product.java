package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class utilized for managing products and associated parts.*/
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Constructor method for products.
     * @param id product id
     * @param name product name
     * @param price product price
     * @param stock product stock
     * @param min product minimum inventory
     * @param max product maximum inventory
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return id for product
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set for product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return name for product
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set for product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return price for product
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set for product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return inventory for product
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the inventory value to set for product
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return minimum inventory for product
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the minimum inventory value to set for product
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return maximum inventory for product
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the maximum inventory value to set for product
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param part the part to associate with product
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the part to remove from product association
     * @return returns whether part was removed from product or not.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return all associated parts to product
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
