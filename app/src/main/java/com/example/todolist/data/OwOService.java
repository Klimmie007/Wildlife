package com.example.todolist.data;

import com.example.todolist.data.model.BaseCall;
import com.example.todolist.data.model.BaseGetCall;
import com.example.todolist.data.model.BaseInsertCall;
import com.example.todolist.data.model.CommentFromDB;
import com.example.todolist.data.model.EntryFromDB;
import com.example.todolist.data.model.ImagePath;
import com.example.todolist.data.model.InsertData;
import com.example.todolist.data.model.SpeciesTag;
import com.example.todolist.data.model.TemplateResult;
import com.example.todolist.data.model.TemplateResultSingle;
import com.example.todolist.data.model.UserContainer;
import com.example.todolist.data.model.findById;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OwOService
{
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/findOne")
    Call<UserContainer> getUser(@Body BaseGetCall<UserContainer> user);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/insertOne")
    Call<InsertData> insertUser(@Body BaseInsertCall<UserContainer> user);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/findOne")
    Call<UserContainer> getUserByID(@Body BaseGetCall<findById> user);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/find")
    Call<TemplateResult<SpeciesTag>> getSpecies(@Body BaseCall call);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/insertOne")
    Call<InsertData> insertEntry(@Body BaseInsertCall<EntryFromDB> entry);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/find")
    Call<TemplateResult<EntryFromDB>> getEntries(@Body BaseCall call);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/findOne")
    Call<TemplateResultSingle<ImagePath>> getImage(@Body BaseGetCall<findById> image);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/insertOne")
    Call<InsertData> sendImage(@Body BaseInsertCall<ImagePath> chunk);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/insertOne")
    Call<InsertData> sendComment(@Body BaseInsertCall<CommentFromDB> comment);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "api-key: QJlKuvpjBaXvmMSJNtxacgajl1IkLstNETzPtMJWTxwItDFfwAkZ1brCUNXJdQqn"
    })
    @POST("action/find")
    Call<TemplateResult<CommentFromDB>> getComments(@Body BaseGetCall<CommentFromDB> comment);
}
