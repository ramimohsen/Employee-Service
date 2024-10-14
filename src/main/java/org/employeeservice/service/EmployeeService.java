package org.employeeservice.service;

import org.employeeservice.dto.AssignEmployeeResponse;
import org.employeeservice.dto.CreateEmployeeRequest;
import org.employeeservice.dto.EmployeeResponse;
import org.employeeservice.dto.UpdateEmployeeRequest;
import org.employeeservice.exception.custom.EmployeeAlreadyExistException;
import org.employeeservice.exception.custom.EmployeeHasSubordinatesException;
import org.employeeservice.exception.custom.InvalidSupervisorAssignmentException;
import org.employeeservice.exception.custom.ResourceNotFoundException;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request) throws EmployeeAlreadyExistException;

    EmployeeResponse updateEmployee(UpdateEmployeeRequest request, Long employeeId) throws ResourceNotFoundException;

    EmployeeResponse getEmployeeById(Long employeeId) throws ResourceNotFoundException;

    void deleteEmployee(Long employeeId) throws ResourceNotFoundException, EmployeeHasSubordinatesException;

    AssignEmployeeResponse assignEmployee(Long employeeId, Long supervisorId) throws ResourceNotFoundException, InvalidSupervisorAssignmentException;

}
