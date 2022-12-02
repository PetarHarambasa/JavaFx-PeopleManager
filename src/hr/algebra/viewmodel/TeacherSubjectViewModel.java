/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Subject;
import hr.algebra.model.Teacher;
import hr.algebra.model.TeacherSubject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Petar
 */
public class TeacherSubjectViewModel {

    private final TeacherSubject teacherSubject;

    public TeacherSubjectViewModel(TeacherSubject teacherSubject) {
        if (teacherSubject == null) {
            teacherSubject = new TeacherSubject(0, new Teacher(0, "", "", ""), new Subject(0, ""));
        }
        this.teacherSubject = teacherSubject;
    }

    public TeacherSubject getTeacherSubject() {
        return teacherSubject;
    }

    public IntegerProperty getIDTeacherSubjectProperty() {
        return new SimpleIntegerProperty(teacherSubject.getIDTeacherSubject());
    }

    public StringProperty getTeacherSubjectSubjectNameProperty() {
        return new SimpleStringProperty(teacherSubject.getSubjectID().getName());
    }

    public StringProperty getTeacherSubjectTeacherNameProperty() {
        return new SimpleStringProperty(teacherSubject.getTeacherID().getFirstName() + " " + teacherSubject.getTeacherID().getLastName());
    }
}
