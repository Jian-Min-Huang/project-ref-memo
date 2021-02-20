Feature: Remove User

  Just Remove User

  Scenario: Success Remove User by Id
    Given Id in User DTO
    When Invoke Remove User Api
    Then Success Response with User Id
  Scenario:
    Given Id in User Dto which is not exist
    When Invoke Remove User Api
    Then Failure Response