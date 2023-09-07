package com.kreuterkeule.SchoolDB.controller;

import com.kreuterkeule.SchoolDB.entity.ClassEntity;
import com.kreuterkeule.SchoolDB.entity.StudentEntity;
import com.kreuterkeule.SchoolDB.entity.TeacherEntity;
import com.kreuterkeule.SchoolDB.repo.ClassRepo;
import com.kreuterkeule.SchoolDB.repo.StudentRepo;
import com.kreuterkeule.SchoolDB.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/db")
public class DBControllerGet {

    private final ClassRepo classRepo;
    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;

    @Autowired
    public DBControllerGet(ClassRepo classRepo, StudentRepo studentRepo, TeacherRepo teacherRepo) {
        this.classRepo = classRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassEntity>> getClasses() {

        return new ResponseEntity<>(classRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentEntity>> getStudents() {
        return new ResponseEntity<>(studentRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherEntity>> getTeachers() {
        System.out.println(teacherRepo.findAll());
        return new ResponseEntity<>(teacherRepo.findAll(), HttpStatus.OK);
    }

}
