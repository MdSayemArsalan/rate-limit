package com.arsalan.ratelimit.service;


import com.arsalan.ratelimit.model.Student;
import com.arsalan.ratelimit.model.Students;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    /**
     * Student's data can be fetched from a database (eg MongoDB, etc), but for the sake of simplicity of this project,
     * returning a hard-coded list of students.
     */
    public Students getStudents() {

        Student student1 = new Student(1l, "Md Sayem Arsalan", "sayem.arsalan@gmai.com");
        Student student2 = new Student(2l, "Maryam Sidrah", "maryam.sidrah@gmai.com");
        Student student3 = new Student(3l, "Jamal Qasmi", "jamal.qasmi@gmai.com");

        return new Students(Arrays.asList(student1, student2, student3));
    }

}
