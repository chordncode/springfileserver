package com.chordncode.springfileserver.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.chordncode.springfileserver.data.compositeKey.AuthKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth")
@IdClass(AuthKey.class)
public class Auth {
    
    @Id
    @Column(name="user_id")
    private String userId;

    @Id
    @Column(name="user_auth")
    private String userAuth;

    @ManyToOne
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User user;

}
