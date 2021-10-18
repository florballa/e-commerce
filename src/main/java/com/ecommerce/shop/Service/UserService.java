package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.OrderModel;
import com.ecommerce.shop.Model.RoleModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Repository.PaginationAndSorting.UserPaginationAndSorting;
import com.ecommerce.shop.Repository.RoleRepository;
import com.ecommerce.shop.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPaginationAndSorting userPaging;

    private final static String USER_NOT_FOUND = "User with the email %s not found!";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public List<UserModel> getAllUsers(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<UserModel> pagedResult = userPaging.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<UserModel>();
        }
    }

    public UserModel findById(Long id) {
        return userRepository.findById(id).get();
    }

    public String signUpUser(UserModel userModel) {

        boolean userExists = userRepository.findByEmail(userModel.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("Email taken!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
        userModel.setPassword(encodedPassword);

        RoleModel role = roleRepository.findByName("Costumer");
        userModel.addRole(role);

        userRepository.save(userModel);

        return "User registered successfully";

    }

    public void saveUser(UserModel user) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public List<RoleModel> getRoles() {
        return roleRepository.findAll();
    }

//    public UserModel getCurrentlyLoggedUser(Authentication authentication){
//
//        if (authentication == null) return null;
//
//        UserModel user = null;
//        Object principal = authentication.getPrincipal();
//
//        if( principal instanceof UserModel){
//            user = ((UserModel) principal).getUser();
//        } else if (principal instanceof CustomOAuth2User){
//            String email ((CustomOAuth2User) principal).getEmail();
//            user = getUserByEmail(email);
//        }
//
//        return user;
//
//    }

}
