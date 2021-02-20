# cucumber-spring-demo

### Road to TDD
* Jump Fast !
* Write code backward !
* Red, Green, Refactor
* Baby Step
* Protect your production code with test cases
* Refine your production code with refactor

### Structure (Test 3A)
* Arrange
  * Class, Method, Variable and Mock
* Act
  * Invoke the method and get result
* Assert
  * validate the return value

### Debug
* cucumber-spring is on JUnit 4 so we can't exclude junit-vintage-engine

### Plugin
* AceJump
* Custom Postfix Template

### References
* https://cucumber.io/docs/cucumber/
* https://www.tutorialspoint.com/cucumber/index.htm
* https://iliubang.cn/java/2019/03/15/Spring_Boot_With_BDD.html
* https://www.baeldung.com/cucumber-rest-api-testing
* https://ithelp.ithome.com.tw/users/20010292/ironman/462
* https://tw.alphacamp.co/blog/bdssd-tdd-cucumber-behaviour-driven-development

# Example
* https://ruddyblog.wordpress.com/tag/%E5%AF%A6%E4%BE%8B%E5%8C%96%E9%9C%80%E6%B1%82/
GET /users?id=1, empty
  200, Response<UserDto>
  500, unexpected
  
GET /users?name=Vincent, empty, 
  200, Response<UserDto>
  500, unexpected
 
POST /users, UserDto, 
  200, Response<UserDto>
  500, unexpected
 
PUT /users, UserDto
  200, Response<UserDto>
  500, unexpected
 
DELETE /users?id=1, empty
  200, empty
  500, unexpected

DELETE /users?name=Vincent, empty
  200, empty
  500, unexpected
