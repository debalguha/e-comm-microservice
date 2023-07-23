package com.ecommerce.usermgmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserModel {
    Long id;
    String name;
    String email;
    @EqualsAndHashCode.Include
    String mobile;
}
