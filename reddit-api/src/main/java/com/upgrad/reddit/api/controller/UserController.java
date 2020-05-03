package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.SigninResponse;
import com.upgrad.reddit.api.model.SignoutResponse;
import com.upgrad.reddit.api.model.SignupUserRequest;
import com.upgrad.reddit.api.model.SignupUserResponse;
import com.upgrad.reddit.service.business.UserBusinessService;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthenticationFailedException;
import com.upgrad.reddit.service.exception.SignOutRestrictedException;
import com.upgrad.reddit.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    /**
     * A controller method for user signup.
     *
     * @param signupUserRequest - This argument contains all the attributes required to store user details in the database.
     * @return - ResponseEntity<SignupUserResponse> type object along with Http status CREATED.
     * @throws SignUpRestrictedException
     */
    @PostMapping("/signupUser")
    public ResponseEntity<SignupUserResponse> signupUser(@RequestBody SignupUserRequest signupUserRequest)
            throws SignUpRestrictedException{
        UserEntity userEntity = new UserEntity();
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setUserName(signupUserRequest.getUserName());
        UserEntity newUser = userBusinessService.signup(userEntity);
        return new ResponseEntity<SignupUserResponse>(new SignupUserResponse()
                .id(newUser.getUuid())
                .status(newUser.getUserName())
                , HttpStatus.OK);

    }

    /**
     * A controller method for user authentication.
     *
     * @param username - A field in the request header which contains the user credentials as Basic authentication.
     * @param password - password
     * @return - ResponseEntity<SigninResponse> type object along with Http status OK.
     * @throws AuthenticationFailedException
     */
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody String username,@RequestBody String password)
            throws AuthenticationFailedException{
        UserAuthEntity userAuthEntity =userBusinessService.authenticate(username,password);
        return new ResponseEntity<>(new SigninResponse().id(userAuthEntity.getUuid()),HttpStatus.OK);
    }

    /**
     * A controller method for user signout.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<SignoutResponse> type object along with Http status OK.
     * @throws SignOutRestrictedException
     */
    @PostMapping("/signout")
    public ResponseEntity<SignoutResponse> signout(@RequestHeader String authorization)
            throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity=userBusinessService.signout(authorization);
        return new ResponseEntity<>(new SignoutResponse().id(userAuthEntity.getUuid()),HttpStatus.OK);
    }

}
