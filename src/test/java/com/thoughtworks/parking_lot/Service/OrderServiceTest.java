package com.thoughtworks.parking_lot.Service;

import com.thoughtworks.parking_lot.Repository.OrderRepository;
import com.thoughtworks.parking_lot.core.Order;
import com.thoughtworks.parking_lot.core.ParkingLot;
import javassist.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderService.class)
@ActiveProfiles(profiles = "test")
public class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Test
    public void should_create_an_order() throws Exception {
        Order acceptedOrder = new Order();
        acceptedOrder.setParkingLotName("Genrev");
        acceptedOrder.setPlateNumber(234);

        List<Order> orderList = new ArrayList<>();
        orderList.add(acceptedOrder);

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setCapacity(3);
        parkingLot.setName("Genrev");
        parkingLot.setOrderList(orderList);

        Order order = new Order();
        order.setPlateNumber(123);

        when(orderRepository.save(order)).thenReturn(order);

        Order actualOrder = orderService.createOrder(parkingLot,order);

        Assertions.assertThat(actualOrder.getParkingLotName()).isEqualTo(parkingLot.getName());
        Assertions.assertThat(actualOrder.getPlateNumber()).isEqualTo(order.getPlateNumber());
    }

    @Test
    public void should_update_an_order() throws NotFoundException {
        Order order = new Order();
        order.setParkingLotName("Genrev");
        order.setPlateNumber(234);
        order.setOrderStatus("Closed");


        when(orderRepository.findOneByPlatenumber(234)).thenReturn(order);
        when(orderRepository.save(any())).thenReturn(order);

        Order expectedOrder = orderService.updateOrder(234);

        Assertions.assertThat(expectedOrder.getPlateNumber()).isEqualTo(order.getPlateNumber());
        Assertions.assertThat(expectedOrder.getParkingLotName()).isEqualTo(order.getParkingLotName());
    }

    @Test
    public void should_not_update_an_order_when_plate_number_is_invalid(){
        when(orderRepository.findOneByPlatenumber(123)).thenReturn(null);
        Assertions.assertThatThrownBy(() -> orderService.updateOrder(123));
    }

    @Test
    public void should_not_create_an_order_when_parking_lot_is_full() throws Exception {
        Order acceptedOrder = new Order();
        acceptedOrder.setParkingLotName("Genrev");
        acceptedOrder.setPlateNumber(234);

        List<Order> orderList = new ArrayList<>();
        orderList.add(acceptedOrder);

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setCapacity(1);
        parkingLot.setName("Genrev");
        parkingLot.setOrderList(orderList);

        Order order = new Order();
        order.setPlateNumber(123);

        assertThrows(Exception.class, () ->{
            orderService.createOrder(parkingLot,order);
        });
    }
}
