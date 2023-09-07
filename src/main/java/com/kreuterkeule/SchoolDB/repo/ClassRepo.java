package com.kreuterkeule.SchoolDB.repo;

import com.kreuterkeule.SchoolDB.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepo extends JpaRepository<ClassEntity, Long> {
    ClassEntity findBy_name(String theClass);
}

