package com.example.todolist;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.data.CommentDB;
import com.example.todolist.data.LoginDataSource;
import com.example.todolist.data.LoginRepository;
import com.example.todolist.data.MyCallback;
import com.example.todolist.data.model.Comment;
import com.example.todolist.data.model.User;
import com.example.todolist.data.model.UserContainer;

import java.util.List;

public class CommentListFragment extends Fragment {
    public static final String KEY_COMMENT_ID_EXTRA = "KEY_COMMENT_ID_EXTRA";
    public static final String KEY_COMMENT_BODY = "KEY_COMMENT_BODY";
    private static final int maxChars = 200;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private EditText newComment;
    private Button sendButton;
    private TextView charCount;

    private String commentBody;
    private String postID;

    public static CommentListFragment newInstance(String ID)
    {
        Log.d("static shit", ID);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COMMENT_ID_EXTRA, ID);
        CommentListFragment fragment = new CommentListFragment();
        Log.d("static shit", Boolean.toString(fragment.isStateSaved()));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        recyclerView = view.findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newComment = view.findViewById(R.id.new_comment_input);
        newComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                commentBody = editable.toString();
                charCount.setText(editable.length() + "/" + maxChars);
                if(editable.length() > maxChars)
                {
                    charCount.setTextColor(Color.RED);
                    sendButton.setEnabled(false);
                }
                else
                {
                    charCount.setTextColor(Color.DKGRAY);
                    sendButton.setEnabled(true);
                }
            }
        });
        sendButton = view.findViewById(R.id.new_comment_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDB.sendComment(new Comment(postID, LoginRepository.getUser(), commentBody), new MyCallback() {
                    @Override
                    public void Callback(Object result) {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        charCount = view.findViewById(R.id.char_count);
        if(savedInstanceState != null)
        {
            postID = savedInstanceState.getString(KEY_COMMENT_ID_EXTRA);
            if(postID != null)
            {
                Log.d("postid", postID);
            }
            else
            {
                Log.d("postid", "null");
            }
            commentBody = savedInstanceState.getString(KEY_COMMENT_BODY);
            newComment.setText(commentBody);
            Log.d("CREATE_COMMENT", commentBody);
        }
        else
        {
            Bundle arguments = getArguments();
            postID = arguments.getString(KEY_COMMENT_ID_EXTRA);
            commentBody = "";
            charCount.setText("0/200");
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView()
    {
        if(adapter == null)
        {
            CommentDB.getComments(postID, new MyCallback<List<Comment>>() {
                @Override
                public void Callback(List<Comment> result) {
                    adapter = new CommentAdapter(result);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
        else
        {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_COMMENT_BODY, commentBody);
        outState.getString(KEY_COMMENT_ID_EXTRA, postID);
    }

    private class CommentHolder extends RecyclerView.ViewHolder
    {
        private TextView commentText;
        private Comment comment;
        public CommentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_comment, parent, false));

            commentText = itemView.findViewById(R.id.comment_string);
        }

        public void bind(Comment comment)
        {
            this.comment = comment;
            this.commentText.setText(comment.toString());
            if(comment.user.username == null)
            {
                LoginDataSource.getUserByID(comment.user._id, new MyCallback<User>() {
                    @Override
                    public void Callback(User result) {
                        comment.user.username = result.username;
                        update(comment.toString());
                    }
                });
            }
        }

        public void update(String text)
        {
            this.commentText.setText(text);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder>
    {
        private List<Comment> comments;

        public CommentAdapter(List<Comment> comments)
        {
            this.comments = comments;
        }

        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CommentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
            holder.bind(comments.get(position));
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }
}
