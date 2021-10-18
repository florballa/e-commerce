package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.CartModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping("/addToCart")
    public ModelAndView addCart(@RequestParam Long id, HttpSession session){

        ModelAndView modelAndView = new ModelAndView("cart");
        ProductModel product = productRepository.getById(id);
        CartModel cart = new CartModel();
        List<CartModel> list = (List<CartModel>) session.getAttribute("cart");
        if(list == null){
            list = new ArrayList<CartModel>();
        }
        if(product != null){
            cart.ToCart(product);
            BigDecimal total = addToCart(list, cart);
            modelAndView.addObject("total", total);
            session.setAttribute("cart", list);
        }
        modelAndView.addObject("listCart", list);
        return modelAndView;
    }

    private BigDecimal addToCart(List<CartModel> list, CartModel cart){
        BigDecimal total = new BigDecimal(0);
        boolean isExit = false;
        for (CartModel c : list){
            if(c.equals(cart)){
                c.setQuantity(c.getQuantity() + 1);
                isExit = true;
            }
            total = total.add(c.getPrice().multiply(new BigDecimal(c.getQuantity())));
        }
        if(isExit == false){
            list.add(cart);
            total = total.add(cart.getPrice().multiply(new BigDecimal(cart.getQuantity())));
        }
        return total;
    }

    @RequestMapping("shop/cart/remove")
    private ModelAndView removeCart(@RequestParam int id, HttpSession session) {
        ModelAndView mav = new ModelAndView("shop/cart");
        List<CartModel> list = (List<CartModel>) session.getAttribute("cart");
        if (list != null) {
            BigDecimal total = removeCartItem(list, id);
            mav.addObject("total", total);
            session.setAttribute("cart", list);
        }
        mav.addObject("listCart", list);
        return mav;
    }

    private BigDecimal removeCartItem(List<CartModel> list, int id) {
        BigDecimal total = new BigDecimal(0);
        CartModel temp = null;
        for (CartModel c : list) {
            if (c.getId() == (id)) {
                temp = c;
                continue;
            }
            total = total.add(c.getPrice().multiply(new BigDecimal(c.getQuantity())));
        }
        if (temp != null)
            list.remove(temp);
        return total;
    }

    @RequestMapping("/shop/cart/update")
    public ModelAndView updateCart(@RequestParam int id,
                                   @RequestParam int quantity,
                                   HttpSession session) {
        ModelAndView mav = new ModelAndView("shop/cart");
        List<CartModel> list = (List<CartModel>) session.getAttribute("cart");
        if (list != null) {
            BigDecimal total = updateCartItem(list, id, quantity);
            mav.addObject("total", total);
            session.setAttribute("cart", list);
        }
        mav.addObject("listCart", list);
        return mav;
    }

    private BigDecimal updateCartItem(List<CartModel> list, int id, int quantity) {
        BigDecimal total = new BigDecimal(0);
        for (CartModel c : list) {
            if (c.getId() == (id)) {
                c.setQuantity(quantity);
            }
            total = total.add(c.getPrice().multiply(new BigDecimal(c.getQuantity())));
        }
        return total;
    }


}
