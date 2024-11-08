package com.intellipick.intern.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name="p_customer", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="auth_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    ///////////////////////////////////////////////////////////////////////////

    @OneToMany(mappedBy="customer",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<AuthorityEntity> authorities =  new HashSet<>();

    ////////////////////////////////////////////////////////////////////////////

    public static Customer create(String username, String nickname, String password) {
        return Customer.builder().username(username).nickname(nickname).password(password).build();
    }

    public void addAuthority(AuthorityEntity authority){
        authorities.add(authority);
    }
}
