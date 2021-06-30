package com.company.repository;

import com.company.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PupilRepasitory extends JpaRepository<Pupil,Integer> {
    boolean existsByFullNameAndCode(String fullName,String code);
    boolean existsByCode(String code);
    List<Pupil> findAllByCodeOrderByTrueAnswersCountDesc(String code);
    @Transactional
    void deletePupilsByCode(String code);
}
