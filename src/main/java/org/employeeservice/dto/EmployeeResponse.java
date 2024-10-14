package org.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    private Long employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String position;

    private String supervisorName;

    private LocalDateTime createdAt;
}
