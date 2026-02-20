CREATE TABLE IF NOT EXISTS users (
    user_id CHAR(36) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    role VARCHAR(10),
    status VARCHAR(10),
    token_id VARCHAR(20)
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

CREATE TABLE IF NOT EXISTS Subject (
    Subject_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Subject_name_thai VARCHAR(100),
    Subject_Name_eng VARCHAR(100),
    Subject_name_id VARCHAR(20) UNIQUE,
    Year INT,
    Type_Subject VARCHAR(100),
    Day_Update VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS post_work (
     Post_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     Subject_ID INT NOT NULL,
     Project_Name VARCHAR(255) NOT NULL,
     Project_Detail TEXT,
     Project_Image VARCHAR(255),
     Project_link VARCHAR(255),
     Project_Docs VARCHAR(255),
     Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     user_id VARCHAR(36) NOT NULL,

     FOREIGN KEY (Subject_ID) REFERENCES subject(Subject_ID),
     FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS verify_user (
  verify_id CHAR(36) PRIMARY KEY,
  name             VARCHAR(100)      NOT NULL,
  email            VARCHAR(254)      NOT NULL,
  password_hash    VARCHAR(100)      NOT NULL,
  verification_code VARCHAR(12)      NOT NULL,
  status           VARCHAR(20)       NOT NULL DEFAULT 'PENDING',
  requested_at     TIMESTAMPTZ       NOT NULL DEFAULT NOW(),
  verified_at      TIMESTAMPTZ       NULL,
  expires_at       TIMESTAMPTZ       NOT NULL,
  attempts         INT               NOT NULL DEFAULT 0
);