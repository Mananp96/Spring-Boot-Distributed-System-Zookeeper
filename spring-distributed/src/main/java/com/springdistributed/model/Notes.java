package com.springdistributed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Manan Prajapati
 */
@AllArgsConstructor
@Getter
public class Notes {
    private String group;
    private List<Note> notes;
}
