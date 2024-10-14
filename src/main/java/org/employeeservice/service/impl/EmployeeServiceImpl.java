package org.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.employeeservice.dto.*;
import org.employeeservice.entity.Employee;
import org.employeeservice.exception.custom.EmployeeAlreadyExistException;
import org.employeeservice.exception.custom.EmployeeHasSubordinatesException;
import org.employeeservice.exception.custom.InvalidSupervisorAssignmentException;
import org.employeeservice.exception.custom.ResourceNotFoundException;
import org.employeeservice.repository.EmployeeRepository;
import org.employeeservice.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final String NOT_FOUND = "Employee Not Found";

    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) throws EmployeeAlreadyExistException {

        if (Boolean.TRUE.equals(this.employeeRepository.existsByEmail(request.getEmail()))) {
            throw new EmployeeAlreadyExistException("Employee already exist");
        }

        Employee employee = Employee
                .builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .position(request.getPosition())
                .build();

        return this.employeeRepository.save(employee).toResponse();
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployee(UpdateEmployeeRequest request, Long employeeId) throws ResourceNotFoundException, EmployeeAlreadyExistException {

        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));

        if (request.getEmail() != null && !request.getEmail().trim().equals(employee.getEmail())) {
            if (this.employeeRepository.existsByEmail(request.getEmail().trim())) {
                throw new EmployeeAlreadyExistException("Employee with this email already exists");
            }
            employee.setEmail(request.getEmail().trim());
        }

        if (request.getFirstName() != null && !request.getFirstName().equals(employee.getFirstName())) {
            employee.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().equals(employee.getLastName())) {
            employee.setLastName(request.getLastName());
        }

        if (request.getPosition() != null && !request.getPosition().equals(employee.getPosition())) {
            employee.setPosition(request.getPosition());
        }

        return this.employeeRepository.save(employee).toResponse();
    }

    @Override
    public EmployeeDetailsResponse getEmployeeDetailsById(Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));

        EmployeeResponse employeeResponse = employee.toResponse();

        return EmployeeDetailsResponse
                .builder()
                .employee(employeeResponse)
                .supervisor(employee.getSupervisor() != null ? employee.getSupervisor().toResponse() : null)
                .subordinates(employee.getSubordinates().stream().map(Employee::toResponse).toList())
                .build();
    }

    @Transactional
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeHasSubordinatesException, ResourceNotFoundException {

        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));

        if (!employee.getSubordinates().isEmpty()) {
            throw new EmployeeHasSubordinatesException("Cannot delete employee with active subordinates");
        }

        this.employeeRepository.delete(employee);
    }


    @Transactional
    @Override
    public AssignEmployeeResponse assignEmployee(Long employeeId, Long supervisorId) throws InvalidSupervisorAssignmentException, ResourceNotFoundException {

        if (employeeId.equals(supervisorId)) {
            throw new InvalidSupervisorAssignmentException("An employee cannot be their own supervisor");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + employeeId + " not found"));


        Employee supervisor = employeeRepository.findById(supervisorId)
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor with ID " + supervisorId + " not found"));


        if (this.employeeRepository.hasCircularSupervisorRelationship(employeeId, supervisorId)) {
            throw new InvalidSupervisorAssignmentException("Circular supervisor relationship detected");
        }

        employee.setSupervisor(supervisor);

        Employee saved = employeeRepository.save(employee);

        return AssignEmployeeResponse
                .builder()
                .employeeId(saved.getEmployeeId())
                .supervisorId(supervisorId)
                .build();
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return this.employeeRepository.findAll(pageable).map(Employee::toResponse);
    }

    @Override
    public List<EmployeeResponse> searchEmployeeByEmail(String email) {

        List<Employee> employees = employeeRepository.findByEmailContainingIgnoreCase(email);

        return employees.stream()
                .map(Employee::toResponse).toList();
    }
}
