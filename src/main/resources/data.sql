CREATE TABLE IF NOT EXISTS applications
(
    id                    SERIAL PRIMARY KEY,
    monthly_income        BIGINT       NOT NULL,
    mortgage_loans        BIGINT       NOT NULL,
    consumer_loans        BIGINT       NOT NULL,
    leasing_amount        BIGINT       NOT NULL,
    credit_card_limit     BIGINT       NOT NULL,
    real_estate_price     BIGINT       NOT NULL,
    down_payment          INT          NOT NULL,
    loan_amount           BIGINT       NOT NULL,
    loan_term             INT          NOT NULL,
    interest_rate_margin  FLOAT        NOT NULL,
    interest_rate_euribor FLOAT        NOT NULL,
    payment_schedule_type VARCHAR(255) NOT NULL,
    children_amount       INT          NOT NULL,
    applicants_amount     INT          NOT NULL,
    application_status    VARCHAR(255) NOT NULL
);

INSERT INTO applications (
                          monthly_income,
                          mortgage_loans,
                          consumer_loans,
                          leasing_amount,
                          credit_card_limit,
                          real_estate_price,
                          down_payment,
                          loan_amount,
                          loan_term,
                          interest_rate_margin,
                          interest_rate_euribor,
                          payment_schedule_type,
                          children_amount,
                          applicants_amount,
                          application_status)
VALUES (
        1000000,
        1500,
        250,
        7000,
        200,
        100000,
        150000,
        85000,
        3,
        0.025,
        0.0535,
        'ANNUITY',
        2,
        1,
        'RECEIVED')
ON CONFLICT (id) DO NOTHING;



CREATE TABLE IF NOT EXISTS constants
(
    id                                 SERIAL PRIMARY KEY,
    min_loan_term                      INT   NOT NULL,
    max_loan_term                      INT   NOT NULL,
    max_num_of_applicants              FLOAT NOT NULL,
    loan_amount_percentage             FLOAT NOT NULL,
    interest_rate_margin               FLOAT NOT NULL,
    max_kids                           INT   NOT NULL,
    min_kids                           INT   NOT NULL,
    max_monthly_obligations_percentage FLOAT NOT NULL
);

INSERT INTO constants (id,
                       min_loan_term,
                       max_loan_term,
                       max_num_of_applicants,
                       loan_amount_percentage,
                       interest_rate_margin,
                       max_kids,
                       min_kids,
                       max_monthly_obligations_percentage)
VALUES (1,
        1,
        30,
        2,
        0.85,
        0.025,
        10,
        0,
        0.4)
ON CONFLICT (id) DO NOTHING;


CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL
);