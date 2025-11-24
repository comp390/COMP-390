PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name TEXT,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone_no TEXT NOT NULL,
    license_no TEXT,
    dob TEXT,
    street_address TEXT,
    city TEXT,
    state TEXT,
    country TEXT,
    zip_code TEXT,
    role TEXT NOT NULL CHECK (role IN ('rider', 'driver')),
    status TEXT CHECK (status IN ('active', 'inactive'))
);

CREATE TABLE IF NOT EXISTS car (
    car_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    make TEXT,
    model TEXT,
    year INTEGER,
    ext_color TEXT,
    int_color TEXT,
    int_materials TEXT,
    price REAL,
    condition TEXT CHECK (condition IN ('fair', 'good', 'very good', 'excellent')),
    license_plate_no TEXT UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS history (
    trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    car_id INTEGER NOT NULL,
    requested_at TEXT NOT NULL DEFAULT (datetime('now')),
    pickup_loc TEXT,
    dropoff_loc TEXT,
    fare_total REAL,
    status TEXT NOT NULL
        CHECK (status IN ('requested', 'accepted', 'enroute', 'completed', 'canceled')),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (car_id) REFERENCES car(car_id)
);

CREATE TABLE IF NOT EXISTS payment (
    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    trip_id INTEGER NOT NULL,
    pay_method TEXT NOT NULL CHECK (pay_method IN('card', 'apple pay', 'google pay')),
    amount REAL NOT NULL,
    status TEXT NOT NULL
        CHECK (status IN ('processing', 'completed', 'failed', 'refunded')),
    processed_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (trip_id) REFERENCES history(trip_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_driver_cars ON car(user_id);
CREATE INDEX IF NOT EXISTS idx_rider_trips ON history(user_id, requested_at DESC);
CREATE INDEX IF NOT EXISTS idx_trip_payments ON payment(trip_id);

INSERT INTO users (username, password, first_name, last_name, email, phone_no, license_no, dob,
    street_address, city, state, country, zip_code, role, status) VALUES
('hv', '123', 'Izaiah', 'Wall', 'iza.wall@example.com', '555-222-0001', 'MA0001', '2000-02-11', '1 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('izaiah.w', 'izaiah.w', 'Izaiah', 'Wall', 'izaiah.wall@example.com', '555-222-0001', 'MA0001', '2000-02-11', '1 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('jayda.c', 'jayda.c', 'Jayda', 'Carpenter', 'jayda.carpenter@example.com', '555-222-0002', 'MA0002', '1990-05-21', '2 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('jeremy.c', 'password', 'Jeremy', 'Conner', 'jeremy.conner@example.com', '555-222-0003', 'MA0003', '1994-03-09', '3 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('alondra.f', 'password', 'Alondra', 'Fitzgerald', 'alondra.fitzgerald@example.com', '555-222-0004', 'MA0004', '1993-01-18', '4 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('peyton.a', 'password', 'Peyton', 'Abbott', 'peyton.abbott@example.com', '555-222-0005', 'MA0005', '1992-12-30', '5 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('melany.n', 'password', 'Melany', 'Nelson', 'melany.nelson@example.com', '555-222-0006', 'MA0006', '1991-07-07', '6 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('dylan.b', 'password', 'Dylan', 'Banks', 'dylan.banks@example.com', '555-222-0007', 'MA0007', '1990-10-15', '7 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('cali.c', 'password', 'Cali', 'Carpenter', 'cali.carpenter@example.com', '555-222-0008', 'MA0008', '2000-04-12', '8 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('jeremy.h', 'password', 'Jeremy', 'Hutchinson', 'jeremy.hutchinson@example.com', '555-222-0009', 'MA0009', '2001-11-23', '9 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('jamie.u', 'password', 'Jamie', 'Underwood', 'jamie.underwood@example.com', '555-222-0010', 'MA0010', '2001-08-02', '10 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('reece.b', 'password', 'Reece', 'Barker', 'reece.barker@example.com', '555-222-0011', 'MA0011', '2002-06-06', '11 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('remington.b', 'password', 'Remington', 'Burns', 'remington.burns@example.com', '555-222-0012', 'MA0012', '2003-09-14', '12 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('august.o', 'password', 'August', 'Orr', 'august.orr@example.com', '555-222-0013', 'MA0013', '2004-03-31', '13 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('alaiya.w', 'password', 'Alaiya', 'Wilkins', 'alaiya.wilkins@example.com', '555-222-0014', 'MA0014', '2005-05-05', '14 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('driver', '123', 'Yusuf', 'Herman', 'yusuf.herman@example.com', '555-222-0015', 'MA0015', '2004-01-20', '15 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'driver', 'active'),
('paulina.c', 'password', 'Paulina', 'Cunningham', 'paulina.cunningham@example.com', '555-222-0016', NULL, '2003-12-01', '16 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('alejandro.m', 'password', 'Alejandro', 'Monroe', 'alejandro.monroe@example.com', '555-222-0017', NULL, '2002-05-28', '17 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('carly.h', 'carly.h', 'Carly', 'Hess', 'carly.hess@example.com', '555-222-0018', NULL, '2001-09-17', '18 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('lawrence.j', 'password', 'Lawrence', 'Joseph', 'lawrence.joseph@example.com', '555-222-0019', NULL, '2000-10-10', '19 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('gracelynn.q', 'password', 'Gracelynn', 'Quintero', 'gracelynn.quintero@example.com', '555-222-0020', NULL, '1999-02-22', '20 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('thatcher.g', 'password', 'Thatcher', 'Gross', 'thatcher.gross@example.com', '555-222-0021', NULL, '1998-04-27', '21 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('angel.g', 'password', 'Angel', 'Griffin', 'angel.griffin@example.com', '555-222-0022', NULL, '1997-07-19', '22 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('ayden.m', 'password', 'Ayden', 'Mills', 'ayden.mills@example.com', '555-222-0023', NULL, '1996-11-09', '23 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('june.f', 'password', 'June', 'Figueroa', 'june.figueroa@example.com', '555-222-0024', NULL, '1995-06-16', '24 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('spencer.h', 'password', 'Spencer', 'Huang', 'spencer.huang@example.com', '555-222-0025', NULL, '1994-01-03', '25 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('drew.c', 'drew.c', 'Drew', 'Callahan', 'drew.callahan@example.com', '555-111-0021', NULL, '1994-01-03', '25 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('rider', '123', 'Quinton', 'Lucero', 'quinton.lucero@example.com', '555-111-0022', NULL, '1994-01-03', '25 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('ila.a', '123', 'Ila', 'Adams', 'ila.adams@example.com', '555-111-0023', NULL, '1994-01-03', '25 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('hudson.s', 'password', 'Hudson', 'Stephenson', 'hudson.stephenson@example.com', '555-111-0024',NULL, '1994-01-03', '25 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active'),
('khaleesi.s', 'password', 'Khaleesi', 'Schroeder', 'khaleesi.schroeder@example.com', '555-111-0025',NULL, '1994-01-03', '25 Summer St', 'Bridgewater', 'MA', 'USA', '02324', 'rider', 'active');

INSERT INTO car (user_id, make, model, year, ext_color, int_color, int_materials, price, condition, license_plate_no) VALUES
(1, 'Toyota', 'Camry', 2020, 'Blue', 'Black', 'cloth', 18000, 'good', 'ABC0001'), 
(2, 'Honda', 'Civic', 2019, 'White', 'Gray', 'cloth', 15000, 'very good', 'ABC0002'), 
(3, 'Ford', 'Focus', 2025, 'Red', 'Black', 'cloth', 12000, 'good', 'ABC0003'), 
(4, 'Chevrolet', 'Malibu', 2021, 'Gray', 'Black', 'leather', 20000, 'excellent', 'ABC0004'), 
(5, 'Nissan', 'Altima', 2020, 'Black', 'Black', 'leather', 19000, 'very good', 'ABC0005'), 
(6, 'Hyundai', 'Santa Fe', 2019, 'Blue', 'Beige', 'cloth', 14000, 'good', 'ABC0006'), 
(7, 'Tesla', 'Model S', 2012, 'Silver', 'Black', 'cloth', 13000, 'fair', 'ABC0007'), 
(8, 'Subaru', 'Impreza', 2021, 'Green', 'Black', 'cloth', 20000, 'very good', 'ABC0008'), 
(9, 'Mazda', '3', 2020, 'White', 'Black', 'cloth', 17500, 'good', 'ABC0009'), 
(10, 'Volkswagen', 'Jetta', 2019, 'Black', 'Gray', 'cloth', 16000, 'good', 'ABC0010'), 
(11, 'Toyota', 'Corolla', 2022, 'Blue', 'Black', 'cloth', 21000, 'excellent', 'ABC0011'), 
(12, 'Honda', 'Accord', 2020, 'Gray', 'Black', 'leather', 23000, 'very good', 'ABC0012'), 
(13, 'Ford', 'Fusion', 2019, 'Silver', 'Black', 'cloth', 17000, 'good', 'ABC0013'), 
(14, 'Chevrolet', 'Equinox', 2023, 'Red', 'Black', 'cloth', 12500, 'fair', 'ABC0014'), 
(15, 'Nissan', 'Altima', 2021, 'White', 'Black', 'cloth', 16500, 'good', 'ABC0015'), 
(1, 'Hyundai', 'Santa Cruz', 2020, 'Blue', 'Gray', 'leather', 19500, 'very good', 'ABC0016'),
(2, 'Kia', 'Forte', 2019, 'Black', 'Black', 'cloth', 14500, 'good', 'ABC0017'),
(3, 'Subaru', 'Legacy', 2021, 'Gray', 'Black', 'cloth', 22000, 'excellent', 'ABC0018'),
(4, 'Tesla', 'Model Y', 2024, 'Blue', 'Beige', 'leather', 16000, 'good', 'ABC0019'),
(5, 'Audi', 'Q3', 2020, 'Silver', 'Black', 'cloth', 18500, 'very good', 'ABC0020'),
(6, 'Toyota', 'Camry', 2023, 'Green', 'Black', 'cloth', 13500, 'good', 'ABC0021'),
(7, 'Hyundai', 'Tucson', 2025, 'Yellow', 'Black', 'cloth', 12000, 'fair', 'ABC0022'),
(8, 'Ford', 'Focus', 2020, 'Gray', 'Black', 'cloth', 20000, 'very good', 'ABC0023'),
(9, 'Chevrolet', 'Malibu', 2019, 'Blue', 'Black', 'cloth', 15500, 'good', 'ABC0024'),
(10, 'BMW', 'M3', 2021, 'White', 'Black', 'leather', 24000, 'excellent', 'ABC0025');


INSERT INTO history (user_id, car_id, pickup_loc, dropoff_loc, fare_total, status, requested_at) VALUES
(1, 1, 'Bridgewater', 'Boston', 55.50, 'canceled', '2025-11-01 12:00:00'),
(2, 2, 'Bridgewater', 'Brockton', 25.25, 'completed', '2025-11-02 12:00:00'),
(3, 3, 'Bridgewater', 'Taunton', 25.25, 'completed', '2025-11-03 12:00:00'),
(4, 4, 'Bridgewater', 'Boston', 60.25, 'completed', '2025-11-04 12:00:00'),
(5, 5, 'Bridgewater', 'Middleboro', 15.00, 'completed', '2025-11-05 12:00:00'),
(6, 6, 'Bridgewater', 'Raynham', 20.50, 'completed', '2025-11-01 12:00:00'),
(7, 7, 'Bridgewater', 'New Bedford', 75.00, 'completed', '2025-11-02 12:00:00'),
(8, 8, 'Bridgewater', 'Plymouth', 62.35, 'completed', '2025-11-03 12:00:00'),
(9, 9, 'Bridgewater', 'Quincy', 49.50, 'completed', '2025-11-04 12:00:00'),
(10, 10, 'Bridgewater', 'Providence', 83.25, 'canceled', '2025-11-05 12:00:00'),
(11, 11, 'Bridgewater', 'Boston', 57.95, 'completed', '2025-11-01 12:00:00'),
(12, 12, 'Bridgewater', 'Brockton', 23.10, 'completed', '2025-11-02 12:00:00'),
(13, 13, 'Bridgewater', 'Taunton', 27.65, 'completed', '2025-11-03 12:00:00'),
(14, 14, 'Bridgewater', 'Boston', 59.70, 'completed', '2025-11-04 12:00:00'),
(15, 15, 'Bridgewater', 'Middleboro', 15.25, 'completed', '2025-11-05 12:00:00'),
(16, 16, 'Bridgewater', 'Raynham', 19.05, 'completed', '2025-11-01 12:00:00'),
(17, 17, 'Bridgewater', 'New Bedford', 42.90, 'completed', '2025-11-02 12:00:00'),
(18, 18, 'Bridgewater', 'Plymouth', 45.40, 'completed', '2025-11-03 12:00:00'),
(19, 19, 'Bridgewater', 'Quincy', 42.15, 'completed', '2025-11-04 12:00:00'),
(20, 20, 'Bridgewater', 'Providence', 72.80, 'completed', '2025-11-05 12:00:00'),
(21, 21, 'Bridgewater', 'Boston', 58.15, 'completed', '2025-11-01 12:00:00'),
(22, 22, 'Bridgewater', 'Brockton', 22.40, 'completed', '2025-11-02 12:00:00'),
(23, 23, 'Bridgewater', 'Taunton', 28.85, 'completed', '2025-11-03 12:00:00'),
(24, 24, 'Bridgewater', 'Boston', 60.55, 'completed', '2025-11-04 12:00:00'),
(25, 25, 'Bridgewater', 'Middleboro', 24.95, 'completed', '2025-11-05 12:00:00'),
(26, 24, 'Bridgewater', 'Boston', 58.50, 'completed', '2025-11-01 12:00:00'),
(27, 22, 'Bridgewater', 'Brockton', 18.75, 'canceled', '2025-11-02 12:00:00'),
(28, 25, 'Bridgewater', 'Taunton', 19.20, 'completed', '2025-11-03 12:00:00'),
(29, 14, 'Bridgewater', 'Boston', 50.10, 'completed', '2025-11-04 12:00:00'),
(30, 15, 'Bridgewater', 'Middleboro', 14.60, 'completed', '2025-11-05 12:00:00');


INSERT INTO payment (trip_id, pay_method, amount, status, processed_at) VALUES
(1, 'card', 55.50, 'refunded', '2025-11-01 12:00:00'),
(2, 'apple pay', 25.25, 'completed', '2025-11-02 12:00:00'),
(3, 'google pay', 25.25, 'completed', '2025-11-03 12:00:00'),
(4, 'card', 60.25, 'completed', '2025-11-04 12:00:00'),
(5, 'apple pay', 15.00, 'completed', '2025-11-05 12:00:00'),
(6, 'google pay', 20.50, 'completed', '2025-11-01 12:00:00'),
(7, 'card', 75.00, 'completed', '2025-11-02 12:00:00'),
(8, 'apple pay', 62.35, 'completed', '2025-11-03 12:00:00'),
(9, 'google pay', 49.50, 'completed', '2025-11-04 12:00:00'),
(10, 'card', 83.25, 'refunded', '2025-11-05 12:00:00'),
(11, 'apple pay', 57.95, 'completed', '2025-11-01 12:00:00'),
(12, 'google pay', 23.10, 'completed', '2025-11-02 12:00:00'),
(13, 'card', 27.65, 'completed', '2025-11-03 12:00:00'),
(14, 'apple pay', 59.70, 'completed', '2025-11-04 12:00:00'),
(15, 'google pay', 15.25, 'completed', '2025-11-05 12:00:00'),
(16, 'card', 19.05, 'completed', '2025-11-01 12:00:00'),
(17, 'apple pay', 42.90, 'completed', '2025-11-02 12:00:00'),
(18, 'google pay', 45.40, 'completed', '2025-11-03 12:00:00'),
(19, 'card', 42.15, 'completed', '2025-11-04 12:00:00'),
(20, 'apple pay', 72.80, 'completed', '2025-11-05 12:00:00'),
(21, 'google pay', 58.15, 'completed', '2025-11-01 12:00:00'),
(22, 'card', 22.40, 'completed', '2025-11-02 12:00:00'),
(23, 'apple pay', 28.85, 'completed', '2025-11-03 12:00:00'),
(24, 'google pay', 60.55, 'completed', '2025-11-04 12:00:00'),
(25, 'card', 24.95, 'completed', '2025-11-05 12:00:00'),
(26, 'card', 58.50, 'completed', '2025-11-01 12:00:00'),
(27, 'apple pay', 18.75, 'refunded', '2025-11-02 12:00:00'),
(28, 'google pay', 19.20, 'completed', '2025-11-03 12:00:00'),
(29, 'card', 50.10, 'completed', '2025-11-04 12:00:00'),
(30, 'apple pay', 14.60, 'completed', '2025-11-05 12:00:00');