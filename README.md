# TPDOS — Développement Orienté Service (3IDL, ISI)

Trois travaux pratiques Spring Boot sur les architectures orientées services :

| TP | Sujet | Technologies | Port |
|----|-------|--------------|------|
| [TP1](#tp1--service-web-soap-contract-first) | Service web **SOAP** (contract-first) + client | Spring Web Services, JAXB, wsdl4j | 8080 |
| [TP2](#tp2--api-rest-employee--mysql) | API **REST** Employee (CRUD) | Spring Web MVC, Spring Data JPA, MySQL | 8080 |
| [TP3](#tp3--microservice-currency-exchange) | **Microservice** currency-exchange | Spring Boot 2.7, JPA, H2, Actuator | 8000 |

**Auteur :** Achref Tlili — Saifeddine Trabelsi 3IDL, Institut Supérieur d'Informatique (ISI), Université de Tunis El Manar

---

## Structure du dépôt

```
TPDOS/
├── TP1DOS/
│   ├── spring-boot-soap-service/spring-boot-soap-service/   # serveur SOAP
│   └── spring-boot-soap-client/spring-boot-soap-client/     # client SOAP (console)
├── TP2DOS/
│   └── employee/employee/                                   # API REST + sql/employee.sql
└── TPDOS3/
    └── currency-exchange-sample-service/currency-exchange-sample-service/
```

> Les fichiers `.zip` présents dans le dépôt sont des archives des mêmes projets (ils peuvent être ignorés).

## Prérequis

- **JDK 17** (TP1 et TP2) — le TP3 compile en Java 8 mais tourne aussi sur JDK 17 ou plus récent
- **Maven** : non obligatoire, chaque projet contient le wrapper `mvnw` (`mvnw.cmd` sous Windows)
- **XAMPP** (MySQL) pour le TP2
- **Postman** (ou SoapUI pour le TP1, ou `curl`)

Depuis le dossier d'un projet :

```bash
./mvnw spring-boot:run        # Linux / macOS / Git Bash
mvnw.cmd spring-boot:run      # Windows (CMD)
```

---

## TP1 — Service web SOAP (contract-first)

Dossier : `TP1DOS/spring-boot-soap-service/spring-boot-soap-service`

**Principe :** le contrat `src/main/resources/Student.xsd` définit `StudentDetailsRequest` / `StudentDetailsResponse`. Le plugin `jaxb-maven-plugin` génère les classes Java (package `com.example.TP1.springbootsoapservice.model`) pendant la compilation, et `wsdl4j` permet de publier le WSDL.

### Lancer le serveur

```bash
cd TP1DOS/spring-boot-soap-service/spring-boot-soap-service
./mvnw clean install      # génère les classes JAXB depuis Student.xsd
./mvnw spring-boot:run
```

Vérifier que les classes générées existent dans `target/generated-sources/` (`Student`, `StudentDetailsRequest`, `StudentDetailsResponse`).

### Endpoints

| Rôle | URL |
|------|-----|
| WSDL | `http://localhost:8080/service/studentDetailsWsdl.wsdl` |
| Service SOAP (POST) | `http://localhost:8080/service/student-details` |

Namespace du contrat : `http://www.exemple.org/student`

### Tester avec Postman / SoapUI

`POST http://localhost:8080/service/student-details` avec l'en-tête `Content-Type: text/xml` et le body (raw) :

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:stu="http://www.exemple.org/student">
   <soapenv:Header/>
   <soapenv:Body>
      <stu:StudentDetailsRequest>
         <stu:name>Sajal</stu:name>
      </stu:StudentDetailsRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

Avec `curl` (enregistrer le XML ci-dessus dans `request.xml`) :

```bash
curl -H "Content-Type: text/xml" -d @request.xml http://localhost:8080/service/student-details
```

### Résultat attendu (HTTP 200)

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
  <SOAP-ENV:Header/>
  <SOAP-ENV:Body>
    <ns2:StudentDetailsResponse xmlns:ns2="http://www.exemple.org/student">
      <ns2:Student>
        <ns2:name>Sajal</ns2:name>
        <ns2:standard>5</ns2:standard>
        <ns2:address>Pune</ns2:address>
      </ns2:Student>
    </ns2:StudentDetailsResponse>
  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

Données disponibles dans `StudentRepository` :

| name | standard | address |
|------|----------|---------|
| Sajal | 5 | Pune |
| Kajal | 5 | Chicago |
| Lokesh | 6 | Delhi |
| Sukesh | 7 | Noida |

Un nom inconnu renvoie une `StudentDetailsResponse` vide (sans `Student`).

### Client SOAP (console)

Dossier : `TP1DOS/spring-boot-soap-client/spring-boot-soap-client`. **Le serveur doit être démarré avant.**

```bash
cd TP1DOS/spring-boot-soap-client/spring-boot-soap-client
./mvnw clean install
./mvnw spring-boot:run                                         # étudiant par défaut : Sajal
./mvnw spring-boot:run -Dspring-boot.run.arguments=Kajal       # autre étudiant
```

Sortie attendue :

```
Nom     : Sajal
Standard: 5
Adresse : Pune
```

Pour un nom inexistant : `Aucun étudiant trouvé : <nom>`.

### Problèmes fréquents

- **Classes `StudentDetailsRequest` introuvables** : le plugin JAXB n'a pas tourné → `./mvnw clean install`.
- **`No endpoint mapping found`** : namespace ou `localPart` différent du XSD (`http://www.exemple.org/student`, `StudentDetailsRequest`).
- **404** : l'URL doit commencer par `/service/` (mapping du `MessageDispatcherServlet`).

---

## TP2 — API REST Employee + MySQL

Dossier : `TP2DOS/employee/employee`

Architecture en couches : `controller` → `service` → `repository` → `model` (entité `Employee`).

### Préparer la base

1. Démarrer **Apache** et **MySQL** dans XAMPP.
2. Ouvrir phpMyAdmin (`http://localhost/phpmyadmin`) → onglet **SQL** → exécuter `sql/employee.sql` (crée la base `employee-schema` et la table `employee`).
3. Configuration utilisée (`src/main/resources/application.properties`) :

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/employee-schema
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

### Lancer

```bash
cd TP2DOS/employee/employee
./mvnw spring-boot:run
```

> ⚠️ Le test `EmployeeApplicationTests` démarre le contexte Spring et nécessite MySQL actif. Si MySQL est éteint : `./mvnw clean install -DskipTests`.

### Endpoints (base : `http://localhost:8080/api`)

| Opération | Requête | Body JSON |
|-----------|---------|-----------|
| Créer | `POST /api/employees` | `{"firstName":"Ali","lastName":"Ben Salah","emailId":"ali@mail.com"}` |
| Lister | `GET /api/employees` | — |
| Modifier | `PUT /api/employees/{id}` | `{"firstName":"Ali","lastName":"Trabelsi","emailId":"ali.t@mail.com"}` |
| Supprimer | `DELETE /api/employees/{id}` | — |

Pour les requêtes avec body : en-tête `Content-Type: application/json`.

### Résultats attendus

| Test | Résultat |
|------|----------|
| `POST` valide | 200, JSON `{"id":1,"firstName":"Ali","lastName":"Ben Salah","emailId":"ali@mail.com"}` |
| `GET /api/employees` | 200, tableau JSON des employés (`[]` si la table est vide) |
| `PUT /api/employees/1` | 200, JSON avec les valeurs mises à jour |
| `DELETE /api/employees/1` | 200, body vide ; l'employé disparaît du `GET` |
| `PUT` sur un id inexistant | 500 (`RuntimeException` « Employé introuvable avec l'id : … ») |

Vérification en base : phpMyAdmin → `employee-schema` → table `employee` (colonnes `emp_id`, `first_name`, `last_name`, `email_id`).

### Problèmes fréquents

- **`Communications link failure`** : MySQL n'est pas démarré dans XAMPP.
- **`Unknown database 'employee-schema'`** : `sql/employee.sql` n'a pas été exécuté.
- **415 Unsupported Media Type** : `Content-Type: application/json` manquant.
- **Port 3306 occupé** : changer le port dans XAMPP ou dans l'URL JDBC.

---

## TP3 — Microservice currency-exchange

Dossier : `TPDOS3/currency-exchange-sample-service/currency-exchange-sample-service`

Microservice de taux de change (port **8000**) avec base **H2 en mémoire**. Il expose deux versions : une logique codée en dur (version du TP) et une lecture depuis la base.

### Lancer

```bash
cd TPDOS3/currency-exchange-sample-service/currency-exchange-sample-service
./mvnw spring-boot:run
```

### Endpoints

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/currency-exchange-sample/fromCurrency/{from}/toCurrency/{to}` | Taux codé en dur |
| GET | `/currency-exchange/from/{from}/to/{to}` | Taux lu depuis H2 (404 si absent) |
| GET | `/currency-exchange` | Liste de tous les taux |
| POST | `/currency-exchange` | Création d'un taux |
| GET | `/actuator/health` | État du service |
| — | `/h2-console` | Console H2 |

### Résultats attendus

**Taux codé en dur** — `GET http://localhost:8000/currency-exchange-sample/fromCurrency/USD/toCurrency/INR`

```json
{"id":1,"from":"USD","to":"INR","conversionMultiple":78,"port":8000}
```

Valeurs codées : USD→INR = 78, INR→USD = 0.013, EUR→INR = 82, AUD→INR = 54 (autre paire : `conversionMultiple` = `null`).

**Taux depuis la base** — `GET http://localhost:8000/currency-exchange/from/EUR/to/INR`

```json
{"id":2,"from":"EUR","to":"INR","conversionMultiple":82,"port":8000}
```

Une paire absente renvoie **404**. La recherche ignore la casse (`usd` = `USD`).

**Liste** — `GET http://localhost:8000/currency-exchange` : 200, les 3 lignes de `data.sql` (le champ `port` y vaut `0`, car il n'est renseigné que par les endpoints de lecture unitaire et de création).

**Création** — `POST http://localhost:8000/currency-exchange` (`Content-Type: application/json`) :

```json
{"from":"GBP","to":"INR","conversionMultiple":95}
```

Attendu : **201 Created** avec le JSON de la ligne créée (`"port":8000`). Les clés JSON sont `from` et `to` (et non `fromCurrency` / `toCurrency`).

**Santé** — `GET http://localhost:8000/actuator/health` → `{"status":"UP"}`

### Console H2

`http://localhost:8000/h2-console` — JDBC URL `jdbc:h2:mem:testdb`, user `sa`, mot de passe vide.

```sql
SELECT * FROM EXCHANGE_VALUE;
```

### Tester deux instances (équilibrage)

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8001
```

Le champ `port` de la réponse indique l'instance qui a répondu.

### Problèmes fréquents

- **`POST` en erreur 500 (clé primaire dupliquée)** : `data.sql` insère des `id` explicites (1, 2, 3) alors que l'`id` est auto-généré ; avec H2 2.x la séquence n'avance pas. Solution : retirer la colonne `id` des `INSERT` :
  ```sql
  INSERT INTO Exchange_Value (currency_from, currency_to, conversion_multiple, port) VALUES ('USD', 'INR', 78, 0);
  INSERT INTO Exchange_Value (currency_from, currency_to, conversion_multiple, port) VALUES ('EUR', 'INR', 82, 0);
  INSERT INTO Exchange_Value (currency_from, currency_to, conversion_multiple, port) VALUES ('AUD', 'INR', 54, 0);
  ```
- **Table absente au démarrage** : garder `spring.jpa.defer-datasource-initialization=true`.
- **Erreur SQL sur `from` / `to`** : ce sont des mots réservés, d'où les colonnes `currency_from` et `currency_to`.

---

## Récapitulatif des tests

| TP | Outil | Vérification principale |
|----|-------|-------------------------|
| TP1 | Postman / SoapUI / client console | `StudentDetailsResponse` pour Sajal, Kajal, Lokesh, Sukesh |
| TP2 | Postman + phpMyAdmin | CRUD `/api/employees`, lignes visibles dans MySQL |
| TP3 | Navigateur / Postman + console H2 | Taux de change avec `port: 8000`, création en 201 |
