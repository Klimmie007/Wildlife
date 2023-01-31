package com.example.todolist;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class CommentsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String ID = getIntent().getStringExtra(CommentListFragment.KEY_COMMENT_ID_EXTRA);
        Log.d("postid", ID);
        return CommentListFragment.newInstance(ID);
    }
}
