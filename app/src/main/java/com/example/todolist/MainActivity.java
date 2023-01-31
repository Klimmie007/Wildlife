package com.example.todolist;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String ID = getIntent().getStringExtra(TaskListFragment.KEY_EXTRA_ENTRY_ID);
        return EntryFragment.newInstance(ID);
    }
}