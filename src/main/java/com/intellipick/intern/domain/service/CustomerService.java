package com.intellipick.intern.domain.service;

import com.intellipick.intern.application.dto.CustomerDto;
import com.intellipick.intern.domain.model.AuthorityEntity;
import com.intellipick.intern.domain.model.Customer;
import com.intellipick.intern.domain.repository.CustomerRepository;
import com.intellipick.intern.domain.type.UserRole;
import com.intellipick.intern.presentaion.request.RequestSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerDto saveCustomer(RequestSignUpDto requestSignUpDto, String password) {

        Customer customer = Customer.create(requestSignUpDto.getUsername(), requestSignUpDto.getNickname(), requestSignUpDto.getPassword());

        AuthorityEntity authorityEntity = AuthorityEntity.create(UserRole.ROLE_USER,customer);

        customer.addAuthority(authorityEntity);

        customerRepository.save(customer);

        return CustomerDto.toDto(customer);
    }

}
