package com.company.service;

import com.company.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAdminRepository {
    public static AdminRepository adminRepository;

    public GetAdminRepository(AdminRepository adminRepository) {
        GetAdminRepository.adminRepository = adminRepository;
    }
}
