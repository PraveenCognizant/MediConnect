Feature: MediConnect frontend UI

  Scenario: Open welcome page and navigate to patient login
    Given I open the MediConnect welcome page
    Then the welcome page should show the MediConnect hero
    When I navigate to login from the welcome page
    Then the patient login form should be displayed

  Scenario Outline: Login role tabs show matching forms
    Given I open the MediConnect welcome page
    When I navigate to login from the welcome page
    And I switch to the "<role>" login tab
    Then the login form title should be "<title>"

    Examples:
      | role    | title         |
      | patient | Patient Login |
      | doctor  | Doctor Login  |
      | admin   | Admin Login   |

  Scenario: Patient registration form can be completed with valid UI data
    Given I open the registration page
    When I fill valid patient registration details
    Then the patient registration submit button should be enabled

  Scenario: Doctor registration form can be completed with valid UI data
    Given I open the registration page
    When I fill valid doctor registration details
    Then the doctor registration submit button should be enabled

  Scenario Outline: Valid users can login and reach their dashboard
    Given I open the login page
    When I login as "<role>" with email "<email>" and password "<password>"
    Then I should be redirected to "<dashboardPath>"

    Examples:
      | role    | email                  | password     | dashboardPath    |
      | patient | john@gmail.com         | John@123     | /userdashboard   |
      | doctor  | praveendu@gmail.com    | Praveen@123  | /doctordashboard |
      | admin   | admin@mediconnect.com  | Admin@123    | /admindashboard  |

  Scenario Outline: Invalid credentials show login error
    Given I open the login page
    When I login as "<role>" with email "<email>" and password "<password>"
    Then a login error containing "<error>" should be shown

    Examples:
      | role    | email                         | password     | error                 |
      | patient | john@gmail.com                | Wrong@123    | Bad credentials       |
      | patient | unknown.patient@example.com   | John@123     | Bad credentials       |
      | doctor  | praveendu@gmail.com           | Wrong@123    | Bad credentials       |
      | doctor  | unknown.doctor@example.com    | Praveen@123  | Bad credentials       |
      | admin   | admin@mediconnect.com         | Wrong@123    | Bad admin credentials |
      | admin   | unknown.admin@example.com     | Admin@123    | Bad admin credentials |

  Scenario Outline: Invalid email formats keep login disabled
    Given I open the login page
    When I enter invalid email "<email>" for "<role>" login
    Then the login submit button should be disabled

    Examples:
      | role    | email     |
      | patient | john      |
      | doctor  | praveendu |
      | admin   | admin     |
