package com.app.login.service.dto;

import com.app.login.config.Constants;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.validation.constraints.*;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Schema
public class UserDTO {

    private Long id;

    @NotNull(message = "login name should not be null")
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Schema(name = "firstName")
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated=false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail()
                , user.isActivated(), user.getImageUrl(), user.getLangKey(), user.getAuthorities()
            .stream()
            .map(Authority::getName)
            .collect(Collectors.toSet()));
    }

    public UserDTO(Long id, String login, String firstName, String lastName, String email, boolean activated, String imageUrl, String langKey, Set<String> authorities) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", imageUrl='" + imageUrl + '\'' + ", activated=" + activated + ", langKey='" + langKey + '\''
            + ", authorities=" + authorities + "}";
    }
}
