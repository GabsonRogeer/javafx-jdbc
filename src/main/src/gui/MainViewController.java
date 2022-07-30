package gui;

import com.application.Main;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;
import java.awt.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

public class MainViewController implements Serializable {
    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private MenuItem menuItemHelp;

    @FXML
    private Hyperlink hyperlinkGitHub;

    @FXML
    private Hyperlink hyperlinkSourceCode;

    @FXML
    private Hyperlink hyperlinkLinkedin;

    @FXML
    private Hyperlink hyperlinkInstagram;



    @FXML
    public void onMenuItemSellerAction() {
        loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
            controller.setSellerService(new SellerService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAboutAction() {
        loadView("/gui/About.fxml", x -> {});
    }

    @FXML
    public void onMenuItemHelpAction(){
        loadView("/gui/Help.fxml", x->{});
    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVbox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVbox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);
        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view",e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void openLinkLinkedin(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/gabson-portela/"));
    }

    @FXML
    void openLinkSourceCode(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://github.com/GabsonRogeer/javafx-jdbc"));
    }

    @FXML
    void openLinkGitHub(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://github.com/GabsonRogeer"));
    }

    @FXML
    void openLinkInstagram(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/gabsonrogeer/"));
    }
}