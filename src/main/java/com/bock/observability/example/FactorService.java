package com.bock.observability.example;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class FactorService {

    private Logger logger = LoggerFactory.getLogger(FactorService.class);

    private List<Integer> mostFactors = new ArrayList<>();

    private final MeterRegistry registry;

    public FactorService(MeterRegistry registry) {
        Gauge
                .builder("most.factors.size", mostFactors, List::size)
                .register(registry);

        this.registry = registry;
    }

    @Timed(value = "factor.time", description = "Time taken to get factor int")
    public List<Integer> factor(int num) {
        Counter counter = registry.counter("factors");

        List<Integer> factors = IntStream.range(1, num)
                .filter(factor -> num % factor == 0)
                .boxed().collect(Collectors.toList());

        if (factors.size() > mostFactors.size()) {
            mostFactors.clear();
            mostFactors.addAll(factors);
        }

        logger.debug(factors.size() + " factors found");

        return factors;
    }
}
