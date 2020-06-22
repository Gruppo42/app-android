package com.gruppo42.app.ui.profile.dialogs.editor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.gruppo42.app.api.models.ProfileChangeRequest;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.databinding.PhotoDialogBinding;
import com.gruppo42.app.ui.dialogs.ChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class PhotoDialog extends BottomSheetDialogFragment {

    private String userToken;
    private PhotoDialogBinding binding;
    private ChangeListener onSuccessListener = null;
    private byte[] imageB;
    private UserApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        userToken = sharedPref.getString("user", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkyNzk1ODg3LCJleHAiOjE1OTM0MDA2ODd9.HadRp2srca8WlO3VcVr1x5CLOT6i3USoYLO8HTZyjiHtenupH7BBkO7KV_7hznacTIDCQhWL6oHovvee5Nzkeg");
        api = UserApi.Instance.get();
        binding = PhotoDialogBinding.inflate(inflater, container, false);
        binding.buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                /*
                * Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                   android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                * */
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 0);//one can be replaced with any action code
            }
        });
        binding.buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 1);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null)
        {
            Toast toast = Toast.makeText(getContext(), "Could not change profile image", Toast.LENGTH_LONG);
            toast.show();
        }
        String image = encodeImage(data.getData().getPath());
        api.changeProfileDetails(userToken, new ProfileChangeRequest(null, null, image))
                .enqueue(new Callback<UserApiResponse>() {
                    @Override
                    public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                        Log.d("Debug api", response.body().toString());

                        if(response.isSuccessful())
                        {
                            Toast toast = Toast.makeText(getContext(), "Profile image changed", Toast.LENGTH_LONG);
                            toast.show();
                            if(onSuccessListener!=null) {
                                byte[] ima = Base64.decode(image, Base64.DEFAULT);
                                onSuccessListener.onChange(ima);
                            }
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getContext(), "Could not change profile image", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserApiResponse> call, Throwable t) {
                        Toast toast = Toast.makeText(getContext(), "Could not change profile image", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    public void setOnSuccessListener(ChangeListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
