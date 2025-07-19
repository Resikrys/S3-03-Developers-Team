# VIRTUAL SCAPE ROOM project
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

### 👾 Authors
* [ArnauAsole](https://github.com/ArnauAsole)
* [anaberod](https://github.com/anaberod)
* [JavierDolo](https://github.com/JavierDolo)
* [Resikrys](https://github.com/Resikrys)

## Summary

This exercise involves developing an application to manage a virtual Escape
Room. The application will allow the creation and management of themed rooms,
intriguing clueObjects, unique decoration items, and maintain an up-to-date inventory.
Additionally, functionalities for tracking income, and user notifications will
be implemented. Data persistence will be achieved using MySQL.

### App flow
- Docker Compose inicia el contenedor mysql-db.
- Java se inicia
- Dentro de tu aplicación Java, en tus clases de implementación del patrón DAO,
  utilizarás la biblioteca JDBC de Java para:
1. Establecer la conexión con la base de datos MySQL (usando la URL, usuario y
   contraseña definidos en el docker-compose.yml).

2. Ejecutar las sentencias SQL (INSERT, SELECT, UPDATE, DELETE) que recuperan
   o modifican los datos.

3. Mapear los resultados de las consultas a objetos Java (tus entidades Room,
   Booking, etc.).

## Technologies

* Java 24
* Maven 3.10
* MySQL 8.2
* Docker compose
* MongoDB
* Git/GitHub

## Additional dependencies
* JDBC library

## 📁 Project structure
```text
escape-room-app/
│
├── docker/
│   ├── docker-compose.yml
│   └── mysql/
│       └── init.sql
│
├── .env
├── pom.xml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── escaperoom/
│   │   │           ├── App.java
│   │   │           ├── dao/
│   │   │           ├── model/
│   │   │           ├── service/
│   │   │           ├── controller/
│   │   │           └── observer/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── log4j2.xml
│   └── test/
│       └── java/
│           └── com/
│               └── escaperoom/
│                   ├── dao/
│                   ├── service/
│                   └── controller/
```
## Configurar entorno (.env)
- "Clona el repositorio."
- "Copia .env.example a .env: cp .env.example .env."
- "Edita el archivo .env y rellena tus credenciales de base de datos."
- "Inicia el contenedor Docker: docker-compose up -d."
- "Asegúrate de que tu aplicación Java use la URL JDBC con localhost:3306, y el
- MYSQL_USER y MYSQL_PASSWORD que definiste."

## Docker configuration (.env)
### Pasos para usar docker-compose.yml:
- STEP 1: Save docker-compose.yml
- STEP 2: Modify password & user (your_root_password & your_db_user_password)
- STEP 3: Create init.sql script
- STEP 4: Start mySQL container
```
docker-compose up -d
```
- STEP 5: Connect from java maven (modify and add to pom.xml jdbc dependency) - JDBC
```
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version> </dependency>
</dependencies>
```

## Configuración docker compose (scripts):
- Step 1: Preparar archivos (docker-compose.yml, .env, init.sql) dentro de la misma carpeta.
- Step 2: Abrir terminal en Docker desktop y navegar a la carpeta del proyecto (dónde está tu archivo yaml)
```
# Ejemplo: cd C:\Users\TuUsuario\Proyectos\EscapeRoom\docker
```
- Step 3: Levantar el contendor mySQL
```
docker-compose up -d
```
- Step 4: Verificar que el contenedor está corriendo
```
docker ps
# Mostrará una línea similar a esto:
CONTAINER ID   IMAGE        COMMAND                  CREATED         STATUS         PORTS                    NAMES
xxxxxxxxxxxx   mysql:8.0    "docker-entrypoint.s…"   X seconds ago   Up X seconds   0.0.0.0:3306->3306/tcp   escaperoom_mysql_db
ipconfig (command to see my ip)
```
- Step 5: Connectar desde mySQL workbench
```
1. Abrir MySQL Workbecnh
2. Añadir nueva connexión (+) -> setup new connection
3. Configurar detalles de la connexión (name, standard tcp, localhost, port 3306, username del .env)
4. Test connection -> si todo ok mostrará "Successfully made the MySQL connection."
```
- Step 6: Guardar, abrir la conexión y explorar la DDBB "escaperoom_db_trial"
- Step 7: Detener el contenedor
```
docker-compose stop
```
- Step 8: Detener y eliminar el contenedor (manteniendo los datos)
```
docker-compose down
```
- Opcional: Detener y eliminar el contenedor Y sus datos (para un reinicio limpio):
```
docker-compose down -v
```

## Useful docker compose commands:
```
docker-compose stop
docker-compose down
docker-compose down -v
docker-compose logs mysql-db
docker-compose exec mysql-db mysql -u root -p
```

## App features: (cosas que hace la app)

## Documentation