package org.employeeservice.service;

import org.employeeservice.dto.*;
import org.employeeservice.exception.custom.EmployeeAlreadyExistException;
import org.employeeservice.exception.custom.EmployeeHasSubordinatesException;
import org.employeeservice.exception.custom.InvalidSupervisorAssignmentException;
import org.employeeservice.exception.custom.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request) throws EmployeeAlreadyExistException;

    EmployeeResponse updateEmployee(UpdateEmployeeRequest request, Long employeeId) throws ResourceNotFoundException, EmployeeAlreadyExistException;

    EmployeeDetailsResponse getEmployeeDetailsById(Long employeeId) throws ResourceNotFoundException;

    void deleteEmployee(Long employeeId) throws ResourceNotFoundException, EmployeeHasSubordinatesException;

    AssignEmployeeResponse assignEmployee(Long employeeId, Long supervisorId) throws ResourceNotFoundException, InvalidSupervisorAssignmentException;

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    List<EmployeeResponse> searchEmployeeByEmail(String email);
}
