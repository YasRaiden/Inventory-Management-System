package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class controls modify part form window for inventory management system.*/
public class ModifyPartController implements Initializable {
    Stage stage;
    Parent scene;
    int currentIndex;

    @FXML
    private TextField partIdTxt;

    @FXML
    private RadioButton partInhouseRbtn;

    @FXML
    private TextField partInvTxt;

    @FXML
    private TextField partMaxTxt;

    @FXML
    private TextField partMinTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private RadioButton partOutsourcedRbtn;

    @FXML
    private TextField partPriceTxt;

    @FXML
    private TextField partSpecialTxt;

    @FXML
    private Label partSpecialLbl;


    /** Method changes special label to Machine ID based on selection of in-house.
     * @param event take action on event
     */
    @FXML
    public void onActionInHouseSelected(ActionEvent event) {
        if(partInhouseRbtn.isSelected())
            partSpecialLbl.setText("Machine ID");
    }

    /** Method changes special label to Company Name based on selection of outsource.
     * @param event take action on event
     */
    @FXML
    public void onActionOutsourcedSelected(ActionEvent event) {
        if(partOutsourcedRbtn.isSelected())
            partSpecialLbl.setText("Company Name");
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

    /** RUNTIME ERROR - Saves modified part information onto inventory.
     * Runtime error corrected for invalid data entry in fields. If invalid entries are found preventing part from
     * saving warning prompt will be displayed asking for validation of entered values. Logical error is test for
     * minimum, maximum, and inventory values. If values are not valid warning message will be displayed describing
     * how values should be entered.
     * @param event take action on event
     * @throws IOException handle exceptions
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        try{
            int id = Integer.parseInt(partIdTxt.getText());
            String name = partNameTxt.getText();
            double price = Double.parseDouble(partPriceTxt.getText());
            int stock = Integer.parseInt(partInvTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            if(min > max || max < min || stock < min || stock > max){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Minimum should be less then Maximum and Inventory should be between both values! Confirm values and try again.");
                alert.showAndWait();
            }
            else {
                if (partInhouseRbtn.isSelected()) {
                    int machineId = Integer.parseInt(partSpecialTxt.getText());
                    Part newPart = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.updatePart(currentIndex, newPart);
                }
                if (partOutsourcedRbtn.isSelected()) {
                    String companyName = partSpecialTxt.getText();
                    Part newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(currentIndex, newPart);
                }
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Invalid values! Confirm values and try again.");
            alert.showAndWait();
        }

    }

    /** Method receives selected part information from Main Form.
     * @param part part object to send to separate form
     */
    public void sendPart(Part part){
        partIdTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(part.getName());
        partPriceTxt.setText(String.valueOf(part.getPrice()));
        partInvTxt.setText(String.valueOf(part.getStock()));
        partMaxTxt.setText(String.valueOf(part.getMax()));
        partMinTxt.setText(String.valueOf(part.getMin()));

        if(part instanceof InHouse) {
            partInhouseRbtn.setSelected(true);
            partSpecialTxt.setText(String.valueOf(((InHouse)part).getMachineId()));
        }
        if(part instanceof Outsourced) {
            partOutsourcedRbtn.setSelected(true);
            partSpecialTxt.setText(((Outsourced) part).getCompanyName());
        }

        currentIndex = Inventory.getAllParts().indexOf(part);
    }

    /** Method initializes modify part form. Part ID text field is disabled. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partIdTxt.setDisable(true);
    }
}
