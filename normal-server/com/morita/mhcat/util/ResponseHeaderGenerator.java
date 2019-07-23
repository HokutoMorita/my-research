package com.morita.mhcat.util;
import java.io.*;

public interface ResponseHeaderGenerator {
    void generate(OutputStream output) throws IOException;
}