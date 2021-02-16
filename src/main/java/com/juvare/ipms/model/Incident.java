package com.juvare.ipms.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "start_date")
    private Date startDate;

    public Incident() { }

    public Incident(String name, Date startDate){
        this.name = name;
        this.startDate = startDate;
    }
}
