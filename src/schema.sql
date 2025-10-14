-- Always enable FK enforcement per-connection in SQLite
PRAGMA foreign_keys = ON;

CREATE TABLE rider (
    rider_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_no TEXT NOT NULL
);

CREATE TABLE driver (
    driver_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_no TEXT NOT NULL,
    license_no TEXT NOT NULL,
    dob TEXT,
    street_address TEXT,
    city TEXT,
    state TEXT,
    country TEXT,
    zip_code TEXT
);

CREATE TABLE car (
    car_id INTEGER PRIMARY KEY AUTOINCREMENT,
    driver_id INTEGER NOT NULL,
    make TEXT,
    model TEXT,
    year INTEGER,
    ext_color TEXT,
    int_color TEXT,
    int_materials TEXT,
    is_damaged INTEGER DEFAULT 0,
    license_plate_no TEXT UNIQUE NOT NULL,
    FOREIGN KEY (driver_id) REFERENCES driver(driver_id)
);

CREATE TABLE history (
    trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
    rider_id INTEGER NOT NULL,
    car_id INTEGER NOT NULL,
    requested_at TEXT NOT NULL DEFAULT (datetime('now')),
    pickup_loc TEXT,
    dropoff_loc TEXT,
    fare_total REAL,
    status TEXT NOT NULL
        CHECK (status IN ('requested', 'accepted', 'enroute', 'completed', 'canceled')),
    FOREIGN KEY (rider_id) REFERENCES rider(rider_id)
    FOREIGN KEY (car_id) REFERENCES car(car_id)
);

CREATE TABLE payment (
    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    trip_id INTEGER NOT NULL,
    pay_method TEXT NOT NULL CHECK (pay_method IN('card', 'apple_pay', 'google_pay')),
    amount REAL NOT NULL,
    status TEXT NOT NULL
        CHECK (status IN ('requested', 'accepted', 'enroute', 'completed', 'canceled')),
    processed_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (trip_id) REFERENCES history(trip_id)
);


-- Anticipated frequent queries
-- Cars for each driver
CREATE INDEX IF NOT EXISTS idx_driver_cars ON car(driver_id);

-- Trips for each rider
CREATE INDEX IF NOT EXISTS idx_rider_trips ON history(rider_id, requested_at DESC);

-- Payments for each trip
CREATE INDEX IF NOT EXISTS idx_trip_payments ON payment(trip_id);

