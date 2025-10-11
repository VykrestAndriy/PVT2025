--
-- PostgreSQL database dump
--

\restrict 7BqKyq0dQa1TGbyhNfIbogKsjXhpHf3r03FJEOklqyGFqF90i4o0yG4Hgq9C1VP

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Графік_роботи; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Графік_роботи" (
    "графік_id" integer NOT NULL,
    "персонал_id" integer NOT NULL,
    "дата" date NOT NULL,
    "початок_зміни" time without time zone NOT NULL,
    "кінець_зміни" time without time zone NOT NULL
);


ALTER TABLE public."Графік_роботи" OWNER TO postgres;

--
-- Name: Графік_роботи_графік_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Графік_роботи_графік_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Графік_роботи_графік_id_seq" OWNER TO postgres;

--
-- Name: Графік_роботи_графік_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Графік_роботи_графік_id_seq" OWNED BY public."Графік_роботи"."графік_id";


--
-- Name: Десерти; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Десерти" (
    "десерт_id" integer NOT NULL,
    "назва_укр" character varying(100) NOT NULL,
    "назва_англ" character varying(100) NOT NULL,
    "ціна" numeric(5,2) NOT NULL,
    CONSTRAINT "Десерти_ціна_check" CHECK (("ціна" > (0)::numeric))
);


ALTER TABLE public."Десерти" OWNER TO postgres;

--
-- Name: Десерти_десерт_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Десерти_десерт_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Десерти_десерт_id_seq" OWNER TO postgres;

--
-- Name: Десерти_десерт_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Десерти_десерт_id_seq" OWNED BY public."Десерти"."десерт_id";


--
-- Name: Деталі_замовлення; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Деталі_замовлення" (
    "деталь_id" integer NOT NULL,
    "замовлення_id" integer CONSTRAINT "Деталі_замовлен_замовлення_id_not_null" NOT NULL,
    "напій_id" integer,
    "десерт_id" integer,
    "кількість" integer NOT NULL,
    "ціна_за_одиницю" numeric(5,2) CONSTRAINT "Деталі_замовле_ціна_за_одиниц_not_null" NOT NULL,
    CONSTRAINT "Деталі_замовлення_check" CHECK (((("напій_id" IS NOT NULL) AND ("десерт_id" IS NULL)) OR (("напій_id" IS NULL) AND ("десерт_id" IS NOT NULL)))),
    CONSTRAINT "Деталі_замовлення_кількість_check" CHECK (("кількість" > 0))
);


ALTER TABLE public."Деталі_замовлення" OWNER TO postgres;

--
-- Name: Деталі_замовлення_деталь_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Деталі_замовлення_деталь_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Деталі_замовлення_деталь_id_seq" OWNER TO postgres;

--
-- Name: Деталі_замовлення_деталь_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Деталі_замовлення_деталь_id_seq" OWNED BY public."Деталі_замовлення"."деталь_id";


--
-- Name: Замовлення; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Замовлення" (
    "замовлення_id" integer NOT NULL,
    "дата_час_замовлення" timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    "клієнт_id" integer,
    "офіціант_id" integer,
    "загальна_сума" numeric(10,2) DEFAULT 0,
    CONSTRAINT "Замовлення_загальна_сума_check" CHECK (("загальна_сума" >= (0)::numeric))
);


ALTER TABLE public."Замовлення" OWNER TO postgres;

--
-- Name: Замовлення_замовлення_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Замовлення_замовлення_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Замовлення_замовлення_id_seq" OWNER TO postgres;

--
-- Name: Замовлення_замовлення_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Замовлення_замовлення_id_seq" OWNED BY public."Замовлення"."замовлення_id";


--
-- Name: Клієнти; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Клієнти" (
    "клієнт_id" integer NOT NULL,
    "піб" character varying(255) NOT NULL,
    "дата_народження" date,
    "телефон" character varying(15),
    "адреса" character varying(255),
    "знижка" numeric(4,2) DEFAULT 0,
    CONSTRAINT "Клієнти_знижка_check" CHECK ((("знижка" >= (0)::numeric) AND ("знижка" <= (100)::numeric)))
);


ALTER TABLE public."Клієнти" OWNER TO postgres;

--
-- Name: Клієнти_клієнт_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Клієнти_клієнт_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Клієнти_клієнт_id_seq" OWNER TO postgres;

--
-- Name: Клієнти_клієнт_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Клієнти_клієнт_id_seq" OWNED BY public."Клієнти"."клієнт_id";


--
-- Name: Напої; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Напої" (
    "напій_id" integer NOT NULL,
    "назва_укр" character varying(100) NOT NULL,
    "назва_англ" character varying(100) NOT NULL,
    "ціна" numeric(5,2) NOT NULL,
    CONSTRAINT "Напої_ціна_check" CHECK (("ціна" > (0)::numeric))
);


ALTER TABLE public."Напої" OWNER TO postgres;

--
-- Name: Напої_напій_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Напої_напій_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Напої_напій_id_seq" OWNER TO postgres;

--
-- Name: Напої_напій_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Напої_напій_id_seq" OWNED BY public."Напої"."напій_id";


--
-- Name: Персонал; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Персонал" (
    "персонал_id" integer NOT NULL,
    "піб" character varying(255) NOT NULL,
    "телефон" character varying(15),
    "адреса" character varying(255),
    "позиція_id" integer NOT NULL
);


ALTER TABLE public."Персонал" OWNER TO postgres;

--
-- Name: Персонал_персонал_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Персонал_персонал_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Персонал_персонал_id_seq" OWNER TO postgres;

--
-- Name: Персонал_персонал_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Персонал_персонал_id_seq" OWNED BY public."Персонал"."персонал_id";


--
-- Name: Позиції; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Позиції" (
    "позиція_id" integer NOT NULL,
    "назва_позиції" character varying(50) NOT NULL
);


ALTER TABLE public."Позиції" OWNER TO postgres;

--
-- Name: Позиції_позиція_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Позиції_позиція_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Позиції_позиція_id_seq" OWNER TO postgres;

--
-- Name: Позиції_позиція_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Позиції_позиція_id_seq" OWNED BY public."Позиції"."позиція_id";


--
-- Name: Графік_роботи графік_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Графік_роботи" ALTER COLUMN "графік_id" SET DEFAULT nextval('public."Графік_роботи_графік_id_seq"'::regclass);


--
-- Name: Десерти десерт_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Десерти" ALTER COLUMN "десерт_id" SET DEFAULT nextval('public."Десерти_десерт_id_seq"'::regclass);


--
-- Name: Деталі_замовлення деталь_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Деталі_замовлення" ALTER COLUMN "деталь_id" SET DEFAULT nextval('public."Деталі_замовлення_деталь_id_seq"'::regclass);


--
-- Name: Замовлення замовлення_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Замовлення" ALTER COLUMN "замовлення_id" SET DEFAULT nextval('public."Замовлення_замовлення_id_seq"'::regclass);


--
-- Name: Клієнти клієнт_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Клієнти" ALTER COLUMN "клієнт_id" SET DEFAULT nextval('public."Клієнти_клієнт_id_seq"'::regclass);


--
-- Name: Напої напій_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Напої" ALTER COLUMN "напій_id" SET DEFAULT nextval('public."Напої_напій_id_seq"'::regclass);


--
-- Name: Персонал персонал_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Персонал" ALTER COLUMN "персонал_id" SET DEFAULT nextval('public."Персонал_персонал_id_seq"'::regclass);


--
-- Name: Позиції позиція_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Позиції" ALTER COLUMN "позиція_id" SET DEFAULT nextval('public."Позиції_позиція_id_seq"'::regclass);


--
-- Data for Name: Графік_роботи; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Графік_роботи" ("графік_id", "персонал_id", "дата", "початок_зміни", "кінець_зміни") FROM stdin;
\.


--
-- Data for Name: Десерти; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Десерти" ("десерт_id", "назва_укр", "назва_англ", "ціна") FROM stdin;
1	Нью-Йорк Чізкейк	New York Cheesecake	85.00
\.


--
-- Data for Name: Деталі_замовлення; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Деталі_замовлення" ("деталь_id", "замовлення_id", "напій_id", "десерт_id", "кількість", "ціна_за_одиницю") FROM stdin;
\.


--
-- Data for Name: Замовлення; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Замовлення" ("замовлення_id", "дата_час_замовлення", "клієнт_id", "офіціант_id", "загальна_сума") FROM stdin;
\.


--
-- Data for Name: Клієнти; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Клієнти" ("клієнт_id", "піб", "дата_народження", "телефон", "адреса", "знижка") FROM stdin;
2	Мельник Володимир Ігорович	2000-11-20	0953334455	Дружби Народів, 5	15.00
\.


--
-- Data for Name: Напої; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Напої" ("напій_id", "назва_укр", "назва_англ", "ціна") FROM stdin;
2	Капучино	Cappuccino	55.00
4	Американо	Americano	50.00
1	Еспресо	Espresso	45.00
3	Ванільний Латте	Vanilla Latte	60.00
\.


--
-- Data for Name: Персонал; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Персонал" ("персонал_id", "піб", "телефон", "адреса", "позиція_id") FROM stdin;
3	Ковальчук Олег Іванович	0937778899	Київ, вул. Кондитерська, 5	3
1	Іваненко Петро Олексійович	0981234567	Київ, вул. Шевченка, 15	1
\.


--
-- Data for Name: Позиції; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Позиції" ("позиція_id", "назва_позиції") FROM stdin;
1	Бариста
2	Офіціант
3	Кондитер
\.


--
-- Name: Графік_роботи_графік_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Графік_роботи_графік_id_seq"', 1, true);


--
-- Name: Десерти_десерт_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Десерти_десерт_id_seq"', 2, true);


--
-- Name: Деталі_замовлення_деталь_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Деталі_замовлення_деталь_id_seq"', 2, true);


--
-- Name: Замовлення_замовлення_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Замовлення_замовлення_id_seq"', 2, true);


--
-- Name: Клієнти_клієнт_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Клієнти_клієнт_id_seq"', 2, true);


--
-- Name: Напої_напій_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Напої_напій_id_seq"', 4, true);


--
-- Name: Персонал_персонал_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Персонал_персонал_id_seq"', 3, true);


--
-- Name: Позиції_позиція_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Позиції_позиція_id_seq"', 3, true);


--
-- Name: Графік_роботи Графік_роботи_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Графік_роботи"
    ADD CONSTRAINT "Графік_роботи_pkey" PRIMARY KEY ("графік_id");


--
-- Name: Графік_роботи Графік_роботи_персонал_id_дата_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Графік_роботи"
    ADD CONSTRAINT "Графік_роботи_персонал_id_дата_key" UNIQUE ("персонал_id", "дата");


--
-- Name: Десерти Десерти_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Десерти"
    ADD CONSTRAINT "Десерти_pkey" PRIMARY KEY ("десерт_id");


--
-- Name: Деталі_замовлення Деталі_замовлення_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Деталі_замовлення"
    ADD CONSTRAINT "Деталі_замовлення_pkey" PRIMARY KEY ("деталь_id");


--
-- Name: Замовлення Замовлення_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Замовлення"
    ADD CONSTRAINT "Замовлення_pkey" PRIMARY KEY ("замовлення_id");


--
-- Name: Клієнти Клієнти_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Клієнти"
    ADD CONSTRAINT "Клієнти_pkey" PRIMARY KEY ("клієнт_id");


--
-- Name: Клієнти Клієнти_телефон_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Клієнти"
    ADD CONSTRAINT "Клієнти_телефон_key" UNIQUE ("телефон");


--
-- Name: Напої Напої_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Напої"
    ADD CONSTRAINT "Напої_pkey" PRIMARY KEY ("напій_id");


--
-- Name: Персонал Персонал_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Персонал"
    ADD CONSTRAINT "Персонал_pkey" PRIMARY KEY ("персонал_id");


--
-- Name: Персонал Персонал_телефон_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Персонал"
    ADD CONSTRAINT "Персонал_телефон_key" UNIQUE ("телефон");


--
-- Name: Позиції Позиції_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Позиції"
    ADD CONSTRAINT "Позиції_pkey" PRIMARY KEY ("позиція_id");


--
-- Name: Позиції Позиції_назва_позиції_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Позиції"
    ADD CONSTRAINT "Позиції_назва_позиції_key" UNIQUE ("назва_позиції");


--
-- Name: Графік_роботи Графік_роботи_персонал_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Графік_роботи"
    ADD CONSTRAINT "Графік_роботи_персонал_id_fkey" FOREIGN KEY ("персонал_id") REFERENCES public."Персонал"("персонал_id");


--
-- Name: Деталі_замовлення Деталі_замовлення_десерт_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Деталі_замовлення"
    ADD CONSTRAINT "Деталі_замовлення_десерт_id_fkey" FOREIGN KEY ("десерт_id") REFERENCES public."Десерти"("десерт_id");


--
-- Name: Деталі_замовлення Деталі_замовлення_замовлення_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Деталі_замовлення"
    ADD CONSTRAINT "Деталі_замовлення_замовлення_id_fkey" FOREIGN KEY ("замовлення_id") REFERENCES public."Замовлення"("замовлення_id") ON DELETE CASCADE;


--
-- Name: Деталі_замовлення Деталі_замовлення_напій_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Деталі_замовлення"
    ADD CONSTRAINT "Деталі_замовлення_напій_id_fkey" FOREIGN KEY ("напій_id") REFERENCES public."Напої"("напій_id");


--
-- Name: Замовлення Замовлення_клієнт_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Замовлення"
    ADD CONSTRAINT "Замовлення_клієнт_id_fkey" FOREIGN KEY ("клієнт_id") REFERENCES public."Клієнти"("клієнт_id");


--
-- Name: Замовлення Замовлення_офіціант_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Замовлення"
    ADD CONSTRAINT "Замовлення_офіціант_id_fkey" FOREIGN KEY ("офіціант_id") REFERENCES public."Персонал"("персонал_id");


--
-- Name: Персонал Персонал_позиція_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Персонал"
    ADD CONSTRAINT "Персонал_позиція_id_fkey" FOREIGN KEY ("позиція_id") REFERENCES public."Позиції"("позиція_id");


--
-- PostgreSQL database dump complete
--

\unrestrict 7BqKyq0dQa1TGbyhNfIbogKsjXhpHf3r03FJEOklqyGFqF90i4o0yG4Hgq9C1VP

