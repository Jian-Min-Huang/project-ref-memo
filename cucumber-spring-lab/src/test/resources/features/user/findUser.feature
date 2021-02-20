Feature: Find User

  This feature we will have many search criteria
  We also need to consider with not found cases

  Scenario: Success Find User by Account
    Given An Exist Account
    When Invoke Find User Api
    Then Success Response with User Object