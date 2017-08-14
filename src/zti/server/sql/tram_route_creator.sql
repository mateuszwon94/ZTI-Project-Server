DROP TABLE IF EXISTS lines;

CREATE TABLE lines (
	number		int		PRIMARY KEY,
	variants	text[]	DEFAULT NULL,
	route		int[]	NOT NULL,
	f_peak		int		NOT NULL,
	f_not_peak	int		NOT NULL,
	first		time	NOT NULL,
	last		time	NOT NULL
);

INSERT INTO lines (number, f_peak, f_not_peak, first, last, route, variants) VALUES (0, 5, 10, '3:30', '22:30', 
	ARRAY[21, 22, 14, 15, 16, 17, 23, 25, 24, 22, 21], ARRAY['N', 'Z']);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (1, 10, 10, '4:30', '22:30', 
	ARRAY[33, 34, 28, 24, 25, 23, 17, 18, 19, 20]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (2, 5, 10, '4:00', '23:00', 
	ARRAY[27, 32, 31, 30, 26, 23, 25, 24, 22, 14]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (3, 5, 10, '3:30', '23:30', 
	ARRAY[35, 34, 28, 24, 22, 14, 15, 4, 3, 2, 1]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (4, 10, 10, '3:30', '23:30', 
	ARRAY[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (5, 10, 20, '4:30', '21:30', 
	ARRAY[21, 22, 24, 25, 23, 26, 30, 31, 37]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (6, 10, 20, '5:30', '21:30', 
	ARRAY[1, 2, 3, 4, 5, 6, 16, 17, 23, 25, 29, 36, 35]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (7, 10, 20, '4:30', '22:30', 
	ARRAY[33, 34, 35, 36, 30, 26, 23, 17,16, 15, 4]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (8, 5, 10, '3:30', '23:30', 
	ARRAY[11, 10, 9, 8, 7, 6, 16, 15, 14, 22, 21]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (9, 10, 10, '3:30', '22:30', 
	ARRAY[20, 19, 13, 12, 8, 7, 6, 5, 4, 15, 14]);
INSERT INTO lines (number, f_peak, f_not_peak, first, last, route) VALUES (10, 10, 10, '3:30', '22:30', 
	ARRAY[27, 32, 31, 30, 26, 23, 17, 18, 19, 13, 12, 8, 9]);