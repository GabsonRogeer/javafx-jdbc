package gui.util;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.w3c.dom.Node;

public class Utils {

    public static Stage currentStage(ActionEvent event){

        return (Stage) ((Node)event.getSource()).getScene().getWindow;
    }
}
