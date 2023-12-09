package com.example.onlinecinema;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.requests.RestClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegFragment extends Fragment {

    private static final String AUTH_FRAG = "AUTH_FRAG";
    private String tagFragment;
    private String PWD_VALID = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^*-]).{8,}$";
    private Button crossToAuthButton;
    private ImageView arrowBackBtn;
    private Button regButton;
    private EditText userNameField;
    private EditText passwordField;
    private EditText repeatPwdField;
    private TextView validText;

    public RegFragment(String tagFragment) {
        this.tagFragment = tagFragment;
    }

    public static RegFragment newInstance(String tagFragment) {
        RegFragment fragment = new RegFragment(tagFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                removeFragment();
            }
        });
        return inflater.inflate(R.layout.fragment_reg, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crossToAuthButton = view.findViewById(R.id.crossToAuthButton);
        arrowBackBtn = view.findViewById(R.id.arrowBackButtonReg);
        regButton = view.findViewById(R.id.regButton);
        userNameField = view.findViewById(R.id.editRegLogin);
        passwordField = view.findViewById(R.id.editRegPassword);
        repeatPwdField = view.findViewById(R.id.editRepPassword);
        validText = view.findViewById(R.id.pwdMessage);
        crossToAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, new AuthFragment(AUTH_FRAG), AUTH_FRAG)
                        .commit();
                BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavView.setEnabled(false);
                bottomNavView.setVisibility(View.GONE);
            }
        });
        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameField.getText().toString();
                String password = passwordField.getText().toString();
                //String repeatedPwd = "helloWorld";
                String repeatedPwd = repeatPwdField.getText().toString();
                if (isValid(password)) {
                    if (repeatedPwd.equals(password)) {
                        //UserRepo userRepo = new UserRepo(getContext());
                        //ArrayList<User> users = userRepo.getUsers();
                        String generatedPwd = securePassword(password);
                        if (generatedPwd != null) {
                            //users.add(new User(userName, generatedPwd));
                            //userRepo.saveUsers(users);
                            RestClient restClient = RestClient.getInstance();
                            OkHttpClient client = restClient.getClient();
                            Gson gson = new Gson();
                            String jsonData = gson.toJson(new User(userName, generatedPwd));
                            RequestBody body = restClient.createRequestBody(jsonData);
                            Request request = restClient.createPostRequest("/users/", body);
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Проверьте интернет-соединение.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "Вы зарегистрировались!", Toast.LENGTH_SHORT).show();
                                                removeFragment();
                                            }
                                        });
                                    } else {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "Пользователь с таким именем уже зарегистрирован.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Пароли не совпадают.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    validText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public boolean isValid(String password) {
        return password.matches(PWD_VALID);
    }

    public String securePassword(String password) {
        byte[] bytesOfPwd = password.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytesOfPwd);
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            Log.d("pwd", sb.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeFragment() {
        BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
        bottomNavView.setEnabled(true);
        bottomNavView.setVisibility(View.VISIBLE);
        Fragment fragment = getActivity().
                getSupportFragmentManager()
                .findFragmentByTag(tagFragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }
}