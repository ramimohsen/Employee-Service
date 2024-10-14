package org.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDetailsResponse {
    private EmployeeResponse employee;
    private EmployeeResponse supervisor; // Supervisor details (if any)
    private List<EmployeeResponse> subordinates; // Direct subordinates
}
