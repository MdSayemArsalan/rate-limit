package com.arsalan.ratelimit.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Students {

    private List<Student> students;

    public Students(List<Student> students) {
        this.students = students;
    }

}
