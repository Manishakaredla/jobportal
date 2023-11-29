/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeemanagementsystem;


import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class userController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button home_btn;

    @FXML
    private Button salary_btn;

    @FXML
    private Button logout;

   
    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalPresents;

    @FXML
    private Label home_totalInactiveEm;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane addEmployee_form;

    @FXML
    private TableView<jobData> addEmployee_tableView;

    @FXML
    private TableColumn<jobData, String> addJob_col_jobID;

    @FXML
    private TableColumn<jobData, String> addJob_col_location;

    @FXML
    private TableColumn<jobData, String> addJob_col_description;

    @FXML
    private TableColumn<jobData, String> addEmployee_col_gender;

    @FXML
    private TableColumn<jobData, String> addEmployee_col_phoneNum;

    @FXML
    private TableColumn<jobData, String> addEmployee_col_position;

    @FXML
    private TableColumn<jobData, String> addEmployee_col_date;

    @FXML
    private TextField addEmployee_search;

    @FXML
    private TextField addJob_jobID;

    @FXML
    private TextField addJob_location;

    @FXML
    private TextField addJob_description;

    @FXML
    private ComboBox<?> addEmployee_gender;

    @FXML
    private TextField addEmployee_phoneNum;

    @FXML
    private ComboBox<?> addEmployee_position;

    @FXML
    private ImageView addEmployee_image;

    @FXML
    private Button addEmployee_importBtn;

    @FXML
    private Button addEmployee_addBtn;

    @FXML
    private Button addEmployee_updateBtn;

    @FXML
    private Button addEmployee_deleteBtn;

    @FXML
    private Button addEmployee_clearBtn;

    @FXML
    private AnchorPane salary_form;

    @FXML
    private TextField salary_jobID;

    @FXML
    private Label salary_location;

    @FXML
    private Label salary_description;

    @FXML
    private Label salary_position;

    @FXML
    private TextField salary_salary;

    @FXML
    private Button salary_updateBtn;

    @FXML
    private Button salary_clearBtn;

    @FXML
    private TableView<jobData> salary_tableView;

    @FXML
    private TableColumn<jobData, String> salary_col_jobID;

    @FXML
    private TableColumn<jobData, String> salary_col_location;

    @FXML
    private TableColumn<jobData, String> salary_col_description;

    @FXML
    private TableColumn<jobData, String> salary_col_position;

    @FXML
    private TableColumn<jobData, String> salary_col_salary;

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    private Image image;

    public void homeTotalJobs() {

        String sql = "SELECT COUNT(job_id) FROM jobs_data";

        connect = database.connectDb();
        int countData = 0;
        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(job_id)");
            }

            home_totalEmployees.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeJobsTotalPresent() {

        String sql = "SELECT COUNT(job_id) FROM jobs_info";

        connect = database.connectDb();
        int countData = 0;
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                countData = result.getInt("COUNT(job_id)");
            }
            home_totalPresents.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeTotalInactive() {

        String sql = "SELECT COUNT(job_id) FROM jobs_info WHERE salary = '0.0'";

        connect = database.connectDb();
        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(job_id)");
            }
            home_totalInactiveEm.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeChart() {

        home_chart.getData().clear();

        String sql = "SELECT date, COUNT(job_id) FROM jobs_data GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7";

        connect = database.connectDb();

        try {
            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<String, Integer>(result.getString(1), result.getInt(2)));
            }

            home_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addJobAdd() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO jobs_data "
                + "(job_id,location,description,gender,phoneNum,position,image,date) "
                + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (addJob_jobID.getText().isEmpty()
                    || addJob_location.getText().isEmpty()
                    || addJob_description.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                String check = "SELECT job_id FROM jobs_data WHERE job_id = '"
                        + addJob_jobID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Job ID: " + addJob_jobID.getText() + " already exist!");
                    alert.showAndWait();
                } else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addJob_jobID.getText());
                    prepare.setString(2, addJob_location.getText());
                    prepare.setString(3, addJob_description.getText());
                    prepare.setString(4, (String) addEmployee_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(5, addEmployee_phoneNum.getText());
                    prepare.setString(6, (String) addEmployee_position.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(7, uri);
                    prepare.setString(8, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    String insertInfo = "INSERT INTO jobs_info "
                            + "(job_id,location,description,position,salary,date) "
                            + "VALUES(?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertInfo);
                    prepare.setString(1, addJob_jobID.getText());
                    prepare.setString(2, addJob_location.getText());
                    prepare.setString(3, addJob_description.getText());
                    prepare.setString(4, (String) addEmployee_position.getSelectionModel().getSelectedItem());
                    prepare.setString(5, "0.0");
                    prepare.setString(6, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    addJobShowListData();
                    addJobReset();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addJobUpdate() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE jobs_data SET location = '"
                + addJob_location.getText() + "', description = '"
                + addJob_description.getText() + "', gender = '"
                + addEmployee_gender.getSelectionModel().getSelectedItem() + "', phoneNum = '"
                + addEmployee_phoneNum.getText() + "', position = '"
                + addEmployee_position.getSelectionModel().getSelectedItem() + "', image = '"
                + uri + "', date = '" + sqlDate + "' WHERE job_id ='"
                + addJob_jobID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;
            if (addJob_jobID.getText().isEmpty()
                    || addJob_location.getText().isEmpty()
                    || addJob_description.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Job ID: " + addJob_jobID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    double salary = 0;

                    String checkData = "SELECT * FROM jobs_info WHERE job_id = '"
                            + addJob_jobID.getText() + "'";

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    while (result.next()) {
                        salary = result.getDouble("salary");
                    }

                    String updateInfo = "UPDATE jobs_info SET location = '"
                            + addJob_location.getText() + "', description = '"
                            + addJob_description.getText() + "', position = '"
                            + addEmployee_position.getSelectionModel().getSelectedItem()
                            + "' WHERE job_id = '"
                            + addJob_jobID.getText() + "'";

                    prepare = connect.prepareStatement(updateInfo);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    addJobShowListData();
                    addJobReset();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addJobDelete() {

        String sql = "DELETE FROM jobs_data WHERE job_id = '"
                + addJob_jobID.getText() + "'";

        connect = database.connectDb();

        try {

            Alert alert;
            if (addJob_jobID.getText().isEmpty()
                    || addJob_location.getText().isEmpty()
                    || addJob_description.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Job ID: " + addJob_jobID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    String deleteInfo = "DELETE FROM jobs_info WHERE job_id = '"
                            + addJob_jobID.getText() + "'";

                    prepare = connect.prepareStatement(deleteInfo);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    addJobShowListData();
                    addJobReset();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addJobReset() {
    	addJob_jobID.setText("");
        addJob_location.setText("");
        addJob_description.setText("");
        addEmployee_gender.getSelectionModel().clearSelection();
        addEmployee_position.getSelectionModel().clearSelection();
        addEmployee_phoneNum.setText("");
        addEmployee_image.setImage(null);
        getData.path = "";
    }

    public void addEmployeeInsertImage() {

        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 101, 127, false, true);
            addEmployee_image.setImage(image);
        }
    }

    private String[] positionList = {"Marketer Coordinator", "Web Developer (Back End)", "Web Developer (Front End)", "App Developer","Teacher","Lecturer", "Doctor"};

    public void addEmployeePositionList() {
        List<String> listP = new ArrayList<>();

        for (String data : positionList) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addEmployee_position.setItems(listData);
    }

    private String[] listGender = {"Male", "Female", "Others"};

    public void addEmployeeGendernList() {
        List<String> listG = new ArrayList<>();

        for (String data : listGender) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);
        addEmployee_gender.setItems(listData);
    }

    public void addJobSearch() {

        FilteredList<jobData> filter = new FilteredList<>(addEmployeeList, e -> true);

        addEmployee_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateEmployeeData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateEmployeeData.getJobId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getLocation().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getDescription().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPhoneNum().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getDate().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<jobData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());
        addEmployee_tableView.setItems(sortList);
    }

    public ObservableList<jobData> addEmployeeListData() {

        ObservableList<jobData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM jobs_data";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            jobData employeeD;

            while (result.next()) {
                employeeD = new jobData(result.getInt("job_id"),
                        result.getString("location"),
                        result.getString("description"),
                        result.getString("gender"),
                        result.getString("phoneNum"),
                        result.getString("position"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(employeeD);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<jobData> addEmployeeList;

    public void addJobShowListData() {
        addEmployeeList = addEmployeeListData();

        addJob_col_jobID.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        addJob_col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        addJob_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        addEmployee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmployee_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        addEmployee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        addEmployee_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeeList);

    }

    public void addEmployeeSelect() {
        jobData employeeD = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addJob_jobID.setText(String.valueOf(employeeD.getJobId()));
        addJob_location.setText(employeeD.getLocation());
        addJob_description.setText(employeeD.getDescription());
        addEmployee_phoneNum.setText(employeeD.getPhoneNum());

        getData.path = employeeD.getImage();

        String uri = "file:" + employeeD.getImage();

        image = new Image(uri, 101, 127, false, true);
        addEmployee_image.setImage(image);
    }

    public void salaryUpdate() {

        String sql = "UPDATE jobs_info SET salary = '" + salary_salary.getText()
                + "' WHERE job_id = '" + salary_jobID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (salary_jobID.getText().isEmpty()
                    || salary_location.getText().isEmpty()
                    || salary_description.getText().isEmpty()
                    || salary_position.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select item first");
                alert.showAndWait();
            } else {
                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                salaryShowListData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salaryReset() {
        salary_jobID.setText("");
        salary_location.setText("");
        salary_description.setText("");
        salary_position.setText("");
        salary_salary.setText("");
    }

    public ObservableList<jobData> salaryListData() {

        ObservableList<jobData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM jobs_info";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            jobData employeeD;

            while (result.next()) {
                employeeD = new jobData(result.getInt("job_Id"),
                         result.getString("location"),
                         result.getString("description"),
                         result.getString("position"),
                         result.getDouble("salary"));

                listData.add(employeeD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<jobData> salaryList;

    public void salaryShowListData() {
        salaryList = salaryListData();

        salary_col_jobID.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        salary_col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        salary_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        salary_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        salary_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        salary_tableView.setItems(salaryList);

    }
    
    private ObservableList<jobData> addApplicationList;
    
    public void applyJob() {
    	
    }

    public void salarySelect() {

        jobData employeeD = salary_tableView.getSelectionModel().getSelectedItem();
        int num = salary_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        salary_jobID.setText(String.valueOf(employeeD.getJobId()));
        salary_location.setText(employeeD.getLocation());
        salary_description.setText(employeeD.getDescription());
        salary_position.setText(employeeD.getPosition());
        salary_salary.setText(String.valueOf(employeeD.getSalary()));

    }

    public void defaultNav() {
        home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
    }

    public void displayUsername() {
        username.setText(getData.username);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
           
            addEmployee_form.setVisible(true);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
          
            salary_btn.setStyle("-fx-background-color:transparent");

          

        } else if (event.getSource() == home_btn) {
        
           addEmployee_form.setVisible(false);
           salary_form.setVisible(false);

           home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            home_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            addEmployeeGendernList();
            addEmployeePositionList();
            addJobSearch();

        } else if (event.getSource() == salary_btn) {
         
            addEmployee_form.setVisible(false);
            salary_form.setVisible(true);

            salary_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            home_btn.setStyle("-fx-background-color:transparent");
          
            salaryShowListData();

        }

    }

    private double x = 0;
    private double y = 0;

    public void logout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Login as admin?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
        displayUsername();
        defaultNav();

        
       addJobShowListData();
       addEmployeeGendernList();
       addEmployeePositionList();

        salaryShowListData();
    }

}
