package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private Department entity;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setDepartment(Department entity){
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(entity == null) {
            throw new IllegalStateException("A entidade está nula");
        }
        if (service == null){
            throw new IllegalStateException("O Serviço está nulo");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e){
            setErrorMesseges(e.getErrors());
        }
        catch (DbException e){
            Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();

        ValidationException exception = new ValidationException("Erro de validação");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "O Campo não pode estar vazio");
        }
        obj.setName(txtName.getText());

        if(exception.getErrors().size() > 0) {
            throw exception;
        }
        return obj;
    }

    @FXML
    public void onBtCacelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 20);
    }

    public void updateFormData(){
        if (entity == null){
            throw new IllegalStateException("A entidade está nula");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    private void setErrorMesseges(Map<String, String> errors){
        Set<String> fields = errors.keySet();
        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }

}
