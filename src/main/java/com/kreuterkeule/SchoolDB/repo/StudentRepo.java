package com.kreuterkeule.SchoolDB.repo;

import com.kreuterkeule.SchoolDB.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
}
