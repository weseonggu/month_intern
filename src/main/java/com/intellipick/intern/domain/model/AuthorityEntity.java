package com.intellipick.intern.domain.model;

import com.intellipick.intern.domain.type.UserRole;
import jakarta.persistence.*;
import lombok.*;

/**
 *  DB에 저장되는 데이터로 권한 관련 정보가 저장 되어있다. 제야조건으로UserEntity랑 연결 되어 있음
 */
@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_authority")
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authority_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    ////////////////////////////////////////////////////////////////////////

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="auth_id")
    private Customer customer;
    
    /////////////////////////////////////////////////////////////////////////

    public static AuthorityEntity create(UserRole role, Customer customer){
        return AuthorityEntity.builder().role(role).customer(customer).build();
    }
}
