# Технологии

- Язык: Java 8+

- Фреймворк: Spring Boot, Spring Data JPA

- База данных: PostgreSQL

- Сборка: Maven

- Тестирование: JUnit, Spring Test

- Контейнеризация: Docker

## Запуск

docker-compose build

docker-compose up -d

## Примеры фильтрации/сортироки

/clients?search=ООО

/clients?sortBy=name&sortOrder=asc

/deposits?clientId=1

/deposits?bankId=2

/deposits?minPercent=5.0&maxPercent=6.0

/banks?sortBy=name&sortOrder=desc


### В resources лежит data с примерами сущностей
