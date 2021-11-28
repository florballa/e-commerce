package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.CartItemModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Repository.CartItemRepository;
import com.ecommerce.shop.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ShoppingCartService {

    @Autowired
    private CartItemRepository cr;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItemModel> listCartItems(UserModel user){
        log.info("List User Cart Items");
        return cr.findByUser(user);
    }

    public Integer addProduct(Long productId, Integer quantity, UserModel user){
        log.info("Add product to cart:: {} by {}", productId, user);
        Integer addedQuantity = quantity;

        ProductModel product = productRepository.findById(productId).get();
        product.setStock(product.getStock() - quantity);

        CartItemModel cartItem = cr.findByUserAndProduct(user, product);

        if (cartItem != null){
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        }else{
            cartItem = new CartItemModel();
            cartItem.setQuantity(quantity);
            cartItem.setUser(user);
            cartItem.setProduct(product);
        }

        cr.save(cartItem);

        return addedQuantity;
    }

    @Transactional
    public BigDecimal updateQuantity(Integer quantity, Long productId, Long userId){
        log.info("Update Quantity");
        cr.updateQuantity(quantity, productId, userId);
        ProductModel product = productRepository.findById(productId).get();

        BigDecimal subtotal = product.getDefaultPrice().multiply(BigDecimal.valueOf(quantity));

        return subtotal;
    }

    @Transactional
    public void removeProductFromCart(UserModel user, Long productId){
        log.info("Remove quantity");
        cr.deleteByUserAndProduct(user, productId);
    }

}
