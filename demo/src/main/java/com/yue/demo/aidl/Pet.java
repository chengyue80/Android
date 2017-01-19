package com.yue.demo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Pet implements Parcelable {

    private String name;
    private double weight;

    public Pet() {
    }

    public Pet(String name, double weight) {
        super();
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(weight);
    }

    public static final Parcelable.Creator<Pet> CREATOR = new Creator<Pet>() {

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }

        @Override
        public Pet createFromParcel(Parcel source) {
            return new Pet(source.readString(), source.readDouble());
        }
    };

    @Override
    public String toString() {
        return "Pet [name=" + name + ", weight=" + weight + "]";
    }

}
