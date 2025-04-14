package me.jooie.minicafe.service;

import jakarta.persistence.EnumType;
import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.CafeOrder;
import me.jooie.minicafe.domain.CafeUser;
import me.jooie.minicafe.domain.UserRole;
import me.jooie.minicafe.dto.CustomerManageDto;
import me.jooie.minicafe.dto.UserCreateDto;
import me.jooie.minicafe.repository.OrderRepository;
import me.jooie.minicafe.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    public void createUser(String username, String password, String email, UserRole role){
        CafeUser cafeUser = new CafeUser();

        cafeUser.setUsername(username);
        cafeUser.setPassword(passwordEncoder.encode(password));
        cafeUser.setEmail(email);
        cafeUser.setRole(role);

        userRepository.save(cafeUser);
    }

    public CafeUser getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found"));
    }

    public CafeUser getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User Not Found"));
    }

    public void deleteUser(Long userId){
        CafeUser cafeUser = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        userRepository.delete(cafeUser);
    }

    public List<CustomerManageDto> getCustomerManageList(){
        List<CafeUser> users = userRepository.findAll();

        return users.stream().map(user-> {
                int orderCount = orderRepository.countByCafeUser(user);
                double totalSpent = orderRepository.findByCafeUser(user).stream()
                        .mapToDouble(CafeOrder::getTotalPrice).sum();

                CustomerManageDto customerManageDto = new CustomerManageDto();
                customerManageDto.setUserId(user.getUserId());
                customerManageDto.setUsername(user.getUsername());
                customerManageDto.setEmail(user.getEmail());
                customerManageDto.setCreatedAt(user.getCreatedAt());
                customerManageDto.setOrderNumber(orderCount);
                customerManageDto.setTotalPrice(totalSpent);
                customerManageDto.setActive(user.getActive());

                return customerManageDto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void toggleActivation(Long userId){
        CafeUser cafeUser = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found"));
        cafeUser.setActive(!cafeUser.getActive());
        userRepository.save(cafeUser);
    }

    public boolean existsAdmin(String userRole){

        return userRepository.existsByRole(userRole);
    }
}
