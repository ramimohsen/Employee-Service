package org.employeeservice.exception.custom;

public class EmployeeHasSubordinatesException extends Exception {
    public EmployeeHasSubordinatesException(String message) {
        super(message);
    }
}
