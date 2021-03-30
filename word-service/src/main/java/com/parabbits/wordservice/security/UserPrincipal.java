package com.parabbits.wordservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.Principal;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class UserPrincipal implements Principal {

    private Long id;
    private String name;

}

