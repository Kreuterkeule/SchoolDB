package com.kreuterkeule.SchoolDB.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatchStudentDto {

    public Long id;
    public String firstname;
    public String lastname;
    public String address;
    public int age;
    public List<String> wp;
    public Long class_id;
}