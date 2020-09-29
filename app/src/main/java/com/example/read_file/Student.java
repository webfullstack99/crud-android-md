package com.example.read_file;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String key;


    private String id;
    private String name;
    private String major;

    public Student(String key, String id, String name, String major) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getStoredData(){
        Map<String, String> docData = new HashMap<>();
        docData.put("id", getId());
        docData.put("name", getName());
        docData.put("major", getMajor());
        return docData;
    }

    @Override
    public String toString(){
        return "id: "+id+", name: "+name+", major: "+major;
    }

}
