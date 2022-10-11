package com.rds.testrds.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "T_ORDER")
@Getter
@Setter
/*@ToString(exclude = "user")*/
public class OrderEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String orderName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entity_id")
    @JsonIgnore
    private UserEntity user;
}
