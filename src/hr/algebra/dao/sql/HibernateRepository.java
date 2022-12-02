/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.model.Student;
import hr.algebra.model.StudentSubject;
import hr.algebra.model.Subject;
import hr.algebra.model.Teacher;
import hr.algebra.model.TeacherStudent;
import hr.algebra.model.TeacherSubject;
import java.util.List;
import javax.persistence.EntityManager;

public class HibernateRepository implements Repository {

    @Override
    public void release() throws Exception {
        HibernateFactory.release();
    }

    @Override
    public int addStudent(Student data) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            Student student = new Student(data);
            em.persist(student);
            em.getTransaction().commit();

            return student.getIDStudent();
        }
    }

    @Override
    public void updateStudent(Student student) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(Student.class, student.getIDStudent()).updateDetails(student);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteStudent(Student student) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            
            em.remove(em.contains(student) ? student : em.merge(student));

            em.getTransaction().commit();

        }
    }

    @Override
    public Student getStudent(int idStudent) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.find(Student.class, idStudent);
        }
    }

    @Override
    public List<Student> getStudents() throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.createNamedQuery(HibernateFactory.SELECT_STUDENTS).getResultList();
        }
    }

    @Override
    public int addTeacher(Teacher data) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            Teacher teacher = new Teacher(data);
            em.persist(teacher);
            em.getTransaction().commit();

            return teacher.getIDTeacher();
        }
    }

    @Override
    public void updateTeacher(Teacher teacher) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(Teacher.class, teacher.getIDTeacher()).updateDetails(teacher);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteTeacher(Teacher teacher) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();

            em.remove(em.contains(teacher) ? teacher : em.merge(teacher));

            em.getTransaction().commit();

        }
    }

    @Override
    public Teacher getTeacher(int idTeacher) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.find(Teacher.class, idTeacher);
        }
    }

    @Override
    public List<Teacher> getTeachers() throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.createNamedQuery(HibernateFactory.SELECT_TEACHERS).getResultList();
        }
    }

    @Override
    public int addSubject(Subject data) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            Subject subject = new Subject(data);
            em.persist(subject);
            em.getTransaction().commit();

            return subject.getIDSubject();
        }
    }

    @Override
    public void updateSubject(Subject subject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(Subject.class, subject.getIDSubject()).updateDetails(subject);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteSubject(Subject subject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();

            em.remove(em.contains(subject) ? subject : em.merge(subject));

            em.getTransaction().commit();

        }
    }

    @Override
    public Subject getSubject(int idSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.find(Subject.class, idSubject);
        }
    }

    @Override
    public List<Subject> getSubjects() throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.createNamedQuery(HibernateFactory.SELECT_SUBJECTS).getResultList();
        }
    }

    @Override
    public int addStudentSubject(StudentSubject data) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            StudentSubject studentSubject = new StudentSubject(data);
            em.persist(studentSubject);
            em.getTransaction().commit();

            return studentSubject.getIDStudentSubject();
        }
    }

    @Override
    public void updateStudentSubject(StudentSubject studentSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(StudentSubject.class, studentSubject.getIDStudentSubject()).updateDetails(studentSubject);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteStudentSubject(StudentSubject studentSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();

            em.remove(em.contains(studentSubject) ? studentSubject : em.merge(studentSubject));

            em.getTransaction().commit();

        }
    }

    @Override
    public StudentSubject getStudentSubject(int idStudentSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.find(StudentSubject.class, idStudentSubject);
        }
    }

    @Override
    public List<StudentSubject> getStudentsSubjects() throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.createNamedQuery(HibernateFactory.SELECT_STUDENTSSUBJETS).getResultList();
        }
    }

    @Override
    public int addTeacherStudent(TeacherStudent data) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            TeacherStudent teacherStudent = new TeacherStudent(data);
            em.persist(teacherStudent);
            em.getTransaction().commit();

            return teacherStudent.getIDTeacherStudent();
        }
    }

    @Override
    public void updateTeacherStudent(TeacherStudent teacherStudent) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(TeacherStudent.class, teacherStudent.getIDTeacherStudent()).updateDetails(teacherStudent);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteTeacherStudent(TeacherStudent teacherStudent) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();

            em.remove(em.contains(teacherStudent) ? teacherStudent : em.merge(teacherStudent));

            em.getTransaction().commit();

        }
    }

    @Override
    public TeacherStudent getTeacherStudent(int idTeacherStudent) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.find(TeacherStudent.class, idTeacherStudent);
        }
    }

    @Override
    public List<TeacherStudent> getTeachersStudents() throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.createNamedQuery(HibernateFactory.SELECT_TEACHERSSTUDENTS).getResultList();
        }
    }

    @Override
    public int addTeacherSubject(TeacherSubject data) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();
            TeacherSubject teacherSubject = new TeacherSubject(data);
            em.persist(teacherSubject);
            em.getTransaction().commit();

            return teacherSubject.getIDTeacherSubject();
        }
    }

    @Override
    public void updateTeacherSubject(TeacherSubject teacherSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(TeacherSubject.class, teacherSubject.getIDTeacherSubject()).updateDetails(teacherSubject);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteTeacherSubject(TeacherSubject teacherSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            em.getTransaction().begin();

            em.remove(em.contains(teacherSubject) ? teacherSubject : em.merge(teacherSubject));

            em.getTransaction().commit();

        }
    }

    @Override
    public TeacherSubject getTeacherSubject(int idTeacherSubject) throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.find(TeacherSubject.class, idTeacherSubject);
        }
    }

    @Override
    public List<TeacherSubject> getTeachersSubjects() throws Exception {
        try (EntityManagerWrapper emw = HibernateFactory.getEntityManagerFactory()) {
            EntityManager em = emw.get();

            return em.createNamedQuery(HibernateFactory.SELECT_TEACHERSSUBJECTS).getResultList();
        }
    }
}
