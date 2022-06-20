package com.marti.humanresbackend.models.DTO;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long Id;
    private String email;
    private Role role;

    public UserDTO(User u){
        this.Id = u.getId();
        this.email = u.getEmail();
        this.role = u.getRole();
    }
}
