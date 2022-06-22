-- Drop table

DROP TABLE public.users;

CREATE TABLE public.users (
    id int8 NOT NULL,
    address varchar(100) NULL,
    email varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    "password" varchar(50) NOT NULL,
    surname varchar(50) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX users_pkey ON public.users USING btree (id);

DROP SEQUENCE public.user_sequence;

CREATE SEQUENCE public.user_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    NO CYCLE;
