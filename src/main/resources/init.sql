CREATE TABLE IF NOT EXISTS monitoring_task(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    next_check_time TEXT NOT NULL,
    url TEXT NOT NULL,
    period INTEGER NOT NULL,
    unit TEXT NOT NULL CHECK(unit IN ('Час', 'Минута', 'Секунда'))
);

CREATE TABLE IF NOT EXISTS site_statistics(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    check_time TEXT NOT NULL,
    http_code INTEGER NOT NULL,
    hash TEXT NOT NULL,
    is_same_hash BOOLEAN NOT NULL,
    monitoring_task_id INTEGER REFERENCES monitoring_task(id) ON DELETE CASCADE
);