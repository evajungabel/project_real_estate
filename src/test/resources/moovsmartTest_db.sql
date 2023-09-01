insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('KovBea', 'kovacsbea1@gmail.com', 'Kovács Bea', 'B3aPass');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('SzIstvan', 'szaboistvan2@yahoo.com', 'Szabó István', 'Istv@n123');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('NaEva', 'nagyeva3@hotmail.com', 'Nagy Éva', '3vaSecr@t');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('TakAd', 'takacsadam4@gmail.com', 'Takács Ádám', 'Ad@mPass');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('SziAnna', 'szilagyianna5@yahoo.com', 'Szilágyi Anna', 'Ann@567');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('KiMi', 'kissmihaly6@hotmail.com', 'Kiss Mihály', 'M1haJelz0');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('NemTunde', 'nemethtunde7@gmail.com', 'Németh Tünde', 'T@nd3Pw');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('SzoGer', 'szokegergely8@yahoo.com', 'Szőke Gergely', 'G3rg3lySec');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('KoTu', 'kovacstunde9@hotmail.com', 'Kovács Tünde', 'Tund3J3lz0');
insert into moovsmartTest.custom_user (username, e_mail, name, password)
values ('SzePet', 'szentespeter10@gmail.com', 'Szentes Péter', 'P3t3rP@ss');

INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (1, true, 300, null, '2023-08-31', null, null, 'Kényelmes családi ház', 'image/jpeg;base64,/2579j/4AAQSk',
        'Eladó ház', 4, 350000000, 'HOUSE', 'SzIstvan', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (2, true, 80, null, '2023-08-29', null, null, 'Stúdió lakás belvárosi környezetben',
        'image/jpeg;base68,/9j143/4Adfhdk', 'Eladó lakás Pécsett', 1, 180000000, 'FLAT', 'SziAnna', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (3, true, 600, null, '2023-08-20', null, null, 'Panasz panorámával', 'image/jpeg;base464667,/sgrsgrgsrerhSk',
        'Eladó lakás a városban', 3, 420000000, 'FLAT', 'NemTunde', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (4, true, 450, null, '2023-08-18', null, null, 'Tágas családi ház zöld környezetben',
        'image/jpeg;base64,/9j0364/4AAQSk..atjtj.', 'Eladó ház', 6, 550000000, 'HOUSE', 'SzePet', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (5, true, 60, null, '2023-08-15', null, null, 'Kis lakás kiadó a belvárosban',
        'image/jpeg;base64,/9036j/4AAQSartjarjk...',
        'Kiadó lakás', 1, 150000000, 'FLAT', 'KoTu', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (6, true, 750, null, '2023-08-27', null, null, 'Modern irodaház vállalkozásnak',
        'image/jpeg;base64,/923j/4AAQERewSk...', 'Eladó irodaház', null, 11000000, 'OFFICE_BUILDING', 'KiMi', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (7, true, 480, null, '2023-08-15', null, null, 'Központi lakás remek kilátással',
        'image/jpeg;base64,/934j/4AAaetraerjQSk...', 'Eladó lakás', 2, 320000000, 'FLAT', 'SziAnna', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (8, true, 990, null, '2023-08-31', null, null, 'Elegáns villa kerttel és medencével',
        'image/jpeg;base64,/3349j/4AAaehaQSk...', 'Eladó villa', 8, 110000000, 'VILLA', 'SzoGer', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (9, true, 90, null, '2023-08-29', null, null, 'Praktikus lakás fiataloknak', 'image/jpeg;base64,/9j/4AAQSk...',
        'Eladó lakás', 3, 250000000, 'FLAT', 'SzoGer', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (10, true, 520, null, '2023-08-20', null, null, 'Egyedi tervezésű ház csendes környéken',
        'image/jpeg;base64,/9j/4AaergAQSk...', 'Eladó ház', 6, 580000000, 'HOUSE', 'NaEva', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (11, true, 50, null, '2023-08-31', null, null, 'Elegáns lakás kerttel',
        'image/jpeg;base64,/9j/4AAasadasdQSk...', 'Eladó villa', 8, 11000000, 'FLAT', 'KovBea', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (12, true, 90, null, '2023-08-29', null, null, 'Praktikus lakás fiataloknak', 'image/jpeg;base64,/9j/4AAQSk...',
        'Eladó lakás', 3, 250000000, 'FLAT', 'KovBea', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (13, true, 520, null, '2023-08-20', null, null, 'Egyedi tervezésű ház csendes környéken',
        'image/jpeg;base64,/9j/4AAQSk...', 'Eladó ház', 6, 580000000, 'HOUSE', 'KiMi', null);
INSERT INTO moovsmartTest.property (property_id, active, area, date_of_activation, date_of_creation,
                                    date_of_inactivation, date_of_sale, description, image_url, name, number_of_rooms,
                                    price, type, custom_user_name, estate_agent_id)
VALUES (14, true, 120, null, '2023-08-20', null, null, 'Egyedi tervezésű ház csendes környéken',
        'image/jpeg;base64,/9j/4AAQSk...', 'Eladó ház', 6, 580000000, 'HOUSE', 'NemTunde', null);

INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (1, 'Monor', 'Magyarország', null, '14', 'Mikes Kelemen utca', 2200, 2);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (2, 'Bénye', 'Magyarország', null, '5', 'Kosztolányi utca', 1200, 1);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (3, 'Káva', 'Magyarország', null, '3', 'Cserép utca', 4200, 4);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (4, 'Tök', 'Magyarország', null, '2', 'Rogán utca', 2202, 3);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (5, 'Zsámbék', 'Magyarország', null, '4', 'Kiss utca', 2255, 6);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (6, 'Alma', 'Magyarország', null, '1', 'Traktor utca', 2456, 5);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (7, 'Novigrád', 'Magyarország', null, '6', 'Almás utca', 2987, 8);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (8, 'Velen', 'Magyarország', null, '23B', 'Árnyas utca', 1230, 7);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (9, 'Tatuin', 'Magyarország', null, 'h/2', 'Kossúth utca', 5600, 9);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (10, 'Pánd', 'Magyarország', null, '34', 'Hosszú utca', 7800, 13);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (11, 'Pilis', 'Magyarország', null, '123', 'Széles út', 8700, 11);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (12, 'Úri', 'Magyarország', null, '12', 'Tó utca', 9900, 10);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (13, 'Felcsút', 'Magyarország', null, '2/3', 'Lölő utca', 4444, 12);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (14, 'Alcsút-Dobozos', 'Magyarország', null, '4', 'Pozsonyi utca', 7777, 14);
INSERT INTO moovsmartTest.address (address_id, city, country, door_number, house_number, street, zipcode, property_id)
VALUES (15, 'Péteri', 'Magyarország', null, '23/a', 'Kapisztrán utca', 8888, 14);
INSERT INTO moovsmartTest.property_data(property_data_id, air_conditioner, balcony, garden, heating, insulation,
                                        parking, property_orientation, property_id)
VALUES (1, true, true, true, 'wood', false, true, 'NORTH_WEST', 2);
INSERT INTO moovsmartTest.property_data(property_data_id, air_conditioner, balcony, garden, heating, insulation,
                                        parking, property_orientation, property_id)
VALUES (2, true, false, true, 'gase', true, true, 'WEST', 3);
INSERT INTO moovsmartTest.property_data(property_data_id, air_conditioner, balcony, garden, heating, insulation,
                                        parking, property_orientation, property_id)
VALUES (3, false, true, false, 'electric', false, true, 'NORTH', 1);
INSERT INTO moovsmartTest.estate_agent(estate_agent_id, email, name, agent_rank, sell_point)
VALUES (1, 'katika@dunahouse.hu', 'Kovács Katalin', 'PROFESSIONAL', 30);
INSERT INTO moovsmartTest.estate_agent(estate_agent_id, email, name, agent_rank, sell_point)
VALUES (2, 'pistike@ingatlanos.hu', 'Kis Pista', 'MEDIOR', 20);
INSERT INTO moovsmartTest.estate_agent(estate_agent_id, email, name, agent_rank, sell_point)
VALUES (3, 'Gézu@dunahouse.hu', 'Bemeteg Géza', 'RECRUIT', 3);






