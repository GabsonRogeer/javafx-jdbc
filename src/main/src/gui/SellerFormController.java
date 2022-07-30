package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private SellerService service;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private Seller entity;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBasealary;

    @FXML
    private ComboBox<Department> comboBoxDepartment;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    @FXML
    private ObservableList<Department> obsList;

    public void setSeller(Seller entity){
        this.entity = entity;
    }

    public void setSellerServices(SellerService service, DepartmentService departmentService){
        this.service = service;
        this.departmentService = departmentService;
    }


    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(entity == null) {
            throw new IllegalStateException("Entity was Null");
        }
        if (service == null){
            throw new IllegalStateException("Service was null");
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
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidationException exception = new ValidationException("Validation Error");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "O Campo não pode estar vazio");
        }
        obj.setName(txtName.getText());

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "O Campo não pode estar vazio");
        }
        obj.setEmail(txtEmail.getText());

        if(dpBirthDate.getValue() == null){
            exception.addError("birthDate", "O Campo não pode estar vazio");
        }
        else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setBirthDate(Date.from(instant));
        }

        if (txtBasealary.getText() == null || txtBasealary.getText().trim().equals("")){
            exception.addError("baseSalary", "O Campo não pode estar vazio");
        }
        obj.setBaseSalary(Utils.tryParseToDouble(txtBasealary.getText()));
        if(exception.getErrors().size() > 0) {
            throw exception;
        }

        obj.setDepartment(comboBoxDepartment.getValue());

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
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBasealary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void updateFormData(){
        if (entity == null){
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBasealary.setText(String.format("%.2f",entity.getBaseSalary()));
        if(entity.getBirthDate() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
        if (entity.getDepartment() == null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        comboBoxDepartment.setValue(entity.getDepartment());
    }

    public void loadAssociatedObjects(){
        if(departmentService == null){
            throw new IllegalStateException("DepartmentService was null");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMesseges(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
        labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
        labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
        labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

}
