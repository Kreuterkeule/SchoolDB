package com.kreuterkeule.SchoolDB.dto;

import lombok.Data;

import java.util.List;

@Data
public class PutTeacherDto {

    public String firstname;
    public String lastname;
    public List<Long> class_ids;
    public List<String> courses;

}
