package org.employeeservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.employeeservice.dto.*;
import org.employeeservice.exception.custom.EmployeeAlreadyExistException;
import org.employeeservice.exception.custom.EmployeeHasSubordinatesException;
import org.employeeservice.exception.custom.InvalidSupervisorAssignmentException;
import org.employeeservice.exception.custom.ResourceNotFoundException;
import org.employeeservice.exception.response.ErrorDetails;
import org.employeeservice.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Operation(summary = "Get employee details by ID")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = EmployeeResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/{employeeId}")
    public EmployeeDetailsResponse getEmployeeDetailsById(@PathVariable("employeeId") Long employeeId) throws ResourceNotFoundException {
        return this.employeeService.getEmployeeDetailsById(employeeId);
    }

    @Operation(summary = "Get all employees pageable")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = EmployeeResponse.class), mediaType = "application/json")})
    @GetMapping
    public Page<EmployeeResponse> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return this.employeeService.getAllEmployees(PageRequest.of(page, size));
    }


    @Operation(summary = "Create a new employee")
    @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = EmployeeResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest) throws EmployeeAlreadyExistException {
        return this.employeeService.createEmployee(createEmployeeRequest);
    }


    @Operation(summary = "Delete an employee")
    @ApiResponse(responseCode = "200", content = @Content)
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("employeeId") Long employeeId) throws ResourceNotFoundException,
            EmployeeHasSubordinatesException {
        this.employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Assign supervisor to an employee")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AssignEmployeeResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/{employeeId}/assign-supervisor")
    public AssignEmployeeResponse assignEmployeeToSupervisor(@PathVariable("employeeId") Long employeeId,
                                                             @RequestBody @Valid AssignEmployeeRequest assignEmployeeRequest) throws InvalidSupervisorAssignmentException, ResourceNotFoundException {
        return this.employeeService.assignEmployee(employeeId, assignEmployeeRequest.getSupervisorId());
    }

    @Operation(summary = "Update an existing employee")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = EmployeeResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable Long employeeId,
                                           @RequestBody UpdateEmployeeRequest request) throws ResourceNotFoundException {
        return this.employeeService.updateEmployee(request, employeeId);
    }
}