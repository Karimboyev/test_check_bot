package com.company.service;

import com.company.repository.PupilRepasitory;
import org.springframework.stereotype.Service;

@Service
public class GetPupilRepository {
    public static PupilRepasitory pupilRepasitory;

    public GetPupilRepository(PupilRepasitory pupilRepasitory) {
        GetPupilRepository.pupilRepasitory = pupilRepasitory;
    }

}
