/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Petar
 */
public class HibernateFactory {

    private static final String PERSISTANCE_UNIT = "CRUDAppPU";
    public static final String SELECT_STUDENTS = "Student.findAll";
    public static final String SELECT_SUBJECTS = "Subject.findAll";
    public static final String SELECT_TEACHERS = "Teacher.findAll";
    public static final String SELECT_STUDENTSSUBJETS = "StudentSubject.findAll";
    public static final String SELECT_TEACHERSSTUDENTS = "TeacherStudent.findAll";
    public static final String SELECT_TEACHERSSUBJECTS = "TeacherSubject.findAll";
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT);
    
    public static EntityManagerWrapper getEntityManagerFactory(){
        return new EntityManagerWrapper(EMF.createEntityManager());
    }
    
    public static void release(){
        EMF.close();
    }
    
    public HibernateFactory() {
    }
}
