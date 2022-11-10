package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class controls main form window for inventory management system.*/
public class MainController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, Integer> partInvLvlCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField partsSearchTxt;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, Integer> productInvLvlCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TextField productsSearchTxt;

    @FXML
    private Label partNoResultsLbl;

    @FXML
    private Label productNoResultsLbl;

    /** Method changes current form to add part form.
     * @param event take action on event
     * @throws IOException handle exceptions
     */
    @FXML
    public void onActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method changes current form to add product form.
     * @param event take action on event
     * @throws IOException handle exceptions
     */
    @FXML
    public void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method deletes currently selected parts record. Confirmation message is displayed validating deletion.
     * @param event take action on event
     */
    @FXML
    public void onActionDeletePart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part record will be deleted! Would you like to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                if(!(Inventory.deletePart(selectedPart))){
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Delete Failed");
                    alert.setContentText("Unable to delete part.");
                    alert.showAndWait();
                }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No Item Selected! Select an item and try again.");
            alert.showAndWait();
        }
    }

    /** Method deletes currently selected product record. Confirmation message is displayed validating deletion. If
     * product contains associated parts warning message will be displayed indicating product may have associated parts.
     * @param event take action on event
     */
    @FXML
    public void onActionDeleteProduct(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Product record will be deleted! Would you like to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                if(!(Inventory.deleteProduct(selectedProduct))){
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Delete Failed");
                    alert.setContentText("Unable to delete product. Parts may be associated with product.");
                    alert.showAndWait();
                }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No Item Selected! Select an item and try again.");
            alert.showAndWait();
        }
    }

    /** Method closes application.
     * @param event take action on event
     */
    @FXML
    public void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    /** Method changes current form to modify part form.
     * @param event take action on event
     * @throws IOException handle exceptions
     */
    @FXML
    public void onActionModifyPart(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartForm.fxml"));
            loader.load();
            ModifyPartController ADMController = loader.getController();
            ADMController.sendPart(partsTableView.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No Part Selected! Select a part and try again.");
            alert.showAndWait();
        }
    }

    /** Method changes current form to modify product form.
     * @param event take action on event
     */
    @FXML
    public void onActionModifyProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductForm.fxml"));
            loader.load();
            ModifyProductController ADMController = loader.getController();
            ADMController.sendProduct(productsTableView.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException | IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No Product Selected! Select a product and try again.");
            alert.showAndWait();
        }
    }

    /** RUNTIME ERROR - Method searches parts inventory based on entered text followed by pressing the return key. If
     * entered text is a number record will display single record matching record ID. If entered text is a string a
     * list of records will be displayed based on part names containing text. First record will be selected by default.
     * Runtime error is corrected by testing integers first and catch all other entered text as string. If no matches
     * are found label will be marked visible indicating no matches were found. This method is primarily for text copied
     * in search field without utilization of pressing keyboard keys.
     * @param event take action on event
     */
    @FXML
    public void onActionPartsFilter(ActionEvent event) {
        ObservableList<Part> partsFilter = FXCollections.observableArrayList();
        try {
            int partIdSearch = Integer.parseInt(partsSearchTxt.getText());
            Part selectedPart = Inventory.lookupPart(partIdSearch);
            if(selectedPart != null) {
                partNoResultsLbl.setVisible(false);
                partsFilter.add(selectedPart);
                partsTableView.getSelectionModel().select(0);
            }
            else
                partNoResultsLbl.setVisible(true);
            partsTableView.setItems(partsFilter);
        }
        catch (NumberFormatException e){
            String partNameSearch = partsSearchTxt.getText();
            if(!(Inventory.lookupPart(partNameSearch).isEmpty()))
                partNoResultsLbl.setVisible(false);
            else
                partNoResultsLbl.setVisible(true);
            partsTableView.setItems(Inventory.lookupPart(partNameSearch));
            partsTableView.getSelectionModel().select(0);
        }
    }

    /** RUNTIME ERROR - Method searches products inventory based on entered text followed by pressing the return key. If
     * entered text is a number record will display single record matching record ID. If entered text is a string a
     * list of records will be displayed based on product names containing text. First record will be selected by default.
     * Runtime error is corrected by testing integers first and catch all other entered text as string. If no matches
     * are found label will be marked visible indicating no matches were found. This method is primarily for text copied
     * in search field without utilization of pressing keyboard keys.
     * @param event take action on event
     */
    @FXML
    public void onActionProductsFilter(ActionEvent event) {
        ObservableList<Product> productsFilter = FXCollections.observableArrayList();
        try {
            int productIdSearch = Integer.parseInt(productsSearchTxt.getText());
            Product selectedProduct = Inventory.lookupProduct(productIdSearch);
            if(selectedProduct != null) {
                productNoResultsLbl.setVisible(false);
                productsFilter.add(selectedProduct);
                productsTableView.getSelectionModel().select(0);
            }
            else
                productNoResultsLbl.setVisible(true);
            productsTableView.setItems(productsFilter);
        }
        catch (NumberFormatException e){
            String productNameSearch = productsSearchTxt.getText();
            if(!(Inventory.lookupProduct(productNameSearch).isEmpty()))
                productNoResultsLbl.setVisible(false);
            else
                productNoResultsLbl.setVisible(true);
            productsTableView.setItems(Inventory.lookupProduct(productNameSearch));
            productsTableView.getSelectionModel().select(0);
        }
    }

    /** RUNTIME ERROR - Method searches parts inventory based on entered text while text field is populated. If
     * entered text is a number record will display single record matching record ID. If entered text is a string a
     * list of records will be displayed based on part names containing text. First record will be selected by default.
     * Runtime error is corrected by testing integers first and catch all other entered text as string. If no matches
     * are found label will be marked visible indicating no matches were found.
     * @param event take action on event
     */
    @FXML
    public void onKeyReleasePartSearch(KeyEvent event) {
        ObservableList<Part> partsFilter = FXCollections.observableArrayList();
        try {
            int partIdSearch = Integer.parseInt(partsSearchTxt.getText());
            Part selectedPart = Inventory.lookupPart(partIdSearch);
            if(selectedPart != null) {
                partNoResultsLbl.setVisible(false);
                partsFilter.add(selectedPart);
                partsTableView.getSelectionModel().select(0);
            }
            else
                partNoResultsLbl.setVisible(true);
            partsTableView.setItems(partsFilter);
        }
        catch (NumberFormatException e){
            String partNameSearch = partsSearchTxt.getText();
            if(!(Inventory.lookupPart(partNameSearch).isEmpty()))
                partNoResultsLbl.setVisible(false);
            else
                partNoResultsLbl.setVisible(true);
            partsTableView.setItems(Inventory.lookupPart(partNameSearch));
            partsTableView.getSelectionModel().select(0);
        }
    }

    /** RUNTIME ERROR - Method searches products inventory based on entered text while text field is populated. If
     * entered text is a number record will display single record matching record ID. If entered text is a string a
     * list of records will be displayed based on product names containing text. First record will be selected by default.
     * Runtime error is corrected by testing integers first and catch all other entered text as string. If no matches
     * are found label will be marked visible indicating no matches were found.
     * @param event take action on event
     */
    @FXML
    public void onKeyReleaseProductSearch(KeyEvent event) {
        ObservableList<Product> productsFilter = FXCollections.observableArrayList();
        try {
            int productIdSearch = Integer.parseInt(productsSearchTxt.getText());
            Product selectedProduct = Inventory.lookupProduct(productIdSearch);
            if(selectedProduct != null) {
                productNoResultsLbl.setVisible(false);
                productsFilter.add(selectedProduct);
                productsTableView.getSelectionModel().select(0);
            }
            else
                productNoResultsLbl.setVisible(true);
            productsTableView.setItems(productsFilter);
        }
        catch (NumberFormatException e){
            String productNameSearch = productsSearchTxt.getText();
            if(!(Inventory.lookupProduct(productNameSearch).isEmpty()))
                productNoResultsLbl.setVisible(false);
            else
                productNoResultsLbl.setVisible(true);
            productsTableView.setItems(Inventory.lookupProduct(productNameSearch));
            productsTableView.getSelectionModel().select(0);
        }
    }

    /** Method initializes main form. Data is populated in parts table and products table based on all data available
     * from inventory.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
