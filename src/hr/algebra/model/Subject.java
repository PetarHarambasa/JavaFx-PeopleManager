/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import static hr.algebra.dao.sql.HibernateFactory.SELECT_SUBJECTS;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "Subject")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = SELECT_SUBJECTS, query = "SELECT s FROM Subject s")})
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDSubject")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDSubject;
    @Column(name = "Name")
    private String name;
    @OneToMany(mappedBy = "subjectID", orphanRemoval = true)
    private Collection<StudentSubject> studentSubjectCollection;
    @OneToMany(mappedBy = "subjectID", orphanRemoval = true)
    private Collection<TeacherSubject> teacherSubjectCollection;

    public Subject() {
    }

    public Subject(Integer iDSubject) {
        this.iDSubject = iDSubject;
    }

    public Subject(Integer iDSubject, String name) {
        this.iDSubject = iDSubject;
        this.name = name;
    }

    public Integer getIDSubject() {
        return iDSubject;
    }

    public void setIDSubject(Integer iDSubject) {
        this.iDSubject = iDSubject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<StudentSubject> getStudentSubjectCollection() {
        return studentSubjectCollection;
    }

    public void setStudentSubjectCollection(Collection<StudentSubject> studentSubjectCollection) {
        this.studentSubjectCollection = studentSubjectCollection;
    }

    @XmlTransient
    public Collection<TeacherSubject> getTeacherSubjectCollection() {
        return teacherSubjectCollection;
    }

    public void setTeacherSubjectCollection(Collection<TeacherSubject> teacherSubjectCollection) {
        this.teacherSubjectCollection = teacherSubjectCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDSubject != null ? iDSubject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.iDSubject == null && other.iDSubject != null) || (this.iDSubject != null && !this.iDSubject.equals(other.iDSubject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public Subject(Subject data) {
        updateDetails(data);
    }

    public void updateDetails(Subject data) {
        name = data.name;
    }
}
