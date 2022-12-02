/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Student;
import hr.algebra.model.Subject;
import hr.algebra.model.Teacher;
import hr.algebra.viewmodel.TeacherStudentViewModel;
import hr.algebra.viewmodel.TeacherSubjectViewModel;
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
public class TeachersSubjectsController implements Initializable {

    private Map<ComboBox, Label> validationMap;
    private final ObservableList<TeacherSubjectViewModel> teachersSubjects = FXCollections.observableArrayList();
    private TeacherSubjectViewModel selectedTeacherSubjectViewModel;
    private Tab previousTab;
    private Stage stage;
    private Parent root;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<TeacherSubjectViewModel> tvTeachersSubjects;
    @FXML
    private TableColumn<TeacherSubjectViewModel, String> tcTeacherName;
    @FXML
    private TableColumn<TeacherSubjectViewModel, String> TcSubjectName;
    @FXML
    private Tab tabAdd;
    @FXML
    private ComboBox<Teacher> cbTeachers;
    @FXML
    private Label lbTeacherError;
    @FXML
    private ComboBox<Subject> cbSubjects;
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
            initTeachersSubjects();
            initTable();
            setupListeners();
        } catch (Exception ex) {
            Logger.getLogger(TeachersSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void openStudentsSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/StudentsSubjects.fxml");
    }

    @FXML
    private void openTeachersStudentsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/TeachersStudents.fxml");
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvTeachersSubjects.getSelectionModel().getSelectedItem() != null) {
            bindTeacherSubject(tvTeachersSubjects.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
            previousTab = tabAdd;
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvTeachersSubjects.getSelectionModel().getSelectedItem() != null) {
            teachersSubjects.remove(tvTeachersSubjects.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void commit(ActionEvent event) {
        if (formValid()) {
            selectedTeacherSubjectViewModel.getTeacherSubject().setTeacherID(cbTeachers.selectionModelProperty().getValue().getSelectedItem());
            selectedTeacherSubjectViewModel.getTeacherSubject().setSubjectID(cbSubjects.selectionModelProperty().getValue().getSelectedItem());

            if (selectedTeacherSubjectViewModel.getTeacherSubject().getIDTeacherSubject() == 0) {
                teachersSubjects.add(selectedTeacherSubjectViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository().updateTeacherSubject(selectedTeacherSubjectViewModel.getTeacherSubject());
                    tvTeachersSubjects.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(TeachersStudentsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedTeacherSubjectViewModel = null;
            tpContent.getSelectionModel().select(tabList);

            resetForm();
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(cbTeachers, lbTeacherError),
                new AbstractMap.SimpleImmutableEntry<>(cbSubjects, lbSubjectError)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void fillComboBoxes() throws Exception {
        ObservableList<Subject> observableArrayListStudents
                = FXCollections.observableArrayList(RepositoryFactory.getRepository().getSubjects());
        ObservableList<Teacher> observableArrayListTeachers
                = FXCollections.observableArrayList(RepositoryFactory.getRepository().getTeachers());

        cbTeachers.setItems(observableArrayListTeachers);
        cbSubjects.setItems(observableArrayListStudents);
    }

    private void initTeachersSubjects() {
        try {
            RepositoryFactory.getRepository().getTeachersSubjects().forEach(
                    ts -> teachersSubjects.add(new TeacherSubjectViewModel(ts)));
        } catch (Exception ex) {
            Logger.getLogger(TeachersSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTable() {
        tcTeacherName.setCellValueFactory(ts -> ts.getValue().getTeacherSubjectTeacherNameProperty());
        TcSubjectName.setCellValueFactory(ts -> ts.getValue().getTeacherSubjectSubjectNameProperty());
        tvTeachersSubjects.setItems(teachersSubjects);
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd)
                    && !tabAdd.equals(previousTab)) {
                bindTeacherSubject(null);
            }
            previousTab = tpContent.getSelectionModel().getSelectedItem();
        });

        teachersSubjects.addListener((ListChangeListener.Change<? extends TeacherSubjectViewModel> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tsvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteTeacherSubject(tsvm.getTeacherSubject());
                        } catch (Exception ex) {
                            Logger.getLogger(TeachersSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tsvm -> {
                        try {
                            int id = RepositoryFactory.getRepository().addTeacherSubject(tsvm.getTeacherSubject());
                            tsvm.getTeacherSubject().setIDTeacherSubject(id);
                        } catch (Exception ex) {
                            Logger.getLogger(TeachersSubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void openNewFrame(String path) throws IOException {
        root = FXMLLoader.load(getClass().getResource(path));
        stage = (Stage) lbSubjectError.getScene().getWindow();
        stage.setTitle("CRUD App");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void bindTeacherSubject(TeacherSubjectViewModel tsvm) {
        resetForm();

        selectedTeacherSubjectViewModel = tsvm != null ? tsvm : new TeacherSubjectViewModel(null);
        cbTeachers.getSelectionModel().select(selectedTeacherSubjectViewModel.getTeacherSubject().getTeacherID());
        cbSubjects.getSelectionModel().select(selectedTeacherSubjectViewModel.getTeacherSubject().getSubjectID());
    }

    private void resetForm() {
        validationMap.values().forEach(lbError -> lbError.setVisible(false));
        cbSubjects.getSelectionModel().clearSelection();
        cbTeachers.getSelectionModel().clearSelection();
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
