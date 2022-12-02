/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.viewmodel.SubjectViewModel;
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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Petar
 */
public class SubjectsController implements Initializable {

    private Map<TextField, Label> validationMap;
    private final ObservableList<SubjectViewModel> subjects = FXCollections.observableArrayList();
    private SubjectViewModel selectedSubjectViewModel;
    private Tab previousTab;
    private Stage stage;
    private Parent root;
    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<SubjectViewModel> tvSubjects;
    @FXML
    private TableColumn<SubjectViewModel, String> tcName;
    @FXML
    private Tab tabAdd;
    @FXML
    private TextField tfSubjectName;
    @FXML
    private Label lbSubjectNameError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initSubjects();
        initTable();
        setupListeners();
    }

    @FXML
    private void openStudentsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/Students.fxml");
    }

    @FXML
    private void openTeachersFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/Teachers.fxml");
    }

    @FXML
    private void openStudentsSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/StudentsSubjects.fxml");
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
        stage = (Stage) tfSubjectName.getScene().getWindow();
        stage.setTitle("CRUD App");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    private void commitAddSubject(ActionEvent event) {
         if (formValid()) {
            selectedSubjectViewModel.getSubject().setName(tfSubjectName.getText());

            if (selectedSubjectViewModel.getSubject().getIDSubject()== 0) {
                subjects.add(selectedSubjectViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository().updateSubject(selectedSubjectViewModel.getSubject());
                    tvSubjects.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedSubjectViewModel = null;
            tpContent.getSelectionModel().select(tabList);

            resetForm();
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvSubjects.getSelectionModel().getSelectedItem() != null) {
            bindSubject(tvSubjects.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
            previousTab = tabAdd;
        }
    }

    @FXML
    private void delete(ActionEvent event) {
         if (tvSubjects.getSelectionModel().getSelectedItem() != null) {
            subjects.remove(tvSubjects.getSelectionModel().getSelectedItem());
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfSubjectName, lbSubjectNameError)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initSubjects() {
        try {
            RepositoryFactory.getRepository().getSubjects().forEach(
                    s -> subjects.add(new SubjectViewModel(s)));
        } catch (Exception ex) {
            Logger.getLogger(SubjectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTable() {
        tcName.setCellValueFactory(s -> s.getValue().getNameProperty());
        tvSubjects.setItems(subjects);
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd)
                    && !tabAdd.equals(previousTab)) {
                bindSubject(null);
            }
            previousTab = tpContent.getSelectionModel().getSelectedItem();
        });

        subjects.addListener((ListChangeListener.Change<? extends SubjectViewModel> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(svm -> {
                        try {
                            RepositoryFactory.getRepository().deleteSubject(svm.getSubject());
                        } catch (Exception ex) {
                            Logger.getLogger(SubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(svm -> {
                        try {
                            int id = RepositoryFactory.getRepository().addSubject(svm.getSubject());
                            svm.getSubject().setIDSubject(id);
                        } catch (Exception ex) {
                            Logger.getLogger(SubjectsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void bindSubject(SubjectViewModel svm) {
        resetForm();

        selectedSubjectViewModel = svm != null ? svm : new SubjectViewModel(null);
        tfSubjectName.setText(selectedSubjectViewModel.getNameProperty().get());
    }

    private void resetForm() {
        validationMap.values().forEach(lbError -> lbError.setVisible(false));
    }

    private boolean formValid() {
         final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.forEach((tf, lb) -> {
            if (tf.getText().isEmpty()) {
                lb.setVisible(true);
                ok.set(false);
            }
        });
        return ok.get();
    }
}
