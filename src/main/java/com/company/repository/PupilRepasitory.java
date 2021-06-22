package com.company.repository;

import com.company.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PupilRepasitory extends JpaRepository<Pupil,Integer> {
    boolean existsByFullNameAndCodeNot(String fullName,String code);
    List<Pupil> findAllByCode(String code);
}
