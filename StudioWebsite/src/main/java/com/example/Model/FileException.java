package com.example.Model;

import java.nio.file.Files;

public class FileException extends Exception{
    public FileException(String message)
    {
        super(message);
    }
}
