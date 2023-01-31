package com.example.todolist.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import retrofit2.Call;

import com.example.todolist.data.model.BaseGetCall;
import com.example.todolist.data.model.BaseInsertCall;
import com.example.todolist.data.model.ImagePath;
import com.example.todolist.data.model.InsertData;
import com.example.todolist.data.model.TemplateResultSingle;
import com.example.todolist.data.model.findById;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageDB
{
    public static List<ImageEntry> images = new ArrayList();
    private static int added = -1;
    private static Retrofit retrofit = null;
    private static OwOService service = null;

    private static void init()
    {
        if(retrofit == null)
        {
            retrofit = RetrofitSingleton.getInstance();
            service = RetrofitSingleton.getService();
        }
    }

    public static void setImage(Bitmap img, String path)
    {
        init();
        if(added >= 0)
        {
            Log.e("image", "images taken too quickly apart");
        }
        added = images.size();

        images.add(new ImageEntry(img, path));
    }

    public static Bitmap getLastTaken()
    {
        if(images.isEmpty() || added < 0)
        {
            return null;
        }
        else
        {
            return images.get(added).bmp;
        }
    }

    public static void sendToDB(MyCallback callback)
    {
        init();

        Call<InsertData> call = service.sendImage(new BaseInsertCall<ImagePath>("Images", new ImagePath(images.get(added).path)));
        call.enqueue(new Callback<InsertData>() {
            @Override
            public void onResponse(Call<InsertData> call, Response<InsertData> response) {
                images.get(added).id = response.body().insertedId;
                callback.Callback(response.body().insertedId);
                added = -1;
            }

            @Override
            public void onFailure(Call<InsertData> call, Throwable t) {

            }
        });
    }

    public static void getImageById(String id, MyCallback callback)
    {
        init();
        if(!images.isEmpty())
        {
            for(int i = 0; i < images.size(); i++)
            {
                if(id.equals(images.get(i).id))
                {
                    callback.Callback(images.get(i).bmp);
                    return;
                }
            }
        }
        Call<TemplateResultSingle<ImagePath>> img = service.getImage(new BaseGetCall<findById>("Images", new findById(id)));
        img.enqueue(new Callback<TemplateResultSingle<ImagePath>>() {
            @Override
            public void onResponse(Call<TemplateResultSingle<ImagePath>> call, Response<TemplateResultSingle<ImagePath>> response) {
                ImageEntry tmp = new ImageEntry(response.body().value._id, response.body().value.path);
                Log.d("kurwa", response.body().value.path);
                images.add(tmp);
                callback.Callback(tmp.bmp);
            }

            @Override
            public void onFailure(Call<TemplateResultSingle<ImagePath>> call, Throwable t) {

            }
        });
    }

}

class ImageEntry
{
    public Bitmap bmp;
    public String id, path;


    public ImageEntry(Bitmap bmp, String path)
    {
        this.bmp = bmp;
        this.path = path;
    }

    public ImageEntry(String id, String path)
    {
        this.id = id;
        this.path = path;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bmp = BitmapFactory.decodeFile(path);
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        //Log.d("kurwa", Integer.toString(bmp.getByteCount()));
    }
}
