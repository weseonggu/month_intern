package com.intellipick.intern.security;

import com.intellipick.intern.domain.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForContext {

    private Long id;
    private String username;
    private Set<UserRole> authoritiesSet;
}
