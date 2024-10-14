package org.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.employeeservice.dto.AssignEmployeeResponse;
import org.employeeservice.dto.CreateEmployeeRequest;
import org.employeeservice.dto.EmployeeResponse;
import org.employeeservice.dto.UpdateEmployeeRequest;
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
    public EmployeeResponse updateEmployee(UpdateEmployeeRequest request, Long employeeId) throws ResourceNotFoundException {

        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));

        if (request.getEmail() != null) {
            employee.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            employee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            employee.setLastName(request.getLastName());
        }
        if (request.getPosition() != null) {
            employee.setPosition(request.getPosition());
        }

        return this.employeeRepository.save(employee).toResponse();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) throws ResourceNotFoundException {
        return this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND))
                .toResponse();
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
}
