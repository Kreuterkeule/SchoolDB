package com.kreuterkeule.SchoolDB.controller;

import com.kreuterkeule.SchoolDB.dto.*;
import com.kreuterkeule.SchoolDB.entity.ClassEntity;
import com.kreuterkeule.SchoolDB.entity.StudentEntity;
import com.kreuterkeule.SchoolDB.entity.TeacherEntity;
import com.kreuterkeule.SchoolDB.enums.CoursesEnum;
import com.kreuterkeule.SchoolDB.enums.WPEnum;
import com.kreuterkeule.SchoolDB.repo.ClassRepo;
import com.kreuterkeule.SchoolDB.repo.StudentRepo;
import com.kreuterkeule.SchoolDB.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/db")
public class DBControllerUpdate {

    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
    private final ClassRepo classRepo;

    @Autowired
    public DBControllerUpdate(StudentRepo studentRepo, TeacherRepo teacherRepo, ClassRepo classRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.classRepo = classRepo;
    }

    @PatchMapping("student")
    public ResponseEntity<StudentEntity> putStudent(@RequestBody PatchStudentDto patchStudentDto) {
        StudentEntity student = studentRepo.findById(patchStudentDto.id).orElse(null);
        if (student == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        student.set_address(patchStudentDto.address);
        student.set_age(patchStudentDto.age);
        student.set_firstname(patchStudentDto.firstname);
        student.set_lastname(patchStudentDto.lastname);
        student.set_wp(new ArrayList<>());
        if (patchStudentDto.wp != null) {
            if (!patchStudentDto.wp.isEmpty()) {
                for (String wp : patchStudentDto.wp) {
                    student.add_wp(WPEnum.valueOf(wp.toUpperCase()));
                }
            }
        }
        if (patchStudentDto.class_id != null) {
            ClassEntity the_class = classRepo.findById(patchStudentDto.class_id).orElse(null);
            if (the_class == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            student.set_class(the_class);
        }
        studentRepo.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PatchMapping("class")
    public ResponseEntity<ClassEntity> putClass(@RequestBody PatchClassDto patchClassDto) {
        ClassEntity the_class = classRepo.findById(patchClassDto.id).orElse(null);
        if (the_class == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        the_class.set_name(patchClassDto.name);
        List<StudentEntity> students = new ArrayList<>();
        if (patchClassDto.student_ids != null) {
            if (!patchClassDto.student_ids.isEmpty()) {
                for (Long id : patchClassDto.student_ids) {
                    StudentEntity student = studentRepo.findById(id).orElse(null);
                    if (student == null) {
                        // Student does not exist in database
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                    students.add(student);
                }
                the_class.set_students(new HashSet<>(students));
            }
        }
        if (patchClassDto.teacher_ids != null) {
            if (!patchClassDto.teacher_ids.isEmpty()) {
                for (Long id : patchClassDto.teacher_ids) {
                    TeacherEntity teacher = teacherRepo.findById(id).orElse(null);
                    if (teacher == null) {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                    the_class.add_teacher(teacher);
                }
            }
        }
        classRepo.save(the_class);
        if (!students.isEmpty()) {
            for (StudentEntity student : students) {
                student.set_class(the_class);
                studentRepo.save(student);
            }
        }
        return new ResponseEntity<>(the_class, HttpStatus.CREATED);
    }

    @PatchMapping("teacher")
    public ResponseEntity<TeacherEntity> putTeacher(@RequestBody PatchTeacherDto patchTeacherDto) {
        TeacherEntity teacher = teacherRepo.findById(patchTeacherDto.id).orElse(null);
        if (teacher == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        teacher.set_first_name(patchTeacherDto.firstname);
        teacher.set_last_name(patchTeacherDto.lastname);
        if (patchTeacherDto.courses != null) {
            if (!patchTeacherDto.courses.isEmpty()) {
                List<CoursesEnum> courses = new ArrayList<>();
                for (String course : patchTeacherDto.courses) {
                    courses.add(CoursesEnum.valueOf(course));
                }
                teacher.set_courses(courses);
            }
        }
        List<ClassEntity> classes = new ArrayList<>();
        if (patchTeacherDto.class_ids != null) {
            if (!patchTeacherDto.class_ids.isEmpty()) {
                for (Long id : patchTeacherDto.class_ids) {
                    ClassEntity the_class = classRepo.findById(id).orElse(null);
                    if (the_class == null) {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                    classes.add(the_class);
                }
            }
        }
        teacherRepo.save(teacher);
        teacher.set_classes(new ArrayList<>());
        if (!classes.isEmpty()) {
            for (ClassEntity the_class : classes) {
                Set<TeacherEntity> tmp = the_class.get_class_teachers();
                tmp.add(teacher);
                the_class.set_class_teachers(tmp);
                classRepo.save(the_class);
            }
        }
        return new ResponseEntity<>(teacher, HttpStatus.CREATED);

    }

}
