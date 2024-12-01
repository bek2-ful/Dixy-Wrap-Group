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
-- Name: update_points_function(); Type: FUNCTION; Schema: public; Owner: gamificationuser
--

CREATE FUNCTION public.update_points_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF EXISTS (SELECT * FROM user_points WHERE user_id = NEW.user.id) THEN
UPDATE user_points

SET current_points = current_points + NEW.points_earned - NEW.points_spent
WHERE user_id = NEW.user_id;
ELSE
INSERT INTO user_points (user_id, current_points)
VALUES (NEW.user_id, NEW.points_earned - NEW.points_spent);
END IF;
END;
$$;


ALTER FUNCTION public.update_points_function() OWNER TO gamificationuser;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: transaction; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.transaction (
    transaction_id integer NOT NULL,
    user_id integer,
    transaction_date date,
    transaction_time time without time zone,
    points_earned integer,
    points_spent integer,
    voucher_id integer,
    transaction_name text
);


ALTER TABLE public.transaction OWNER TO gamificationuser;

--
-- Name: user_information; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.user_information (
    user_id integer NOT NULL
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
-- Name: voucher; Type: TABLE; Schema: public; Owner: gamificationuser
--

CREATE TABLE public.voucher (
    voucher_id integer NOT NULL,
    points_needed integer,
    company text,
    counter integer,
    reward text
);


ALTER TABLE public.voucher OWNER TO gamificationuser;

--
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.transaction (transaction_id, user_id, transaction_date, transaction_time, points_earned, points_spent, voucher_id, transaction_name) FROM stdin;
\.


--
-- Data for Name: user_information; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.user_information (user_id) FROM stdin;
\.


--
-- Data for Name: user_points; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.user_points (user_id, current_points) FROM stdin;
\.


--
-- Data for Name: voucher; Type: TABLE DATA; Schema: public; Owner: gamificationuser
--

COPY public.voucher (voucher_id, points_needed, company, counter, reward) FROM stdin;
1000	500	Argos	0	10% OFF Purchase
1001	700	Sainsburys	0	œ5 OFF on in-store groceries
1002	900	Iceland	0	œ8 OFF Purchase
1003	1500	Aldi	0	50% OFF Purchase
1004	2000	KFC	0	FREE ORIGINAL CHICKEN
\.


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
-- Name: transaction update_current_points; Type: TRIGGER; Schema: public; Owner: gamificationuser
--

CREATE TRIGGER update_current_points BEFORE INSERT ON public.transaction FOR EACH ROW EXECUTE FUNCTION public.update_points_function();


--
-- Name: user_points fk_user; Type: FK CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.user_points
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.user_information(user_id);


--
-- Name: transaction fk_user; Type: FK CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.user_information(user_id);


--
-- Name: transaction transaction_voucher_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: gamificationuser
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_voucher_id_fkey FOREIGN KEY (voucher_id) REFERENCES public.voucher(voucher_id);


--
-- PostgreSQL database dump complete
--

