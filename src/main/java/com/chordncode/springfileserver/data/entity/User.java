package com.chordncode.springfileserver.data.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name="user_id")
    private String userId;

    @Column(name="user_pw")
    private String userPw;

    @OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    List<Auth> authList;

}
