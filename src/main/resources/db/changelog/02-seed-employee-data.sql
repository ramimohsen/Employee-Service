-- Insert supervisors first (without supervisor_id)
INSERT INTO employee (first_name, last_name, email, position, created_at)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'CEO', '2024-01-01 09:00:00'),  -- employee_id = 1
    ('Jane', 'Smith', 'jane.smith@example.com', 'CTO', '2024-01-02 10:00:00'), -- employee_id = 2
    ('Robert', 'Johnson', 'robert.johnson@example.com', 'CFO', '2024-01-03 11:00:00'), -- employee_id = 3
    ('Emily', 'Davis', 'emily.davis@example.com', 'HR Manager', '2024-01-04 12:00:00'), -- employee_id = 4
    ('Michael', 'Brown', 'michael.brown@example.com', 'Sales Manager', '2024-01-05 13:00:00'); -- employee_id = 5

-- Insert subordinates with supervisor_id
INSERT INTO employee (first_name, last_name, email, position, supervisor_id, created_at)
VALUES
    ('Sophia', 'Garcia', 'sophia.garcia@example.com', 'Lead Developer', 1, '2024-01-06 09:00:00'),
    ('Liam', 'Martinez', 'liam.martinez@example.com', 'Marketing Manager', 1, '2024-01-07 10:00:00'),
    ('Isabella', 'Lopez', 'isabella.lopez@example.com', 'Operations Manager', 1, '2024-01-08 11:00:00'),

-- Employees reporting to 'Jane Smith' (CTO, employee_id = 2)
    ('Olivia', 'Wilson', 'olivia.wilson@example.com', 'Senior Developer', 2, '2024-01-09 09:00:00'),
    ('Noah', 'Clark', 'noah.clark@example.com', 'DevOps Engineer', 2, '2024-01-10 10:00:00'),
    ('Ava', 'Walker', 'ava.walker@example.com', 'Junior Developer', 2, '2024-01-11 11:00:00'),

-- Employees reporting to 'Robert Johnson' (CFO, employee_id = 3)
    ('William', 'Hall', 'william.hall@example.com', 'Financial Analyst', 3, '2024-01-12 09:00:00'),
    ('James', 'Allen', 'james.allen@example.com', 'Accountant', 3, '2024-01-13 10:00:00'),

-- Employees reporting to 'Emily Davis' (HR Manager, employee_id = 4)
    ('Mia', 'Young', 'mia.young@example.com', 'HR Coordinator', 4, '2024-01-14 09:00:00'),
    ('Ethan', 'Hernandez', 'ethan.hernandez@example.com', 'Recruiter', 4, '2024-01-15 10:00:00'),

-- Employees reporting to 'Michael Brown' (Sales Manager, employee_id = 5)
    ('Charlotte', 'King', 'charlotte.king@example.com', 'Sales Executive', 5, '2024-01-16 09:00:00'),
    ('Benjamin', 'Scott', 'benjamin.scott@example.com', 'Sales Representative', 5, '2024-01-17 10:00:00'),

-- Additional employees with different supervisors
    ('Amelia', 'Green', 'amelia.green@example.com', 'Product Manager', 2, '2024-01-18 09:00:00'),
    ('Elijah', 'Adams', 'elijah.adams@example.com', 'UX Designer', 2, '2024-01-19 10:00:00'),
    ('Lucas', 'Baker', 'lucas.baker@example.com', 'IT Support', 1, '2024-01-20 11:00:00'),
    ('Harper', 'Carter', 'harper.carter@example.com', 'Office Manager', 4, '2024-01-21 12:00:00');
