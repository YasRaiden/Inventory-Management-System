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
import model.*;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class controls modify product form window for inventory management system.*/
public class ModifyProductController implements Initializable {
    Stage stage;
    Parent scene;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    int currentIndex;

    @FXML
    private TableColumn<Part, Integer> assocPartIdCol;

    @FXML
    private TableColumn<Part, Integer> assocPartInvLvlCol;

    @FXML
    private TableColumn<Part, String> assocPartNameCol;

    @FXML
    private TableColumn<Part, Double> assocPartPriceCol;

    @FXML
    private TableView<Part> assocPartsTableView;

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
    private TableView<Part> partsTableView;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField productInvTxt;

    @FXML
    private TextField productMaxTxt;

    @FXML
    private TextField productMinTxt;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField productPriceTxt;

    @FXML
    private Label partNoResultsLbl;

    /** Method adds part to product.
     * @param event take action on event
     */
    @FXML
    public void onActionAddPart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            associatedParts.add(selectedPart);
        }
        assocPartsTableView.setItems(associatedParts);
    }

    /** Method changes current form to Main Form. Confirmation will display voiding any entered information.
     * @param event take action on event
     * @throws IOException handle exceptions
     */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes will not be saved! Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** Method removes part from product. Confirmation message is displayed confirming removal of part from product.
     * @param event take action on event
     */
    @FXML
    public void onActionRemoveAssoc(ActionEvent event) {
        Part selectedAssocPart = assocPartsTableView.getSelectionModel().getSelectedItem();
        if(selectedAssocPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Associated part will be deleted! Would you like to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                associatedParts.remove(selectedAssocPart);
        }
    }

    /** RUNTIME ERROR - Saves modified part information onto inventory.
     * Runtime error corrected for invalid data entry in fields. If invalid entries are found preventing product from
     * saving warning prompt will be displayed asking for validation of entered values. Logical error is test for
     * minimum, maximum, and inventory values. If values are not valid warning message will be displayed describing
     * how values should be entered.
     * @param event take action on event
     */
    @FXML
    public void onActionSaveProduct(ActionEvent event) {
        try {
            int id = Integer.parseInt(productIdTxt.getText());
            String name = productNameTxt.getText();
            double price = Double.parseDouble(productPriceTxt.getText());
            int stock = Integer.parseInt(productInvTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());

            if(min > max || max < min || stock < min || stock > max){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Minimum should be less then Maximum and Inventory should be between both values! Confirm values and try again.");
                alert.showAndWait();
            }
            else {

                Product newProduct = new Product(id, name, price, stock, min, max);
                for (Part part : associatedParts) {
                    newProduct.addAssociatedPart(part);
                }

                Inventory.updateProduct(currentIndex, newProduct);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Invalid values! Confirm values and try again.");
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

    /** Method receives selected product information from Main Form.
     * @param product object to send to separate form
     */
    public void sendProduct(Product product){
        productIdTxt.setText(String.valueOf(product.getId()));
        productNameTxt.setText(product.getName());
        productPriceTxt.setText(String.valueOf(product.getPrice()));
        productInvTxt.setText(String.valueOf(product.getStock()));
        productMaxTxt.setText(String.valueOf(product.getMax()));
        productMinTxt.setText(String.valueOf(product.getMin()));

        for(Part part : product.getAllAssociatedParts()){
            associatedParts.add(part);
        }

        currentIndex = Inventory.getAllProducts().indexOf(product);
    }

    /** Method initializes modify product form. Data is populated in associated parts table based on selected product.
     * All parts listed on inventory are populated in parts table. Product ID field is disabled.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartsTableView.setItems(associatedParts);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdTxt.setDisable(true);
    }
}
