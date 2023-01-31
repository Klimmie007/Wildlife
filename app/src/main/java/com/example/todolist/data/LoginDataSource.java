package com.example.todolist.data;

import android.os.StrictMode;
import android.util.Log;

import com.example.todolist.data.model.BaseGetCall;
import com.example.todolist.data.model.BaseInsertCall;
import com.example.todolist.data.model.InsertData;
import com.example.todolist.data.model.LoggedInUser;
import com.example.todolist.data.model.RetrofitSingleton;
import com.example.todolist.data.model.User;
import com.example.todolist.data.model.UserContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.todolist.data.model.OwOService;
import com.example.todolist.data.model._id;
import com.example.todolist.data.model.findById;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static List<User> users = new ArrayList<>();

    private static OwOService owOService = null;
    private static Retrofit retrofit = null;

    public LoginDataSource()
    {
        if(retrofit == null)
        {
            retrofit = RetrofitSingleton.getInstance();

            owOService = RetrofitSingleton.getService();
        }
    }

    public Result<LoggedInUser> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication
            Call<UserContainer> APICall = owOService.getUser(new BaseGetCall<UserContainer>("Users", new UserContainer(username, password)));
            Response<UserContainer> response = APICall.execute();
            LoggedInUser user =
                    new LoggedInUser(response.body().user._id, response.body().user.username);
            Log.d("login", user.toString());
            return new Result.Success<>(user);
        } catch (Exception e) {
            Log.e("login", "failed login", e);
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public Result<LoggedInUser> register(String username, String password) {
        try{
            User user = new User();
            user.username = username;
            Call<UserContainer> CheckIfExists = owOService.getUser(new BaseGetCall<UserContainer>("Users", new UserContainer(user)));
            Response<UserContainer> ifExists = CheckIfExists.execute();
            Log.d("register", Boolean.toString(ifExists.body().user == null));
            if(ifExists.body().user != null)
            {
                return new Result.Error(new Exception("User with specified username exists"));
            }
            Call<InsertData> APICall = owOService.insertUser(new BaseInsertCall<UserContainer>("Users", new UserContainer(username, password)));
            Response<InsertData> response = APICall.execute();
            Call<UserContainer> APICall2 = owOService.getUserByID(new BaseGetCall<findById>("Users", new findById(response.body().insertedId)));
            Response<UserContainer> response2 = APICall2.execute();
            return new Result.Success<>(new LoggedInUser(response2.body().user._id, response2.body().user.username));

        }catch (Exception e) {
            Log.e("register", "failed register", e);
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public static void getUserByID(String id, MyCallback callback)
    {
        if(users.isEmpty())
        {
            Call<UserContainer> call = owOService.getUserByID(new BaseGetCall<findById>("Users", new findById(id)));
            call.enqueue(new Callback<UserContainer>() {
                @Override
                public void onResponse(Call<UserContainer> call, Response<UserContainer> response) {
                    users.add(response.body().user);
                    callback.Callback(response.body().user);
                }

                @Override
                public void onFailure(Call<UserContainer> call, Throwable t) {

                }
            });
        }
        else
        {
            for(int i = 0; i < users.size(); i++)
            {
                if(users.get(i)._id.equals(id))
                {
                    callback.Callback(users.get(i));
                    return;
                }
            }
            Call<UserContainer> call = owOService.getUserByID(new BaseGetCall<findById>("Users", new findById(id)));
            call.enqueue(new Callback<UserContainer>() {
                @Override
                public void onResponse(Call<UserContainer> call, Response<UserContainer> response) {
                    users.add(response.body().user);
                    callback.Callback(response.body().user);
                }

                @Override
                public void onFailure(Call<UserContainer> call, Throwable t) {

                }
            });
        }
    }
}