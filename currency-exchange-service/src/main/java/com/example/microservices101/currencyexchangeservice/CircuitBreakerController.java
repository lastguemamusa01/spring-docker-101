package com.example.microservices101.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

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
