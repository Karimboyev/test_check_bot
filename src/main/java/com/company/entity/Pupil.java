package com.company.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pupil")
public class Pupil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private Integer trueAnswersCount;

    private String code;

    private LocalDateTime localDateTime=LocalDateTime.now();

    public Pupil(String fullName, Integer trueAnswersCount, String code) {
        this.fullName = fullName;
        this.trueAnswersCount = trueAnswersCount;
        this.code = code;
    }
}
