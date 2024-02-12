package com.example.Model;


import java.util.Date;

public interface Content {
    public String getFileType();
    public String getPath();
    public Date getUploadDate();

    public int getFileId();

    public String getFileName();

    public String getFileUploader();

    public float getFileSize();


}
