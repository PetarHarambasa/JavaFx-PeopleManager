/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Teacher;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Petar
 */
public class TeacherViewModel {

    private final Teacher teacher;

    public TeacherViewModel(Teacher teacher) {
        if (teacher == null) {
            teacher = new Teacher(0, "", "", "");
        }
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public IntegerProperty getIDTeacherProperty() {
        return new SimpleIntegerProperty(teacher.getIDTeacher());
    }

    public StringProperty getFirstNameProperty() {
        return new SimpleStringProperty(teacher.getFirstName());
    }

    public StringProperty getLastNameProperty() {
        return new SimpleStringProperty(teacher.getLastName());
    }

    public StringProperty getEmailProperty() {
        return new SimpleStringProperty(teacher.getEmail());
    }

    public ObjectProperty<byte[]> getPictureProperty() {
        return new SimpleObjectProperty<>(teacher.getPicture());
    }
}
