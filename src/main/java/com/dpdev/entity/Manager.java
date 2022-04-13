package com.dpdev.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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
public class Manager extends User {

    private String projectName;

    @Builder
    public Manager(Long id, String username, PersonalInfo personalInfo, String info, Role role,
                   Company company, Profile profile, List<UserChat> userChats, String projectName) {
        super(id, username, personalInfo, info, role, company, profile, userChats);
        this.projectName = projectName;
    }
}
