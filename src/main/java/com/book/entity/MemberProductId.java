package com.book.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MemberProductId implements Serializable {

    private String member;
    private String product;

}
