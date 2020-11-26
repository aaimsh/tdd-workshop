# **Production TDD for spring**
### **Application features**
We will build A simple rest application that will perform create, retrieve, update, and delete (CRUD) operations for stc customer device credit limit.
The service will be [RESTful](https://restfulapi.net/) with the following endpoint:
```
POST /credit-limit/
GET /credit-limit/{customer-id}
PUT /credit-limit/{customer-id}
```
The Request body for the POSTing new customer is the following JSON:
```
{
    "nationalId": "string", //Required
    "idType":"char", //Required
    "customerName":"string", //Optional
    "creditLimit": "integer" , //Optional Default 0
}
```
The request body for updating an existing customer:
```
{
    "creditLimit": 123456
}
```

GET endpoint request/response sample:
```
GET /credit-limit/1234567890 HTTP/1.1
Content-Type: application/json;charset=UTF-8
HOST:  localhost:8080

HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
{
    "nationalId": "1234567890",
    "idType":"S",
    "customerName":"Abdulailah Alhussayen",
    "creditLimit": 4500,
}
``` 

### **Validations**
The API should validate customer id is matching idType pattern. The pattern for each idType is the following:  
|idType|regex pattern
|---|---
|S|^1\\d{9}$
|I|^2\\d{9}$

The API also should validate the idType is either `S` or `I`.  
Whenever there is a violation the API should return HTTP error response with status code `400 Bad Request`, and a proper message.  
### **Service errors**
Whenever an error response (Http status code >= 400) is returned, the body will contain a JSON object that describes the problem. The error object has the following structure:
```
HTTP/1.1 {http-status}
Content-Type: application/json;charset=UTF-8

{
   "code": {error-code},
   "message": {error-message}
}
```
| HTTP status code | Error code | Message
|---|---|---|
|400|4001|Missing fields: [fields].
|400|4002|Id is not matching IdType pattern.
|400|4003| Id Type is not supported.
|400|4003|Customer with same Id already exists.
|404|4041|Customer does not exist.


### **Set up**
Go to [*spring initializer*](start.spring.io) and select the following dependencies:
- Spring Web
- Lombok
   
click generate save the folder, unzip it, and open it on your favorite editor.
### **Use Junit 5**
If you have generated your spring project from [*spring initializer*](start.spring.io) the you will notice that they added the test dependency and excluded junit4 engine to enable junit 5.  
`pom.xml`
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
> *vintage* is junit4 engine, while *jupiter* is junit5 engine.
## **Testing Controller slice**
***live demo***  
> src/test/java/com/example/tddworkshop/CreditLimitControllerTests.java

Testing get customer credit limit:
- Get customer should return customer.
- If service throws CustomerDoesNotExistException the controller should return 404.
- If service throws CustomerAlreadyExistsException the controller should return 400.
- POST, PUT endpoints should validate required fields, return 400 if missing or invalid.
```
@WebMvcTest(CreditLimitController.class)
public class CreditLimitControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CreditLimitService CreditLimitService;

    @Test
    void getCreditLimitShouldReturnCreditLimit() throws Exception{
        when(creditLimitService.getCustomerCreditLimit(anyString())).thenReturn(new CreditLimit("1234567890", "S", "Abdulailah", 123456));
        mockMvc
        .perform(MockMvcRequestBuilders.get("/credit-limit/1234567890"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("nationalId").value("1234567890"))
        .andExpect(MockMvcResultMatchers.jsonPath("idType").value("S"))
        ...
        ;
    }
}
```
> Read about @WebMvcTest [here](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured-mvc-tests).  

## **Testing Data slice**
When testing the data slice, first we need to setup the environment by setting up in-memory databases.
If you haven't already chosen h2 in the spring initializer go to your `pom.xml` and add the following:
```
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```
Here in v-channels we either own the database -design the schema and perform CRUD operations on it- or we connect to an existing database then read from it.
### **What should we test?**
Since we will depend on Spring Data JPA to create our schema, we shouldn't write tests for it, instead we should write tests for our Entity Classes.

### **Live Demo**
#### Person repository tests:
Create `PersonRepoTests.java` and annotate it with `@DataJpaTest`. `@DataJpaTest` will run the following autoconfigureres-if present in the classpath-:
- CacheAutoConfiguration
- JpaRepositoriesAutoConfiguration
- FlywayAutoConfiguration
- DataSourceAutoConfiguration
- DataSourceTransactionManagerAutoConfiguration
- JdbcTemplateAutoConfiguration
- LiquibaseAutoConfiguration
- TransactionAutoConfiguration
- TestDatabaseAutoConfiguration




