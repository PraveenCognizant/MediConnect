Feature: MediConnect Welcome Page

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
