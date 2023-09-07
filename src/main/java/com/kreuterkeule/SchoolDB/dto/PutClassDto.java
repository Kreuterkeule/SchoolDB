package com.kreuterkeule.SchoolDB.dto;

import lombok.Data;

import java.util.List;

@Data
public class PutClassDto {

    public String name;
    public List<Long> teacher_ids;
    public List<Long> student_ids;

}
