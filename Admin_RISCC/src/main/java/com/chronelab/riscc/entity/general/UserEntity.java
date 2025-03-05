package com.chronelab.riscc.entity.general;

import com.chronelab.riscc.entity.AnswerEntity;
import com.chronelab.riscc.entity.FeedbackEntity;
import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.NotificationEntity;
import com.chronelab.riscc.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email_address", "mobile_number"})
        }
)
public class UserEntity extends CommonEntity {

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM ('MALE', 'FEMALE', 'OTHER')")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @Column(name = "last_password_reset_date", nullable = false)
    private LocalDateTime lastPasswordResetDate;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_deadline")
    private LocalDateTime verificationCodeDeadline;

    @Column(name = "mobile_number_verified")
    private Boolean mobileNumberVerified;

    @Column(name = "email_address_verified")
    private Boolean emailAddressVerified;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private DeviceEntity device;

    @ManyToMany(mappedBy = "users")
    private List<NotificationEntity> notifications;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<FeedbackEntity> feedbacks;

    @OneToMany(mappedBy = "user")
    private List<AnswerEntity> answers;

    @ManyToMany(mappedBy = "users")
    private List<GroupEntity> groups;

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserEntity setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public UserEntity setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserEntity setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public UserEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UserEntity setLastPasswordResetDate(LocalDateTime lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
        return this;
    }

    public UserEntity setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public UserEntity setVerificationCodeDeadline(LocalDateTime verificationCodeDeadline) {
        this.verificationCodeDeadline = verificationCodeDeadline;
        return this;
    }

    public UserEntity setMobileNumberVerified(Boolean mobileNumberVerified) {
        this.mobileNumberVerified = mobileNumberVerified;
        return this;
    }

    public UserEntity setEmailAddressVerified(Boolean emailAddressVerified) {
        this.emailAddressVerified = emailAddressVerified;
        return this;
    }

    public UserEntity setRole(RoleEntity role) {
        this.role = role;
        return this;
    }

    public UserEntity setAddress(AddressEntity address) {
        this.address = address;
        return this;
    }

    public UserEntity setDevice(DeviceEntity device) {
        this.device = device;
        return this;
    }

    public UserEntity setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
        return this;
    }

    public UserEntity setFeedbacks(List<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
        return this;
    }

    public UserEntity setAnswers(List<AnswerEntity> answers) {
        this.answers = answers;
        return this;
    }

    public UserEntity setGroups(List<GroupEntity> groups) {
        this.groups = groups;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserEntity)) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return this == that || Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
