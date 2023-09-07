package com.kreuterkeule.SchoolDB.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kreuterkeule.SchoolDB.enums.WPEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "students")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;

    private String _firstname;
    private String _lastname;
    private String _address;
    private int _age;

    @ManyToOne
    @JoinColumn(name="class_id")
    @JsonIgnore
    private ClassEntity _class;

    @ElementCollection(targetClass = WPEnum.class)
    Collection<WPEnum> _wp = new ArrayList<>();

    public void add_wp(WPEnum wp) {
        this._wp.add(wp);
    }

    public String get_the_class() { // is used in JSON conversion
        return (this._class == null) ? null : this._class.get_name();
    }

}
