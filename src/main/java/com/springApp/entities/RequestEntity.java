package com.springApp.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "requests")
public class RequestEntity implements Comparable<RequestEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    Integer root;

    @Column
    Date date;

    @Column
    String status;

    @Column
    Integer client;

    @Column
    Integer ticket;

    @Override
    public int compareTo(RequestEntity req) {
        return (int) (this.getDate().getTime() - req.getDate().getTime());
    }
}
