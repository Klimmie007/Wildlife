package com.example.todolist.data;

import android.util.Log;

import com.example.todolist.data.model.BaseCall;
import com.example.todolist.data.model.BaseInsertCall;
import com.example.todolist.data.model.Entry;
import com.example.todolist.data.model.EntryFromDB;
import com.example.todolist.data.model.InsertData;
import com.example.todolist.data.model.TemplateResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class EntryDB {
    private static List<Entry> entries = new ArrayList<>();
    public static void InsertEntry(Entry entry)
    {
        entries.add(entry);
        Call<InsertData> insertCall = RetrofitSingleton.getService().insertEntry(
                new BaseInsertCall<EntryFromDB>("Entries", new EntryFromDB(entry.user._id,
                        entry.imageID, entry.species._id, entry.Longitude, entry.Latitude)));

        insertCall.enqueue(new Callback<InsertData>() {
            @Override
            public void onResponse(Call<InsertData> call, Response<InsertData> response) {
                Log.d("entry", "ZAJEBIŚCIE");
            }

            @Override
            public void onFailure(Call<InsertData> call, Throwable t) {
                Log.e("entry", "KURWA");
            }
        });
    }

    public static void fetchEntries(MyCallback callback)
    {
        if(!entries.isEmpty())
        {
            callback.Callback(entries);
            return;
        }
        Call<TemplateResult<EntryFromDB>> fetch = RetrofitSingleton.getService().getEntries(new BaseCall("Entries"));
        fetch.enqueue(new Callback<TemplateResult<EntryFromDB>>() {
            @Override
            public void onResponse(Call<TemplateResult<EntryFromDB>> call, Response<TemplateResult<EntryFromDB>> response) {
                for(int i = 0; i < response.body().values.size(); i++)
                {
                    EntryFromDB shortcut = response.body().values.get(i);
                    entries.add(new Entry(shortcut.id, shortcut.UserID, shortcut.ImageID, shortcut.SpeciesID, shortcut.Latitude, shortcut.Longitude));
                }
                callback.Callback(entries);
            }

            @Override
            public void onFailure(Call<TemplateResult<EntryFromDB>> call, Throwable t) {
                Log.e("ENTRYDB:60", t.getMessage());
            }
        });
    }

    public static Entry getEntry(String id)
    {
        if(entries.isEmpty())
        {
            return null;
        }
        for(int i = 0; i < entries.size(); i++)
        {
            if(entries.get(i).entryID.equals(id))
            {
                return entries.get(i);
            }
        }
        return null;
    }
}
