# APIs

## [POST] register for customer

- url: localhost:8686/register
- request body:

```json
      {
  "full_name": "John Doe",
  "email": "john@gmail.com",
  "phone_number": "0396911111",
  "password": "password",
  "gender": "mail",
  "role_id": 4
}

```

- response:
  User created successfully

## [POST] login for customer

- url: localhost:8686/login
- request body:

```json
{
  "username": "0396911111",
  "password": "password"
}
```

- response:
  ```json
  {
  "token" : "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImpvaG5AZ21haWwuY29tIiwic3ViIjoiam9obkBnbWFpbC5jb20iLCJleHAiOjE3MTM5NDAxMTF9.WQAXFt9jiaPmWofBQ3_dQjLjviRRr1ilAYbBLCziWLk",
  "role_id" : 4,
  "full_name" : "John Doe"
  }
  ```