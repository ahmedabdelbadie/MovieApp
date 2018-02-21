package com.example.ahmed.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 12/8/2017.
 */

public class MovieModel implements Parcelable {

    private String posterPath;
    private String adult;
    private String overview;
    private String releaseDate;
    private String id;
    private String originalTitle;
    private String originalLang;
    private String title;
    private String backdrop;
    private String popularity;
    private String voteCount;
    private String video;
    private String voteRange;

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOriginalLang(String originalLang) {
        this.originalLang = originalLang;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setVoteRange(String voteRange) {
        this.voteRange = voteRange;
    }

    public MovieModel(String posterPath,
                      String adult,
                      String overview,
                      String releaseDate,
                      String id,
                      String originalTitle,
                      String originalLang,
                      String title,
                      String backdrop,
                      String popularity,
                      String voteCount,
                      String video,
                      String voteRange) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLang = originalLang;
        this.title = title;
        this.backdrop = backdrop;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteRange = voteRange;
    }

    protected MovieModel(Parcel in) {
        posterPath = in.readString();
        adult = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readString();
        originalTitle = in.readString();
        originalLang = in.readString();
        title = in.readString();
        backdrop = in.readString();
        popularity = in.readString();
        voteCount = in.readString();
        video = in.readString();
        voteRange = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public String getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLang() {
        return originalLang;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getVideo() {
        return video;
    }

    public String getVoteRange() {
        return voteRange;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeString(adult);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(originalLang);
        parcel.writeString(title);
        parcel.writeString(backdrop);
        parcel.writeString(popularity);
        parcel.writeString(voteCount);
        parcel.writeString(video);
        parcel.writeString(voteRange);

    }
}
