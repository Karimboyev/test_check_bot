package com.company.repository;

import com.company.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    boolean existsByCode(String code);
    boolean existsByUserName(String userName);
    Admin findAdminByCode(String code);
    Admin findAdminByUserName(String userName);
    @Transactional
    void deleteAdminByUserName(String userName);
}
