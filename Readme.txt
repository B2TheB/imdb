docker exec -it guide-mysql bash
mysql -uroot -padmin

CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON imdb.* TO 'admin'@'%';


insert into movie (id, title, genre) values (1, 'The Dark Knight', 0);
insert into movie (id, title, genre) values (2, 'The Shawshank Redemption', 2);
insert into movie (id, title, genre) values (3, 'The Godfather', 0);
insert into movie (id, title, genre) values (4, 'Dumb and Dumber', 1);
insert into movie (id, title, genre) values (5, 'Scary Movie', 1);

Invoke-WebRequest -Uri http://localhost:8080/user `
                   -Method Post `
                   -ContentType "application/json" `
                   -Body '{"username": "testuser", "password": "testpassword2", "email": "testuser@example.com"}'

                   Invoke-WebRequest -Uri http://localhost:8080/rating `
                   -Method Post `
                   -ContentType "application/json" `
                   -Body '{"username": "testuser", "movieTitle": "Dumb and Dumber", "rating": 3}'