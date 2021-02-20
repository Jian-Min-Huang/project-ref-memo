Feature: User Logout

  User logout has two main scenarios
  1. Right JWT header can invoke logout
  2. Non-exist Token cannot invoke logout well

  Scenario: Success
    Given JWT Auth Header
    When Invoke Logout
    Then Success Response with Message

  Scenario: Token Not Found
    Given Non-Exist JWT Auth Header
    When Invoke Logout
    Then Failure Response with Message