
1. Description
   - This sample is using SpringBoot.
   - FriendManageController --> FriendManageService --> 2 sub services ( UserService and UserRelationshipService) -->JPA -> entities
   - There are 2 entities: User (1) --> UserRelationship(1..n)
   - All Exceptions are handled in GlobalExceptionHandler using ControllerAdvice
   - Error messages and error code are declared in errors_msg.properties
   - It will take time to start db completed first and then application.
2. How to run
   docker-compose up --build

3. Build jar file
   1. cd /app
   2. ./gradlew build

4. Test APIs
   import sp.postman_collection file into PostMan plugin and test.
