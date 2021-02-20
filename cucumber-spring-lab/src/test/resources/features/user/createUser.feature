Feature: Create User

  Create user api accept user dto
  Some property might be non-nullable
  Some property might be default when it's null
  We need to check many case include success and failure

  Scenario: Success Create User with User DTO
    Given An User Dto
    When Invoke Create User Api
    Then Success Response with User Entity

