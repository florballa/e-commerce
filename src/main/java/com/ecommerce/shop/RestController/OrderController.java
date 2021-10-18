package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.OrderModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ModelAndView findAll(Model model,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "id") String sortBy) {

        List<OrderModel> orders = orderService.getAllOrders(pageNo, pageSize, sortBy);
        model.addAttribute("orders", orders);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping(path = "get/{orderId}")
    public Optional<OrderModel> findById(@PathVariable("orderId") Long orderId) {
        return orderService.findById(orderId);
    }

    @PostMapping("/addOrder")
    public OrderModel addOrder(OrderModel order) {
        orderService.addNewOrder(order);
        return order;
    }

    @PutMapping(path = "{orderId}")
    public void updateOrder(@PathVariable("orderId") Long orderId,
                            @RequestParam(required = false) UserModel user) {

        orderService.updateOrder(orderId, user);

    }

    @DeleteMapping(path = "/delete/{orderId}")
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
    }


}
