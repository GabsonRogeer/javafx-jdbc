package gui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.Serializable;

public class MainViewController implements Serializable {
    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("Click on seller acction");
    }

    @FXML
    public void onMenuItemDepartmentAction() {
        System.out.println("Click on department action");
    }

    @FXML
    public void onMenuItemAboutAction() {
        System.out.println("Click on About action");
    }
}