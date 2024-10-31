package com.app.login.web.rest;

import com.app.login.service.IUserService;
import com.app.login.service.dto.UserDTO;
import com.app.login.web.rest.util.ResourceNotFoundException;
import com.app.login.web.rest.vm.KeyAndPasswordVM;
import com.app.login.web.rest.vm.ManagedUserVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 * @author Administrator
 * @date 2018-4-29
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "UserAccount",description = "User Account API")
public class UserAccountController {

    /**
     * userServiceImpl
     */
    private final IUserService userServiceImpl;

    /**
     * UserAccountController
     * @param userServiceImpl userService
     */
    public UserAccountController(IUserService userServiceImpl) {

        this.userServiceImpl = userServiceImpl;
    }

    /**
     * POST /register : register the user.
     *
     * @param managedUserVM
     *            the managed user View Model
     * @param request
     * @return the ResponseEntity with status 201 (Created) if the user is
     *         registered or 400 (Bad Request) if the login or email is already
     *         in use
     */

    @Operation(summary = "registerAccount", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagedUserVM.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(path = "/register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) {

        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

        log.info("getting remote ip = " + request.getRemoteAddr());
        String ipAddress = request.getRemoteAddr();

        return userServiceImpl.registerUserAccount(managedUserVM, textPlainHeaders, ipAddress);
    }


    /**
     * GET /activate : activate the registered user.
     *
     * @param key the activation key
     *
     * @return the ResponseEntity with status 200 (OK) and the activated user in
     *         body, or status 500 (Internal Server Error) if the user couldn't
     *         be activated
     */

    @Operation(summary = "activateAccount", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userServiceImpl.activateRegistration(key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET /authenticate : check if the user is authenticated, and return its
     * login.
     *
     * @param request
     *            the HTTP request
     * @return the login if the user is authenticated
     */

    @Operation(summary = "isAuthenticated", responses = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in
     *         body, or ResourceNotFoundException if the user couldn't
     *         be returned
     */
    @Operation(summary = "getAccount", responses = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/account")
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userServiceImpl.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("No user found with current login name"));
    }

    /**
     * POST /account : update the current user information.
     *
     * @param userDTO
     *            the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad
     *         Request) or 500 (Internal Server Error) if the user couldn't be
     *         updated
     */

    @Operation(summary = "saveAccount", responses = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/account")
    public ResponseEntity saveAccount(@Valid @RequestBody UserDTO userDTO) {
        return  userServiceImpl.saveUserAccount(userDTO);
    }


    /**
     * POST /account/change_password : changes the current user's password
     *
     * @param password
     *            the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad
     *         Request) if the new password is not strong enough
     */

    @Operation(summary = "changePassword", responses = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(path = "/account/change_password", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity changePassword(@RequestBody String password) {
        return userServiceImpl.changePassword(password);
    }

    /**
     * POST /account/reset_password/init : Send an email to reset the password
     * of the user
     *
     * @param mail
     *            the mail of the user
     * @return the ResponseEntity with status 200 (OK) if the email was sent, or
     *         status 400 (Bad Request) if the email address is not registered
     */

    @Operation(summary = "requestPasswordReset", responses = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(path = "/account/reset_password/init", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity requestPasswordReset(@RequestBody String mail) {
        return userServiceImpl.requestPasswordReset(mail);

    }

    /**
     * POST /account/reset_password/finish : Finish to reset the password of the
     * user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been
     *         reset, or status 400 (Bad Request) or 500 (Internal Server Error)
     *         if the password could not be reset
     */

    @Operation(summary = "finishPasswordReset", responses = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(path = "/account/reset_password/finish", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> finishPasswordReset(@Valid @RequestBody KeyAndPasswordVM keyAndPassword) {
        return userServiceImpl.completePasswordReset(keyAndPassword)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>("Incorrect key or not found.",HttpStatus.INTERNAL_SERVER_ERROR));
    }


}
