/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.github.db;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GithubTypeConverters {
    @SuppressLint("RestrictedApi")
    @TypeConverter
    public static List<Integer> stringToIntList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToIntList(data);
    }

    @SuppressLint("RestrictedApi")
    @TypeConverter
    public static String intListToString(List<Integer> ints) {
        return StringUtil.joinIntoString(ints);
    }

    @TypeConverter
    public static ArrayList<String> stringToArrayListOfString(String string) {
        if (string == null) {
            return null;
        }
        Type type = new TypeToken<ArrayList<String>>(){
        }.getType();
        return new Gson().fromJson(string, type);
    }

    @TypeConverter
    public static String arrayListOfStringToString(ArrayList<String> list) {
        if (list == null) {
            return null;
        }
        Type type = new TypeToken<ArrayList<String>>(){
        }.getType();
        return new Gson().toJson(list, type);
    }


}
