package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.OrderModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Repository.OrderRepository;
import com.ecommerce.shop.Repository.PaginationAndSorting.OrderPaginationAndSorting;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderPaginationAndSorting orderPaging;

    public List<OrderModel> findAll() {
        return orderRepository.findAll();
    }

    public List<OrderModel> getAllOrders(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<OrderModel> pagedResult = orderPaging.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<OrderModel>();
        }
    }

    public Optional<OrderModel> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public void addNewOrder(OrderModel order) {
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrder(Long orderId, UserModel user) {
        OrderModel orderModel = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException(
                        "Order with ID " + orderId + "does not exist!"
                ));
        orderModel.setUser(user);
    }

    public void deleteOrder(Long orderId) {
        boolean exists = orderRepository.existsById(orderId);
        if (!exists) {
            throw new IllegalStateException(
                    "Order with ID " + orderId + "does not exist!"
            );
        }

        orderRepository.deleteById(orderId);
    }

}
