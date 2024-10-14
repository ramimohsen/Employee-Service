package org.employeeservice.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.employeeservice.dto.*;
import org.employeeservice.exception.custom.EmployeeAlreadyExistException;
import org.employeeservice.exception.custom.EmployeeHasSubordinatesException;
import org.employeeservice.exception.custom.InvalidSupervisorAssignmentException;
import org.employeeservice.exception.custom.ResourceNotFoundException;
import org.employeeservice.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/api/employees")
@Tag(name = "Employee Controller", description = "Employee controller")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public EmployeeResponse getEmployeeById(@PathVariable("employeeId") Long employeeId) throws ResourceNotFoundException {
        return this.employeeService.getEmployeeById(employeeId);
    }

    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest) throws EmployeeAlreadyExistException {
        return this.employeeService.createEmployee(createEmployeeRequest);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("employeeId") Long employeeId) throws ResourceNotFoundException,
            EmployeeHasSubordinatesException {
        this.employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{employeeId}/assign-supervisor")
    public AssignEmployeeResponse assignEmployeeToSupervisor(@PathVariable("employeeId") Long employeeId,
                                                             @RequestBody @Valid AssignEmployeeRequest assignEmployeeRequest) throws InvalidSupervisorAssignmentException, ResourceNotFoundException {
        return this.employeeService.assignEmployee(employeeId, assignEmployeeRequest.getSupervisorId());
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable Long employeeId,
                                           @RequestBody UpdateEmployeeRequest request) throws ResourceNotFoundException {
        return this.employeeService.updateEmployee(request, employeeId);
    }
}