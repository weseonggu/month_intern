package com.intellipick.intern.application.dto;

import com.intellipick.intern.domain.model.AuthorityEntity;
import com.intellipick.intern.domain.model.Customer;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class CustomerDto {
    private String username;
    private String nickname;
    private Set<RoleDto> authorities;

    public static CustomerDto toDto(Customer customer) {
        Set<RoleDto> roleDtos = new HashSet<>();
        for(AuthorityEntity authority : customer.getAuthorities()){
            roleDtos.add(new RoleDto(authority.getRole().getValue()));
        }
        return CustomerDto.builder().username(customer.getUsername()).nickname(customer.getNickname())
                .authorities(roleDtos).build();
    }

}
