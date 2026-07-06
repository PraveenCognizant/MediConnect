Feature: MediConnect Registration

  Scenario: Patient registration form can be completed with valid UI data
    Given I open the registration page
    When I fill valid patient registration details
    Then the patient registration submit button should be enabled

  Scenario: Doctor registration form can be completed with valid UI data
    Given I open the registration page
    When I fill valid doctor registration details
    Then the doctor registration submit button should be enabled
