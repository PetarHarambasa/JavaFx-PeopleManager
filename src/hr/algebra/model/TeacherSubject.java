/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import static hr.algebra.dao.sql.HibernateFactory.SELECT_TEACHERSSUBJECTS;
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
@Table(name = "TeacherSubject")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = SELECT_TEACHERSSUBJECTS, query = "SELECT t FROM TeacherSubject t")})
public class TeacherSubject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDTeacherSubject")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDTeacherSubject;
    @JoinColumn(name = "SubjectID", referencedColumnName = "IDSubject")
    @ManyToOne
    private Subject subjectID;
    @JoinColumn(name = "TeacherID", referencedColumnName = "IDTeacher")
    @ManyToOne
    private Teacher teacherID;

    public TeacherSubject() {
    }

    public TeacherSubject(Integer iDTeacherSubject) {
        this.iDTeacherSubject = iDTeacherSubject;
    }

    public TeacherSubject(Integer iDTeacherSubject, Teacher teacherID, Subject subjectID) {
        this.iDTeacherSubject = iDTeacherSubject;
        this.teacherID = teacherID;
        this.subjectID = subjectID;
    }

    public Integer getIDTeacherSubject() {
        return iDTeacherSubject;
    }

    public void setIDTeacherSubject(Integer iDTeacherSubject) {
        this.iDTeacherSubject = iDTeacherSubject;
    }

    public Subject getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(Subject subjectID) {
        this.subjectID = subjectID;
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
        hash += (iDTeacherSubject != null ? iDTeacherSubject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeacherSubject)) {
            return false;
        }
        TeacherSubject other = (TeacherSubject) object;
        if ((this.iDTeacherSubject == null && other.iDTeacherSubject != null) || (this.iDTeacherSubject != null && !this.iDTeacherSubject.equals(other.iDTeacherSubject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.TeacherSubject[ iDTeacherSubject=" + iDTeacherSubject + " ]";
    }
    public TeacherSubject(TeacherSubject data) {
        updateDetails(data);
    }

    public void updateDetails(TeacherSubject data) {
        teacherID = data.teacherID;
        subjectID = data.subjectID;
    }
}
