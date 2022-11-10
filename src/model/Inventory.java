package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class utilized for managing parts and products.*/
public class Inventory {
    private static int lastPartInvId = 4;
    private static int lastProductInvId = 1001;
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart adding part to all parts list
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * @param newProduct adding product to all products list
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * @return part object found in all parts list
     * @param partId search by part id from all parts list
     */
    public static Part lookupPart(int partId){
        for(Part part : Inventory.getAllParts()){
            if(part.getId() == partId)
                return part;
        }
        return null;
    }

    /**
     * @return product object found in all products list
     * @param productId search by product id from all products list
     */
    public static Product lookupProduct(int productId){
        for(Product product : getAllProducts()){
            if(product.getId() == productId)
                return product;
        }
        return null;
    }

    /**
     * @return all parts containing part name
     * @param partName search by part name from all parts list
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for(Part part : getAllParts()) {
            if (part.getName().contains(partName))
                filteredParts.add(part);
        }
        return filteredParts;
    }

    /**
     * @return all products containing product name
     * @param productName search by product name from all products list
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for(Product product : getAllProducts()) {
            if (product.getName().contains(productName))
                filteredProducts.add(product);
        }
        return filteredProducts;
    }

    /**
     * @param selectedPart update part from inventory by index value
     * @param index update part index from inventory to update
     */
    public static void updatePart(int index, Part selectedPart){
        Inventory.getAllParts().set(index, selectedPart);
    }

    /**
     * @param selectedProduct update product from inventory by index value
     * @param index update part index from inventory to update
     */
    public static void updateProduct(int index, Product selectedProduct){
        Inventory.getAllProducts().set(index, selectedProduct);
    }

    /**
     * @return if part was successfully removed from inventory
     * @param selectedPart remove part from inventory by object reference
     */
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    /**
     * @return if product was successfully removed from inventory
     * @param selectedProduct remove product from inventory by object reference. Checks if product has associated parts. If no parts are associated product will be removed from inventory.
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(selectedProduct.getAllAssociatedParts() == null)
            return allProducts.remove(selectedProduct);
        else
            return false;

    }

    /**
     * @return all parts list in inventory
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * @return all products list in inventory
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * @return increments last used part ID and returns result for new parts
     */
    public static int lastPartInvId() {
        Inventory.lastPartInvId += 1;
        return lastPartInvId;
    }

    /**
     * @return increments last used product ID and returns result for new products
     */
    public static int lastProductInvId() {
        Inventory.lastProductInvId += 1;
        return lastProductInvId;
    }


}
