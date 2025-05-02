--liquibase formatted sql

--changeset AI:create-table-error_logs
CREATE TABLE error_logs (
    id UUID PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    trace_id VARCHAR(255),
    span_id VARCHAR(255),
    timestamp TIMESTAMP NOT NULL,
    log_message TEXT
);

--changeset AI:create-table-request_logs
CREATE TABLE request_logs (
  id UUID PRIMARY KEY,
  service_name VARCHAR(255) NOT NULL,
  event_type VARCHAR(50) NOT NULL,
  trace_id VARCHAR(255),
  parent_span_id VARCHAR(255),
  span_id VARCHAR(255),
  timestamp TIMESTAMP NOT NULL,
  log_message TEXT,
  http_method VARCHAR(10),
  url TEXT
);

--changeset AI:create-table-response_logs
CREATE TABLE response_logs (
   id UUID PRIMARY KEY,
   service_name VARCHAR(255) NOT NULL,
   event_type VARCHAR(50) NOT NULL,
   trace_id VARCHAR(255),
   span_id VARCHAR(255),
   timestamp TIMESTAMP NOT NULL,
   log_message TEXT,
   duration_ms INTEGER,
   http_status VARCHAR(3)
);

--changeset AI:create-table-deleted_tokens
CREATE TABLE deleted_tokens (
    id VARCHAR(255) PRIMARY KEY
);

--changeset AI:create-index-error_logs
CREATE INDEX idx_error_logs_trace_id ON error_logs(trace_id);
CREATE INDEX idx_error_logs_timestamp ON error_logs(timestamp);
CREATE INDEX idx_error_logs_service ON error_logs(service_name);

--changeset AI:create-index-request_logs
CREATE INDEX idx_request_logs_trace_id ON request_logs(trace_id);
CREATE INDEX idx_request_logs_timestamp ON request_logs(timestamp);
CREATE INDEX idx_request_logs_service ON request_logs(service_name);
CREATE INDEX idx_request_logs_parent_span ON request_logs(parent_span_id);

--changeset AI:create-index-response_logs
CREATE INDEX idx_response_logs_trace_id ON response_logs(trace_id);
CREATE INDEX idx_response_logs_timestamp ON response_logs(timestamp);
CREATE INDEX idx_response_logs_service ON response_logs(service_name);
CREATE INDEX idx_response_logs_http_status ON response_logs(http_status);