package com.arsalan.ratelimit.controller;


import com.arsalan.ratelimit.model.Students;
import com.arsalan.ratelimit.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * After the RateLimitInterceptor has verified the remaining quota, request is passed on to the controller
     */
    @GetMapping
    public ResponseEntity<Students> getStudents() {
        return ResponseEntity.ok(studentService.getStudents());
    }

}
