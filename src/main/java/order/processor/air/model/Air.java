package order.processor.air.model;

import java.util.Objects;

public class Air {

    private String iata;

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Air air = (Air) o;
        return iata.equals(air.iata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iata);
    }

    @Override
    public String toString() {
        return "Air{" +
                "iata='" + iata + '\'' +
                '}';
    }
}
