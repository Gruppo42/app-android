package com.gruppo42.app.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionsDTO
{
    List<RegionsDTO> regions;

    public RegionsDTO() {
    }

    public RegionsDTO(List<RegionsDTO> regions) {
        this.regions = regions;
    }

    public List<RegionsDTO> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionsDTO> regions) {
        this.regions = regions;
    }

    public class Region
    {
        @SerializedName("name")
        String name;
        @SerializedName("code2")
        String code2;
        @SerializedName("code3")
        String code3;
        @SerializedName("flag")
        String flag;

        public Region() {
        }

        public Region(String name, String code2, String code3, String flag) {
            this.name = name;
            this.code2 = code2;
            this.code3 = code3;
            this.flag = flag;
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

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        @NonNull
        @Override
        public String toString()
        {
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
            Region region = (Region) obj;
            return this.name.equals(region.name)
                    && this.code2.equals(region.code2)
                    && this.code3.equals(region.code3);
        }
    }

}

