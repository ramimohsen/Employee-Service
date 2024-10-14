-- Create the employee table
CREATE TABLE employee
(
    employee_id   BIGSERIAL PRIMARY KEY,               -- IDENTITY equivalent for auto-increment
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(255) UNIQUE NOT NULL,        -- Unique and not null constraint
    position      VARCHAR(255),
    supervisor_id BIGINT,                              -- Supervisor reference
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Creation timestamp
    CONSTRAINT fk_supervisor FOREIGN KEY (supervisor_id) REFERENCES employee (employee_id) ON DELETE SET NULL
);
