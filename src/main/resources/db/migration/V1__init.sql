CREATE TABLE carbon_record (
                               id BIGSERIAL PRIMARY KEY,
                               activity_name VARCHAR(255) NOT NULL,
                               category VARCHAR(100) NOT NULL,
                               quantity DECIMAL(10,2) NOT NULL,
                               unit VARCHAR(50) NOT NULL,
                               emission_factor DECIMAL(10,4),
                               carbon_kg DECIMAL(10,2),
                               activity_date DATE NOT NULL,
                               status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                               ai_description TEXT,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_carbon_record_category ON carbon_record(category);
CREATE INDEX idx_carbon_record_activity_date ON carbon_record(activity_date);
CREATE INDEX idx_carbon_record_status ON carbon_record(status);