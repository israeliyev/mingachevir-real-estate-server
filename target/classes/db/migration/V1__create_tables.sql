-- Create schema and set search path
CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION postgres;
GRANT USAGE, CREATE ON SCHEMA public TO postgres;
SET search_path TO public, public;

-- Create sequences with caching
CREATE SEQUENCE public.parameter_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 20;
ALTER SEQUENCE public.parameter_id_seq OWNER TO postgres;

CREATE SEQUENCE public.selective_parameter_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 20;
ALTER SEQUENCE public.selective_parameter_id_seq OWNER TO postgres;

CREATE SEQUENCE public.task_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 20;
ALTER SEQUENCE public.task_seq OWNER TO postgres;

-- Update timestamp function
CREATE OR REPLACE FUNCTION public.update_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.update_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Tables with updated relationships and cascades
CREATE TABLE public.main_category
(
    id          BIGSERIAL PRIMARY KEY,
    enabled     BOOLEAN                   DEFAULT true,
    is_deleted  BOOLEAN                   DEFAULT false,
    code        VARCHAR(255),
    name        VARCHAR(255),
    create_date TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.main_category
    OWNER TO postgres;
CREATE TRIGGER update_main_category_timestamp
    BEFORE UPDATE
    ON public.main_category
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.sub_category
(
    id               BIGSERIAL PRIMARY KEY,
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    code             VARCHAR(255),
    name             VARCHAR(255),
    main_category_id BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_main_category_id
        FOREIGN KEY (main_category_id)
            REFERENCES public.main_category (id)
            ON DELETE RESTRICT
);
ALTER TABLE public.sub_category
    OWNER TO postgres;
CREATE TRIGGER update_sub_category_timestamp
    BEFORE UPDATE
    ON public.sub_category
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();
CREATE INDEX idx_sub_category_main_category_id ON public.sub_category (main_category_id);

CREATE TABLE public.parameters
(
    id               BIGINT PRIMARY KEY                DEFAULT nextval('public.parameter_id_seq'),
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    code             VARCHAR(255),
    name             VARCHAR(255),
    type             VARCHAR(50)               CHECK (type IN ('SELECT', 'INPUT')),
    sub_category_id  BIGINT,
    main_category_id BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sub_category_id
        FOREIGN KEY (sub_category_id)
            REFERENCES public.sub_category (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_main_category_id
        FOREIGN KEY (main_category_id)
            REFERENCES public.main_category (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE public.parameters
    OWNER TO postgres;
ALTER SEQUENCE public.parameter_id_seq OWNED BY public.parameters.id;
CREATE TRIGGER update_parameters_timestamp
    BEFORE UPDATE
    ON public.parameters
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();
CREATE INDEX idx_parameters_sub_category_id ON public.parameters (sub_category_id);
CREATE INDEX idx_parameters_main_category_id ON public.parameters (main_category_id);

CREATE TABLE public.selective_parameter_value
(
    id           BIGINT PRIMARY KEY                DEFAULT nextval('public.selective_parameter_id_seq'),
    enabled      BOOLEAN                   DEFAULT true,
    is_deleted   BOOLEAN                   DEFAULT false,
    code         VARCHAR(255),
    name         VARCHAR(255),
    parameter_id BIGINT,
    create_date  TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date  TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_selective_parameter
        FOREIGN KEY (parameter_id)
            REFERENCES public.parameters (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE public.selective_parameter_value
    OWNER TO postgres;
ALTER SEQUENCE public.selective_parameter_id_seq OWNED BY public.selective_parameter_value.id;
CREATE TRIGGER update_selective_parameter_timestamp
    BEFORE UPDATE
    ON public.selective_parameter_value
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.house
(
    id               BIGSERIAL PRIMARY KEY,
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    name             VARCHAR(255),
    price            DOUBLE PRECISION          DEFAULT 0 CHECK (price >= 0),
    price_type       VARCHAR(50)               CHECK (price_type IN ('MONTH', 'SALE', 'MORTGAGE', 'DAY')),
    location         VARCHAR(255),
    description      TEXT,
    cover_image      VARCHAR(255),
    house_video      VARCHAR(255),
    main_category_id BIGINT,
    sub_category_id  BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_main_category_id
        FOREIGN KEY (main_category_id)
            REFERENCES public.main_category (id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_sub_category_id
        FOREIGN KEY (sub_category_id)
            REFERENCES public.sub_category (id)
            ON DELETE RESTRICT
);
ALTER TABLE public.house
    OWNER TO postgres;
CREATE TRIGGER update_house_timestamp
    BEFORE UPDATE
    ON public.house
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();
CREATE INDEX idx_house_price ON public.house (price);
CREATE INDEX idx_house_location ON public.house (location);

CREATE TABLE public.house_selective_parameter_value
(
    house_id               BIGINT,
    selective_parameter_id BIGINT,
    PRIMARY KEY (house_id, selective_parameter_id),
    CONSTRAINT fk_house_id
        FOREIGN KEY (house_id)
            REFERENCES public.house (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_selective_parameter_id
        FOREIGN KEY (selective_parameter_id)
            REFERENCES public.selective_parameter_value (id)
            ON DELETE CASCADE
);
ALTER TABLE public.house_selective_parameter_value
    OWNER TO postgres;

CREATE TABLE public.section
(
    id          BIGSERIAL PRIMARY KEY,
    enabled     BOOLEAN                   DEFAULT true,
    is_deleted  BOOLEAN                   DEFAULT false,
    name        VARCHAR(255),
    page        VARCHAR(255),
    create_date TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.section
    OWNER TO postgres;
CREATE TRIGGER update_section_timestamp
    BEFORE UPDATE
    ON public.section
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.house_section
(
    house_id   BIGINT,
    section_id BIGINT,
    PRIMARY KEY (house_id, section_id),
    CONSTRAINT fk_house_id
        FOREIGN KEY (house_id)
            REFERENCES public.house (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_section_id
        FOREIGN KEY (section_id)
            REFERENCES public.section (id)
            ON DELETE CASCADE
);
ALTER TABLE public.house_section
    OWNER TO postgres;

CREATE TABLE public.broker
(
    id          BIGSERIAL PRIMARY KEY,
    enabled     BOOLEAN                   DEFAULT true,
    is_deleted  BOOLEAN                   DEFAULT false,
    email       VARCHAR(255),
    phone       VARCHAR(255),
    facebook    VARCHAR(255),
    instagram   VARCHAR(255),
    tiktok      VARCHAR(255),
    location    VARCHAR(255),
    create_date TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.broker
    OWNER TO postgres;
CREATE TRIGGER update_broker_timestamp
    BEFORE UPDATE
    ON public.broker
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.basket_addition
(
    id                 BIGSERIAL PRIMARY KEY,
    enabled            BOOLEAN                   DEFAULT true,
    is_deleted         BOOLEAN                   DEFAULT false,
    addition_date_time TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    visitor_ip         VARCHAR(255),
    session_id         VARCHAR(255),
    house_id           BIGINT,
    create_date        TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date        TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_house_id
        FOREIGN KEY (house_id)
            REFERENCES public.house (id)
            ON DELETE CASCADE
);
ALTER TABLE public.basket_addition
    OWNER TO postgres;
CREATE TRIGGER update_basket_addition_timestamp
    BEFORE UPDATE
    ON public.basket_addition
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.house_view
(
    id             BIGSERIAL PRIMARY KEY,
    enabled        BOOLEAN                   DEFAULT true,
    is_deleted     BOOLEAN                   DEFAULT false,
    view_date_time TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    visitor_ip     VARCHAR(255),
    session_id     VARCHAR(255),
    house_id       BIGINT,
    create_date    TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date    TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_house_id
        FOREIGN KEY (house_id)
            REFERENCES public.house (id)
            ON DELETE CASCADE
);
ALTER TABLE public.house_view
    OWNER TO postgres;
CREATE TRIGGER update_house_view_timestamp
    BEFORE UPDATE
    ON public.house_view
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.search_query
(
    id               BIGSERIAL PRIMARY KEY,
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    search_key       VARCHAR(255),
    search_date_time TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    visitor_ip       VARCHAR(255),
    session_id       VARCHAR(255),
    main_category_id BIGINT,
    sub_category_id  BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_main_category_id
        FOREIGN KEY (main_category_id)
            REFERENCES public.main_category (id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_sub_category_id
        FOREIGN KEY (sub_category_id)
            REFERENCES public.sub_category (id)
            ON DELETE RESTRICT
);
ALTER TABLE public.search_query
    OWNER TO postgres;
CREATE TRIGGER update_search_query_timestamp
    BEFORE UPDATE
    ON public.search_query
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.input_parameter_range
(
    id              BIGSERIAL PRIMARY KEY,
    enabled         BOOLEAN                   DEFAULT true,
    is_deleted      BOOLEAN                   DEFAULT false,
    min_value       INTEGER CHECK (min_value >= 0),
    max_value       INTEGER CHECK (max_value >= 0),
    parameter_id    BIGINT,
    search_query_id BIGINT,
    create_date     TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date     TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parameter_id
        FOREIGN KEY (parameter_id)
            REFERENCES public.parameters (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_search_query_id
        FOREIGN KEY (search_query_id)
            REFERENCES public.search_query (id)
            ON DELETE CASCADE ON UPDATE CASCADE);
ALTER TABLE public.input_parameter_range
    OWNER TO postgres;
CREATE TRIGGER update_input_parameter_range_timestamp
    BEFORE UPDATE
    ON public.input_parameter_range
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.search_query_selective_parameters
(
    search_query_id              BIGINT,
    selective_parameter_value_id BIGINT,
    PRIMARY KEY (search_query_id, selective_parameter_value_id),
    CONSTRAINT fk_search_query_id
        FOREIGN KEY (search_query_id)
            REFERENCES public.search_query (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_selective_parameter_value_id
        FOREIGN KEY (selective_parameter_value_id)
            REFERENCES public.selective_parameter_value (id)
            ON DELETE CASCADE
);
ALTER TABLE public.search_query_selective_parameters
    OWNER TO postgres;

CREATE TABLE public.site_visit
(
    id              BIGSERIAL PRIMARY KEY,
    enabled         BOOLEAN                   DEFAULT true,
    is_deleted      BOOLEAN                   DEFAULT false,
    visit_date_time TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    visitor_ip      VARCHAR(255),
    session_id      VARCHAR(255),
    create_date     TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date     TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.site_visit
    OWNER TO postgres;
CREATE TRIGGER update_site_visit_timestamp
    BEFORE UPDATE
    ON public.site_visit
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.house_request
(
    id               BIGSERIAL PRIMARY KEY,
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    requester        VARCHAR(255),
    price            DOUBLE PRECISION CHECK (price >= 0),
    price_type       VARCHAR(50) CHECK (price_type IN ('MONTH', 'MORTGAGE', 'SALE', 'DAY')),
    location         VARCHAR(255),
    number           VARCHAR(255),
    cover_image      VARCHAR(255),
    main_category_id BIGINT,
    sub_category_id  BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_main_category_id
        FOREIGN KEY (main_category_id)
            REFERENCES public.main_category (id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_sub_category_id
        FOREIGN KEY (sub_category_id)
            REFERENCES public.sub_category (id)
            ON DELETE RESTRICT
);
ALTER TABLE public.house_request
    OWNER TO postgres;
CREATE TRIGGER update_house_request_timestamp
    BEFORE UPDATE
    ON public.house_request
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.house_image
(
    id               BIGSERIAL PRIMARY KEY,
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    path             VARCHAR(255),
    file_name        VARCHAR(255),
    size             BIGINT CHECK (size >= 0),
    house_id         BIGINT,
    house_request_id BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_house_id
        FOREIGN KEY (house_id)
            REFERENCES public.house (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_house_request_id
        FOREIGN KEY (house_request_id)
            REFERENCES public.house_request (id)
            ON DELETE CASCADE
);
ALTER TABLE public.house_image
    OWNER TO postgres;
CREATE TRIGGER update_house_image_timestamp
    BEFORE UPDATE
    ON public.house_image
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.input_parameter_value
(
    id               BIGSERIAL PRIMARY KEY,
    enabled          BOOLEAN                   DEFAULT true,
    is_deleted       BOOLEAN                   DEFAULT false,
    code             VARCHAR(255),
    value            INTEGER,
    parameter_id     BIGINT,
    house_id         BIGINT,
    house_request_id BIGINT,
    create_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    update_date      TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parameter_id
        FOREIGN KEY (parameter_id)
            REFERENCES public.parameters (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_house_id
        FOREIGN KEY (house_id)
            REFERENCES public.house (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_house_request_id
        FOREIGN KEY (house_request_id)
            REFERENCES public.house_request (id)
            ON DELETE CASCADE
);
ALTER TABLE public.input_parameter_value
    OWNER TO postgres;
CREATE TRIGGER update_input_parameter_value_timestamp
    BEFORE UPDATE
    ON public.input_parameter_value
    FOR EACH ROW
EXECUTE FUNCTION public.update_timestamp();

CREATE TABLE public.house_request_selective_parameter_value
(
    house_request_id       BIGINT,
    selective_parameter_id BIGINT,
    PRIMARY KEY (house_request_id, selective_parameter_id),
    CONSTRAINT fk_house_request_id
        FOREIGN KEY (house_request_id)
            REFERENCES public.house_request (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_selective_parameter_id
        FOREIGN KEY (selective_parameter_id)
            REFERENCES public.selective_parameter_value (id)
            ON DELETE CASCADE
);
ALTER TABLE public.house_request_selective_parameter_value
    OWNER TO postgres;

-- Users table
CREATE TABLE public.users (
                              id BIGSERIAL PRIMARY KEY,
                              username VARCHAR(255) NOT NULL UNIQUE,
                              password VARCHAR(255) NOT NULL,
                              enabled BOOLEAN NOT NULL
);
ALTER TABLE public.users
    OWNER TO postgres;

-- User devices table (for tracking logged-in devices)
CREATE TABLE public.user_devices (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
                                     device_id VARCHAR(255) NOT NULL,
                                     token TEXT NOT NULL
);
ALTER TABLE public.user_devices
    OWNER TO postgres;

-- Grant schema-wide permissions *after* all tables and sequences are created
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO postgres;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO postgres;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO postgres;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO postgres;

-- Specific table permissions
GRANT SELECT ON public.users TO postgres;
GRANT INSERT, UPDATE ON public.house TO postgres;

-- Insert initial data
INSERT INTO public.users VALUES (DEFAULT, 'mingecevir@admin.az', '$2a$12$W1hpp5bL0SYlXcOTiIf9VO4xEs3A6Zl6v/MIaXw0Ocscbnqdkmdqu', true);

-- Insert PRICE parameter with conflict handling
INSERT INTO public.parameters (id, enabled, is_deleted, code, name, sub_category_id, create_date, update_date, main_category_id, type)
VALUES (DEFAULT, true, false, 'PRICE', 'Qiymət', null, '2025-02-11 22:50:01.000000', '2025-02-11 22:50:03.000000', null, 'INPUT');

-- Insert LOCATION parameter with conflict handling
INSERT INTO public.parameters (id, enabled, is_deleted, code, name, sub_category_id, create_date, update_date, main_category_id, type)
VALUES (DEFAULT, true, false, 'LOCATION', 'Bölgə', null, '2025-02-11 22:50:01.000000', '2025-02-11 22:50:03.000000', null, 'SELECT');
