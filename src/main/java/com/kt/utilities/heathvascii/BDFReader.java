package com.kt.utilities.heathvascii;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class BDFReader {

    String bdfName = null;
    List<String> chbdyeList = null;


    void readBDF (){

        try {
            bdfName = Files.list(Paths.get(""))
                    .filter(path -> path.toString().endsWith(".bdf"))
                    .findFirst()
                    .get()
                    .toString();

            chbdyeList = Files.lines(Paths.get(bdfName), StandardCharsets.ISO_8859_1)
                    .filter(p -> p.startsWith("CHBDYE  "))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getBdfName() {
        return bdfName;
    }

    List<String> getChbdyeList() {
        return chbdyeList;
    }
}
