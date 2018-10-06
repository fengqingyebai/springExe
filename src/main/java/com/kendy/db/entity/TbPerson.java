package com.kendy.db.entity;

import javax.persistence.*;

@Table(name = "tb_person")
public class TbPerson extends GenericEntity {
    @Id
    @Column(name = "person_id")
    private String personId;

    @Column(name = "person_name")
    private String personName;

    private Integer age;

    /**
     * @return person_id
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId
     */
    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
    }

    /**
     * @return person_name
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName
     */
    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}