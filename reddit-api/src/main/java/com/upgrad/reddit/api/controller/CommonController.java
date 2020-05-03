package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.UserDetailsResponse;
import com.upgrad.reddit.service.business.CommonBusinessService;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;
    /**
     * A controller method to fetch the details of other user.
     *
     * @param userId        - The uuid of the user whose details are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<UserDetailsResponse> type object along with Http status OK.
     * @throws UserNotFoundException
     * @throws AuthorizationFailedException
     */

    @GetMapping("/fetchUserDetails")
    public ResponseEntity<UserDetailsResponse> fetchUserDetails(@RequestBody String userId,@RequestHeader String authorization)
            throws UserNotFoundException,AuthorizationFailedException {
        UserEntity userEntity = commonBusinessService.getUser(userId,authorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
                .userName(userEntity.getUserName())
                .aboutMe(userEntity.getAboutMe())
                .contactNumber(userEntity.getContactNumber())
                .country(userEntity.getCountry())
                .dob(userEntity.getDob())
                .emailAddress(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse,HttpStatus.OK);
    }
}
