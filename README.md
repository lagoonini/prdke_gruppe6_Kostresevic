# Projekt Setup (WICHTIG: Release 3 ist der aktuelle und finale Stand des Projekts)

## Backend einrichten mit Spring Boot

Das Backend und die benötigten Abhängigkeiten sind bereits installiert und einsatzbereit. Es sind keine weiteren Schritte erforderlich, um das Backend zu starten. Einfach zum Backend-Ordner navigieren und die Spring Boot-Anwendung über das Terminal mit dem folgenden Befehl ausführen:

```bash
./gradlew bootRun
```

Um den Spring Boot-Server zu beenden, einfach Strg + C drücken.



## Frontend einrichten mit Angular

Diesen Schritten folgen, um das Frontend einzurichten und zu starten:

### 1. Node.js und npm installieren

Sicher stellen, dass Node.js auf dem Computer installiert ist, um npm (Node Package Manager) verwenden zu können. Die [offizielle Node.js-Website](https://nodejs.org/) für Download und Installationsanleitung.

### 2. Angular CLI installieren

Terminal öffnen oder die Eingabeaufforderung und folgenden Befehl ausführen, um Angular CLI global auf dem Computer zu installieren:

```bash
npm install -g @angular/cli
```
### 3. Angular-Projekt erstellen

Das Terminal innerhalb der IDE öffnen, zum Frontend-Ordner navigieren und folgenden Befehl ausführen:

```bash
npm install
```

### 4. Angular-Anwendung starten

Im Frontend-Ordner bleiben und folgenden Befehl ausführen , um die Angular-Anwendung zu starten:

```bash
npm serve
```
Angular sollte jetzt laufen und unter http://localhost:4200 erreichbar sein.


## Anmerkungen

### 1. Datenbankverbindung
Eine lokale Oracle19c-Datenbank wurde zu Testzwecken installiert. Eine Oracle-Datenbank wird später vom Institut bereitgestellt. Die Verbindung erfolgt in `.backend/src/resources/application.properties` mit folgendem Code:
```bash
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl
spring.datasource.username=username
spring.datasource.password=pass
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```
