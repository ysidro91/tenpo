CREATE TABLE IF NOT EXISTS history (
    id SERIAL PRIMARY KEY,
    type TEXT NOT NULL,
    request TEXT NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp
);