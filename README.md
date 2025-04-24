# Microservice_users_tests

I first did a unit test for UserService where i tested the method to create and delete a user along with get a user by its ID since i thought these were the most important and they all worked as they should.
I used a mocked repo to isolate the test completely from the original app and database. 

Second I did a component test for UserController focusing on the GetUserById method with MockMvc to test the controllers behavior isolated from other parts of the app. I Mocked a webClientBuilder since WebClient is used 
to perform HTTP-calls to a extern service in my original UserController and therefor is depending on it and had to be used in the test aswell. 

Last I did a integration test to test the flow from rest api-controller-service-database/repository. Tho i chosed to only test the CreateUser and GetUserById, verifying that post and get endpoints are wokring as they should (save created user in database and be able to use at GET to find it).
