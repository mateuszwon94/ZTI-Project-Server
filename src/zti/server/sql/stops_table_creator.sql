DROP TABLE IF EXISTS stops;

CREATE TABLE stops (
	id		serial	PRIMARY KEY,
	name	text	NOT NULL UNIQUE,
	nz		boolean	DEFAULT FALSE,
	loc_x	real	NOT NULL,
	loc_y	real	NOT NULL
);

INSERT INTO stops (name, loc_x, loc_y) VALUES ('Grzybowa',		2.88, 0.85);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Kamienna',		4.03, 1.62);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Plac Nowy',		4.01, 2.79);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stary Dwór',	5.34, 2.73);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('AGH',			4.82, 4.22);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Nowy Dwór',		5.18, 5.82);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('11. Listopada',	4.09, 6.72);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Zapomniana',	3.55, 7.91);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Czerwone Maki',	3.37, 9.11);
INSERT INTO stops (name, loc_x, loc_y, nz) VALUES ('Granica',	2.05, 10.05, TRUE);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Lotnisko',		0.23, 11.55);
INSERT INTO stops (name, loc_x, loc_y, nz) VALUES ('Borsucza',	4.95, 8.17,  TRUE);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Badylowa',		6.47, 8.49);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Róg',			7.81, 2.67);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Teatr',			6.66, 3.53);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Uniwersytet',	6.55, 5.05);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Poczta',		8.01, 6.27);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Wołowa',		7.79, 7.27);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Krakowska',		7.75, 8.67);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Nocna',			8.63, 9.63);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Ostra Brama',	9.55, 1.23);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Dworzec',		9.23, 2.65);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Standardowa',	9.51, 6.01);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Długa',			10.77, 3.51);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Opera',			10.75, 5.13);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Brozowa',		11.11, 6.95);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Ostatnia',		10.77, 10.65);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stadion',		12.15, 2.57);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Różana',		12.61, 5.33);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Bystra',		12.81, 6.85);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Sklep',			13.23, 8.21);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Odnogowa',		12.23, 9.67);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Nowe Osiedle',	15.13, 1.51);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stare Osiedla',	13.89, 2.75);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Starorzeczna',	15.03, 4.05);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Muzeum',		14.33, 5.63);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stawy',			14.63, 9.01);