package com.book.entity;

import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private Date createdDate;
    private Date lastModifiedDate;
}
