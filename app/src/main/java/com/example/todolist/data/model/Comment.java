package com.example.todolist.data.model;

public class Comment {
    public String id, body, postID;
    public User user;

    public Comment(CommentFromDB comment)
    {
        id = comment._id;
        body = comment.body;
        postID = comment.postID;
        user = new User();
        user._id = comment.userID;
    }

    public Comment(String postID, User user, String body)
    {
        this.postID = postID;
        this.user = user;
        this.body = body;
    }

    public String toString()
    {
        return (user.username == null ? "Lorem ipsum" : user.username) + ": " + body;
    }
}
