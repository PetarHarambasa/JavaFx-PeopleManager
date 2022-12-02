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
import hr.algebra.viewmodel.StudentViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author Petar
 */
public class StudentsController implements Initializable {

    private Map<TextField, Label> validationMap;
    private final ObservableList<StudentViewModel> students = FXCollections.observableArrayList();
    private StudentViewModel selectedStudentViewModel;
    private Tab previousTab;
    private Stage stage;
    private Parent root;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<StudentViewModel> tvStudents;
    @FXML
    private TableColumn<StudentViewModel, String> tcFirstName;
    @FXML
    private TableColumn<StudentViewModel, String> tcLastName;
    @FXML
    private TableColumn<StudentViewModel, Integer> tcYearOfStudy;
    @FXML
    private TableColumn<StudentViewModel, String> tcEmail;
    @FXML
    private Tab tabAdd;
    @FXML
    private ImageView ivStudent;
    @FXML
    private TextField tfFirstName;
    @FXML
    private Label lbFirstNameError;
    @FXML
    private TextField tfLastName;
    @FXML
    private Label lbLastNameError;
    @FXML
    private TextField tfYearOfStudy;
    @FXML
    private Label lbYearOfStudyError;
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
        initStudents();
        initTable();
        addIntegerMask(tfYearOfStudy);
        setupListeners();
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvStudents.getSelectionModel().getSelectedItem() != null) {
            bindStudent(tvStudents.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
            previousTab = tabAdd;
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvStudents.getSelectionModel().getSelectedItem() != null) {
            students.remove(tvStudents.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        File file = FileUtils.uploadFileDialog(tfYearOfStudy.getScene().getWindow(), "jpg", "png", "jpeg");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ivStudent.setImage(image);
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            try {
                selectedStudentViewModel.getStudent().setPicture(ImageUtils.imageToByteArray(image, ext));
            } catch (IOException ex) {
                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void commitAddStudent(ActionEvent event) {
        if (formValid()) {
            selectedStudentViewModel.getStudent().setFirstName(tfFirstName.getText());
            selectedStudentViewModel.getStudent().setLastName(tfLastName.getText());
            selectedStudentViewModel.getStudent().setEmail(tfEmail.getText());
            selectedStudentViewModel.getStudent().setYearOfStudy(Integer.valueOf(tfYearOfStudy.getText()));

            if (selectedStudentViewModel.getStudent().getIDStudent() == 0) {
                students.add(selectedStudentViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository().updateStudent(selectedStudentViewModel.getStudent());
                    tvStudents.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedStudentViewModel = null;
            tpContent.getSelectionModel().select(tabList);

            resetForm();
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbFirstNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbLastNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfYearOfStudy, lbYearOfStudyError),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbEmailError)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initStudents() {
        try {
            RepositoryFactory.getRepository().getStudents().forEach(
                    s -> students.add(new StudentViewModel(s)));
        } catch (Exception ex) {
            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTable() {
        tcFirstName.setCellValueFactory(s -> s.getValue().getFirstNameProperty());
        tcLastName.setCellValueFactory(s -> s.getValue().getLastNameProperty());
        tcEmail.setCellValueFactory(s -> s.getValue().getEmailProperty());
        tcYearOfStudy.setCellValueFactory(s -> s.getValue().getYearOfStudyProperty().asObject());
        tvStudents.setItems(students);
    }

    private void addIntegerMask(TextField tf) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().matches("\\d*")) {
                return change;
            }
            return null;
        };

        tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, filter));
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd)
                    && !tabAdd.equals(previousTab)) {
                bindStudent(null);
            }
            previousTab = tpContent.getSelectionModel().getSelectedItem();
        });

        students.addListener((ListChangeListener.Change<? extends StudentViewModel> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(svm -> {
                        try {
                            RepositoryFactory.getRepository().deleteStudent(svm.getStudent());
                        } catch (Exception ex) {
                            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(svm -> {
                        try {
                            int id = RepositoryFactory.getRepository().addStudent(svm.getStudent());
                            svm.getStudent().setIDStudent(id);
                        } catch (Exception ex) {
                            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void bindStudent(StudentViewModel svm) {
        resetForm();

        selectedStudentViewModel = svm != null ? svm : new StudentViewModel(null);
        tfFirstName.setText(selectedStudentViewModel.getFirstNameProperty().get());
        tfLastName.setText(selectedStudentViewModel.getLastNameProperty().get());
        tfEmail.setText(selectedStudentViewModel.getEmailProperty().get());
        tfYearOfStudy.setText(selectedStudentViewModel.getYearOfStudyProperty().get() + "");

        ivStudent.setImage(selectedStudentViewModel.getPictureProperty().get() != null
                ? new Image(new ByteArrayInputStream(
                        selectedStudentViewModel.getPictureProperty().get()))
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

        if (selectedStudentViewModel.getPictureProperty().get() == null) {
            lbImageError.setVisible(true);
            ok.set(false);
        }

        return ok.get();
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
        stage = (Stage) tfEmail.getScene().getWindow();
        stage.setTitle("CRUD App");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
