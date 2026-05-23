package com.contractmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionAnswerResponseDTO {

    private String contractId;
    private String contractName;
    private String question;
    private String answer;
    private int score;
}