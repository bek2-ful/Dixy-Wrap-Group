--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: sync_table_user_points(); Type: FUNCTION; Schema: public; Owner: gamificationuser
--

CREATE FUNCTION public.sync_table_user_points() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM user_points WHERE user_id = NEW.user_id) THEN
        UPDATE user_points
        SET 
            current_points = COALESCE(NEW.current_points, current_points) 
        WHERE user_id = NEW.user_id;
    ELSE
        INSERT INTO user_points (user_id, current_points)
        VALUES (
            NEW.user_id, 
            COALESCE(NEW.current_points, 0)
        );
    END IF;
    
    RETURN NEW; 
END;
$$;


ALTER FUNCTION public.sync_table_user_points() OWNER TO gamificationuser;

--
-- Name: update_points_function(); Type: FUNCTION; Schema: public; Owner: gamificationuser
--

CREATE FUNCTION public.update_points_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
           UPDATE user_points
        SET current_points = current_points + NEW.points_earned - NEW.points_spent
        WHERE user_id = NEW.user_id;

       RETURN NEW; -- Ensure the trigger returns the updated row
END;
$$;


ALTER FUNCTION public.update_points_function() OWNER TO gamificationuser;

--
-- Name: transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: gamificationuser
--

CREATE SEQUENCE public.transaction_id_seq
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transaction_id_seq OWNER TO gamificationuser;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: transaction; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.transaction (
    transaction_id integer DEFAULT nextval('public.transaction_id_seq'::regclass) NOT NULL,
    user_id integer,
    transaction_date date,
    transaction_time time without time zone,
    points_earned integer,
    points_spent integer,
    transaction_name text
);


ALTER TABLE public.transaction OWNER TO gamificationuser;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: gamificationuser
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_seq OWNER TO gamificationuser;

--
-- Name: user_information; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.user_information (
    user_id integer DEFAULT nextval('public.user_id_seq'::regclass) NOT NULL,
    name text
);


ALTER TABLE public.user_information OWNER TO gamificationuser;

--
-- Name: user_points; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.user_points (
    user_id integer NOT NULL,
    current_points integer
);


ALTER TABLE public.user_points OWNER TO gamificationuser;

--
-- Name: voucher_id_seq; Type: SEQUENCE; Schema: public; Owner: gamificationuser
--

CREATE SEQUENCE public.voucher_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.voucher_id_seq OWNER TO gamificationuser;

--
-- Name: voucher; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.voucher (
    voucher_id integer DEFAULT nextval('public.voucher_id_seq'::regclass) NOT NULL,
    points_needed integer,
    company text,
    counter integer,
    reward text,
    logo_path character varying
);


ALTER TABLE public.voucher OWNER TO gamificationuser;

--
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.transaction (transaction_id, user_id, transaction_date, transaction_time, points_earned, points_spent, transaction_name) FROM stdin;
1002	1	2024-11-09	14:30:00	50	0	Check-In
1003	1	2024-11-15	09:15:00	0	700	Voucher Redeemed
1004	1	2024-11-21	17:15:00	50	0	Check-In
1005	1	2024-11-30	10:23:00	50	0	Check-In
1006	1	2024-12-03	12:55:00	0	500	Voucher Redeemed
1007	1	2024-12-03	14:00:00	0	500	Voucher Redeemed
1008	1	2024-12-03	14:00:00	50	0	Check-In
\.


--
-- Data for Name: user_information; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.user_information (user_id, name) FROM stdin;
1	Mark
2	Naylea
3	John
4	Sara
5	Bill
\.


--
-- Data for Name: user_points; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.user_points (user_id, current_points) FROM stdin;
2	200
3	700
4	50
5	500
1	700
\.


--
-- Data for Name: voucher; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.voucher (voucher_id, points_needed, company, counter, reward, logo_path) FROM stdin;
100	500	Argos	0	10% OFF Purchase	\N
101	700	Sainsburys	0	$5 OFF for in-store groceries	\N
102	900	Iceland	0	$8 OFF Purchase	\N
103	1500	Aldi	0	50% OFF Purchase	\N
104	2000	KFC	0	FREE ORIGINAL CHICKEN	\N
\.


--
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: gamificationuser
--

SELECT pg_catalog.setval('public.transaction_id_seq', 1008, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: gamificationuser
--

SELECT pg_catalog.setval('public.user_id_seq', 6, true);


--
-- Name: voucher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: gamificationuser
--

SELECT pg_catalog.setval('public.voucher_id_seq', 104, true);


--
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (transaction_id);


--
-- Name: user_information user_information_pkey; Type: CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.user_information
    ADD CONSTRAINT user_information_pkey PRIMARY KEY (user_id);


--
-- Name: voucher voucher_pkey; Type: CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.voucher
    ADD CONSTRAINT voucher_pkey PRIMARY KEY (voucher_id);


--
-- Name: user_information sync_table_user_points_trigger; Type: TRIGGER; Schema: public; Owner: gamificationuser
--

CREATE TRIGGER sync_table_user_points_trigger AFTER INSERT OR UPDATE ON public.user_information FOR EACH ROW EXECUTE FUNCTION public.sync_table_user_points();


--
-- Name: transaction update_points_trigger; Type: TRIGGER; Schema: public; Owner: gamificationuser
--

CREATE TRIGGER update_points_trigger AFTER INSERT OR UPDATE ON public.transaction FOR EACH ROW EXECUTE FUNCTION public.update_points_function();


--
-- Name: transaction fk_user; Type: FK CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.user_information(user_id);


--
-- Name: user_points fk_user; Type: FK CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.user_points
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.user_information(user_id) ON UPDATE CASCADE;


--
-- PostgreSQL database dump complete
--

