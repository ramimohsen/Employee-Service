package org.employeeservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.employeeservice.dto.EmployeeResponse;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> subordinates;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public EmployeeResponse toResponse() {
        return EmployeeResponse
                .builder()
                .employeeId(employeeId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .position(position)
                .supervisorName(supervisor != null ? supervisor.getFirstName().concat(" ").concat(supervisor.getLastName()) : null)
                .createdAt(createdAt)
                .build();
    }

}
