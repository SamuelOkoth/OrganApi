CREATE DATABASE api;
\c org;
CREATE TABLE departments (
 id SERIAL PRIMARY KEY ,
 dpt_name VARCHAR,
 dpt_description VARCHAR,
 dpt_empNo INT
);

CREATE TABLE employees (
 id SERIAL PRIMARY KEY ,
 emp_name VARCHAR,
 emp_details VARCHAR,
 emp_role VARCHAR,
 emp_position VARCHAR
);

CREATE TABLE  news (
 id SERIAL PRIMARY KEY,
 news_name VARCHAR,
 news_content VARCHAR,
 dpt_id INTEGER
);

CREATE TABLE  departments_employees (
 id SERIAL PRIMARY KEY ,
 dpt_id INTEGER,
 emp_id INTEGER
);

CREATE DATABASE api_test WITH TEMPLATE api;