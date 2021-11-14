package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.RoleModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Repository.PaginationAndSorting.UserPaginationAndSorting;
import com.ecommerce.shop.Repository.RoleRepository;
import com.ecommerce.shop.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPaginationAndSorting userPaging;

    private final static String USER_NOT_FOUND = "User with the email '%s' not found!";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found with username:: {}", email);
            throw new UsernameNotFoundException("User not found!");
        } else {
            log.info("User found with username:: {}", email);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public UserModel findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<UserModel> getAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        log.info("Fetching all users");

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<UserModel> pagedResult = userPaging.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<UserModel>();
        }
    }

    public UserModel findById(Long id) {
        log.info("Find user by ID {}", id);
        return userRepository.findById(id).get();
    }

    public UserModel signUpUser(UserModel userModel) {

        log.info("Sign up user:: {}", userModel);

        UserModel userExists = userRepository.findByEmail(userModel.getEmail());
        if (userExists.getEmail() != null) {
            log.error("Email already exists:: {}", userModel.getEmail());
            throw new IllegalStateException("Email taken!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
        userModel.setPassword(encodedPassword);

        RoleModel role = roleRepository.findByName("Costumer");
        userModel.addRole(role);

        userRepository.save(userModel);

        return userModel;

    }

    public void saveUser(UserModel user) {

        log.info("Saving new user to DB:: {}", user);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public List<RoleModel> getRoles() {
        return roleRepository.findAll();
    }

}
