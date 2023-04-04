package io.github.jbgwese.AuthenticationApp.token;

import io.github.jbgwese.AuthenticationApp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {
    //mytoken class
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
