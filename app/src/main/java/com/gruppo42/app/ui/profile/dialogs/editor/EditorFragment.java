package com.gruppo42.app.ui.profile.dialogs.editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.databinding.FragmentEditorBinding;
import com.gruppo42.app.session.SessionManager;
import com.gruppo42.app.ui.dialogs.ChangeListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends DialogFragment {

    private Bitmap image;
    private String name;
    private String username;
    private String email;
    private UserApi api;
    private SessionManager sessionManager;
    private FragmentEditorBinding binding;
    private ChangeListener nameListener;
    private ChangeListener mailListener;
    private ChangeListener imageListener;
    private ChangeListener onLogout;

    public EditorFragment() {
        // Required empty public constructor
    }

    public EditorFragment(Bitmap image, String name, String username, String email)
    {
        super();
        this.api = UserApi.Instance.get();
        this.image = image;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public static EditorFragment newInstance(String param1, String param2) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.sessionManager = new SessionManager(getContext());
        this.api = UserApi.Instance.get();
        binding = FragmentEditorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.email.setText(email);
        binding.name.setText(name);
        if(this.image==null)
            Glide.with(getContext())
                    .asBitmap()
                    .load(R.drawable.profile_placeholder)
                    .placeholder(R.drawable.place_holder_profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .into(binding.imageViewProfilePic);
        else
            Glide.with(getContext())
                    .asBitmap()
                    .load(image)
                    .placeholder(R.drawable.place_holder_profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(binding.imageViewProfilePic);

        binding.imageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDialog dialog = new PhotoDialog();
                dialog.setCallApi(true);
                dialog.setOnSuccessListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        Pair<String, Bitmap> pair = (Pair<String, Bitmap>)object;
                        Glide.with(getContext())
                                .asBitmap()
                                .load(pair.second)
                                .placeholder(new ColorDrawable(Color.GRAY))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .circleCrop()
                                .into(binding.imageViewProfilePic);
                        imageListener.onChange(object);
                    }
                });
                dialog.show(getParentFragmentManager(), "Photo pick");
            }
        });
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Logout");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                onLogout.onChange(null);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Button");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        binding.passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPasswordDialog dialog = new NewPasswordDialog();
                dialog.setOnSuccessListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        Toast toast = Toast.makeText(getContext(), "Profile password changed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                dialog.show(getParentFragmentManager(), "Password change");
            }
        });
        binding.nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "name");
                NameDialog dialog = new NameDialog();
                dialog.setOnSuccessListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        binding.name.setText((CharSequence) object);
                        nameListener.onChange(object);
                    }
                });
                dialog.show(getParentFragmentManager(), "Name pick");
            }
        });

        binding.emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "email");
                EmailDialog dialog = new EmailDialog();
                dialog.setOnSuccessListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        binding.email.setText((CharSequence) object);
                        mailListener.onChange(object);
                    }
                });
                dialog.show(getParentFragmentManager(), "Email pick");
            }
        });
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setNameListener(ChangeListener nameListener) {
        this.nameListener = nameListener;
    }

    public void setMailListener(ChangeListener mailListener) {
        this.mailListener = mailListener;
    }

    public void setImageListener(ChangeListener imageListener) {
        this.imageListener = imageListener;
    }
    public void setOnLogout(ChangeListener onLogout) {
        this.onLogout = onLogout;
    }
}
