package com.app.login.service.Impl;

import com.app.login.config.Constants;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.AuthorityRepository;
import com.app.login.repository.UserRepository;
import com.app.login.security.AuthoritiesConstants;
import com.app.login.security.SecurityUtils;
import com.app.login.service.IMailService;
import com.app.login.service.IUserService;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.util.RandomUtil;
import com.app.login.web.rest.util.HeaderUtil;
import com.app.login.web.rest.vm.ManagedUserVM;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 * @author Megadotnet
 * @date  2018-01-01
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    /**
     * mailService
     */
    private final IMailService mailService;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
                           IMailService mailServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.mailService=mailServiceImpl;
    }

    /**
     *
     * @param managedUserVM managedUserVM
     * @param textPlainHeaders textPlainHeaders
     * @param ipAddress ipAddress
     * @param mailService mailService
     * @return
     */
    @Override
    public ResponseEntity registerUserAccount(ManagedUserVM managedUserVM
            , HttpHeaders textPlainHeaders, String ipAddress) {
        Instant instant = Instant.now();

        List<User> usersByIpAndRegistrationDate = userRepository.findAllByIpAddressAndCreatedDateBetween(ipAddress, instant.minus(1, ChronoUnit.DAYS), instant);
        if (!usersByIpAndRegistrationDate.isEmpty()) {
            if (usersByIpAndRegistrationDate.size() == Constants.DOS_MAX_REGISTRATION_ALLOWED) {
                return new ResponseEntity<>("You cannot register", textPlainHeaders, HttpStatus.BAD_REQUEST);
            }
        }

        return userRepository.findOneByLogin(managedUserVM.getLogin()
                .toLowerCase())
                .map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> userRepository.findOneByEmail(managedUserVM.getEmail())
                        .map(user -> new ResponseEntity<>("email address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                        .orElseGet(() -> {
                            User user = createUser(managedUserVM.getLogin(), managedUserVM.getPassword(), managedUserVM.getFirstName(), managedUserVM.getLastName(), managedUserVM.getEmail()
                                    .toLowerCase(), managedUserVM.getImageUrl(), managedUserVM.getLangKey(), Instant.now(), ipAddress);

                            mailService.sendActivationEmail(user);
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        }));
    }

    /**
     *
     * @param userDTO userDTO
     * @return
     */
    @Override
    public ResponseEntity saveUserAccount(UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get()
                .getLogin()
                .equalsIgnoreCase(userLogin))) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
                    .body(null);
        }
        return userRepository.findOneByLogin(userLogin)
                .map(u -> {
                    updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(), userDTO.getImageUrl());
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     *  activateRegistration
     * @param key activation key
     * @return Optional<User>
     */
    @Override
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    /**
     * completePasswordReset
     * @param newPassword new password
     * @param key activation key
     * @return Optional<User>
     */
    @Override
    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate()
                .isAfter(Instant.now()
                    .minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            });
    }

    /**
     * requestPasswordReset
     * @param mail email string
     * @return Optional<User>
     */
    @Override
    public ResponseEntity requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            }).map(user -> {
                    mailService.sendPasswordResetMail(user);
                    return new ResponseEntity<>("email was sent", HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>("email address not registered", HttpStatus.BAD_REQUEST));
    }

    /**
     * createUser
     * @param login login name
     * @param password password
     * @param firstName firstname
     * @param lastName lastname
     * @param email email
     * @param imageUrl imageUrl
     * @param langKey lang key
     * @param createdDate create date
     * @param ipAddress ip address
     * @return User
     */
    @Override
    public User createUser(String login, String password, String firstName, String lastName, String email, String imageUrl, String langKey, Instant createdDate, String ipAddress) {

        User newUser = new User();
        Authority authorityquery = new Authority();
        authorityquery.setName(AuthoritiesConstants.USER);
        Example<Authority> example = Example.of(authorityquery);
        Optional<Authority> authority = authorityRepository.findOne(example);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setCreatedDate(Instant.now());
        newUser.setImageUrl(imageUrl);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority.get());
        newUser.setAuthorities(authorities);

        newUser.setCreatedDate(createdDate);
        newUser.setIpAddress(ipAddress);

        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            // default language
            user.setLangKey(Constants.DEFAULT_LANGUAGE_KEY);
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userDTO.getAuthorities()
                .forEach(authority -> {

                    Authority authorityquery = new Authority();
                    authorityquery.setName(authority);
                    Example<Authority> exampleAuthority = Example.of(authorityquery);

                    authorities.add(
                        authorityRepository.findOne(exampleAuthority).get()

                ); });
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    @Override
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    @Override
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        //https://www.jianshu.com/p/9936ba98da5a
        //https://github.com/spring-projects/spring-data-examples/tree/master/jpa/query-by-example/src/test/java/example/springdata/jpa/querybyexample
        User userquery=new User();
        userquery.setId(userDTO.getId());
        Example<User> exampleUserDTO = Example.of(userquery);
        return userRepository.findOne(exampleUserDTO)
          .map(user -> {
              user.setLogin(userDTO.getLogin());
              user.setFirstName(userDTO.getFirstName());
              user.setLastName(userDTO.getLastName());
              user.setEmail(userDTO.getEmail());
              user.setImageUrl(userDTO.getImageUrl());
              user.setActivated(userDTO.isActivated());
              user.setLangKey(userDTO.getLangKey());
              Set<Authority> managedAuthorities = user.getAuthorities();
              managedAuthorities.clear();

              userDTO.getAuthorities()
                  .stream()
                      .map(e -> {
                          Authority authorityquery = new Authority();
                          authorityquery.setName(e);
                          Example<Authority> exampleAuthority = Example.of(authorityquery);
                          return authorityRepository.findOne(exampleAuthority).get();
                          })
                  .forEach(managedAuthorities::add);
              log.debug("Changed Information for User: {}", user);
              return user;
          })
          .map(UserDTO::new);

    }

    /**
     * deleteUser
     * @param login login string
     */
    @Override
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * changePassword
     * @param password password
     */
    @Override
    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .ifPresent(user -> {
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
    }

    /**
     * getAllManagedUsers
     * @param pageable pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER)
                .map(UserDTO::new);
    }

    /**
     * getUserWithAuthoritiesByLogin
     * @param login login
     * @return Optional<User>
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     *  getUserWithAuthorities
     * @param id userid
     * @return User
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    /**
     * getUserWithAuthorities
     * @return User
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin())
            .orElse(null);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    // @Scheduled(cron = "0 0 1 * * ?")
    // public void removeNotActivatedUsers() {
    // List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
    // for (User user : users) {
    // log.debug("Deleting not activated user {}", user.getLogin());
    // userRepository.delete(user);
    // }
    // }

    /**
     * getAuthorities
     * @return a list of all the authorities
     */
    @Override
    public List<String> getAuthorities() {
        return authorityRepository.findAll()
            .stream()
            .map(Authority::getName)
            .collect(Collectors.toList());
    }
}
