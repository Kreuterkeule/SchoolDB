package com.kreuterkeule.SchoolDB.repo;

import com.kreuterkeule.SchoolDB.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherEntity, Long> {
}
