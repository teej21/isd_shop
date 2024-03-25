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
  "gender":"mail",
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
  "username":"0396911111",
  "password": "password"
}
```