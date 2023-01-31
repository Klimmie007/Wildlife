package com.example.todolist;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.todolist.data.LoginDataSource;
import com.example.todolist.data.LoginRepository;
import com.example.todolist.ui.login.LoginActivity;

public class TaskListActivity extends SingleFragmentActivity {
    LoginRepository repo = LoginRepository.getInstance(new LoginDataSource());
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!repo.isLoggedIn())
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}