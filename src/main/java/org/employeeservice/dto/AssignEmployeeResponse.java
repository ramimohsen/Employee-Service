package org.employeeservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignEmployeeResponse {

    private Long employeeId;

    private Long supervisorId;

    @Builder.Default
    private final String message = "Employee Assigned Successfully";
}
