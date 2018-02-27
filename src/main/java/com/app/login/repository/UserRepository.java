package com.app.login.repository;

import com.app.login.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


 /**
 * UserRepository
 * @author megadotnet
 * @date 2018-2-27
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * findOneByActivationKey
     *
     * @param activationKey rule id
     * @return Result<User>
     *
     */
    Optional<User> findOneByActivationKey(String activationKey);

    /**
     * findOneByResetKey
     *
     * @param resetKey resetKey
     * @return Result<User>
     *
     */
    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    List<User> findAllByIpAddressAndCreatedDateBetween(String ipAddress, Instant startDate, Instant currentDate);
}
