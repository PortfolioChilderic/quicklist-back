package com.quicklist.quicklist.dto;

import com.quicklist.quicklist.domain.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;
    @NotNull
    // todo : exceptionHandler
    private String email;
    private Role role;
}
