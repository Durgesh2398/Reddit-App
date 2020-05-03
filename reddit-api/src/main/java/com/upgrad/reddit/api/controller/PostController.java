package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.PostBusinessService;
import com.upgrad.reddit.service.entity.PostEntity;
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

@RestController
@RequestMapping("/post")
public class PostController  {

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

    @PostMapping("/createPost")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostResponse postRequest,@RequestHeader String authorization)
            throws AuthorizationFailedException {
        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postRequest.getStatus());
        PostEntity newPostEntity = postBusinessService.createPost(postEntity,authorization);

        PostResponse postResponse = new PostResponse().status(newPostEntity.getContent()).id(newPostEntity.getUuid());
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);

    }

    /**
     * A controller method to fetch all the posts from the database.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     */

    @GetMapping("/getAllPosts")

    public ResponseEntity<List<PostDetailsResponse>> getAllPosts(@RequestHeader String authorization)
            throws AuthorizationFailedException {
        List<PostEntity> postList = postBusinessService.getPosts(authorization).getResultList();
        List<PostDetailsResponse> postDetailsResponses = new ArrayList<>();
        for(PostEntity postEntity : postList) {
            postDetailsResponses.add(new PostDetailsResponse()
                    .content(postEntity.getContent())
                    .id(postEntity.getUuid()));
        }

        return new ResponseEntity<>(postDetailsResponses,HttpStatus.OK);
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

    @PostMapping("/editPostContent")
    public ResponseEntity<PostEditResponse> editPostContent(@RequestBody PostEditRequest postEditRequest,@RequestBody String postId,@RequestHeader String authorization)
            throws AuthorizationFailedException,InvalidPostException {
        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postEditRequest.getContent());
        PostEntity editPostEntity =postBusinessService.editPostContent(postEntity,postId,authorization);

        return new ResponseEntity<>(new PostEditResponse()
                .id(editPostEntity.getUuid())
                .status(editPostEntity.getContent()),HttpStatus.OK);

    }

    /**
     * A controller method to delete the post in the database.
     *
     * @param postId    - The uuid of the post to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

    @PostMapping("/deletePost")
    public ResponseEntity<PostDeleteResponse> deletePost(@RequestBody String postId,@RequestHeader String authorization)
            throws AuthorizationFailedException,InvalidPostException {
        PostEntity postEntity=postBusinessService.deletePost(postId,authorization);
        return new ResponseEntity<>(new PostDeleteResponse().id(postId),HttpStatus.OK);

    }

    @GetMapping("/getAllPostsByUser")
    public ResponseEntity<List<PostDetailsResponse>> getAllPostsByUser(@RequestBody String userId,@RequestHeader String authorization)
            throws AuthorizationFailedException,UserNotFoundException
    {
        List<PostEntity> postEntities=postBusinessService.getPostsByUser(userId,authorization).getResultList();
        List<PostDetailsResponse> postDetailsResponses = new ArrayList<>();

        for(PostEntity postEntity : postEntities) {
            postDetailsResponses.add(new PostDetailsResponse().id(postEntity.getUuid())
                    .content(postEntity.getContent()));
        }

        return new ResponseEntity<>(postDetailsResponses,HttpStatus.OK);
    }

}
