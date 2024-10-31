package com.app.login.service;

import com.app.TestBase;
import com.app.login.common.utils.ValidationFacade;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.AuthorityRepository;
import com.app.login.repository.UserRepository;
import com.app.login.service.impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.mapper.UserMapper;
import com.app.login.web.rest.vm.ManagedUserVM;
import com.github.javafaker.Faker;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.*;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UserServiceMockitoTest
 * @author  megadotnet
 * @date  2018/3/26 0026.
 */
public class UserServiceMockitoTest extends TestBase {

    @Mock
    private UserRepository  userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private  AuthorityRepository authorityRepository;

    @Mock
    private IMailService mailService;

    private UserMapper userMapper=new UserMapper();

    private ValidationFacade validationFacade=new ValidationFacade(Validation.buildDefaultValidatorFactory().getValidator());

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private static Validator validator;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        ArrayList<User> users=  new ArrayList<>();
        User mockuser= createUser();

        Optional<User> userOptional=Optional.of(mockuser);
        users.add(mockuser);

        Optional<Authority> defaultautthoriy=mockuser.getAuthorities().stream().findFirst();
        List<Authority> authorityList= Lists.newArrayList();
        authorityList.addAll(mockuser.getAuthorities());
        when(userRepository.findOne(Mockito.any())).thenReturn(userOptional);
        when(authorityRepository.findOne(Mockito.any())).thenReturn(defaultautthoriy);
        when(authorityRepository.findAll()).thenReturn(authorityList);
        when(userRepository.findOneWithAuthoritiesByLogin(Mockito.anyString())).thenReturn(userOptional);

        userServiceImpl =new UserServiceImpl(userRepository,passwordEncoder, authorityRepository,mailService,validationFacade,userMapper);

        //ValidatorFactory
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(mockuser.getLogin());
    }

    @Test
    public void updateUserTest()
    {
        //assume
        UserDTO userDTO=new UserMapper().userToUserDTO(createUser());

        //act
        Optional<UserDTO> updateUser = userServiceImpl.updateUser(userDTO);

        //assert
        assertNotNull(updateUser);
        assertTrue(updateUser.isPresent());
        UserDTO userDTO1=updateUser.get();
        assertNotNull(userDTO1);
        assertEquals(createAuthorities().size(), userDTO1.getAuthorities().size());
        assertFalse(userDTO1.getAuthorities().isEmpty());
    }

    @Test
    public void emailRegexValidatorTest()  {
        //assume
        Faker faker =new Faker();
        User user=createUser();
        user.setEmail(faker.address().firstName());
        user.setLogin("  ");
        UserDTO userDTO=new UserMapper().userToUserDTO(user);

        //act
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        //assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.size()==3);
    }

    @Test
    public void createUserTest() {
        User user=userServiceImpl.createUser(createUserDto());
        assertNotNull(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createInvalidUserTest() {
        User user=userServiceImpl.createUser(new UserDTO());
        assertNotNull(user);
    }

    @Test
    public void changePassword()
    {
        //User user=userServiceImpl.createUser(createUserDto());
        //assertNotNull(user);
        userServiceImpl.changePassword("batman");
       // User userdb=userServiceImpl.getUserWithAuthorities(user.getId());
       // assertEquals(userdb.getPassword(),"newpassoword");
    }

    @Test
    public void getAuthorities()
    {

        List<String> stringList= userServiceImpl.getAuthorities();
        assertNotNull(stringList);
        assertTrue(stringList.size()>0);
    }

    public void registerUserAccount()
    {
        //userServiceImpl.geta
    }

    @Test
    public void prepareMockApplicationUser() {
        User applicationUser = mock(User.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
    }

    @Test
    public void getUserWithAuthorities()
    {
        User userfromDb= userServiceImpl.getUserWithAuthorities();
        assertNotNull(userfromDb);
        assertEquals(userfromDb.getLogin(),createUser().getLogin());
    }

    @Test
    public void SaveUserAccountTest()
    {
        userServiceImpl.saveUserAccount(new UserDTO());
    }

    @Test
    public void createUserInvalidmanagedUserVM()
    {
        ManagedUserVM managedUserVM=new ManagedUserVM();
        userServiceImpl.createUser(managedUserVM, Instant.now(), "127.0.0.1");
    }


    private UserDTO createUserDto()
    {
        return  new UserMapper().userToUserDTO(createUser());
    }




}
