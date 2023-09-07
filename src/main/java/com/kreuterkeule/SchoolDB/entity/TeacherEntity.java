package com.kreuterkeule.SchoolDB.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kreuterkeule.SchoolDB.enums.CoursesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "teachers")
public class TeacherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private String _first_name;
    private String _last_name;

    @ElementCollection(targetClass = CoursesEnum.class)
    Collection<CoursesEnum> _courses = new ArrayList<>();

    @ManyToMany(mappedBy = "_class_teachers")
    @JsonIgnore
    private List<ClassEntity> _classes = new ArrayList<>(); // TODO: make deletable (for now it violates foreign key constraint)

    public List<String> get_the_classes() { // this method is used when converting to JSON
        return this._classes.stream().map(ClassEntity::get_name).collect(Collectors.toList());
    }

    public void add_course(String course) {
        this._courses.add(CoursesEnum.valueOf(course.toUpperCase()));
    }

    @Override
    public String toString() {
        return "TeacherEntity{" +
                "_id=" + _id +
                ", _first_name='" + _first_name + '\'' +
                ", _last_name='" + _last_name + '\'' +
                ", _courses=" + _courses +
                ", _classes=" + _classes +
                '}';
    }
}
