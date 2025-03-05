package com.chronelab.riscc.entity.general;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_authority")
public class AuthorityEntity extends CommonEntity {

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "authorities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RoleEntity> roles;

    public AuthorityEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public AuthorityEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AuthorityEntity)) {
            return false;
        }
        AuthorityEntity that = (AuthorityEntity) o;
        return this == that || Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
