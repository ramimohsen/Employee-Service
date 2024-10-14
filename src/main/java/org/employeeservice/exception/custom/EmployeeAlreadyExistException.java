package org.employeeservice.exception.custom;

public class EmployeeAlreadyExistException extends Exception {
    public EmployeeAlreadyExistException(String message) {
        super(message);
    }

}
