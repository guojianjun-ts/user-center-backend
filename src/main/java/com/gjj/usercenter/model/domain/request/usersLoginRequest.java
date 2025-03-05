package com.gjj.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;


@Data
public class usersLoginRequest implements Serializable {

    private static final long serialVersionUID = -6453391909948250420L;

    private String userAccount;
    private String userPassword;


}