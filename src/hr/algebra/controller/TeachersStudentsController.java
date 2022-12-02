/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Student;
import hr.algebra.model.Teacher;
import hr.algebra.viewmodel.TeacherStudentViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Petar
 */
public class TeachersStudentsController implements Initializable {

    private Map<ComboBox, Label> validationMap;
    private final ObservableList<TeacherStudentViewModel> teachersStudents = FXCollections.observableArrayList();
    private TeacherStudentViewModel selectedTeacherStudentViewModel;
    private Tab previousTab;
    private Stage stage;
    private Parent root;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<TeacherStudentViewModel> tvTeachersStudents;
    @FXML
    private TableColumn<TeacherStudentViewModel, String> tcTeacherName;
    @FXML
    private TableColumn<TeacherStudentViewModel, String> tcStudentName;
    @FXML
    private Tab tabAdd;
    @FXML
    private ComboBox<Teacher> cbTeachers;
    @FXML
    private ComboBox<Student> cbStudents;
    @FXML
    private Label lbTeacherError;
    @FXML
    private Label lbStudentError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initValidation();
            fillComboBoxes();
            initTeachersStudents();
            initTable();
            setupListeners();
        } catch (Exception ex) {
            Logger.getLogger(TeachersStudentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/Subjects.fxml");
    }

    @FXML
    private void openTeachersFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/Teachers.fxml");
    }

    @FXML
    private void openStudentsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/Students.fxml");
    }

    @FXML
    private void openTeachersSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/TeachersSubjects.fxml");
    }

    @FXML
    private void openStudentsSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/StudentsSubjects.fxml");
    }

    private void openNewFrame(String path) throws IOException {
        root = FXMLLoader.load(getClass().getResource(path));
        stage = (Stage) lbStudentError.getScene().getWindow();
        stage.setTitle("CRUD App");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvTeachersStudents.getSelectionModel().getSelectedItem() != null) {
            bindTeacherStudent(tvTeachersStudents.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
            previousTab = tabAdd;
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvTeachersStudents.getSelectionModel().getSelectedItem() != null) {
            teachersStudents.remove(tvTeachersStudents.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void commit(ActionEvent event) {
        if (formValid()) {
            selectedTeacherStudentViewModel.getTeacherStudent().setTeacherID(cbTeachers.selectionModelProperty().getValue().getSelectedItem());
            selectedTeacherStudentViewModel.getTeacherStudent().setStudentID(cbStudents.selectionModelProperty().getValue().getSelectedItem());

            if (selectedTeacherStudentViewModel.getTeacherStudent().getIDTeacherStudent()== 0) {
                teachersStudents.add(selectedTeacherStudentViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository().updateTeacherStudent(selectedTeacherStudentViewModel.getTeacherStudent());
                    tvTeachersStudents.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(TeachersStudentsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedTeacherStudentViewModel = null;
            tpContent.getSelectionModel().select(tabList);

            resetForm();
        }
    }

    private void bindTeacherStudent(TeacherStudentViewModel tsvm) {
        resetForm();

        selectedTeacherStudentViewModel = tsvm != null ? tsvm : new TeacherStudentViewModel(null);
        cbStudents.getSelectionModel().select(selectedTeacherStudentViewModel.getTeacherStudent().getStudentID());
        cbTeachers.getSelectionModel().select(selectedTeacherStudentViewModel.getTeacherStudent().getTeacherID());

    }

    private void resetForm() {
        validationMap.values().forEach(lbError -> lbError.setVisible(false));
        cbStudents.getSelectionModel().clearSelection();
        cbTeachers.getSelectionModel().clearSelection();
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(cbTeachers, lbTeacherError),
                new AbstractMap.SimpleImmutableEntry<>(cbStudents, lbStudentError)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
     }

    private void fillComboBoxes() throws Exception {
       ObservableList<Student> observableArrayListStudents
                = FXCollections.observableArrayList(RepositoryFactory.getRepository().getStudents());
        ObservableList<Teacher> observableArrayListTeachers
                = FXCollections.observableArrayList(RepositoryFactory.getRepository().getTeachers());

        cbTeachers.setItems(observableArrayListTeachers);
        cbStudents.setItems(observableArrayListStudents);
    }

    private void initTeachersStudents() {
         try {
            RepositoryFactory.getRepository().getTeachersStudents().forEach(
                    ts -> teachersStudents.add(new TeacherStudentViewModel(ts)));
        } catch (Exception ex) {
            Logger.getLogger(TeachersStudentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTable() {
        tcTeacherName.setCellValueFactory(ts -> ts.getValue().getTeacherStudentTeacherNameProperty());
        tcStudentName.setCellValueFactory(ts -> ts.getValue().getTeacherStudentStudentNameProperty());
        tvTeachersStudents.setItems(teachersStudents);
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd)
                    && !tabAdd.equals(previousTab)) {
                bindTeacherStudent(null);
            }
            previousTab = tpContent.getSelectionModel().getSelectedItem();
        });

        teachersStudents.addListener((ListChangeListener.Change<? extends TeacherStudentViewModel> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tsvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteTeacherStudent(tsvm.getTeacherStudent());
                        } catch (Exception ex) {
                            Logger.getLogger(TeachersStudentsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tsvm -> {
                        try {
                            int id = RepositoryFactory.getRepository().addTeacherStudent(tsvm.getTeacherStudent());
                            tsvm.getTeacherStudent().setIDTeacherStudent(id);
                        } catch (Exception ex) {
                            Logger.getLogger(TeachersStudentsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private boolean formValid() {
       final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.forEach((cb, lb) -> {
            if (cb.getSelectionModel().isEmpty()) {
                lb.setVisible(true);
                ok.set(false);
            }
        });

        return ok.get();
    }
}
