-- Создание схемы
CREATE SCHEMA IF NOT EXISTS rudikone;

-- Установка схемы по умолчанию для пользователя
ALTER USER rudikone SET search_path TO rudikone;

-- Создание таблицы messages
CREATE TABLE rudikone.message_details (
                                   id VARCHAR PRIMARY KEY,
                                   client_id VARCHAR,
                                   source_msg VARCHAR,
                                   encrypt_str VARCHAR,
                                   result_msg VARCHAR,
                                   trace_id VARCHAR
);

-- Предоставление прав пользователю rudikone
GRANT ALL PRIVILEGES ON SCHEMA rudikone TO rudikone;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA rudikone TO rudikone;