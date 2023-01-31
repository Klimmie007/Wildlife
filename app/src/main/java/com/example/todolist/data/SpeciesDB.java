package com.example.todolist.data;

import android.util.Log;

import com.example.todolist.data.model.BaseCall;
import com.example.todolist.data.model.SpeciesTag;
import com.example.todolist.data.model.TemplateResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeciesDB {
    private static List<SpeciesTag> species = new ArrayList<>();

    public static void getSpecies(MyCallback callback)
    {
        if(species.isEmpty())
        {
            RetrofitSingleton.getInstance();
            Call<TemplateResult<SpeciesTag>> call = RetrofitSingleton.getService().getSpecies(new BaseCall("Species"));
            call.enqueue(new Callback<TemplateResult<SpeciesTag>>() {
                @Override
                public void onResponse(Call<TemplateResult<SpeciesTag>> call, Response<TemplateResult<SpeciesTag>> response) {
                    species = response.body().values;
                    callback.Callback(species);
                    Log.d("species", Integer.toString(species.size()));
                }

                @Override
                public void onFailure(Call<TemplateResult<SpeciesTag>> call, Throwable t) {
                    Log.e("species", t.getMessage());
                }
            });
        }
        else
        {
            callback.Callback(species);
        }
    }

    public static void getSpecies(String id, MyCallback callback)
    {
        if(species.isEmpty())
        {
            getSpecies(new MyCallback<List<SpeciesTag>>() {
                @Override
                public void Callback(List<SpeciesTag> result) {
                    callback.Callback(getSpecies(id));
                }
            });
        }
        else
        {
            callback.Callback(getSpecies(id));
        }
    }

    private static SpeciesTag getSpecies(String id)
    {
        for(int i = 0; i < species.size(); i++)
        {
            if(species.get(i)._id.equals(id))
            {
                return species.get(i);
            }
        }
        return null;
    }
}
