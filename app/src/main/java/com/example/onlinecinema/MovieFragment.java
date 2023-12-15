package com.example.onlinecinema;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinecinema.entities.FavOfMovie;
import com.example.onlinecinema.entities.MarkOfMovie;
import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.DownloadFavMovies;
import com.example.onlinecinema.requests.DownloadMoviesFromDB;
import com.example.onlinecinema.requests.RestClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MovieFragment extends Fragment {

    private Movie movie;
    private String tagFragment;
    private static final int SHORT_TEXT_LENGTH = 200;
    private boolean isExpandedText = false;
    private boolean isBeMarked = false;

    public MovieFragment(Movie movie) {
        this.movie = movie;
        this.tagFragment = movie.getId() + "";
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
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavView = getActivity().findViewById(R.id.bottomNavView);
        bottomNavView.setEnabled(false);
        bottomNavView.setVisibility(View.GONE);

        ImageView poster = view.findViewById(R.id.posterView);
        TextView movieName = view.findViewById(R.id.movieName);
        TextView origName = view.findViewById(R.id.origNameView);
        TextView genresView = view.findViewById(R.id.genresView);
        TextView countriesView = view.findViewById(R.id.countryView);
        TextView yearView = view.findViewById(R.id.yearView);
        TextView descText = view.findViewById(R.id.descText);
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonMovie);
        ImageView hideNshowBtn = view.findViewById(R.id.hideNshowButton);
        Button markButton = view.findViewById(R.id.markButton);
        Button favButton = view.findViewById(R.id.favButton);
        Slider markSlider = view.findViewById(R.id.markSlider);
        TextView markValueText = view.findViewById(R.id.markValueText);
        Button saveMarkButton = view.findViewById(R.id.saveMarkButton);
        TextView ratingView = view.findViewById(R.id.ratingView);
        String ratingText = ratingView.getText().toString();
        ratingView.setText(ratingText + movie.getRating());

        markValueText.setText("Ваша оценка: " + (int) markSlider.getValue());

        markSlider.addOnChangeListener((slider, value, fromUser) -> {
            markValueText.setText("");
            int valueText = (int) slider.getValue();
            markValueText.setText("Ваша оценка: " + valueText);
        });

        String allGenres = genresView.getText().toString() + readGenres();
        String allCountries = countriesView.getText().toString() + readCountries();
        Glide.with(getContext())
                .load(movie.getPoster())
                .error(R.drawable.baseline_no_internet_24)
                .into(poster);
        movieName.setText(movie.getNameRu());
        if (movie.getOrigName() != null) {
            origName.setText(movie.getOrigName());
        }
        genresView.setText(allGenres);
        countriesView.setText(allCountries);
        String year = yearView.getText().toString();
        yearView.setText(year + " " + movie.getYear());
        if (movie.getDesc().length() > SHORT_TEXT_LENGTH) {
            descText.setText(movie.getDesc().substring(0, SHORT_TEXT_LENGTH));
        } else {
            descText.setText(movie.getDesc());
            hideNshowBtn.setVisibility(View.GONE);
        }

        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        hideNshowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpandedText) {
                    descText.setText(movie.getDesc());
                    isExpandedText = true;
                    hideNshowBtn.setImageResource(R.drawable.baseline_hide_text_24);
                } else {
                    descText.setText(movie.getDesc().substring(0, SHORT_TEXT_LENGTH));
                    isExpandedText = false;
                    hideNshowBtn.setImageResource(R.drawable.baseline_open_full_text_24);
                }
            }
        });

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBeMarked) {
                    markValueText.setVisibility(View.VISIBLE);
                    markSlider.setVisibility(View.VISIBLE);
                    saveMarkButton.setVisibility(View.VISIBLE);
                    isBeMarked = true;
                } else {
                    markValueText.setVisibility(View.GONE);
                    markSlider.setVisibility(View.GONE);
                    saveMarkButton.setVisibility(View.GONE);
                    isBeMarked = false;
                }
            }
        });
        if (UserRepo.getCurrentUser().getValue() != null) {
            RestClient client = RestClient.getInstance();
            Request request = client.createGetRequest("/marks/" + movie.getId() + "/"
                    + UserRepo.getCurrentUser().getValue().getId());
            client.getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Не удалось загрузить вашу оценку.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String value = response.body().string();
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                markValueText.setVisibility(View.VISIBLE);
                                markValueText.setText("Ваша оценка: " + value);
                                Drawable top = getContext().getResources().getDrawable(R.drawable.fav_round_star_24,
                                        getActivity().getTheme());
                                markButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                            }
                        });
                    }
                }
            });
        }

        MovieRepo movieRepo = new MovieRepo();
        Boolean isFav = movieRepo.getFavMovieMutableLiveData().getValue().contains(movie.getId());
        if (isFav) {
            Drawable top = getContext().getResources().getDrawable(R.drawable.fav_heart,
                    getActivity().getTheme());
            favButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer idUser = UserRepo.getCurrentUser().getValue().getId();
                Integer idMovie = movie.getId();
                FavOfMovie fav = new FavOfMovie(idUser, idMovie, !isFav);
                String json = new Gson().toJson(fav, FavOfMovie.class);
                RestClient client = RestClient.getInstance();
                RequestBody body = client.createRequestBody(json);
                Request request = client.createPostRequest("/movies/fav/", body);
                client.getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Соединение с сервром потеряно.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isFav) {
                                        Drawable top = getContext().getResources().getDrawable(R.drawable.ic_baseline_fav,
                                                getActivity().getTheme());
                                        favButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                    } else {
                                        Drawable top = getContext().getResources().getDrawable(R.drawable.fav_heart,
                                                getActivity().getTheme());
                                        favButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                    }
                                    Toast.makeText(getContext(), "Изменение сохранено.", Toast.LENGTH_SHORT).show();
                                    DownloadFavMovies.downloadMovies(movieRepo);
                                }
                            });
                            DownloadMoviesFromDB.downloadMovies(new MovieRepo());
                        } else {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Ошибка сохранения.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        saveMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer idUser = UserRepo.getCurrentUser().getValue().getId();
                Integer idMovie = movie.getId();
                Integer value = (int) markSlider.getValue();
                MarkOfMovie mark = new MarkOfMovie(idMovie, idUser, value);
                String json = new Gson().toJson(mark, MarkOfMovie.class);
                RestClient client = RestClient.getInstance();
                RequestBody body = client.createRequestBody(json);
                Request request = client.createPostRequest("/marks/", body);
                client.getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Соединение с сервром потеряно.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Фильм оценен.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            DownloadMoviesFromDB.downloadMovies(new MovieRepo());
                        } else {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Ошибка создания оценки.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private String readGenres() {
        String allGenres = " ";
        for (int i = 0; i < movie.getGenres().size(); i++) {
            String genre = movie.getGenres().get(i);
            if (i != movie.getGenres().size() - 1) {
                allGenres += (genre + ", ");
            } else {
                allGenres += (genre + ".");
            }
        }
        return allGenres;
    }

    private String readCountries() {
        String allCountries = " ";
        for (int i = 0; i < movie.getCountries().size(); i++) {
            String genre = movie.getCountries().get(i);
            if (i != movie.getCountries().size() - 1) {
                allCountries += (genre + ", ");
            } else {
                allCountries += (genre + ".");
            }
        }
        return allCountries;
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