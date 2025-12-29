# Учебное задание по созданию онлайн платформы.

📋 О проекте

Платформа для онлайн-обучения с курсами, уроками, заданиями и тестами. Демонстрирует работу Spring Data JPA + Hibernate ORM с PostgreSQL.
🛠 Технологии

    Java 17+, Spring Boot 3.2

    Spring Data JPA, Hibernate ORM

    PostgreSQL, H2 (для тестов)

    Maven, Lombok

🏗 Сущности (15+)

    User (студенты/преподаватели)

    Course, Category, Module, Lesson

    Assignment, Submission

    Quiz, Question, AnswerOption, QuizSubmission

    Enrollment, CourseReview

🔗 Связи

    One-to-One (Quiz ↔ Module)

    One-to-Many (Course → Modules, Module → Lessons)

    Many-to-Many (User ↔ Course через Enrollment)

⚡ Быстрый старт
1. Настройка БД
bash

# PostgreSQL
createdb learning_platform

# Или Docker
docker run -d --name learning-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=learning_platform \
  -p 5432:5432 postgres:15

2. Запуск приложения
bash

# Сборка
mvn clean compile

# Запуск с демо-данными
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Или
java -jar target/*.jar --spring.profiles.active=dev

3. Тестирование
bash

# Все тесты
mvn test


📊 Что проверялось

    ✅ Создание сущностей через Builder

    ✅ Ленивая загрузка (Lazy Loading)

    ✅ Транзакции (@Transactional)

    ✅ Двунаправленные связи

    ✅ CRUD операции

    ✅ Расчет баллов в тестах

    ✅ Прохождение/непрохождение квизов


📁 Структура
text

ormtask/
├── src/main/java/com/learning/platform/
│   ├── model/          # JPA сущности
│   ├── repository/     # Spring Data репозитории  
│   ├── service/        # Бизнес-логика
│   └── controller/     # REST API
└── src/test/           # Тесты

🧪 Демо-данные

При запуске с профилем dev создаются:

    3 пользователя: студент, преподаватель, админ

    Категории: Programming, Design, Business

    Курсы: Java Programming, Spring Boot

    Тесты, задания, модули