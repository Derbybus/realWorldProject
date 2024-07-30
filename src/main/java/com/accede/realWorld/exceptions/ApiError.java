package com.accede.realWorld.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {

    private String message;
    private List<String> details;
    private HttpStatus status;
    private LocalDateTime time;
}
