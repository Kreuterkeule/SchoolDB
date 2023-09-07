package com.kreuterkeule.SchoolDB.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;

    private String _name;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(
            name = "classes_class_teachers",
            joinColumns = @JoinColumn(name = "class_teacher_id", referencedColumnName = "_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id", referencedColumnName = "_id"))
    private Set<TeacherEntity> _class_teachers = new HashSet<>();

    @OneToMany(mappedBy = "_class", cascade = CascadeType.DETACH)
    private Set<StudentEntity> _students = new HashSet<>();

    public void add_teacher(TeacherEntity teacher) {
        this._class_teachers.add(teacher);
    }
}
