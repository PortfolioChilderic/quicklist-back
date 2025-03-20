package com.quicklist.quicklist.dto;

import com.quicklist.quicklist.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String name;
    private String email;
    private Role role;
}
