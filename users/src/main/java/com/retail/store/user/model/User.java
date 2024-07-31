package com.retail.store.user.model;

import com.retail.store.user.constant.UserType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document("users")
public class User {

    @Transient
    public static final String SEQUENCE = "user_sequence";

    @Id
    private long id;
    private String name;
    private UserType userType;
    private Instant createdDate;
    private Instant modifiedDate;
}