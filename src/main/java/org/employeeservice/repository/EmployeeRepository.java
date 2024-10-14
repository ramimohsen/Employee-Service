package org.employeeservice.repository;


import org.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    @Query(value = """
                WITH RECURSIVE supervisor_chain AS (
                    SELECT employee_id, supervisor_id
                    FROM employee
                    WHERE employee_id = :employeeId
                    UNION ALL
                    SELECT e.employee_id, e.supervisor_id
                    FROM employee e
                    INNER JOIN supervisor_chain sc ON e.supervisor_id = sc.employee_id
                )
                SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END
                FROM supervisor_chain
                WHERE employee_id = :supervisorId;
            """, nativeQuery = true)
    boolean hasCircularSupervisorRelationship(@Param("employeeId") Long employeeId, @Param("supervisorId") Long supervisorId);

}
