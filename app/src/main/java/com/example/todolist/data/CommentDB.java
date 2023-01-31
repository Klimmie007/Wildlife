package com.example.todolist.data;

import android.util.Log;

import com.example.todolist.data.model.BaseGetCall;
import com.example.todolist.data.model.BaseInsertCall;
import com.example.todolist.data.model.Comment;
import com.example.todolist.data.model.CommentFromDB;
import com.example.todolist.data.model.InsertData;
import com.example.todolist.data.model.RetrofitSingleton;
import com.example.todolist.data.model.TemplateResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDB {
    private static List<Comment> comments = new ArrayList<>();
    private static int added;

    public static void sendComment(Comment comment, MyCallback callback)
    {
        added = comments.size();
        comments.add(comment);
        CommentFromDB toSend = new CommentFromDB(comment);
        Call<InsertData> sendCommentCall = RetrofitSingleton.getService().sendComment(new BaseInsertCall<CommentFromDB>(
                "Comments", toSend));

        sendCommentCall.enqueue(new Callback<InsertData>() {
            @Override
            public void onResponse(Call<InsertData> call, Response<InsertData> response) {
                comments.get(added).id = response.body().insertedId;
                added = -1;
                callback.Callback(response.body().insertedId);
            }

            @Override
            public void onFailure(Call<InsertData> call, Throwable t) {
                Log.e("Comments", t.getMessage());
            }
        });
    }

    public static void getComments(String postID, MyCallback callback)
    {
        if(!comments.isEmpty())
        {
            callback.Callback(comments);
            return;
        }

        CommentFromDB filter = new CommentFromDB();
        filter.postID = postID;

        Call<TemplateResult<CommentFromDB>> commentCall = RetrofitSingleton.getService().getComments(
                new BaseGetCall<CommentFromDB>("Comments", filter));

        commentCall.enqueue(new Callback<TemplateResult<CommentFromDB>>() {
            @Override
            public void onResponse(Call<TemplateResult<CommentFromDB>> call, Response<TemplateResult<CommentFromDB>> response) {
                comments.clear();
                for(int i = 0; i < response.body().values.size(); i++)
                {
                    comments.add(new Comment(response.body().values.get(i)));
                }
                callback.Callback(comments);
            }

            @Override
            public void onFailure(Call<TemplateResult<CommentFromDB>> call, Throwable t) {
                Log.e("Comments", t.getMessage());
            }
        });
    }
}
