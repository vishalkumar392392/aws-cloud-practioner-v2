package com.aws.cloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aws.cloud.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
