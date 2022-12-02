/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Student;
import hr.algebra.model.Teacher;
import hr.algebra.model.TeacherStudent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Petar
 */
public class TeacherStudentViewModel {

    private final TeacherStudent teacherStudent;

    public TeacherStudentViewModel(TeacherStudent teacherStudent) {
        if (teacherStudent == null) {
            teacherStudent = new TeacherStudent(0, new Teacher(0, "", "", ""), new Student(0, "", "", "", 0));
        }
        this.teacherStudent = teacherStudent;
    }

    public TeacherStudent getTeacherStudent() {
        return teacherStudent;
    }

    public IntegerProperty getIDTeacherStudentProperty() {
        return new SimpleIntegerProperty(teacherStudent.getIDTeacherStudent());
    }

    public StringProperty getTeacherStudentStudentNameProperty() {
        return new SimpleStringProperty(teacherStudent.getStudentID().getFirstName() + " " + teacherStudent.getStudentID().getLastName());
    }

    public StringProperty getTeacherStudentTeacherNameProperty() {
        return new SimpleStringProperty(teacherStudent.getTeacherID().getFirstName() + " " + teacherStudent.getStudentID().getLastName());
    }
}
