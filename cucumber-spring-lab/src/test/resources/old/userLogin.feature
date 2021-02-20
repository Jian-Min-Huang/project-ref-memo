Feature: User Login

  User login feature has two main scenarios
  1. Success Login
  2. Failure Login

  Scenario: Success
    Given Right Information
    When Invoke Login
    Then Success Response with JWT Auth Header

  Scenario: Account and Password not Match
    Given Wrong Information
    When Invoke Login
    Then Failure Response with Message