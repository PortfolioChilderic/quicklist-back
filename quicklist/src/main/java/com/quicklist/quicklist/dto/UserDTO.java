package com.quicklist.quicklist.dto;

import com.quicklist.quicklist.domain.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String email;
    private Role role;
}
