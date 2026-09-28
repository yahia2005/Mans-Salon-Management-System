CREATE TABLE Service (
    id INT AUTO_INCREMENT,
    name TEXT NOT NULL,
    price INT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Workers (
    id INT AUTO_INCREMENT,
    name TEXT NOT NULL,
    daily_salary INT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE worker_attendance (
    id INT AUTO_INCREMENT,
    worker_id INT NOT NULL,
    work_date DATE NOT NULL,
    is_present BOOL DEFAULT TRUE,
    PRIMARY KEY(id),
    FOREIGN KEY(worker_id) REFERENCES Workers(id)
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT,
    worker_id INT NOT NULL,
    service_id INT NOT NULL,
    price INT NOT NULL,
    order_date DATE NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(worker_id) REFERENCES Workers(id),
    FOREIGN KEY(service_id) REFERENCES Service(id)
);

CREATE TABLE expenses (
    id INT AUTO_INCREMENT,
    expense_date DATE NOT NULL,
    description TEXT,
    amount INT NOT NULL,
    PRIMARY KEY(id)
);

