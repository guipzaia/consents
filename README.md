## ABOUT THE PROJECT
Consent API for Junior/Mid-Level Engineer Test Case.

Available on https://github.com/guipzaia/consents

### BUILT WITH
* Java v17
* Spring-boot v3.4.1

### SOURCE TREE
Developed in Clean Architecture
```
.
├── local.env
├── Makefile
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── raidiam
    │   │           └── consents
    │   │               ├── adapters
    │   │               │   ├── repositories
    │   │               │   │   └── IConsentRepository.java
    │   │               │   └── rest
    │   │               │       ├── ConsentControllerAdvice.java
    │   │               │       ├── ConsentController.java
    │   │               │       ├── port
    │   │               │       │   ├── ConsentErrorResponse.java
    │   │               │       │   ├── ConsentRequest.java
    │   │               │       │   ├── ConsentResponse.java
    │   │               │       │   └── ConsentUpdateRequest.java
    │   │               │       └── RateLimitingFilter.java
    │   │               ├── config
    │   │               │   └── ClockConfiguration.java
    │   │               ├── ConsentApplication.java
    │   │               ├── domain
    │   │               │   ├── entities
    │   │               │   │   └── Consent.java
    │   │               │   ├── enums
    │   │               │   │   ├── ConsentPermission.java
    │   │               │   │   └── ConsentStatus.java
    │   │               │   ├── exceptions
    │   │               │   │   ├── ConsentNotFoundException.java
    │   │               │   │   ├── ConsentPermissionsWithDuplicateValueException.java
    │   │               │   │   └── ConsentWithInvalidStatusException.java
    │   │               │   └── messages
    │   │               │       └── ErrorMessage.java
    │   │               ├── usecases
    │   │               │   ├── createconsent
    │   │               │   │   ├── CreateConsent.java
    │   │               │   │   ├── ICreateConsent.java
    │   │               │   │   └── port
    │   │               │   │       ├── CreateConsentRequest.java
    │   │               │   │       └── CreateConsentResponse.java
    │   │               │   ├── retrieveconsent
    │   │               │   │   ├── IRetrieveConsent.java
    │   │               │   │   ├── port
    │   │               │   │   │   ├── RetrieveConsentRequest.java
    │   │               │   │   │   └── RetrieveConsentResponse.java
    │   │               │   │   └── RetrieveConsent.java
    │   │               │   ├── revokeconsent
    │   │               │   │   ├── IRevokeConsent.java
    │   │               │   │   ├── port
    │   │               │   │   │   └── RevokeConsentRequest.java
    │   │               │   │   └── RevokeConsent.java
    │   │               │   └── updateconsent
    │   │               │       ├── IUpdateConsent.java
    │   │               │       ├── port
    │   │               │       │   ├── UpdateConsentRequest.java
    │   │               │       │   └── UpdateConsentResponse.java
    │   │               │       └── UpdateConsent.java
    │   │               └── utils
    │   │                   ├── CustomFormatter.java
    │   │                   └── EnumDuplicateChecker.java
    │   └── resources
    │       └── application.yaml
    └── test
        └── java
            └── com
                └── raidiam
                    └── consents
                        ├── adapters
                        │   └── rest
                        │       ├── ConsentControllerAdviceTest.java
                        │       ├── ConsentControllerTest.java
                        │       ├── ConsentControllerWebMVCTest.java
                        │       └── RateLimitingFilterTest.java
                        ├── usecases
                        │   ├── CreateConsentTest.java
                        │   ├── RetrieveConsentTest.java
                        │   ├── RevokeConsentTest.java
                        │   └── UpdateConsentTest.java
                        └── utils
                            ├── CustomFormatterTest.java
                            └── EnumDuplicateCheckerTest.java
```

## GETTING STARTED

### PREREQUISITES
* [Java v17](https://oracle.com/java/technologies/javase/jdk17-archive-download.html)
* [Maven](https://maven.apache.org/install.html)

### INSTALLATION
Build project from command line
```sh
mvn install
```
or
```sh
make install
```

### UNIT TESTS
Run tests from command line
```sh
mvn test
```
or
```sh
make test
```

### USAGE
Run project from command line (with environment variables)

_Note: Environment variables in local.env for application test_ 
```sh
set -a; . ./local.env; set +a; mvn spring-boot:run
```
or
```sh
make run
```

## API REST
### Create new consent
`POST /consents`

Request
```sh
curl -X POST \
-H 'Content-Type: application/json' \
-d '{"userId":"user-12345", "status":"AWAITING_AUTHORISATION", "permissions": ["READ_DATA", "WRITE_DATA"]}' \
localhost:8080/consents
```

Responses

* 200
```json
{
  "consentId": "consent-6",
  "userId": "user-12345",
  "permissions": [
    "READ_DATA",
    "WRITE_DATA"
  ],
  "status": "AWAITING_AUTHORISATION",
  "createdAt": "2025-01-12T03:34:46Z",
  "updatedAt": "2025-01-12T03:34:46Z",
  "meta": {
    "requestDateTime": "2025-01-12T03:34:46Z"
  }
}
```

* 400
```json
{
  "message": "Invalid input",
  "errors": [
    "Field userId is required",
    "Field status is required",
    "Field permissions is required"
  ]
}
```

### Retrive consent details
`GET /consents/{consentId}`

Request
```sh
curl -X GET \
-H 'Content-Type: application/json' \
localhost:8080/consents/consent-12345
```

Responses

* 200
```json
{
  "consentId": "consent-1",
  "userId": "user-12345",
  "permissions": [
    "READ_DATA",
    "WRITE_DATA"
  ],
  "status": "AWAITING_AUTHORISATION",
  "createdAt": "2025-01-12T03:26:55Z",
  "updatedAt": "2025-01-12T03:26:55Z",
  "meta": {
    "requestDateTime": "2025-01-12T03:38:25Z"
  }
}
```

* 400
```json
{
  "message": "Invalid input",
  "errors": [
    "Path parameter consentId must have the pattern 'consent-N' (N = number)"
  ]
}

```

* 404
```json
{
  "message": "Consent not found"
}
```

### Update consent details
`PUT /consents/{consentId}`

Request
```sh
curl -X PUT \
-H 'Content-Type: application/json' \
-d '{"status": "AUTHORISED", "permissions": ["READ_DATA", "WRITE_DATA"]}' \
localhost:8080/consents/consent-1
```

Responses

* 200
```json
{
  "consentId": "consent-1",
  "userId": "user-12345",
  "permissions": [
    "READ_DATA",
    "WRITE_DATA"
  ],
  "status": "AUTHORISED",
  "createdAt": "2025-01-12T03:26:55Z",
  "updatedAt": "2025-01-12T03:45:41Z",
  "meta": {
    "requestDateTime": "2025-01-12T03:45:41Z"
  }
} 
```

* 400
```json
{
  "message": "Invalid input",
  "errors": [
    "Duplicate permissions detected"
  ]
}
```

* 404
```json
{
  "message": "Consent not found"
}
```

### Revoke consent
`DELETE consents/{consentId}`

Request
```sh
curl -X DELETE \
-H 'Content-Type: application/json' \
localhost:8080/consents/consent-1
```

Responses

* 204
```json
```
* 400
```json
{
  "message": "Invalid input",
  "errors": [
    "Path parameter consentId must have the pattern 'consent-N' (N = number)"
  ]
}
```

* 404
```json
{
  "message": "Consent not found"
}
```

### API request validation

#### Resource not found

Request
```sh
curl -X GET \
-H 'Content-Type: application/json' \
localhost:8080/permissions
```

Response
* 404
````json
{
  "message": "No static resource permissions."
}
````

#### Method not allowed

Request
```sh
curl -X GET \
-H 'Content-Type: application/json' \
localhost:8080/consents
```
Response
* 405
````json
{
  "message": "Request method 'GET' is not supported"
}
````

#### Rate limiting filter

Request
```sh
for i in {1..10} ;  do
  echo "-X GET -H 'Content-Type: application/json' localhost:8080/consents/consent-${i} :-"
done | xargs curl -s
```

Response
* 429
```json
{
  "message": "Too many requests"
}
```

#### Internal server error

Request
```sh
curl -X GET \
-H 'Content-Type: application/json' \
localhost:8080/consents/consent-99999999999999999999
```

Response
* 500
```json
{
  "message": "Internal server error"
}
```