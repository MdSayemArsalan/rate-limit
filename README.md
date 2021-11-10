# Rate Limiting
### Implementation Doc
This project is created with Spring Boot.

For rate-limiting implementation in a production setting, we will more likely have an API gateway (such as Spring Cloud Gateway, Netflix Zuul, etc) to direct or reject the requests based on the remaining quota and other filters.

For the sake of simplicity of this project, instead of a gateway, a simple Interceptor is used to achieve the same
functionality.

The Interceptor checks if the provided API-KEY has a remaining quota to consume the service.
If a quota is available then a request is allowed to consume the service, else the TOO_MANY_REQUESTS error is returned with a retry-after-seconds header in the response.

The main logic of rate-limiting resides in the QuotaBucket.class where the function tryConsumeAndReturnRemaining() does the job of refilling and consuming the remaining quota.

When a request is made, QuotaBucket refills the quota based on whether the refilling interval has passed or not.
Then it consumes from the remaining quota, thereby decrementing the remaining quota by 1.

A HashMap of QuotaBucket is maintained for each API-KEY respectively.

In a production setting, the remaining quota details will be fetched from a database (such as Redis, etc), such that
all the instances running this service can share the same remaining quota details.

For the sake of simplicity of this project, a HashMap is used to store the remaining quota details.

### API Details
Only one endpoint available (http://localhost:8080/api/students):
- Request method: GET
- Header: api-key

There are three valid API keys available with following rate limits:
| Key | Rate Limit |
| ------ | ------ |
| "api-key1" | 10 calls per hour |
| "api-key2" | 20 calls per hour |
| "api-key3" | 30 calls per hour |

### Sample response when API called successfully
- Body:

    ```sh
    {
        "students": [
            {
                "id": 1,
                "name": "Md Sayem Arsalan",
                "email": "sayem.arsalan@gmai.com"
            }
        ]
    }
    ```

### Sample response when rate limit exceeded
- Headers:
    | Key | Value |
    | ------ | ------ |
    | retry-after-seconds | 3175 |

- Body:
    ```sh
    {
        "timestamp": "2021-11-10T02:33:09.692+00:00",
        "status": 429,
        "error": "Too Many Requests",
        "message": "You have exhausted your API request quota. Try after 3175 seconds",
        "path": "/api/students"
    }
    ```

### How to run the project?
- Open the project in a compatible IDE (Eg. IntelliJ IDEA)
- Build the project
- Run RateLimitApplication

