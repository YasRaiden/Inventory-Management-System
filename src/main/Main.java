package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/** Class starts main form window for inventory management system.*/
public class Main extends Application {

    /** Method sets up MainForm by loading FXML file. */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("C482-Task1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /** FUTURE ENHANCEMENT - Database connectivity. Add database connectivity so data is retained when application is
     * closed. Current build retains sample data on start but any updates or adds to products or parts is lost when
     * application is closed.
     *
     * Main method to start application.
     *
     * JavaDocs is located from folder path ".\Johnson-Trevian C482 Task1\javadoc"
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Add Sample InHouse Parts
        Part brakes = new InHouse(1,"Brakes",15.00,10,1,20, 1111);
        Part wheel = new InHouse(2,"Wheel",11.00,16,2,6, 2222);
        Part seat = new InHouse(3,"Seat",15.00,10,1,9, 3333);
        Inventory.addPart(brakes);
        Inventory.addPart(wheel);
        Inventory.addPart(seat);

        // Add Sample Outsourced Parts
        Part rim = new Outsourced(4, "Rim", 60.59, 20, 1, 25, "Outdoor Sports");

        // Add Sample Product with associated parts
        Product giantBike = new Product(1000, "Giant Bike", 299.99, 15, 1, 20);
        Product tricycle = new Product(1001, "Tricycle", 99.99, 3, 1, 5);
        giantBike.addAssociatedPart(brakes);
        tricycle.addAssociatedPart(brakes);
        Inventory.addProduct(giantBike);
        Inventory.addProduct(tricycle);


        launch(args);
    }
}
