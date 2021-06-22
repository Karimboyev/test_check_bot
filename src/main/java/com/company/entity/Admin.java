package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private Long chatId;

    private String code;

    private String answers;

    public Admin(String userName, Long chatId, String code, String answers) {
        this.userName = userName;
        this.chatId = chatId;
        this.code = code;
        this.answers = answers;
    }
}
