package com.kreuterkeule.SchoolDB.controller;

import com.kreuterkeule.SchoolDB.dto.PutClassDto;
import com.kreuterkeule.SchoolDB.dto.PutStudentDto;
import com.kreuterkeule.SchoolDB.dto.PutTeacherDto;
import com.kreuterkeule.SchoolDB.entity.ClassEntity;
import com.kreuterkeule.SchoolDB.entity.StudentEntity;
import com.kreuterkeule.SchoolDB.entity.TeacherEntity;
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
public class DBControllerCreate {

    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
    private final ClassRepo classRepo;

    @Autowired
    public DBControllerCreate(StudentRepo studentRepo, TeacherRepo teacherRepo, ClassRepo classRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.classRepo = classRepo;
    }

    @PutMapping("student")
    public ResponseEntity<StudentEntity> putStudent(@RequestBody PutStudentDto putStudentDto) {
        StudentEntity student = new StudentEntity();
        student.set_address(putStudentDto.address);
        student.set_age(putStudentDto.age);
        student.set_firstname(putStudentDto.firstname);
        student.set_lastname(putStudentDto.lastname);
        student.set_wp(new ArrayList<>());
        if (putStudentDto.wp != null) {
            if (!putStudentDto.wp.isEmpty()) {
                for (String wp : putStudentDto.wp) {
                    student.add_wp(WPEnum.valueOf(wp.toUpperCase()));
                }
            }
        }
        if (putStudentDto.class_id != null) {
            ClassEntity the_class = classRepo.findById(putStudentDto.class_id).orElse(null);
            if (the_class == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            student.set_class(the_class);
        }
        studentRepo.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("class")
    public ResponseEntity<ClassEntity> putClass(@RequestBody PutClassDto putClassDto) {
        ClassEntity the_class = new ClassEntity();
        the_class.set_name(putClassDto.name);
        List<StudentEntity> students = new ArrayList<>();
        if (putClassDto.student_ids != null) {
            if (!putClassDto.student_ids.isEmpty()) {
                for (Long id : putClassDto.student_ids) {
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
        if (putClassDto.teacher_ids != null) {
            if (!putClassDto.teacher_ids.isEmpty()) {
                for (Long id : putClassDto.teacher_ids) {
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

    @PutMapping("teacher")
    public ResponseEntity<TeacherEntity> putTeacher(@RequestBody PutTeacherDto putTeacherDto) {
        TeacherEntity teacher = new TeacherEntity();
        teacher.set_first_name(putTeacherDto.firstname);
        teacher.set_last_name(putTeacherDto.lastname);
        if (putTeacherDto.courses != null) {
            if (!putTeacherDto.courses.isEmpty()) {
                for (String course : putTeacherDto.courses) {
                    teacher.add_course(course);
                }
            }
        }
        List<ClassEntity> classes = new ArrayList<>();
        if (putTeacherDto.class_ids != null) {
            if (!putTeacherDto.class_ids.isEmpty()) {
                for (Long id : putTeacherDto.class_ids) {
                    ClassEntity the_class = classRepo.findById(id).orElse(null);
                    if (the_class == null) {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                    classes.add(the_class);
                }
            }
        }
        teacherRepo.save(teacher);
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
