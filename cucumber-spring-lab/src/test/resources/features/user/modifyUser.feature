Feature: Modify User

  Id in User Dto is the unique identifier
  The User Dto only pass id and property they want to change
  Modify it and return entity

  Scenario: Success Modify User with User Dto
    Given An User Dto
    When Invoke Modify User Api
    Then Success Response with User Entity