CREATE TABLE IF NOT EXISTS patients (
  patients_id SERIAL PRIMARY KEY NOT NULL,
  first_name TEXT,
  last_name TEXT,
  location_ TEXT
);