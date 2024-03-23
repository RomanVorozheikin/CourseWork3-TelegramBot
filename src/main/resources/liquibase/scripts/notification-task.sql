--liquibase formatted sql

--changeset roma:1

CREATE TABLE notifications (
 id serial PRIMARY KEY,
 chat_id INT NOT NULL,
 notification_text VARCHAR(255) NOT NULL,
 notification_datetime timestamp NOT NULL
)