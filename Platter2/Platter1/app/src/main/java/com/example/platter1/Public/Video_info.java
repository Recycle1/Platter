package com.example.platter1.Public;

public class Video_info {

    public String video_id;
    public String name;
    public int video_class;

    public int year;
    public String description;

    public Video_info(String video_id, String name) {
        this.video_id = video_id;
        this.name = name;
    }

    public Video_info(String video_id, String name, int video_class) {
        this.video_id = video_id;
        this.name = name;
        this.video_class = video_class;
    }

    public Video_info(String video_id, String name, int video_class, int year, String description) {
        this.video_id = video_id;
        this.name = name;
        this.video_class = video_class;
        this.year = year;
        this.description = description;
    }

}
