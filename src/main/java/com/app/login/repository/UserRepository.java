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

     /**
      * findOneByEmail
      *
      * @param email resetKey
      * @return Optional<User>
      *
      */
    Optional<User> findOneByEmail(String email);

     /**
      * findOneByLogin
      *
      * @param  login login
      * @return Optional<User>
      *
      */
    Optional<User> findOneByLogin(String login);

     /**
      * findOneWithAuthoritiesById
      *
      * @param  id id
      * @return User
      *
      */
    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

     /**
      * findOneWithAuthoritiesByLogin
      *
      * @param  login login
      * @return  Optional<User>
      *
      */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    /**
     * findAllByLoginNot
     * @param  pageable pageable
     * @param  login login
     * @return Page<User>
     */
    Page<User> findAllByLoginNot(Pageable pageable, String login);

     /**
      * findAllByIpAddressAndCreatedDateBetween
      * @param  ipAddress ipAddress
      * @param  startDate startDate
      * @param currentDate currentDate
      * @return List<User>
      */
    List<User> findAllByIpAddressAndCreatedDateBetween(String ipAddress, Instant startDate, Instant currentDate);
}
