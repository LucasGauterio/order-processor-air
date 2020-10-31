package order.processor.air.model;

import io.micronaut.core.annotation.Introspected;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Introspected
public class Order {

    private String id;

    private List<String> products = new ArrayList<>();

    private List<String> processedStates = new ArrayList<>();

    private List<Air> air = new ArrayList<>();

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<String> getProcessedStates() {
        return processedStates;
    }

    public void setProcessedStates(List<String> processedStates) {
        this.processedStates = processedStates;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Air> getAir() {
        return air;
    }

    public void setAir(List<Air> air) {
        this.air = air;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(products, order.products) &&
                Objects.equals(processedStates, order.processedStates) &&
                Objects.equals(air, order.air) &&
                Objects.equals(message, order.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products, processedStates, air, message);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", products=" + products +
                ", processedStates=" + processedStates +
                ", air=" + air +
                ", message='" + message + '\'' +
                '}';
    }
}
