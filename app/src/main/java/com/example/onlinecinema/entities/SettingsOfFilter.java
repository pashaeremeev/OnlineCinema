package com.example.onlinecinema.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SettingsOfFilter implements Parcelable {

    private String genre;

    private String country;

    private Integer year;

    private Float minRating;

    private Float maxRating;

    private Integer typeOfSort;

    public SettingsOfFilter() {

    }

    public SettingsOfFilter(String genre, String country, Integer year,
                            Float minRating, Float maxRating, Integer typeOfSort) {
        this.genre = genre;
        this.country = country;
        this.year = year;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.typeOfSort = typeOfSort;
    }

    protected SettingsOfFilter(Parcel in) {
        genre = in.readString();
        country = in.readString();
        if (in.readByte() == 0) {
            year = null;
        } else {
            year = in.readInt();
        }
        if (in.readByte() == 0) {
            minRating = null;
        } else {
            minRating = in.readFloat();
        }
        if (in.readByte() == 0) {
            maxRating = null;
        } else {
            maxRating = in.readFloat();
        }
        if (in.readByte() == 0) {
            typeOfSort = null;
        } else {
            typeOfSort = in.readInt();
        }
    }

    public static final Creator<SettingsOfFilter> CREATOR = new Creator<SettingsOfFilter>() {
        @Override
        public SettingsOfFilter createFromParcel(Parcel in) {
            return new SettingsOfFilter(in);
        }

        @Override
        public SettingsOfFilter[] newArray(int size) {
            return new SettingsOfFilter[size];
        }
    };

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getMinRating() {
        return minRating;
    }

    public void setMinRating(Float minRating) {
        this.minRating = minRating;
    }

    public Float getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Float maxRating) {
        this.maxRating = maxRating;
    }

    public Integer getTypeOfSort() {
        return typeOfSort;
    }

    public void setTypeOfSort(Integer typeOfSort) {
        this.typeOfSort = typeOfSort;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{genre + "",
                country+ "",
                year + "",
                minRating + "",
                maxRating + "",
                typeOfSort + ""});
    }
}
