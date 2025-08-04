CREATE TABLE IF NOT EXISTS users (
    user_id CHAR(36) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    role VARCHAR(10),
    status VARCHAR(10),
    token_id VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Date_Token (
    Token_ID VARCHAR(20),
    user_id INT,
    Token VARCHAR(100),
    Date_start TIME,
    Date_end TIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS oauth_users (
    OAuth_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT,
    email VARCHAR(100),
    password VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS update_users (
    Update_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100),
    OTP VARCHAR(5),
    Update_start TIME,
    Update_end TIME,
    Date DATE,
    time TIME
);

CREATE TABLE IF NOT EXISTS detail_users (
    Detail_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT,
    number VARCHAR(10),
    personal_information VARCHAR(5000),
    Education_information VARCHAR(100),
    Birth_Date VARCHAR(100),
    Field_study VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS log_user (
    Log_user_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT,
    action VARCHAR(100),
    time TIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
