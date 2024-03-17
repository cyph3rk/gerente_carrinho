-- Database: base_dev

BEGIN;

CREATE TABLE IF NOT EXISTS public.itens
(
    id serial NOT NULL,
    nome text NOT NULL,
    valor text NOT NULL,
    estoque text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.roles
(
    id serial NOT NULL,
    name text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.usuario
(
    id serial NOT NULL,
    login text NOT NULL,
    password text NOT NULL,
    role text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.carrinho
(
    id serial NOT NULL,
    id_usuario serial NOT NULL,
    id_itens serial NOT NULL,
    quantidade text NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.carrinho
    ADD FOREIGN KEY (id_usuario)
        REFERENCES public.usuario (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;

ALTER TABLE IF EXISTS public.carrinho
    ADD FOREIGN KEY (id_itens)
        REFERENCES public.itens (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


INSERT INTO ROLES(NAME) VALUES('ADMIN');
INSERT INTO ROLES(NAME) VALUES('CLIENTE');
INSERT INTO ROLES(NAME) VALUES('USER');

END;
