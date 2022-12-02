/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Student;
import hr.algebra.model.Subject;
import hr.algebra.viewmodel.StudentSubjectViewModel;
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
public class StudentsSubjectsController implements Initializable {

    private Map<ComboBox, Label> validationMap;
    private final ObservableList<StudentSubjectViewModel> studentsSubjects = FXCollections.observableArrayList();
    private StudentSubjectViewModel selectedStudentSubjectViewModel;
    private Tab previousTab;
    private Stage stage;
    private Parent root;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<StudentSubjectViewModel> tvStudentsSubjects;
    @FXML
    private TableColumn<StudentSubjectViewModel, String> tcStudentName;
    @FXML
    private TableColumn<StudentSubjectViewModel, String> tcSubjectName;
    @FXML
    private Tab tabAdd;
    @FXML
    private ComboBox<Student> cbStudents;
    @FXML
    private ComboBox<Subject> cbSubjects;
    @FXML
    private Label lbStudentError;
    @FXML
    private Label lbSubjectError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initValidation();
            fillComboBoxes();
            initStudentsSubjects();
            initTable();
            setupListeners();
        } catch (Exception ex) {
            Logger.getLogger(StudentsSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void openTeachersStudentsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/TeachersStudents.fxml");
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
        if (tvStudentsSubjects.getSelectionModel().getSelectedItem() != null) {
            bindStudentSubject(tvStudentsSubjects.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
            previousTab = tabAdd;
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvStudentsSubjects.getSelectionModel().getSelectedItem() != null) {
            studentsSubjects.remove(tvStudentsSubjects.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void commit(ActionEvent event) throws Exception {

        if (formValid()) {
            selectedStudentSubjectViewModel.getStudentSubject().setStudentID(cbStudents.selectionModelProperty().getValue().getSelectedItem());
            selectedStudentSubjectViewModel.getStudentSubject().setSubjectID(cbSubjects.selectionModelProperty().getValue().getSelectedItem());

            if (selectedStudentSubjectViewModel.getStudentSubject().getIDStudentSubject() == 0) {
                studentsSubjects.add(selectedStudentSubjectViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository().updateStudentSubject(selectedStudentSubjectViewModel.getStudentSubject());
                    tvStudentsSubjects.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(StudentsSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedStudentSubjectViewModel = null;
            tpContent.getSelectionModel().select(tabList);

            resetForm();
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(cbStudents, lbStudentError),
                new AbstractMap.SimpleImmutableEntry<>(cbSubjects, lbSubjectError)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void fillComboBoxes() throws Exception {
        ObservableList<Student> observableArrayListStudents
                = FXCollections.observableArrayList(RepositoryFactory.getRepository().getStudents());
        ObservableList<Subject> observableArrayListSubjects
                = FXCollections.observableArrayList(RepositoryFactory.getRepository().getSubjects());

        cbStudents.setItems(observableArrayListStudents);
        cbSubjects.setItems(observableArrayListSubjects);
    }

    private void initStudentsSubjects() {
        try {
            RepositoryFactory.getRepository().getStudentsSubjects().forEach(
                    ss -> studentsSubjects.add(new StudentSubjectViewModel(ss)));
        } catch (Exception ex) {
            Logger.getLogger(StudentsSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTable() {
        tcStudentName.setCellValueFactory(ss -> ss.getValue().getStudentSubjectStudentNameProperty());
        tcSubjectName.setCellValueFactory(ss -> ss.getValue().getStudentSubjectSubjectNameProperty());
        tvStudentsSubjects.setItems(studentsSubjects);
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd)
                    && !tabAdd.equals(previousTab)) {
                bindStudentSubject(null);
            }
            previousTab = tpContent.getSelectionModel().getSelectedItem();
        });

        studentsSubjects.addListener((ListChangeListener.Change<? extends StudentSubjectViewModel> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(ssvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteStudentSubject(ssvm.getStudentSubject());
                        } catch (Exception ex) {
                            Logger.getLogger(StudentsSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(ssvm -> {
                        try {
                            int id = RepositoryFactory.getRepository().addStudentSubject(ssvm.getStudentSubject());
                            ssvm.getStudentSubject().setIDStudentSubject(id);
                        } catch (Exception ex) {
                            Logger.getLogger(StudentsSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void bindStudentSubject(StudentSubjectViewModel ssvm) {

        resetForm();

        selectedStudentSubjectViewModel = ssvm != null ? ssvm : new StudentSubjectViewModel(null);
        cbStudents.getSelectionModel().select(selectedStudentSubjectViewModel.getStudentSubject().getStudentID());
        cbSubjects.getSelectionModel().select(selectedStudentSubjectViewModel.getStudentSubject().getSubjectID());

    }

    private void resetForm() {
        validationMap.values().forEach(lbError -> lbError.setVisible(false));
        cbSubjects.getSelectionModel().clearSelection();
        cbStudents.getSelectionModel().clearSelection();
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
