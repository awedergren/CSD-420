Wedergren_Module_10_2_Assignment
=================================

Overview
--------
This folder contains a single-file, standalone Java application `Wedergren_Module_10_2_Assignment.java` that
lets you view and update fan records stored in a MySQL database table named `fans`.

The program provides:
- A simple Swing UI with two buttons: Display (load by id) and Update (save changes).
- JDBC helper methods for loading and saving a fan record.
- An embedded non-GUI test runner (run with `--test`) that inserts/updates a fixture row and verifies load/update behavior.

Prerequisites
-------------
- Java JDK (javac/java) installed. The code is written to work with Java 8+ but newer JDKs are fine.
- A running MySQL Server accessible on localhost and port 3306 (adjust DB_URL in the source if different).
- MySQL Connector/J (JDBC driver) JAR (e.g. `mysql-connector-java-8.0.xx.jar`). Download it from the MySQL website or Maven central.

Recommended paths (examples used in commands below):
- Connector JAR: `C:\libs\mysql-connector-java-8.0.33.jar`
- Source file path: `C:\csd\csd-420\Module-10\Wedergren_Module_10_2_Assignment.java`

Database setup (SQL)
--------------------
Run the following SQL as a privileged user (root) to create the database, user, and the `fans` table used by the program.
It is safe: the app itself will NOT create or drop the table; this SQL is provided to prepare your environment.

-- Create the database
CREATE DATABASE IF NOT EXISTS databasedb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Create or reset the student1 user (change password as needed)
CREATE USER IF NOT EXISTS 'student1'@'localhost' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON databasedb.* TO 'student1'@'localhost';
FLUSH PRIVILEGES;

-- Create the fans table expected by the assignment
USE databasedb;

CREATE TABLE IF NOT EXISTS fans (
  id INT PRIMARY KEY,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  favorite_team VARCHAR(100)
) ENGINE=InnoDB;

-- Optional: add a small sample row
INSERT INTO fans (id, first_name, last_name, favorite_team) VALUES (1, 'Jane', 'Doe', 'Example FC')
  ON DUPLICATE KEY UPDATE first_name=VALUES(first_name), last_name=VALUES(last_name), favorite_team=VALUES(favorite_team);

Compile & Run (PowerShell/Windows)
----------------------------------
Open PowerShell and cd into the folder that contains the Java file, e.g.:

```powershell
cd C:\csd\csd-420\Module-10
```

Compile (replace the connector path with the correct path to your jar):

```powershell
javac -cp .;C:\libs\mysql-connector-java-8.0.33.jar Wedergren_Module_10_2_Assignment.java
```

Run the GUI:

```powershell
java -cp .;C:\libs\mysql-connector-java-8.0.33.jar Wedergren_Module_10_2_Assignment
```

Run the embedded tests (non-GUI):

```powershell
java -cp .;C:\libs\mysql-connector-java-8.0.33.jar Wedergren_Module_10_2_Assignment --test
```

What the embedded tests do
--------------------------
- Insert/update a fixture row with id = 999999 (chosen to avoid collisions).
- Load it back and assert the values match.
- Update the fixture and assert the new values are persisted.

If the tests pass you will see "All tests passed." in stdout and the process exits with code 0.
If they fail the process exits with non-zero and prints error details.

Troubleshooting
---------------
- "MySQL JDBC driver not found": You must provide the Connector/J jar on the classpath when running `java` and `javac`.
- "Communications link failure" or "Connection refused": verify MySQL is running and listening on the expected port (3306 by default), and that `DB_URL` in the source matches host/port/database.
- Authentication errors: ensure the user/password and host match what MySQL expects. The README SQL creates `student1`@`localhost`.
- Timezone or serverTimezone errors: adding `?serverTimezone=UTC` to the JDBC URL helps. The provided source already includes `serverTimezone=UTC`.

Notes and constraints
---------------------
- The Java program intentionally does NOT create or drop the `fans` table at runtime. The README SQL above is provided to prepare the environment.
- The embedded tests perform INSERT/UPDATE using `INSERT ... ON DUPLICATE KEY UPDATE` and do not delete the fixture.

Next steps / optional improvements
---------------------------------
- Add an automated build (Maven/Gradle) if you want repeatable dependency management.
- Add unit tests using JUnit if you prefer a formal test framework rather than the embedded runner.
- Improve UI (validation, nicer layout) or add logging for easier debugging.

If you want me to run the embedded test here, tell me and I will run the test in the terminal and report the result.