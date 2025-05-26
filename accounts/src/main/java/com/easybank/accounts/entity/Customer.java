package com.easybank.accounts.entity;

import com.easybank.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="customer_id")
    private Long customerId;

    private String name;

    private String email;
    @Column(name="mobile_number")
    private String mobileNumber;
}
