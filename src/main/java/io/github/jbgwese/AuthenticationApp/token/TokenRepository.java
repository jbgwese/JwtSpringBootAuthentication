package io.github.jbgwese.AuthenticationApp.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query(value = "select t from Token  t" +
            "inner join User u " +
            "on u.id = t.user.id" +
            " where u.id=:userId  " +
            "and (t.expired = false or t.revoked=false )",nativeQuery = true)
    List<Token>findAllTokenByUser(long userId);
    Optional<Token> findByToken(String token);
}
