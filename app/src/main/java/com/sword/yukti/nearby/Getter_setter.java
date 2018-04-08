package com.sword.yukti.nearby;


import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Getter_setter implements Parcelable
{     String email;
     String password;
    String address;
    String alternate_number;
    String rating;
    String confirm_password;
    String placeName;
    String vicinity;
    String reference;


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    protected Getter_setter(Parcel in) {
        email = in.readString();
        password = in.readString();
        address = in.readString();
        alternate_number = in.readString();
        confirm_password = in.readString();
        placeName = in.readString();
        vicinity = in.readString();
        rating=in.readString();
        reference=in.readString();
    }

    public static final Creator<Getter_setter> CREATOR = new Creator<Getter_setter>() {
        @Override
        public Getter_setter createFromParcel(Parcel in) {
            return new Getter_setter(in);
        }

        @Override
        public Getter_setter[] newArray(int size) {
            return new Getter_setter[size];
        }
    };

    public Getter_setter() {

    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlternate_number() {
        return alternate_number;
    }

    public void setAlternate_number(String alternate_number) {
        this.alternate_number = alternate_number;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(address);
        dest.writeString(alternate_number);
        dest.writeString(confirm_password);
        dest.writeString(placeName);
        dest.writeString(vicinity);
        dest.writeString(rating);
        dest.writeString(reference);
    }
}
