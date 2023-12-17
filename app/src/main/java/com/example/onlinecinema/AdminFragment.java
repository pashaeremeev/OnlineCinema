package com.example.onlinecinema;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.RestClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminFragment extends Fragment {

    private String tagFragment;
    private static String numberValid = "^\\d+$";
    private Integer userId;
    private User user;

    public AdminFragment(String tagFragment) {
        this.tagFragment = tagFragment;
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
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView arrowBackButton = view.findViewById(R.id.arrowBackButtonAdm);
        Button actionButton = view.findViewById(R.id.actionButtonAdm);
        TextView userInfoText = view.findViewById(R.id.userInfoTextAdm);
        EditText userInfoField = view.findViewById(R.id.userInfoFieldAdm);
        EditText passwordField = view.findViewById(R.id.passwordFieldAdm);
        Button searchUserButton = view.findViewById(R.id.searchUserButtonAdm);
        ProgressBar progress = view.findViewById(R.id.progressSearchUserAdm);

        arrowBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userInfoField.getText().toString().equals("")
                        && userInfoField.getText().toString() != null) {
                    RestClient client = RestClient.getInstance();
                    RequestBody body = client.createRequestBody("");
                    Request request;
                    if (userInfoField.getText().toString().matches(numberValid)) {
                        userId = Integer.parseInt(userInfoField.getText().toString());
                        request = client.createGetRequest("/users/" + userId);
                    } else {
                        request = client.createGetRequest("/users/"
                                + userInfoField.getText().toString());
                    }
                    userInfoText.setText("");
                    progress.setVisibility(View.VISIBLE);
                    client.getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Cоединение с сервером разорвано.", Toast.LENGTH_SHORT).show();
                                    progress.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String json = response.body().string();
                                user = new Gson().fromJson(json, User.class);
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        userInfoText.setText("Информация о пользователе:\n"
                                                + "ИД: " + user.getId()
                                                + "\nИмя: " + user.getUsername()
                                                + "\nИД роли: " + user.getIdRole()
                                                + "\nНе заблокирован: " + user.getActive());
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        userInfoText.setText("Информация о пользователе:\n"
                                                + "Пользователь не найден");
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Поле ввода пустое.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordField.getText().toString();
                if (securePassword(password).equals(UserRepo.getCurrentUser().getValue().getPassword())) {
                    if (user != null && user.getIdRole() > 1) {
                        progress.setVisibility(View.VISIBLE);
                        RestClient client = RestClient.getInstance();
                        RequestBody body = client.createRequestBody("");
                        Request request = client.createPutRequest("/users/appoint/" + user.getId(), body);
                        client.getClient().newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Разорвана связь с сервером.", Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String responseText = response.body().string();
                                if (response.isSuccessful()) {
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), responseText, Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.GONE);
                                            userInfoField.setText("");
                                            userInfoText.setText("Информация о пользователе: ");
                                            passwordField.setText("");
                                            user = null;
                                        }
                                    });
                                } else {
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), responseText, Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });
                    } else if (user != null && user.getIdRole() == 1) {
                        Toast.makeText(getContext(), "Уже им является.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Пользователя не существует.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Неверный пароль.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void removeFragment() {
        BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
        bottomNavView.setEnabled(true);
        bottomNavView.setVisibility(View.VISIBLE);
        Fragment fragment = getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(tagFragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }
}