
CREATE TABLE categoria (
 id integer NOT NULL PRIMARY KEY,
 nome varchar(255) NOT NULL
);

CREATE TABLE notes (
 id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
 descricao varchar(255) NOT NULL,
 categoria_id NOT NULL,
 FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);
