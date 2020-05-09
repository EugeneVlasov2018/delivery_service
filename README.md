# delivery_service
Сервис курьерской службы

Перед запуском:

- создать бд в PostgreSql
- в pom.xml указать параметры БД (url, password, username) в блоке properties:
'''
<flyway.user>тут юзера</flyway.user>
<flyway.url>тут урл</flyway.url>
<flyway.password>тут пароль</flyway.password>
'''

комманды для запуска:
```
mvn flyway:clean - очистит БД от старых таблиц и данных
mvn flyway:migrate - создаст таблицы, накатит тестовые данные
mvn clean install
mvn spring-boot:run
```

ВАЖНО!!! Даты в БД хранятся в таймзоне сервера, но возвращаются в UTC, чтобы на фронте біло удобнее подгонять ответ под таймзону клиента
swagger c докой для Api (после запуска приложения)
```
http://localhost:8080/deliveryservice/swagger-ui.html#/
```
