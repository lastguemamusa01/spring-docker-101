# spring-cloud-101


Microservices with spring boot


Microservices – small autonomous services that work together.
There is a bare minimum of centralized management of these services, which may be written in different programming languages and use different data storage technologies.
Rest services , small well choson deployable units and cloud enabled

Cloud – have multiple instances 

Challenges – 

1 – bounded context – how to indentify boundely of each microservices, business knowledge for boundely. This is done evolutionary – gaining knowledge, put in the microservices.
2 – configuration management – 10 microservices x 5 enviroments = 50 instances.  – maintain
3 – dynamic scale up and scale down – load in dieferent microservices will be different. Bring down the instance when they don’t need it.  Dynmic load balancing. Dynamic distribuition among active instances
4 – visibility – centralized log for know where is the functionality of 10 microservices, where it the bug. Monitoring microservices, 100 microservices. Automatically identify servers without disks spaces – instances
5 - pack of cards – if not weel designed, microservices calling another microservices, if the core microservices fail, all microservices that is depending fall like domino. Have fault tolerance for your microservices.

Spring cloud – help to resolve challenge – provides tools for developers to quickly build some of the common patterns in distributed systems. Not only 1 project, a lot of project.

Important – spring cloud Netflix – open source of Netflix – eureka

-	Configuration of management – multiple instances of microservices – a lot of configurations – SpringCloudConfigServer – store all the configurations all different environment of all microservices(centralized location)

-	Dynamic scale up and down 

-	Eureka(naming server) – instances will be registered, ribbon(client side load balancing) and feign(easier rest clients)
 
![image](https://user-images.githubusercontent.com/25869911/120239629-49efde00-c224-11eb-91fc-a80296b1659f.png)





Visibility and monitoring 
	- Zipkin distrubted tracing – trace request across to multiple components
	- netflix zuel API gateway

Logging, security and analytics are the common thing in microservices. Don’t implement all this common features in each microservices, netflix zuel API gateway
provide great solution.

Fault tolerance using hystrix if a service down – hystrix help us configure a default response.


Advantage of microservices 


-	Enable to adapt new technology and processes very easy
-	Monolithic don’t have that flexibility of communication
-	Java microservice, node js microservice, microservice kotlin etc etc. 
-	Dynamic scaling – example amazon – same amount of load , traffic or users thoroughout the year, special holiday season.
-	Microservices cloud enabled scale dynamically. Scale up and down based on load.
-	Small component – faster release cycles, new features faster.
-	


Components  or applications – 

Ports
Application	Port
Limits Service	8080, 8081, ...
Spring Cloud Config Server	8888
	
Currency Exchange Service	8000, 8001, 8002, ..
Currency Conversion Service	8100, 8101, 8102, ...
Netflix Eureka Naming Server	8761
Netflix Zuul API Gateway Server	8765
Zipkin Distributed Tracing Server	9411
URLs
Application	URL
Limits Service	http://localhost:8080/limits http://localhost:8080/actuator/refresh (POST)

Spring Cloud Config Server	http://localhost:8888/limits-service/default http://localhost:8888/limits-service/dev

Currency Converter Service - Direct Call	http://localhost:8100/currency-converter/from/USD/to/INR/quantity/10

Currency Converter Service - Feign	http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10000

Currency Exchange Service	http://localhost:8000/currency-exchange/from/EUR/to/INR http://localhost:8001/currency-exchange/from/USD/to/INR

Eureka	http://localhost:8761/

Zuul - Currency Exchange & Exchange Services	http://localhost:8765/currency-exchange-service/currency-exchange/from/EUR/to/INR http://localhost:8765/currency-conversion-service/currency-converter-feign/from/USD/to/INR/quantity/10

Zipkin	http://localhost:9411/zipkin/

Spring Cloud Bus Refresh	http://localhost:8080/actuator/bus-refresh (POST)


![image](https://user-images.githubusercontent.com/25869911/120239656-58d69080-c224-11eb-972c-3849d37f9e48.png)

![image](https://user-images.githubusercontent.com/25869911/120239665-5ffd9e80-c224-11eb-96ac-5695aff1a50a.png)

 
 

Microservice version 2

 


Spring cloud -tools for microservices
Git and git repository


-	Spring boot centralized configuration –
-	How to limit services


Add config client – spring cloud config in spring start io

 
 ![image](https://user-images.githubusercontent.com/25869911/120239678-655ae900-c224-11eb-8035-f07368fd1cf0.png)


![image](https://user-images.githubusercontent.com/25869911/120239686-68ee7000-c224-11eb-849a-f0b5eb83c11f.png)








Agregar esto en application properties to use config server

.	spring.config.import=optional:configserver:http://localhost:8888


limit-service – if for denifing limits of minimum and maximum  - simple limit service

http://localhost:8080/limits


{
minimum: 1,
maximum: 1000
}


Use application configuration and connect it to centralized configuration

Application.properties

## configure where spring config server will be
spring.config.import=optional:configserver:http://localhost:8888

## values for limits of minimum and maximum
limits-service.minimum=2
limits-service.maximum=998



Create configuration.java file in configuration folder for reuse values in application.properties in our rest controller service


@Component
@ConfigurationProperties("limits-service")
public class Configuration {
    private int minimum;
    private int maximum;
    public int getMinimum() {
        return minimum;
    }
    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
    public int getMaximum() {
        return maximum;
    }
    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}


@Autowired
    private Configuration configuration;

@GetMapping("/limits")
    public Limits retrieveLimits() {
        // return new Limits(1,1000);
        return new Limits(configuration.getMinimum(), configuration.getMaximum());
    }


Set up spring cloud config server and connect to github repository

Create spring initializr the app
Application properties
## put name the application
spring.applciation.name=spring-cloud-config-server

## configure the port of this server
server.port = 8888


create git hub repository

add limits-service.propertie file

limits-service.minimum=3
limits-service.maximum=997



add in the spring cloud server application propertie file, the path of limits-service.propertie file of git

spring.cloud.config.server.git.uri=file:///Users/mxn1020/Documents/VSCode/git-
localconfig-repo

and enable config server
@EnableConfigServer
@SpringBootApplication
public class SpringCloudConfigServerApplication {


connect limits microservice to spring cloud config server

in the limit microservices application.properties

## configure where spring config server will be
spring.config.import=optional:configserver:http://localhost:8888


So limit services use config server that use values from git

If there are various enviroments for limits services – for github repositories

In the github you can create each environment propertie files

limits-service-dev.properties

google chrome browser

http://localhost:8888/limits-service/qa   - this will show first the will take qa values and default value(second option)

limit service in application propertie file you can use profile to select the environment
## multiple enviroments using profile
spring.profiles.active=dev


![image](https://user-images.githubusercontent.com/25869911/120239714-7c99d680-c224-11eb-9a17-efe538baae81.png)



 

Create currency extetchange service- add config client from spring cloud

Set in the application.property the port and name of app


## put name the application
spring.application.name=currency-exchange

## configure where spring config server will be
spring.config.import=optional:configserver:http://localhost:8888

## configure the port of this server
server.port = 8000

create controller with pathvariable and java beans for return hardcoded values.



 ![image](https://user-images.githubusercontent.com/25869911/120239719-83c0e480-c224-11eb-8c34-8425fe469dbf.png)





Add In the bean environment of string in the currency exchange service


Run multiple instances in the different port

 mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8001


create currency conversion service
application properties

## put name the application
spring.application.name=currency-conversion

## configure where spring config server will be
spring.config.import=optional:configserver:http://localhost:8888

## configure the port of this server
server.port = 8100

create beans

create controller

you can use rest template(only for 1 service) or feign(much better to use 100 services) for use another rest service

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        
        // restTemplate can be use to call rest api
        HashMap<String,String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
        CurrencyConversion currencyConversion = responseEntity.getBody();

        return new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());
         
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        
       
        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        return new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());
         
    }
    
}


enable feign

@SpringBootApplication
@EnableFeignClients
public class CurrencyConversionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConversionServiceApplication.class, args);
    }

}





spring cloud provide feign – easy to call another rest services – add feign in the pom.xml
   <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-openfeign</artifactId>
        </dependency>


Create proxy interface for connect to another service - 

@FeignClient(name="currency-exchange", url="localhost:8000")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
    
}



When we create naming server(create various instances) , feign help with the load balancer of the service.

For dynamically launch currency exchange  instances and distribute load between them(load balancer).
Don’t harcode the feing client url putting directly the url with ports
-	service registry or naming server


naming server – will be registered all the instances of the microservices

currency conversion microservice ask to the naming server what are the addresses of currency exchange microservices.
Create naming server on the spring initialzr -  eureka
Eureka naming server


@EnableEurekaServer
@SpringBootApplication
public class NamingServerApplication {


application.properties

## put name the application
spring.application.name=naming-server

## configure where spring config server will be
spring.config.import=optional:configserver:http://localhost:8888

## configure the port of this server
server.port = 8761

## we dont want to register with itself, only can find another ones
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false


Top Recommendation From Debugging Guide:
Give these settings a try individually in application.properties of all microservices (currency-exchange, currency-conversion) to see if they help
1.	eureka.instance.prefer-ip-address=true
OR
1.	eureka.instance.hostname=localhost

we need register service in eureka.
Add in the pom xml from currency conversion and currency exchange
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>


Now in eureka http://localhost:8761/
You can see the instance up

Applicatrion properties of currency exchange and conversion service
## configure url for eureka server - for connect specific or another server
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka


add load balancer (spring cloud loadbalancer)
Spring cloud load balancer

@FeignClient(name="currency-exchange")
public interface CurrencyExchangeProxy {

Feign client talk to eureka and pick up the instances of currency exchange and do load balancing.

Client side load balancing – spring cloud load balancer inside eureka

Micrservices have common features – authentication, authorization, logging and rate limiting -> api gateway(old is zuul) -> spring cloud gateway

Spring cloud gateway

Spring initializr create the api-gateway
Add config client and eureka discovery client and gateway(spring cloud routing)

Add application properties of api gateway service

## put name the application
spring.application.name=api-gateway

## configure where spring config server will be
spring.config.import=optional:configserver:http://localhost:8888

## configure the port of this server
server.port = 8765

## configure url for eureka server - for connect specific or another server
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
eureka.instance.hostname=localhost

## enable the discovery using discovery client, this is possible(eureka client)
spring.cloud.gateway.discovery.locator.enabled=true
## convert the api name to lower case
spring.cloud.gateway.discovery.locator.lower-case-service-id=true



api gateway

api gateway application properties


api gateway need to talk to eureka to fin the server location

client for current exchange you can give this url 
http://localhost:8765/CURRENCY-EXCHANGE/currency-exchange/from/USD/to/WON

you can implement authentication in api gateway. 
Build custom route – create configuration file

@Configuration
public class ApiGatewayConfiguration {
    
    // customiza your route of gateway
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        
        // uri can be specfric url, for the authentication header you can use addRequestHeader
        // everything in the currency change path  "/currency-exchange/**"
        // use load balancer regitered in eureka lb://currency-exchange
        // custom url : http://localhost:8765/currency-exchange/from/USD/to/WON
        // regex about all segment is copied in the second 
        //f.rewritePath("/currency-conversion-new/(?<segment>.*)", "/currency-conversion-feign/${segment}"))

        return builder.routes().route(p -> p.path("/get")
                                .filters(f -> f.addRequestHeader("MyHeader", "MyURI")
                                .addRequestParameter("Param", "MyValue"))
                                .uri("http://httpbin.org:80"))
                                .route(p -> p.path("/currency-exchange/**")
                                .uri("lb://currency-exchange"))
                                .route(p -> p.path("/currency-conversion/**")
                                .uri("lb://currency-conversion"))
                                .route(p -> p.path("/currency-conversion-feign/**")
                                .uri("lb://currency-conversion"))
                                .route(p -> p.path("/currency-conversion-new/**")
                                .filters(f -> f.rewritePath("/currency-conversion-new/(?<segment>.*)", "/currency-conversion-feign/${segment}"))
                                .uri("lb://currency-conversion"))
                                .build();
    }
}

Cloud gateaway logging filter
In api gateway you can add global filters
-	log every request that goest through the api gateway
-	@Component
-	public class LoggingFilter implements GlobalFilter {
-	
-	    private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
-	
-	    @Override
-	    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
-	    
-	        logger.info("Path of the request received -> {}", exchange.getRequest().getPath());
-	        return chain.filter(exchange);
-	    
-	    }
-	    
-	}


What url was logged in printed in console in the api gateway rest api
Autentification is implemented in api gateway
Gateway – route to Apis
Privide cross cutting concerns : security, monitoring/metrics
Spring cloud gateway is built on top of spring webflux and you can match routes on any request attributes(header, host, request method, query parameter), you can define predicates and filters
return builder.routes().route(p -> p.path("/get")
                                .filters(f -> f.addRequestHeader("MyHeader", "MyURI")


Spring cloud gateway integrates with spring cloud discovery client(load balancing) and path rewriting
 
  
  ![image](https://user-images.githubusercontent.com/25869911/120239774-a3f0a380-c224-11eb-827d-6edd03bb069e.png)


  ![image](https://user-images.githubusercontent.com/25869911/120239782-a6eb9400-c224-11eb-8c15-790455c97db6.png)

 
Circuit breaker with resilience4j
Microservice like credit card cannot send fallback response but for e commercial site is possible do that.
Circuit breaker pattern
Rate limiting – only allowed certain amount of request in certain amount of time.
Only after certain temporary failures return fallback default response
Use resilence4j – lightweith, easy to use fault tolerance library inspired by netflix hystrix
In currency exchange service in pom.xml
We alredy have actuactor,  we need add aop and resilience4j
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-spring-boot2</artifactId>
        </dependency>

-	Retry -
Application.proerties – you can setting retry numbers
## custom the number of maximum retry calling by retry name is 5
resilience4j.retry.instances.sample-api.maxRetryAttempts=5

@Retry(name="sample-api", fallbackMethod = "hardcodedResponse")
    public String sampleApi() {

Create circuitBreakController - > rest controller

Manually fired various request in command
watch curl http://localhost:8000/sample-api
10 request per seconds
watch -n 0.1 curl http://localhost:8000/sample-api

circuit breaker, if some service is down, its not calling the down service and return default response.
3 states in circuit breaker – closed(always call microservices) , open(not call microservices, return callback response) and half open (would be sending a percentage of requests to the dependent microservice, for rest of the requests, it would return the hardcoded response or the fall back response.)
Start closed, 1000 times all failing 90% -> switch open state, wait little while change half open(check if up, percentage), if have proper response its go to closed, if not go back to open.
You can customize failureratethreshold, etc.
You can configure in yaml file or application properties
## circuit breaker - when 90% of request fail, switch to open state
resilience4j.circuitbreaker.instances.default.failureRateThreshold=90

rate limiting
// 10 seconds allows only 10000 calls to this api
@RateLimiter(name="default")
    public String sampleApi() {

in application properties you can set configuration

## rate limiter config - 2 request per 10 seconds
resilience4j.ratelimiter.instances.default.limitForPeriod=2
resilience4j.ratelimiter.instances.default.limitRefreshPeriod=10s





Bulkhead – how many concurrent call alloweed
@Bulkhead(name="default")
    public String sampleApi() {

in application properties you can set configuration
## bulkhead config - maximum 10 concurrent call, sample api is the name
resilience4j.bulkhead.instances.default.maxConcurrentCalls=10
resilience4j.bulkhead.instances.sample-api.maxConcurrentCalls=10


CircuitController.java
@RestController
public class CircuitBreakerController {

    // add logger for this particular controller
    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    // with retry annotation(multiple time when server is temporary down) when this is down, can do it again
    // default is the default configuration for retry -> if fail try another 2 times - total 3 times - after 3 times return error only
    // configure fallbackMethod -> hardcodedResponse is method
    @GetMapping("/sample-api")
    //@Retry(name="dafault")
    //@Retry(name="sample-api", fallbackMethod = "hardcodedResponse")
    //@CircuitBreaker(name="default", fallbackMethod = "hardcodedResponse")
    // 10 seconds allows only 10000 calls to this api
    //@RateLimiter(name="default")
    @Bulkhead(name="default")
    public String sampleApi() {

        logger.info("Sample Api call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://locahost:8080/some-dummy-url", String.class);
        // for testing fallback response
        //ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://locahost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    
    }
        
    // you can customize by different fallback - different exception
    public String hardcodedResponse(Exception ex) {
        return "fallback-response";
    }
    
}


