package com.aws.cloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aws.cloud.entity.Student;
import com.aws.cloud.repository.StudentRepository;

@RestController
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@PostMapping("/create")
	public Student create(@RequestBody Student student) {

		return studentRepository.save(student);

	}
	
	@GetMapping("/students")
	public List<Student> getStudents() {

		return studentRepository.findAll();

	}

}
