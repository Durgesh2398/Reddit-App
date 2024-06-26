package com.upgrad.reddit.service.business;


import com.upgrad.reddit.service.dao.CommonDao;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CommonBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonDao commonDao;

    /**
     * The method implements the business logic for userProfile endpoint.
     */
    public UserEntity getUser(String uuid, String authorization) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity !=null){
            ZonedDateTime logout = userAuthEntity.getLogoutAt();
            if(logout!=null){
                throw new AuthorizationFailedException("ATHR-002","User is signed out. Sign in first to get user details");
            }
            UserEntity userEntity= commonDao.getUserByUuid(uuid);
            if (userEntity==null){
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            }
            return userEntity;
        }
        throw new AuthorizationFailedException("ATHR-001","User has not signed in");

    }
}
