/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.TeacherViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Petar
 */
public class TeachersController implements Initializable {

    private Map<TextField, Label> validationMap;
    private final ObservableList<TeacherViewModel> teachers = FXCollections.observableArrayList();
    private TeacherViewModel selectedTeacherViewModel;
    private Tab previousTab;
    private Stage stage;
    private Parent root;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<TeacherViewModel> tvTeachers;
    @FXML
    private TableColumn<TeacherViewModel, String> tcFirstName;
    @FXML
    private TableColumn<TeacherViewModel, String> tcLastName;
    @FXML
    private TableColumn<TeacherViewModel, String> tcEmail;
    @FXML
    private Tab tabAdd;
    @FXML
    private ImageView ivTeacher;
    @FXML
    private TextField tfFirstName;
    @FXML
    private Label lbFirstNameError;
    @FXML
    private TextField tfLastName;
    @FXML
    private Label lbLastNameError;
    @FXML
    private TextField tfEmail;
    @FXML
    private Label lbEmailError;
    @FXML
    private Label lbImageError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initTeachers();
        initTable();
        setupListeners();
    }

    @FXML
    private void openSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/Subjects.fxml");
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
    private void openTeachersSubjectsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/TeachersSubjects.fxml");
    }

    @FXML
    private void openTeachersStudentsFrame(ActionEvent event) throws IOException {
        openNewFrame("/hr/algebra/view/TeachersStudents.fxml");
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvTeachers.getSelectionModel().getSelectedItem() != null) {
            bindTeacher(tvTeachers.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
            previousTab = tabAdd;
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvTeachers.getSelectionModel().getSelectedItem() != null) {
            teachers.remove(tvTeachers.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        File file = FileUtils.uploadFileDialog(tfFirstName.getScene().getWindow(), "jpg", "png", "jpeg");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ivTeacher.setImage(image);
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            try {
                selectedTeacherViewModel.getTeacher().setPicture(ImageUtils.imageToByteArray(image, ext));
            } catch (IOException ex) {
                Logger.getLogger(TeachersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void commitAddTeacher(ActionEvent event) {
        if (formValid()) {
            selectedTeacherViewModel.getTeacher().setFirstName(tfFirstName.getText());
            selectedTeacherViewModel.getTeacher().setLastName(tfLastName.getText());
            selectedTeacherViewModel.getTeacher().setEmail(tfEmail.getText());

            if (selectedTeacherViewModel.getTeacher().getIDTeacher() == 0) {
                teachers.add(selectedTeacherViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository().updateTeacher(selectedTeacherViewModel.getTeacher());
                    tvTeachers.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(TeachersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedTeacherViewModel = null;
            tpContent.getSelectionModel().select(tabList);

            resetForm();
        }
    }

    private void openNewFrame(String path) throws IOException {
        root = FXMLLoader.load(getClass().getResource(path));
        stage = (Stage) tfEmail.getScene().getWindow();
        stage.setTitle("CRUD App");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbFirstNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbLastNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbEmailError)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initTeachers() {
        try {
            RepositoryFactory.getRepository().getTeachers().forEach(
                    t -> teachers.add(new TeacherViewModel(t)));
        } catch (Exception ex) {
            Logger.getLogger(TeachersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTable() {
        tcFirstName.setCellValueFactory(t -> t.getValue().getFirstNameProperty());
        tcLastName.setCellValueFactory(t -> t.getValue().getLastNameProperty());
        tcEmail.setCellValueFactory(t -> t.getValue().getEmailProperty());
        tvTeachers.setItems(teachers);
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd)
                    && !tabAdd.equals(previousTab)) {
                bindTeacher(null);
            }
            previousTab = tpContent.getSelectionModel().getSelectedItem();
        });

        teachers.addListener((ListChangeListener.Change<? extends TeacherViewModel> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteTeacher(tvm.getTeacher());
                        } catch (Exception ex) {
                            Logger.getLogger(TeachersController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tvm -> {
                        try {
                            int id = RepositoryFactory.getRepository().addTeacher(tvm.getTeacher());
                            tvm.getTeacher().setIDTeacher(id);
                        } catch (Exception ex) {
                            Logger.getLogger(TeachersController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void bindTeacher(TeacherViewModel tvm) {
        resetForm();

        selectedTeacherViewModel = tvm != null ? tvm : new TeacherViewModel(null);
        tfFirstName.setText(selectedTeacherViewModel.getFirstNameProperty().get());
        tfLastName.setText(selectedTeacherViewModel.getLastNameProperty().get());
        tfEmail.setText(selectedTeacherViewModel.getEmailProperty().get());

        ivTeacher.setImage(selectedTeacherViewModel.getPictureProperty().get() != null
                ? new Image(new ByteArrayInputStream(
                        selectedTeacherViewModel.getPictureProperty().get()))
                : new Image(new File("src/assets/no_image.png").toURI().toString())
        );
    }

    private void resetForm() {
        validationMap.values().forEach(lbError -> lbError.setVisible(false));
        lbImageError.setVisible(false);
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.forEach((tf, lb) -> {
            if (tf.getText().isEmpty()
                    || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText())) {
                lb.setVisible(true);
                ok.set(false);
            }
        });

        if (selectedTeacherViewModel.getPictureProperty().get() == null) {
            lbImageError.setVisible(true);
            ok.set(false);
        }

        return ok.get();
    }
}
