package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "def_identity_type")
public class IdentityType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdentityType;

    @Size(min = 1, max = 256)
    @Column(name = "name")
    private String name;

    public Long getIdIdentityType() {
        return idIdentityType;
    }

    public void setIdIdentityType(Long idIdentityType) {
        this.idIdentityType = idIdentityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
