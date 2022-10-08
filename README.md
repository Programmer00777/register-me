![readme-registerme](https://user-images.githubusercontent.com/55809302/194723568-25724cc2-0ec4-4e17-83ee-edc22195b602.png)
REGISTER.ME is a Spring Boot based Java application that handles security and accessibility aspects of a web app.

**With REGISTER.ME you can:**
- Sign up
- Sign in
- Set up user roles
- Set up accessibility to each endpoint
- Verify users by email via a confirmation token. By default, token expires in 15 minutes!

**Features TODO:**
- [ ] Create custom Sign up and Sign in pages.

## Endpoints
Basically, the app is running on port 8080 (if you don't specify another).

There is a REST controller that handles endpoints provided below:
- **GET /api/v1/registration/** Basic page that informs a user that sign authorization and authentication were performed successfully.
- **POST /api/v1/registration/** Add a new user (data is being passed in a JSON format, more details below).
- **GET /api/v1/registration/confirm?token=** Used for the email confirmation.

## Dependencies used in the service
- **spring-boot-starter-data-jpa** Persist data in SQL stores with Java Persistance API using Spring Data and Hibernate.
- **spring-boot-starter-mail** Send email using Java Mail and Spring Framework's JavaMailSender.
- **spring-boot-starter-security** Highly customizable authentication and access-control framework for Spring applications.
- **spring-boot-starter-web** Used to build web, including REST, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
- **mysql-connector-java** MySQL JDBC and R2DBC driver.
- **lombok** Java annotation library which helps to reduce boilerplate code.

## Usage
1. Clone the repository with ```git clone https://github.com/Programmer00777/resiter-me.git``` into your working directory.

2. Launch the app with ```./mvnw spring-boot:run```

3. Go to ```http://localhost:8080/api/v1/resitration/```. You should see the default Spring Security's log in page:
<img width="1440" alt="Screen Shot 2022-10-08 at 10 34 08 PM" src="https://user-images.githubusercontent.com/55809302/194724436-e664a827-3e4f-4a8b-9ab0-9139a55f6f35.png">

4. Before we add a new user, we have to set up the way of how we will test the email confirmation.
- Now, we can use MailDev to check it out.
Install **Docker**
Open up the Terminal and type ```docker run -p 1080:1080 -p 1025:1025 maildev/maildev```
You will see something like this:
<img width="755" alt="Screen Shot 2022-10-08 at 10 46 12 PM" src="https://user-images.githubusercontent.com/55809302/194725290-bdbc54dd-05d3-4097-8d37-edb8825851c2.png">

- Go to ```http://localhost:1080/```

5. Okay. Now, let's add a new user. You can use Postman to send HTTP requests.
- Open up Postman
- Choose **POST** request and type ```localhost:8080/api/v1/register/```:
<img width="864" alt="Screen Shot 2022-10-08 at 10 37 28 PM" src="https://user-images.githubusercontent.com/55809302/194724552-23fb835c-1bbf-4fd4-b029-f42b03b1ca8e.png">
- Go to **Body** tab and choose *row* and *JSON*. Add to the body ther JSON document, use the example below:
<img width="841" alt="Screen Shot 2022-10-08 at 10 40 21 PM" src="https://user-images.githubusercontent.com/55809302/194724634-e353961b-1870-4bef-90ed-b9b4b00a504b.png">
- Hit the "Send" button
If everything goes well, you will receive the confirmation token in the response body. You don't have to copy it.
<img width="450" alt="Screen Shot 2022-10-08 at 10 41 07 PM" src="https://user-images.githubusercontent.com/55809302/194724676-69eaed19-221c-48a4-892a-266593ef3b21.png">

6. After adding a new user, you should receive a confirmation token sent on the specified email adress.
If you check the ```localhost:1080```, you will see a new email:
<img width="1440" alt="Screen Shot 2022-10-08 at 10 51 11 PM" src="https://user-images.githubusercontent.com/55809302/194725576-31c311f0-3acd-47a8-a2bd-a1fb76471230.png">
Open this up and click **CONFIRM EMAIL ADRESS**
<img width="1440" alt="Screen Shot 2022-10-08 at 10 51 46 PM" src="https://user-images.githubusercontent.com/55809302/194725606-b99763ec-b9cc-491f-b7e6-b651df64046f.png">

7. You will be redirected to ```http://localhost:8080/api/v1/registration/confirm?token=```. We can see that our email is confirmed:
<img width="1440" alt="Screen Shot 2022-10-08 at 10 53 05 PM" src="https://user-images.githubusercontent.com/55809302/194725664-9179995a-c984-4223-b9b2-b831ea137860.png">

8. Let's check rows in database:
*users* table
<img width="1091" alt="Screen Shot 2022-10-08 at 10 57 51 PM" src="https://user-images.githubusercontent.com/55809302/194725753-1aa5be10-8fa8-4581-b153-02a447f135c8.png">
**enabled** column has the value of 1 which means that account is activated and email adress confirmed.
*confiration_token* table
<img width="858" alt="Screen Shot 2022-10-08 at 10 59 38 PM" src="https://user-images.githubusercontent.com/55809302/194725796-0ebaf75a-d1bf-4e57-b894-97ab7cb1e663.png">
Note that there is information about when the token was created, when it expires, and when it confirmed (if confirmed at all)

9. Let's go back to ```http://localhost:8080/api/v1/registration/``` and try to sign in with registered credentials:
<img width="1440" alt="Screen Shot 2022-10-08 at 11 03 44 PM" src="https://user-images.githubusercontent.com/55809302/194726020-389d76e9-3943-4ee7-9ddf-077c10317cc2.png">

10. As we can see, everything is okay:
<img width="1440" alt="Screen Shot 2022-10-08 at 11 06 08 PM" src="https://user-images.githubusercontent.com/55809302/194726050-f40fe2f5-e4c0-4fbb-acfd-dfc5ce5b0bf0.png">

**What if token hasn't been confirmed in the allocated time range**
Suppose, that the user didn't confirm an email verification on time.

First, the null value will be stored in the ```confirmed_at``` column:
<img width="819" alt="Screen Shot 2022-10-08 at 11 10 36 PM" src="https://user-images.githubusercontent.com/55809302/194726192-6c5158fa-a1b8-4897-a9a5-9aa7a69290cd.png">

Second, the value of 0 will be stored in the ```enabled``` column:
<img width="1074" alt="Screen Shot 2022-10-08 at 11 11 24 PM" src="https://user-images.githubusercontent.com/55809302/194726216-8a35acf9-a86f-405d-b43a-085061b651a8.png">

Third, if they will try to confirm it, the following exception will be thrown:
<img width="1020" alt="Screen Shot 2022-10-08 at 11 12 16 PM" src="https://user-images.githubusercontent.com/55809302/194726277-334b28f7-a8be-4213-a21a-45eb4f89b20d.png">

And the next message will be shown on the try to sing in:
<img width="1231" alt="Screen Shot 2022-10-08 at 11 16 23 PM" src="https://user-images.githubusercontent.com/55809302/194726394-d4534fad-7076-4654-b9d9-d100175d478e.png">

So, that's pretty much it! The app can be upgraded and expanded as much as you want.

## License
MIT
