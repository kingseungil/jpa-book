package com.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER", uniqueConstraints = {
  @UniqueConstraint(
    name = "NAME_AGE_UNIQUE",
    columnNames = {"NAME", "AGE"}
  )
})
public class Member extends BaseEntity {

    @Setter
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "NAME", nullable = false, length = 10) // 추가
    private String username;

    @Setter
    @Column(name = "AGE")
    private int age;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
