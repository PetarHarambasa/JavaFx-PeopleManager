/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import static hr.algebra.dao.sql.HibernateFactory.SELECT_TEACHERS;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "Teacher")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = SELECT_TEACHERS, query = "SELECT t FROM Teacher t")})
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDTeacher")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDTeacher;
    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "Email")
    private String email;
    @Lob
    @Column(name = "Picture")
    private byte[] picture;
    @OneToMany(mappedBy = "teacherID", orphanRemoval = true)
    private Collection<TeacherStudent> teacherStudentCollection;
    @OneToMany(mappedBy = "teacherID", orphanRemoval = true)
    private Collection<TeacherSubject> teacherSubjectCollection;

    public Teacher() {
    }

    public Teacher(Integer iDTeacher) {
        this.iDTeacher = iDTeacher;
    }

    public Teacher(Integer iDTeacher, String firstName, String lastName, String email) {
        this.iDTeacher = iDTeacher;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getIDTeacher() {
        return iDTeacher;
    }

    public void setIDTeacher(Integer iDTeacher) {
        this.iDTeacher = iDTeacher;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @XmlTransient
    public Collection<TeacherStudent> getTeacherStudentCollection() {
        return teacherStudentCollection;
    }

    public void setTeacherStudentCollection(Collection<TeacherStudent> teacherStudentCollection) {
        this.teacherStudentCollection = teacherStudentCollection;
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
        hash += (iDTeacher != null ? iDTeacher.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Teacher)) {
            return false;
        }
        Teacher other = (Teacher) object;
        if ((this.iDTeacher == null && other.iDTeacher != null) || (this.iDTeacher != null && !this.iDTeacher.equals(other.iDTeacher))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public Teacher(Teacher data) {
        updateDetails(data);
    }

    public void updateDetails(Teacher data) {
        firstName = data.firstName;
        lastName = data.lastName;
        email = data.email;
        picture = data.picture;
    }
}
