package com.easybank.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private String apiPath;
    private String errorMessage;
    private String errorCode;
    private LocalDateTime errorTime;

}
