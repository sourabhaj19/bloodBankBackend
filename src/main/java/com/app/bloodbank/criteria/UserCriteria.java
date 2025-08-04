package com.app.bloodbank.criteria;

import java.io.Serializable;
import com.app.bloodbank.model.Users;
import tech.jhipster.service.filter.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCriteria implements Serializable {

    private LongFilter id;
    private StringFilter fullName;
    private StringFilter email;
    private StringFilter phone;
    private StringFilter phoneprefix;
    private IntegerFilter age;
    private LocalDateFilter dob;
    private StringFilter gender;
    private StringFilter state;
    private StringFilter country;
    private StringFilter city;
    private StringFilter address;
    private StringFilter bloodGroup;
    private DoubleFilter latitude;
    private DoubleFilter longitude;
    private LocalDateFilter lastDonationDate;
    private BooleanFilter isAvailable;
    private BooleanFilter isActive;

    // Enum filtering
    private RoleFilter role;

    public static class RoleFilter extends Filter<Users.Role> {
    }
}
