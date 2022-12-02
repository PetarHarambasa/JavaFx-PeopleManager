/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Student;
import hr.algebra.model.StudentSubject;
import hr.algebra.model.Subject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Petar
 */
public class StudentSubjectViewModel {

    private final StudentSubject studentSubject;

    public StudentSubjectViewModel(StudentSubject studentSubject) {
        if (studentSubject == null) {
            studentSubject = new StudentSubject(0, new Student(0, "", "", "", 0), new Subject(0, ""));
        }
        this.studentSubject = studentSubject;
    }

    public StudentSubject getStudentSubject() {
        return studentSubject;
    }

    public IntegerProperty getIDStudentSubjectProperty() {
        return new SimpleIntegerProperty(studentSubject.getIDStudentSubject());
    }

    public StringProperty getStudentSubjectStudentNameProperty() {
        return new SimpleStringProperty(studentSubject.getStudentID().getFirstName() + " " + studentSubject.getStudentID().getLastName());
    }

    public StringProperty getStudentSubjectSubjectNameProperty() {
        return new SimpleStringProperty(studentSubject.getSubjectID().getName());
    }
}
