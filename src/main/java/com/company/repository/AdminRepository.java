package com.company.repository;

import com.company.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    boolean existsByCode(String code);
    Admin findAdminByCode(String code);
    Admin findAdminByUserName(String userName);
    void deleteAdminByUserName(String userName);
}
