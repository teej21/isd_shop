# APIs

## [POST] register for customer

- url: localhost:8686/register
- request body:

```json
{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@gmail.com",
  "address": "123, Main Street, New York",
  "password": "password",
  "retype_password": "password",
  "date_of_birth": "1990-01-01",
  "role_id": 4
}

```

## [POST] login for customer
- url: localhost:8686/login
- request body:

```json
{
    "email":"john@gmail.com",
     "password": "passwordd",
    "role_id": 4
}
```