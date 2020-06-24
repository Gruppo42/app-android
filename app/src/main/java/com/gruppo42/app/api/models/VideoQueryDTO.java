package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoQueryDTO {

    @SerializedName("results")
    private List<VideoDTO> results;

    public List<VideoDTO> getResults() {
        return results;
    }

    public void setResults(List<VideoDTO> results) {
        this.results = results;
    }

    public String getYoutubeVideoID()
    {
        for(VideoDTO v : results)
            if(v.hasYoutube()) {
                return v.key;
            }
        return null;
    }

    public class VideoDTO
    {
        @SerializedName("site")
        String site;
        @SerializedName("key")
        String key;

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean hasYoutube()
        {
            return site.equals("YouTube");
        }
    }
}
