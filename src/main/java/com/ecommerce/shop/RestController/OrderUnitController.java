package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.OrderModel;
import com.ecommerce.shop.Model.OrderUnitModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Service.OrderUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("order/units")
public class OrderUnitController {

    @Autowired
    private OrderUnitService unitService;

    @GetMapping
    public ModelAndView findAll(Model model) {
        List<OrderUnitModel> units = unitService.findAll();
        model.addAttribute("units", units);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping(path = "get/{unitId}")
    public Optional<OrderUnitModel> findById(@PathVariable("unitId") Long unitId) {
        return unitService.findById(unitId);
    }

    @PostMapping("/addUnit")
    public OrderUnitModel addUnit(OrderUnitModel unit) {
        unitService.addNewOrderUnit(unit);
        return unit;
    }

    @PutMapping(path = "{unitId}")
    public void updateUnit(@PathVariable("unitId") Long unitId,
                           @RequestParam(required = false) ProductModel product,
                           @RequestParam(required = false) OrderModel order,
                           @RequestParam(required = false) int amount,
                           @RequestParam(required = false) float price) {

        unitService.updateOrderUnit(unitId, order, product, amount, price);

    }

    @DeleteMapping(path = "/delete/{unitId}")
    public void deleteUnit(@PathVariable("unitId") Long unitId) {
        unitService.deleteOrderUnit(unitId);
    }


}
