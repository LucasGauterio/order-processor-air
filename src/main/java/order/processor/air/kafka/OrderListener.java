package order.processor.air.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import order.processor.air.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class OrderListener {

    private final Logger log = LoggerFactory.getLogger(OrderListener.class);

    public static final String FARE_AIR = "FARE_AIR";
    public static final String BOOKING_AIR = "BOOKING_AIR";
    public static final String EMISSION_AIR = "EMISSION_AIR";
    public static final String INIT = "INIT";
    public static final String PAYMENT = "PAYMENT";
    public static final String AIR = "AIR";
    public static final String RISK_ANALYSIS = "RISK_ANALYSIS";

    @Inject
    OrderProvider provider;

    @Inject
    ObjectMapper mapper;

    @Topic("orders")
    public void receive(@KafkaKey String key, Order orderState) {
        try {
            if (!orderState.getProducts().contains(AIR))
                return;
            Order order = mapper.readValue(orderState.getMessage(), Order.class);
            order.setMessage(orderState.getMessage());
            order.setProcessedStates(orderState.getProcessedStates());
            order.setId(orderState.getId());

            if (canExecuteState(FARE_AIR, order)) {
                process(key, order, FARE_AIR);
            } else if (canExecuteState(BOOKING_AIR, order)) {
                process(key, order, BOOKING_AIR);
            } else if (canExecuteState(EMISSION_AIR, order)) {
                process(key, order, EMISSION_AIR);
            } else {
                log.info("Ignored Order - " + order + " by " + key);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void process(@KafkaKey String key, Order order, String fare_air) {
        logReceived(key, fare_air, order);
        sleep();
        logProcessed(key, fare_air, order);
        order.getProcessedStates().add(fare_air);
        provider.sendOrder(order.getId(), order);
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean canExecuteState(String state, Order order) {
        if (order.getProcessedStates().contains(state))
            return false;
        List<String> needed = new ArrayList<>();
        switch (state) {
            case FARE_AIR:
                needed = Collections.singletonList(INIT);
                break;
            case BOOKING_AIR:
                needed = Arrays.asList(INIT, FARE_AIR);
                break;
            case EMISSION_AIR:
                needed = Arrays.asList(INIT, FARE_AIR, BOOKING_AIR, RISK_ANALYSIS, PAYMENT);
                break;
        }
        return order.getProcessedStates().containsAll(needed);
    }

    private void logReceived(String key, String state, Order order){
        log.info("Received " + state + " Order - " + order + " by " + key);
    }

    private void logProcessed(String key, String state, Order order){
        log.info("Processed " + state + " - Order - " + order + " by " + key);
    }

}