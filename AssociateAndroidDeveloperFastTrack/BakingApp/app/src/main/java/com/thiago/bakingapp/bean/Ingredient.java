/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:40
 * Copyright (c) 2017. All right reserved.
 * File Ingredient.java belongs to Project BakingApp
 *
 * Last modified 23/06/17 20:25
 *
 */
package com.thiago.bakingapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean represents ingredient.
 */
public class Ingredient implements Parcelable{

    private int quantity;
    private String measure;
    private String ingredient;

    public Ingredient(){}

    protected Ingredient(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
