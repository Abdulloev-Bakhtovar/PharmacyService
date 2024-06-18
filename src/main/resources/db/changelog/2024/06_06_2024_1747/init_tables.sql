-- Таблица для хранения данных о аптеках
CREATE TABLE IF NOT EXISTS pharmacies (
    id		SERIAL 			PRIMARY KEY,
    name	VARCHAR(100) 	NOT NULL,
    address	VARCHAR(255) 	NOT NULL,
    phone	VARCHAR(15) 	NOT NULL
);

COMMENT ON TABLE  pharmacies IS 'Таблица для хранения данных о аптеках';
COMMENT ON COLUMN pharmacies.id IS 'Уникальный идентификатор аптеки';
COMMENT ON COLUMN pharmacies.name IS 'Название аптеки';
COMMENT ON COLUMN pharmacies.address IS 'Адрес аптеки';
COMMENT ON COLUMN pharmacies.phone IS 'Телефон аптеки';

CREATE INDEX idx_pharmacies_name ON pharmacies(name);

-- Таблица для хранения данных о лекарствах
CREATE TABLE IF NOT EXISTS medications (
    id              SERIAL 		PRIMARY KEY,
    name            VARCHAR 	NOT NULL,
    form            VARCHAR(50) NOT NULL,
    price           DECIMAL 	NOT NULL,
    expiration_date DATE 		NOT NULL
);

COMMENT ON TABLE medications IS 'Таблица для хранения данных о лекарствах';
COMMENT ON COLUMN medications.id IS 'Уникальный идентификатор лекарства';
COMMENT ON COLUMN medications.name IS 'Название лекарства';
COMMENT ON COLUMN medications.form IS 'Форма лекарства (таблетки, сироп и т.д.)';
COMMENT ON COLUMN medications.price IS 'Цена лекарства';
COMMENT ON COLUMN medications.expiration_date IS 'Срок годности лекарства';

CREATE INDEX idx_medications_name ON medications(name);

-- Таблица для хранения данных о сотрудниках
CREATE TABLE IF NOT EXISTS employees (
    id          SERIAL 			PRIMARY KEY,
    name        VARCHAR(255) 	NOT NULL,
    position    VARCHAR(50) 	NOT NULL,
    email       VARCHAR(100)	NOT NULL,
    pharmacy_id INT 			REFERENCES pharmacies(id) ON DELETE CASCADE
);

COMMENT ON TABLE employees IS 'Таблица для хранения данных о сотрудниках';
COMMENT ON COLUMN employees.id IS 'Уникальный идентификатор сотрудника';
COMMENT ON COLUMN employees.name IS 'Имя сотрудника';
COMMENT ON COLUMN employees.position IS 'Должность сотрудника';
COMMENT ON COLUMN employees.email IS 'Электронная почта сотрудника';
COMMENT ON COLUMN employees.pharmacy_id IS 'Идентификатор аптеки, в которой работает сотрудник';

CREATE INDEX idx_employees_name ON employees(name);

-- Таблица для хранения данных о покупателях
CREATE TABLE IF NOT EXISTS customers (
    id      SERIAL 			PRIMARY KEY,
    name    VARCHAR(255) 	NOT NULL,
    address VARCHAR(255) 	NOT NULL,
    phone   VARCHAR(15) 	NOT NULL
);

COMMENT ON TABLE customers IS 'Таблица для хранения данных о покупателях';
COMMENT ON COLUMN customers.id IS 'Уникальный идентификатор покупателя';
COMMENT ON COLUMN customers.name IS 'Имя покупателя';
COMMENT ON COLUMN customers.address IS 'Адрес покупателя';
COMMENT ON COLUMN customers.phone IS 'Телефонный номер покупателя';

CREATE INDEX idx_customers_phone ON customers(phone);

-- Таблица для хранения данных о заказах
CREATE TABLE IF NOT EXISTS orders (
    id            SERIAL 		PRIMARY KEY,
    customer_id   INT 			REFERENCES customers(id) ON DELETE CASCADE,
    employee_id   INT 			REFERENCES employees(id) ON DELETE CASCADE,
    pharmacy_id   INT 			REFERENCES pharmacies(id) ON DELETE CASCADE,
    medication_id INT 			REFERENCES medications(id) ON DELETE CASCADE,
    quantity      INT 			NOT NULL,
    total_amount  DECIMAL,
    order_date    DATE 			NOT NULL,
    status        VARCHAR(50) 	NOT NULL
);

COMMENT ON TABLE orders IS 'Таблица для хранения данных о заказах';
COMMENT ON COLUMN orders.id IS 'Уникальный идентификатор заказа';
COMMENT ON COLUMN orders.customer_id IS 'Идентификатор покупателя, связанного с заказом';
COMMENT ON COLUMN orders.employee_id IS 'Идентификатор сотрудника, связанного с заказом';
COMMENT ON COLUMN orders.pharmacy_id IS 'Идентификатор аптеки, связанной с заказом';
COMMENT ON COLUMN orders.medication_id IS 'Идентификатор лекарства, связанного с заказом';
COMMENT ON COLUMN orders.quantity IS 'Количество лекарства в заказе';
COMMENT ON COLUMN orders.total_amount IS 'Общая сумма заказа';
COMMENT ON COLUMN orders.order_date IS 'Дата заказа';
COMMENT ON COLUMN orders.status IS 'Статус заказа';

-- Таблица для связи многие ко многим между аптеками и лекарствами
CREATE TABLE IF NOT EXISTS pharmacy_medications (
    pharmacy_id   INT REFERENCES pharmacies(id) ON DELETE CASCADE,
    medication_id INT REFERENCES medications(id) ON DELETE CASCADE,
    quantity      INT NOT NULL,
    PRIMARY KEY (pharmacy_id, medication_id)
);

COMMENT ON TABLE pharmacy_medications IS 'Таблица для связи многие ко многим между аптеками и лекарствами';
COMMENT ON COLUMN pharmacy_medications.pharmacy_id IS 'Идентификатор аптеки';
COMMENT ON COLUMN pharmacy_medications.medication_id IS 'Идентификатор лекарства';
COMMENT ON COLUMN pharmacy_medications.quantity IS 'Количество лекарства в аптеке';
