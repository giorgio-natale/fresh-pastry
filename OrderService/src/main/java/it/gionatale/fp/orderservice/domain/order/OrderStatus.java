package it.gionatale.fp.orderservice.domain.order;

import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class OrderStatus {
    @Enumerated(EnumType.STRING)
    private OrderStatusName orderStatusName;
    private String reason;

    protected OrderStatus() {}

    public OrderStatus(OrderStatusName orderStatusName, String reason) {
        this.orderStatusName = orderStatusName;
        this.reason = reason;
    }

    public OrderStatusName getOrderStatusName() {
        return orderStatusName;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return orderStatusName == that.orderStatusName && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatusName, reason);
    }
}
