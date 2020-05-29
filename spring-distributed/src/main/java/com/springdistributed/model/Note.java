package com.springdistributed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Manan Prajapati
 */
@AllArgsConstructor
@Getter
@Setter
public class Note {
    private int id;
    private String name;
    private String description;
}
