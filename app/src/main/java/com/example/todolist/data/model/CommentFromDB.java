package com.example.todolist.data.model;

public class CommentFromDB {
    public String body, postID, _id, userID;
    public CommentFromDB(){};
    public CommentFromDB(Comment comment)
    {
        body = comment.body;
        userID = comment.user._id;
        postID = comment.postID;
    }
}
