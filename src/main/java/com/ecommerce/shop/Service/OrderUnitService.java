package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.OrderModel;
import com.ecommerce.shop.Model.OrderUnitModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Repository.OrderRepository;
import com.ecommerce.shop.Repository.OrderUnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderUnitService {

    @Autowired
    private OrderUnitRepository orderUnitRepository;

    public List<OrderUnitModel> findAll() {
        return orderUnitRepository.findAll();
    }

    public Optional<OrderUnitModel> findById(Long unitId) {
        return orderUnitRepository.findById(unitId);
    }

    public void addNewOrderUnit(OrderUnitModel orderUnit) {
        orderUnitRepository.save(orderUnit);
    }

    @Transactional
    public void updateOrderUnit(Long unitId, OrderModel order, ProductModel product, int amount, float price) {
        OrderUnitModel orderModel = orderUnitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalStateException(
                        "OrderUnit with ID " + unitId + "does not exist!"
                ));
        orderModel.setOrder(order);
        orderModel.setProduct(product);
        orderModel.setAmount(amount);
        orderModel.setPrice(price);
    }

    public void deleteOrderUnit(Long unitId) {
        boolean exists = orderUnitRepository.existsById(unitId);
        if (!exists) {
            throw new IllegalStateException(
                    "OrderUnit with ID " + unitId + "does not exist!"
            );
        }

        orderUnitRepository.deleteById(unitId);
    }

}
