package com.app.login.service;

import com.app.login.domain.User;
import com.app.login.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2018/3/27 0027.
 * @author Megadotnet
 * @date 2018-03-27
 */
public interface IUserService {
    /**
     *  activateRegistration
     * @param key activation key
     * @return Optional<User>
     */
    Optional<User> activateRegistration(String key);

    Optional<User> completePasswordReset(String newPassword, String key);

    Optional<User> requestPasswordReset(String mail);

    User createUser(String login, String password, String firstName, String lastName, String email, String imageUrl, String langKey, Instant createdDate, String ipAddress);

    User createUser(UserDTO userDTO);

    void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl);

    Optional<UserDTO> updateUser(UserDTO userDTO);

    void deleteUser(String login);

    void changePassword(String password);

    @Transactional(readOnly = true)
    Page<UserDTO> getAllManagedUsers(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<User> getUserWithAuthoritiesByLogin(String login);

    @Transactional(readOnly = true)
    User getUserWithAuthorities(Long id);

    @Transactional(readOnly = true)
    User getUserWithAuthorities();

    List<String> getAuthorities();
}
