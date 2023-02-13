package com.bock.observability.example;


import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ExampleController {

    private Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @Autowired
    private FactorService factorService;

    @GetMapping("debugTest")
    public String log(){
        logger.debug("call logTest");
        return "Hello World";
    }

    @GetMapping("errorTest")
    public String error(){
        logger.error("Gonna throw an exception and get a 404");
        throw new RuntimeException("Uh oh");
    }

    @GetMapping("traceTest")
    public String trace(){
        return "trace run";
    }

    @GetMapping("factor/{num}")
    public ResponseEntity getFactors(@PathVariable("num") int num) {
        if (num < 1) {
            logger.error("Invalid number requested for factoring" + num);
            return ResponseEntity.badRequest().body("Invalid number requested for factoring " + num);
        }
        return ResponseEntity.ok(factorService.factor(num));
    }


}
