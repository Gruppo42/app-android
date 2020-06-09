package com.gruppo42.app.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguagesDTO
{
    List<Language> languages;

    public LanguagesDTO() {
    }

    public LanguagesDTO(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }


    public class Language {
        @SerializedName("name")
        String name;
        @SerializedName("iso639_1")
        String code2;
        @SerializedName("iso639_2")
        String code3;
        @SerializedName("nativeName")
        String nativeName;

        public Language() {
        }

        public Language(String name, String code2, String code3, String nativeName) {
            this.name = name;
            this.code2 = code2;
            this.code3 = code3;
            this.nativeName = nativeName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode2() {
            return code2;
        }

        public void setCode2(String code2) {
            this.code2 = code2;
        }

        public String getCode3() {
            return code3;
        }

        public void setCode3(String code3) {
            this.code3 = code3;
        }

        public String getNativeName() {
            return nativeName;
        }

        public void setNativeName(String nativeName) {
            this.nativeName = nativeName;
        }

        @NonNull
        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if(this==obj)
                return true;
            if(obj==null)
                return false;
            if(getClass()!= obj.getClass())
                return false;
            Language lang = (Language) obj;
            return this.name.equals(lang.name)
                    && this.code2.equals(lang.code2)
                    && this.code3.equals(lang.code3);
        }
    }
}

