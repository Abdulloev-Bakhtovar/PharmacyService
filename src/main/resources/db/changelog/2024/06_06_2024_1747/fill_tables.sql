INSERT INTO public.pharmacies (name, address, phone) VALUES
    ('Apteka #1', 'Lenina St., 1', '84951234567'),
    ('Apteka #2', 'Krupskaya St., 15', '84957654321'),
    ('Apteka #3', 'Gogolya St., 7', '84959876543'),
    ('Apteka #4', 'Pushkina St., 18', '84953456789'),
    ('Apteka #5', 'Mira St., 3', '84951239876');

INSERT INTO public.medications (name, form, price, expiration_date) VALUES
    ('Paracetamol', 'TABLET', 50.00, '2025-12-31'),
    ('Amoxicillin', 'TABLET', 120.50, '2024-11-30'),
    ('Ibuprofen', 'TABLET', 85.75, '2023-10-31'),
    ('Ceftriaxone', 'TABLET', 200.00, '2026-09-30'),
    ('Loratadine', 'TABLET', 60.00, '2025-08-31');

INSERT INTO public.employees (name, position, email, pharmacy_id) VALUES
    ('Ivan Ivanov', 'PHARMACIST', 'abdulloev.b.03@gmail.com', 1),
    ('Petr Petrov', 'PHARMACIST', 'petrov@apteka2.ru', 2),
    ('Sergey Sergeev', 'PHARMACIST', 'bakhtovar.abdulloev.03@mail.ru', 3),
    ('Anna Annova', 'PHARMACIST', 'annova@apteka4.ru', 4),
    ('Maria Marieva', 'PHARMACIST', 'marieva@apteka5.ru', 5);

INSERT INTO public.customers (name, address, phone) VALUES
    ('Alexey Alexeev', 'Lesnaya St., 5', '89261234567'),
    ('Boris Borisov', 'Polevaya St., 8', '89263456789'),
    ('Viktor Viktorov', 'Sadovaya St., 10', '89265678901'),
    ('Galina Galina', 'Lugovaya St., 12', '89267890123'),
    ('Dmitry Dmitriev', 'Vinogradnaya St., 3', '89269012345');

INSERT INTO public.orders (customer_id, employee_id, pharmacy_id, medication_id, quantity, total_amount, order_date, status) VALUES
    (1, 1, 1, 1, 2, 100.00, '2023-01-15', 'PROCESSING'),
    (2, 2, 2, 2, 1, 100.00, '2023-02-15', 'PROCESSING'),
    (3, 3, 3, 3, 3, 90.00, '2023-03-15', 'PROCESSING'),
    (4, 4, 4, 4, 4, 280.00, '2023-04-15', 'PROCESSING'),
    (5, 5, 5, 5, 5, 200.00, '2023-05-15', 'PROCESSING');

INSERT INTO public.pharmacy_medications (pharmacy_id, medication_id, quantity) VALUES
    (1, 1, 50),
    (2, 2, 100),
    (3, 3, 0),
    (4, 4, 20),
    (4, 2, 100),
    (1, 3, 0),
    (2, 5, 780),
    (5, 5, 0);
