package com.ecommerce.shop.Repository;

import com.ecommerce.shop.Model.CartItemModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemModel, Long> {

    List<CartItemModel> findByUser(UserModel user);

    public CartItemModel findByUserAndProduct(UserModel user, ProductModel product);

    @Query("UPDATE CartItemModel c SET c.quantity = ?1 WHERE c.product.id = ?2 AND c.user.id = ?3")
    @Modifying
    public void updateQuantity(Integer quantity, Long id, Long userId);

    @Query("DELETE FROM CartItemModel c WHERE c.user.id = ?1 AND c.product.id = ?2 ")
    @Modifying
    public void deleteByUserAndProduct(UserModel user, Long productId);
}
