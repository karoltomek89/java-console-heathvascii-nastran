package com.kt.utilities.heathvascii;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFiles {

    public static void main(String[] args) throws IOException {

        String bdfName = "model.bdf";
        String f06Name = "results.f06";

        List<String> chbdyeList;
        List<String> hbdyList;

        try (Stream<String> stream = Files.lines(Paths.get(bdfName))) {
            chbdyeList = stream.filter(p -> p.startsWith("CHBDYE  ")).sorted().collect(Collectors.toList());
        }

        try (Stream<String> stream = Files.lines(Paths.get(f06Name))) {
            hbdyList = stream.filter(p -> p.startsWith("                ") & p.contains("      0.000000E+00     ")).sorted().collect(Collectors.toList());
        }

        List<String> hvasciiList = new ArrayList<String>();

        hvasciiList.add("ALTAIR ASCII FILE");
        hvasciiList.add("$DELIMITER =	,");
        hvasciiList.add("$SUBCASE = 	1	Subcase	1");
        hvasciiList.add("$BINDING = 	ELEMENT");
        hvasciiList.add("$COLUMN_INFO = 	ENTITY_ID");
        hvasciiList.add("$RESULT_TYPE = APPLIED-LOAD(s), FREE-CONVECTION(s), FORCED-CONVECTION(s), RADIATION(s), TOTAL(s)");

        for (int i = 0; i < chbdyeList.size(); i++) {
            if (chbdyeList.get(i).trim().substring(9, 16).trim().equals(hbdyList.get(i).trim().substring(0, 8).trim())) {
                hvasciiList.add(chbdyeList.get(i).trim().substring(17, 24) + ',' + hbdyList.get(i).trim().substring(9).replace("     ", ",").replace("0.000000E+00", "0").trim());
            } else {
                System.out.println("error parsing data, check elements configuration and numbers");
                break;
            }
        }

        System.out.println(System.currentTimeMillis());

        Files.write(Paths.get("heathvascii.neu"), hvasciiList);

        System.out.println(System.currentTimeMillis());

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("heathvascii_v2.neu"))) {
            for (String s : hvasciiList) {
                writer.write(s + "\n");
            }
        }

        System.out.println(System.currentTimeMillis());
    }

}
