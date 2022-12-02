/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import static hr.algebra.dao.sql.HibernateFactory.SELECT_TEACHERSSTUDENTS;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "TeacherStudent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = SELECT_TEACHERSSTUDENTS, query = "SELECT t FROM TeacherStudent t")})
public class TeacherStudent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDTeacherStudent")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDTeacherStudent;
    @JoinColumn(name = "StudentID", referencedColumnName = "IDStudent")
    @ManyToOne
    private Student studentID;
    @JoinColumn(name = "TeacherID", referencedColumnName = "IDTeacher")
    @ManyToOne
    private Teacher teacherID;

    public TeacherStudent() {
    }

    public TeacherStudent(Integer iDTeacherStudent) {
        this.iDTeacherStudent = iDTeacherStudent;
    }

    public TeacherStudent(Integer iDTeacherStudent, Teacher teacherID, Student studentID) {
        this.iDTeacherStudent = iDTeacherStudent;
        this.teacherID = teacherID;
        this.studentID = studentID;
    }

    public Integer getIDTeacherStudent() {
        return iDTeacherStudent;
    }

    public void setIDTeacherStudent(Integer iDTeacherStudent) {
        this.iDTeacherStudent = iDTeacherStudent;
    }

    public Student getStudentID() {
        return studentID;
    }

    public void setStudentID(Student studentID) {
        this.studentID = studentID;
    }

    public Teacher getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(Teacher teacherID) {
        this.teacherID = teacherID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDTeacherStudent != null ? iDTeacherStudent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeacherStudent)) {
            return false;
        }
        TeacherStudent other = (TeacherStudent) object;
        if ((this.iDTeacherStudent == null && other.iDTeacherStudent != null) || (this.iDTeacherStudent != null && !this.iDTeacherStudent.equals(other.iDTeacherStudent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.TeacherStudent[ iDTeacherStudent=" + iDTeacherStudent + " ]";
    }

    public TeacherStudent(TeacherStudent data) {
        updateDetails(data);
    }

    public void updateDetails(TeacherStudent data) {
        teacherID = data.teacherID;
        studentID = data.studentID;
    }
}
