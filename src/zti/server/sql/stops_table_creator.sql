DROP TABLE IF EXISTS stops;

CREATE TABLE stops (
	id		serial	PRIMARY KEY,
	name	text	NOT NULL UNIQUE,
	nz		boolean	DEFAULT FALSE,
	loc_x	real	NOT NULL,
	loc_y	real	NOT NULL,
	conns	int[]	DEFAULT NULL,
	times	int[]	DEFAULT NULL
);

INSERT INTO stops (name, loc_x, loc_y) VALUES ('Grzybowa',		28.8, 08.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Kamienna',		40.3, 16.2);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Plac Nowy',		40.1, 27.9);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stary Dwor',	53.4, 27.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('AGH',			48.2, 42.2);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Nowy Dwor',		51.8, 58.2);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('11. Listopada',	40.9, 67.2);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Zapomniana',	35.5, 79.1);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Czerwone Maki',	33.7, 91.1);
INSERT INTO stops (name, loc_x, loc_y, nz) VALUES ('Granica',	20.5, 100.5, TRUE);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Lotnisko',		02.3, 115.5);
INSERT INTO stops (name, loc_x, loc_y, nz) VALUES ('Borsucza',	49.5, 81.7,  TRUE);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Badylowa',		64.7, 84.9);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Rog',			78.1, 26.7);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Teatr',			66.6, 35.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Uniwersytet',	65.5, 50.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Poczta',		80.1, 62.7);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Wolowa',		77.9, 72.7);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Krakowska',		77.5, 86.7);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Nocna',			86.3, 96.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Ostra Brama',	95.5, 12.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Dworzec',		92.3, 26.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Standardowa',	95.1, 60.1);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Dluga',			107.7, 35.1);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Opera',			107.5, 51.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Brzozowa',		111.1, 69.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Ostatnia',		107.7, 106.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stadion',		121.5, 25.7);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Rozana',		126.1, 53.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Bystra',		128.1, 68.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Sklep',			132.3, 82.1);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Odnogowa',		122.3, 96.7);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Nowe Osiedle',	151.3, 15.1);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stare Osiedla',	138.9, 27.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Starorzeczna',	150.3, 40.5);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Muzeum',		143.3, 56.3);
INSERT INTO stops (name, loc_x, loc_y) VALUES ('Stawy',			146.3, 90.1);

UPDATE Stops SET conns = ARRAY[2] WHERE name = 'Grzybowa';		-- 1
UPDATE Stops SET conns = ARRAY[1, 3] WHERE name = 'Kamienna';		-- 2
UPDATE Stops SET conns = ARRAY[2, 4] WHERE name = 'Plac Nowy';	-- 3
UPDATE Stops SET conns = ARRAY[3, 5, 15] WHERE name = 'Stary Dwor';-- 4
UPDATE Stops SET conns = ARRAY[4, 6] WHERE name = 'AGH';			-- 5
UPDATE Stops SET conns = ARRAY[5, 7, 16] WHERE name = 'Nowy Dwor';-- 6
UPDATE Stops SET conns = ARRAY[6, 8] WHERE name = '11. Listopada';-- 7
UPDATE Stops SET conns = ARRAY[7, 9, 12] WHERE name = 'Zapomniana';-- 8
UPDATE Stops SET conns = ARRAY[8, 10] WHERE name = 'Czerwone Maki';-- 9
UPDATE Stops SET conns = ARRAY[9, 11] WHERE name = 'Granica';		-- 10
UPDATE Stops SET conns = ARRAY[10] WHERE name = 'Lotnisko'; 		-- 11
UPDATE Stops SET conns = ARRAY[8, 13] WHERE name = 'Borsucza';	-- 12
UPDATE Stops SET conns = ARRAY[12, 19] WHERE name = 'Badylowa';	-- 13
UPDATE Stops SET conns = ARRAY[22, 15] WHERE name = 'Rog';		-- 14
UPDATE Stops SET conns = ARRAY[4, 14, 16] WHERE name = 'Teatr';	-- 15
UPDATE Stops SET conns = ARRAY[15, 6, 17] WHERE name = 'Uniwersytet';-- 16
UPDATE Stops SET conns = ARRAY[16, 18, 23] WHERE name = 'Poczta';	-- 17
UPDATE Stops SET conns = ARRAY[17, 19] WHERE name = 'Wolowa';		-- 18
UPDATE Stops SET conns = ARRAY[18, 20, 13] WHERE name = 'Krakowska';-- 19
UPDATE Stops SET conns = ARRAY[19] WHERE name = 'Nocna';			-- 20
UPDATE Stops SET conns = ARRAY[22] WHERE name = 'Ostra Brama';	-- 21
UPDATE Stops SET conns = ARRAY[21, 14, 24] WHERE name = 'Dworzec';-- 22
UPDATE Stops SET conns = ARRAY[17, 25, 26] WHERE name = 'Standardowa';-- 23
UPDATE Stops SET conns = ARRAY[22, 25, 28] WHERE name = 'Dluga';	-- 24
UPDATE Stops SET conns = ARRAY[24, 29, 23] WHERE name = 'Opera';	-- 25
UPDATE Stops SET conns = ARRAY[23, 30] WHERE name = 'Brzozowa';	-- 26
UPDATE Stops SET conns = ARRAY[32] WHERE name = 'Ostatnia';		-- 27
UPDATE Stops SET conns = ARRAY[24, 34] WHERE name = 'Stadion';	-- 28
UPDATE Stops SET conns = ARRAY[25, 36] WHERE name = 'Rozana';		-- 29
UPDATE Stops SET conns = ARRAY[36, 31, 26] WHERE name = 'Bystra';	-- 30
UPDATE Stops SET conns = ARRAY[30, 32, 37] WHERE name = 'Sklep';	-- 31
UPDATE Stops SET conns = ARRAY[31, 27] WHERE name = 'Odnogowa';	-- 32
UPDATE Stops SET conns = ARRAY[34] WHERE name = 'Nowe Osiedle';	-- 33
UPDATE Stops SET conns = ARRAY[33, 28, 35] WHERE name = 'Stare Osiedla';-- 34
UPDATE Stops SET conns = ARRAY[34, 36] WHERE name = 'Starorzeczna';		-- 35
UPDATE Stops SET conns = ARRAY[29, 35, 30] WHERE name = 'Muzeum';			-- 36
UPDATE Stops SET conns = ARRAY[31] WHERE name = 'Stawy';			-- 37

UPDATE Stops SET times = ARRAY[3] WHERE name = 'Grzybowa';		-- 1
UPDATE Stops SET times = ARRAY[3, 3] WHERE name = 'Kamienna';		-- 2
UPDATE Stops SET times = ARRAY[3, 2] WHERE name = 'Plac Nowy';	-- 3
UPDATE Stops SET times = ARRAY[2, 4, 3] WHERE name = 'Stary Dwor';-- 4
UPDATE Stops SET times = ARRAY[4, 4] WHERE name = 'AGH';			-- 5
UPDATE Stops SET times = ARRAY[4, 2, 3] WHERE name = 'Nowy Dwor';-- 6
UPDATE Stops SET times = ARRAY[2, 3] WHERE name = '11. Listopada';-- 7
UPDATE Stops SET times = ARRAY[3, 2, 3] WHERE name = 'Zapomniana';-- 8
UPDATE Stops SET times = ARRAY[2, 4] WHERE name = 'Czerwone Maki';-- 9
UPDATE Stops SET times = ARRAY[4, 12] WHERE name = 'Granica';		-- 10
UPDATE Stops SET times = ARRAY[12] WHERE name = 'Lotnisko'; 		-- 11
UPDATE Stops SET times = ARRAY[3, 3] WHERE name = 'Borsucza';	-- 12
UPDATE Stops SET times = ARRAY[3, 2] WHERE name = 'Badylowa';	-- 13
UPDATE Stops SET times = ARRAY[2, 3] WHERE name = 'Rog';		-- 14
UPDATE Stops SET times = ARRAY[3, 3, 4] WHERE name = 'Teatr';	-- 15
UPDATE Stops SET times = ARRAY[4, 3, 4] WHERE name = 'Uniwersytet';-- 16
UPDATE Stops SET times = ARRAY[4, 2, 3] WHERE name = 'Poczta';	-- 17
UPDATE Stops SET times = ARRAY[2, 3] WHERE name = 'Wolowa';		-- 18
UPDATE Stops SET times = ARRAY[3, 2, 2] WHERE name = 'Krakowska';-- 19
UPDATE Stops SET times = ARRAY[2] WHERE name = 'Nocna';			-- 20
UPDATE Stops SET times = ARRAY[3] WHERE name = 'Ostra Brama';	-- 21
UPDATE Stops SET times = ARRAY[3, 2, 4] WHERE name = 'Dworzec';-- 22
UPDATE Stops SET times = ARRAY[3, 3, 3] WHERE name = 'Standardowa';-- 23
UPDATE Stops SET times = ARRAY[4, 3, 4] WHERE name = 'Dluga';	-- 24
UPDATE Stops SET times = ARRAY[3, 3, 3] WHERE name = 'Opera';	-- 25
UPDATE Stops SET times = ARRAY[3, 2] WHERE name = 'Brzozowa';	-- 26
UPDATE Stops SET times = ARRAY[4] WHERE name = 'Ostatnia';		-- 27
UPDATE Stops SET times = ARRAY[4, 3] WHERE name = 'Stadion';	-- 28
UPDATE Stops SET times = ARRAY[3, 3] WHERE name = 'Rozana';		-- 29
UPDATE Stops SET times = ARRAY[4, 3, 2] WHERE name = 'Bystra';	-- 30
UPDATE Stops SET times = ARRAY[3, 3, 2] WHERE name = 'Sklep';	-- 31
UPDATE Stops SET times = ARRAY[3, 4] WHERE name = 'Odnogowa';	-- 32
UPDATE Stops SET times = ARRAY[4] WHERE name = 'Nowe Osiedle';	-- 33
UPDATE Stops SET times = ARRAY[4, 3, 4] WHERE name = 'Stare Osiedla';-- 34
UPDATE Stops SET times = ARRAY[4, 4] WHERE name = 'Starorzeczna';		-- 35
UPDATE Stops SET times = ARRAY[3, 4, 4] WHERE name = 'Muzeum';			-- 36
UPDATE Stops SET times = ARRAY[2] WHERE name = 'Stawy';			-- 37
