# MediConnect UI Automation

This project contains Selenium UI test cases for the MediConnect Angular frontend using:

- Page Object Model
- Selenium PageFactory locators
- TestNG
- Cucumber BDD with TestNG runner

## Test Structure

- `src/test/java/com/mediconnect/base` - driver setup and base page/test classes
- `src/test/java/com/mediconnect/pages` - PageFactory page objects
- `src/test/java/com/mediconnect/steps` - Cucumber step definitions and hooks
- `src/test/java/com/mediconnect/runner` - Cucumber TestNG runner
- `src/test/java/com/mediconnect/tests` - direct TestNG UI tests
- `src/test/resources/features` - Cucumber feature files
- `testng.xml` - TestNG suite for Cucumber and direct UI tests

## Run Tests

By default, tests run against the hosted frontend:

`https://healthcare-management-system-mediconnect.vercel.app`

```powershell
mvn test
```

Override defaults when you want to run against local Angular:

```powershell
mvn test -DbaseUrl=http://localhost:4200 -Dbrowser=chrome -Dheadless=true
```

Supported browser values are `chrome`, `edge`, and `firefox`.

## Reports

Cucumber reports are generated at:

- `target/cucumber-report.html`
- `target/cucumber-report.json`

## Current UI Coverage

- Welcome page hero and navigation to login
- Patient, doctor, and admin login tab switching
- Valid login for patient, doctor, and admin
- Invalid email and password combinations for patient, doctor, and admin
- Invalid email format keeps Sign In disabled
- Data-driven flow where doctor Praveen schedules a slot, patient John books it, and the doctor accepts it
- Patient registration form completion
- Doctor registration form tab and form completion
- Data-driven doctor registration followed by admin approval
