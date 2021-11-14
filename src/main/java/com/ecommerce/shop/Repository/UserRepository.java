package com.ecommerce.shop.Repository;

import com.ecommerce.shop.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByEmail(String email);

}
