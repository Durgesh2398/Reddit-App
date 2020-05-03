package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.PostBusinessService;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostBusinessService postBusinessService;

    /**
     * A controller method to create a post.
     *
     * @param postRequest - This argument contains all the attributes required to store post details in the database.
     * @param authorization   - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostResponse> type object along with Http status CREATED.
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.POST,path = "post/create/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPost(final PostRequest postRequest, @RequestHeader final String authorization) throws  AuthorizationFailedException{
        UserAuthEntity userAuthEntity=postBusinessService.createPost(postRequest,authorization);
        UserEntity userEntity=userAuthEntity.getUser();
        PostEntity postEntity1=new PostEntity();
        postEntity1.setUser(userAuthEntity.getUser());
        postEntity1.setUuid(UUID.randomUUID().toString());
        postEntity1.setContent(postRequest.getContent());
        final ZonedDateTime now=ZonedDateTime.now();
        postEntity1.setDate(now);
        PostEntity createPost=postBusinessService.createPost(postEntity1);
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.CREATED);
    }

    /**
     * A controller method to fetch all the posts from the database.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.GET,path = "/post/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllPost(@RequestHeader final String authorization) throws  AuthorizationFailedException{
        UserAuthEntity userAuthEntity= (UserAuthEntity) postBusinessService.getPosts(authorization);
        List<PostEntity> postEntityList=postBusinessService.getPosts();

    }

    /**
     * A controller method to edit the post in the database.
     *
     * @param postEditRequest - This argument contains all the attributes required to edit the post details in the database.
     * @param postId          - The uuid of the post to be edited in the database.
     * @param authorization       - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostEditResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

    /**
     * A controller method to delete the post in the database.
     *
     * @param postId    - The uuid of the post to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

    /**
     * A controller method to fetch all the posts posted by a specific user.
     *
     * @param userId        - The uuid of the user whose posts are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */

}
