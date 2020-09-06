package com.springApp.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "requests")
public class RequestEntity implements Comparable<RequestEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Column
    Integer root;

    @NotNull
    @Column
    Date date;

    @NotNull
    @Column
    String status;

    @NotNull
    @Column
    Integer client;

    @NotNull
    @Column
    Integer ticket;

    @Override
    public int compareTo(RequestEntity req) {
        return (int) (this.getDate().getTime() - req.getDate().getTime());
    }
}
