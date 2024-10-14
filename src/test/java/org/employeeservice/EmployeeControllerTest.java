package org.employeeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.employeeservice.dto.*;
import org.employeeservice.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetEmployeeDetailsById() throws Exception {

        Long employeeId = 1L;
        EmployeeResponse employeeResponse = new EmployeeResponse(employeeId, "John", "Doe", "john.doe@example.com", "Developer", "x", null);
        EmployeeResponse supervisorResponse = new EmployeeResponse(2L, "Jane", "Smith", "jane.smith@example.com", "Manager", "x", null);

        List<EmployeeResponse> subordinates = List.of(
                new EmployeeResponse(3L, "Sam", "Brown", "sam.brown@example.com", "Intern", "x", null)
        );

        EmployeeDetailsResponse employeeDetailsResponse = EmployeeDetailsResponse.builder()
                .employee(employeeResponse)
                .supervisor(supervisorResponse)
                .subordinates(subordinates)
                .build();

        given(employeeService.getEmployeeDetailsById(employeeId)).willReturn(employeeDetailsResponse);

        // When & Then
        mockMvc.perform(get("/v1/api/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employee.firstName").value("John"))
                .andExpect(jsonPath("$.employee.lastName").value("Doe"))
                .andExpect(jsonPath("$.supervisor.firstName").value("Jane"))
                .andExpect(jsonPath("$.subordinates[0].firstName").value("Sam"));
    }


    @Test
    void testGetAllEmployees() throws Exception {
        Page<EmployeeResponse> employeePage = new PageImpl<>(List.of(
                new EmployeeResponse(1L, "John", "Doe", "john.doe@example.com", "Developer", "x", null)
        ));

        given(employeeService.getAllEmployees(any())).willReturn(employeePage);

        // When & Then
        mockMvc.perform(get("/v1/api/employees?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateEmployee() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", "Doe", "john.doe@example.com", "Developer");
        EmployeeResponse employeeResponse = new EmployeeResponse(1L, "John", "Doe", "john.doe@example.com", "Developer", "x", null);

        given(employeeService.createEmployee(createEmployeeRequest)).willReturn(employeeResponse);

        mockMvc.perform(post("/v1/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testCreateEmployee_InvalidEmail() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", "Doe", "invalid-email", "Developer");

        mockMvc.perform(post("/v1/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Long employeeId = 1L;

        Mockito.doNothing().when(employeeService).deleteEmployee(employeeId);

        mockMvc.perform(delete("/v1/api/employees/{employeeId}", employeeId))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignEmployeeToSupervisor() throws Exception {
        AssignEmployeeRequest assignEmployeeRequest = new AssignEmployeeRequest(2L); // Supervisor ID
        AssignEmployeeResponse assignEmployeeResponse = AssignEmployeeResponse
                .builder()
                .employeeId(1L)
                .supervisorId(2L)
                .build();

        given(employeeService.assignEmployee(1L, 2L)).willReturn(assignEmployeeResponse);

        mockMvc.perform(put("/v1/api/employees/{employeeId}/assign-supervisor", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest("John", "Doe", "john.doe@example.com", "Senior Developer");
        EmployeeResponse employeeResponse = new EmployeeResponse(1L, "John", "Doe", "john.doe@example.com", "Senior Developer", "x", null);

        given(employeeService.updateEmployee(updateEmployeeRequest, 1L)).willReturn(employeeResponse);

        mockMvc.perform(put("/v1/api/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
