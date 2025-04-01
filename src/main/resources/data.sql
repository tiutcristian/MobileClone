-- INSERT USERS --
INSERT INTO users (`id`, `first_name`, `last_name`, `email`, `phone`)
VALUES (1,'Cristian','Tiut','tiutcristian@gmail.com','0721644423');




-- INSERT LISTINGS --
INSERT INTO listings (
    `id`,
    `user_id`,
    `image_url`,
    `title`,
    `price`,
    `make`,
    `model`,
    `description`,
    `manufacture_year`,
    `mileage`,
    `engine_size`,
    `horsepower`,
    `transmission`,
    `fuel_type`
) VALUES (
    1,
    1,
    '/images/corolla.png',
    'Toyota Corolla 2020 for sale',
    20000,
    'Toyota',
    'Corolla',
    'For Sale: 2020 Toyota Corolla - Reliable & Fuel Efficient!',
    2023,
    10000,
    1.8,
    140,
    'AUTOMATIC',
    'DIESEL'
);

INSERT INTO listings (
    `id`,
    `user_id`,
    `image_url`,
    `title`,
    `price`,
    `make`,
    `model`,
    `description`,
    `manufacture_year`,
    `mileage`,
    `engine_size`,
    `horsepower`,
    `transmission`,
    `fuel_type`
) VALUES (
    2,
    1,
    '/images/yaris.png',
    'Toyota Yaris 2019 for sale',
    15000,
    'Toyota',
    'Yaris',
    'This is a 2019 Toyota Yaris for sale',
    2019,
    20000,
    1.5,
    110,
    'MANUAL',
    'PETROL'
);

INSERT INTO listings (
    `id`,
    `user_id`,
    `image_url`,
    `title`,
    `price`,
    `make`,
    `model`,
    `description`,
    `manufacture_year`,
    `mileage`,
    `engine_size`,
    `horsepower`,
    `transmission`,
    `fuel_type`
) VALUES (
    3,
    1,
    '/images/camry.png',
    'Toyota Camry 2018 for sale',
    25000,
    'Toyota',
    'Camry',
    'This is a 2018 Toyota Camry for sale',
    2018,
    30000,
    2.5,
    200,
    'AUTOMATIC',
    'HYBRID'
);

INSERT INTO listings (
    `id`,
    `user_id`,
    `image_url`,
    `title`,
    `price`,
    `make`,
    `model`,
    `description`,
    `manufacture_year`,
    `mileage`,
    `engine_size`,
    `horsepower`,
    `transmission`,
    `fuel_type`
) VALUES (
    4,
    1,
    '/images/rav4.png',
    'Toyota RAV4 2017 for sale',
    30000,
    'Toyota',
    'RAV4',
    'This is a 2017 Toyota RAV4 for sale',
    2000,
    40000,
    2,
    150,
    'AUTOMATIC',
    'DIESEL'
);