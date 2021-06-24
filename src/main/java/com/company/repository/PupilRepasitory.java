package com.company.repository;

import com.company.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PupilRepasitory extends JpaRepository<Pupil,Integer> {
    boolean existsByFullNameAndCodeNot(String fullName,String code);
    List<Pupil> findAllByCode(String code);
    void deleteAllByCode(String code);
}
