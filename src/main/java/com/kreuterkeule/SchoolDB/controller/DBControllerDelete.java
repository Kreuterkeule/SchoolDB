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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/db")
public class DBControllerDelete {

    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
    private final ClassRepo classRepo;

    @Autowired
    public DBControllerDelete(StudentRepo studentRepo, TeacherRepo teacherRepo, ClassRepo classRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.classRepo = classRepo;
    }

    @DeleteMapping("class")
    public ResponseEntity<String> deleteClass(@RequestParam("id") Long id) {
        ClassEntity the_class = classRepo.findById(id).orElse(null);
        if (the_class == null) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        for (TeacherEntity teacher : the_class.get_class_teachers()) { // remove from relationship (clean up)
            List<ClassEntity> new_classes = teacher.get_classes();
            new_classes.remove(the_class);
            teacher.set_classes(new_classes);
            teacherRepo.save(teacher);
        }
        for (StudentEntity student : the_class.get_students()) { // remove from relationship (clean up)
            student.set_class(null);
            studentRepo.save(student);
        }
        classRepo.delete(the_class);
        return new ResponseEntity<>(the_class.get_name(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("teacher")
    public ResponseEntity<String> deleteTeacher(@RequestParam("id") Long id) {
        TeacherEntity teacher = teacherRepo.findById(id).orElse(null);
        if (teacher == null) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        for (ClassEntity the_class : teacher.get_classes()) { // remove from relationship (clean up)
            Set<TeacherEntity> new_teachers = the_class.get_class_teachers();
            new_teachers.remove(teacher);
            the_class.set_class_teachers(new_teachers);
            classRepo.save(the_class);
        }
        teacherRepo.delete(teacher);
        return new ResponseEntity<>(teacher.get_first_name(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("student")
    public ResponseEntity<String> deleteStudent(@RequestParam("id") Long id) {
        StudentEntity student = studentRepo.findById(id).orElse(null);
        if (student == null) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        // remove from relationship (clean up)
        ClassEntity the_class = student.get_class();
        if (the_class != null) {
            Set<StudentEntity> new_students = the_class.get_students();
            new_students.remove(student);
            the_class.set_students(new_students);
            classRepo.save(the_class);
        }

        studentRepo.delete(student);
        return new ResponseEntity<>(student.get_firstname(), HttpStatus.ACCEPTED);
    }

}
