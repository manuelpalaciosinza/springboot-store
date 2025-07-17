package com.codewithmosh.store.users;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class UserDto {
    Long id;
    String name;
    String email;
}
