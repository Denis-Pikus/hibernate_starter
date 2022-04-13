package com.dpdev.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Programmer extends User {

    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder
    public Programmer(Long id, String username, PersonalInfo personalInfo, String info, Role role,
                      Company company, Profile profile, List<UserChat> userChats,
                      Language language) {
        super(id, username, personalInfo, info, role, company, profile, userChats);
        this.language = language;
    }
}
