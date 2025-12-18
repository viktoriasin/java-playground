package ru.sinvic.patterns.orders.notification.system;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class OrderNotificationManager {
    private final List<OrderEventListener> orderSubscriberList = new ArrayList<>();

    public OrderNotificationManager(OrderEventListener... listeners) {
        orderSubscriberList.addAll(Arrays.asList(listeners));
    }

    public void subscribe(OrderEventListener listener) {
        orderSubscriberList.add(listener);
    }

    public void unsubscribe(OrderEventListener listener) {
        orderSubscriberList.remove(listener);
    }

    public void notify(Order order) {
        for (OrderEventListener listener : orderSubscriberList) {
            listener.update(order);
        }
    }
}
