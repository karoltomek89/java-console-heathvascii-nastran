package com.kt.utilities.heathvascii;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class F06Reader {

    String f06Name = null;
    List<String> hbdyList = new ArrayList<>();
    List<String> hbdyListForCompare = new ArrayList<>();

    void readF06() {

        try {
            f06Name = Files.list(Paths.get(""))
                    .filter(path -> path.toString().endsWith(".f06"))
                    .findFirst()
                    .get()
                    .toString();

            hbdyList = Files.lines(Paths.get(f06Name), StandardCharsets.ISO_8859_1)
                    .filter(p -> p.startsWith("                ") & p.contains("      0.000000E+00     "))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        hbdyListForCompare = hbdyList.stream()
                .map(p -> p.trim()
                        .substring(0, 8)
                        .trim())
                .collect(Collectors.toList());
    }
}
