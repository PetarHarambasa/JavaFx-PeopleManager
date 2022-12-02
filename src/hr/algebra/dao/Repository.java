/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.Student;
import hr.algebra.model.StudentSubject;
import hr.algebra.model.Subject;
import hr.algebra.model.Teacher;
import hr.algebra.model.TeacherStudent;
import hr.algebra.model.TeacherSubject;
import java.util.List;

/**
 *
 * @author Petar
 */
public interface Repository {
    
    int addStudent(Student data) throws Exception;
    void updateStudent(Student student) throws Exception;
    void deleteStudent(Student student) throws Exception;
    Student getStudent(int idStudent) throws Exception;
    List<Student> getStudents() throws Exception;
    
    int addTeacher(Teacher data) throws Exception;
    void updateTeacher(Teacher teacher) throws Exception;
    void deleteTeacher(Teacher teacher) throws Exception;
    Teacher getTeacher(int idTeacher) throws Exception;
    List<Teacher> getTeachers() throws Exception;
    
    int addSubject(Subject data) throws Exception;
    void updateSubject(Subject subject) throws Exception;
    void deleteSubject(Subject subject) throws Exception;
    Subject getSubject(int idSubject) throws Exception;
    List<Subject> getSubjects() throws Exception;
    
    int addStudentSubject(StudentSubject data) throws Exception;
    void updateStudentSubject(StudentSubject studentSubject) throws Exception;
    void deleteStudentSubject(StudentSubject studentSubject) throws Exception;
    StudentSubject getStudentSubject(int idStudentSubject) throws Exception;
    List<StudentSubject> getStudentsSubjects() throws Exception;
    
    int addTeacherStudent(TeacherStudent data) throws Exception;
    void updateTeacherStudent(TeacherStudent teacherStudent) throws Exception;
    void deleteTeacherStudent(TeacherStudent teacherStudent) throws Exception;
    TeacherStudent getTeacherStudent(int idTeacherStudent) throws Exception;
    List<TeacherStudent> getTeachersStudents() throws Exception;
    
    int addTeacherSubject(TeacherSubject data) throws Exception;
    void updateTeacherSubject(TeacherSubject teacherSubject) throws Exception;
    void deleteTeacherSubject(TeacherSubject teacherSubject) throws Exception;
    TeacherSubject getTeacherSubject(int idTeacherSubject) throws Exception;
    List<TeacherSubject> getTeachersSubjects() throws Exception;
    
    default void release() throws Exception{
        
    }
}
