# Meta data

- host: localhost:8686/
- database:
    - username: ISD_shop
    - password: ISD_shop
    - name: isd_shop

---

# Sprint 1

## Common Response

### Error Result REsponse

  ```json
  {
  "error": "{error message}"
}
  ```

## API

### 1. Register for a new customer

#### Request

- Method: **POST**
- URL: ```/register```
- Body:
  ```json
  {
  "full_name": "{fullname}",
  "email": "{email}",
  "phone_number": "{phone_number}",
  "password": "{password}",
  "gender": "{gender}",
  "role": "{role}"
  }
  ```

#### Response
- if success:
  ```json
  {
  "message": "Register successfully"
  }
  ```