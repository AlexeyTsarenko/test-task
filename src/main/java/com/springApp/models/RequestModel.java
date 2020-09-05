package com.springApp.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RequestModel {
    private Integer root;
    private Date date;
    private Integer client;
    private Integer ticket;
}
