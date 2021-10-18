package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.CartItemModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Repository.CartItemRepository;
import com.ecommerce.shop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    private CartItemRepository cr;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItemModel> listCartItems(UserModel user){
        return cr.findByUser(user);
    }

    public Integer addProduct(Long productId, Integer quantity, UserModel user){

        Integer addedQuantity = quantity;

        ProductModel product = productRepository.findById(productId).get();

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

        cr.updateQuantity(quantity, productId, userId);
        ProductModel product = productRepository.findById(productId).get();

        BigDecimal subtotal = product.getDefaultPrice().multiply(BigDecimal.valueOf(quantity));

        return subtotal;
    }

    @Transactional
    public void removeProductFromCart(Long userId, Long productId){

        cr.deleteByUserAndProduct(userId, productId);

    }

}
