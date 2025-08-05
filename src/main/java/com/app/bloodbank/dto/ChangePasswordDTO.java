package com.app.bloodbank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {
    public Long userId;
    public String oldPassword;
    public String newPassword;
}
