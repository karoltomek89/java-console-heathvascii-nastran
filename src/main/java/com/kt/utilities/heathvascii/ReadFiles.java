package com.kt.utilities.heathvascii;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadFiles {

    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> hvasciiList = new ArrayList<>();
        BDFReader bdfReader = new BDFReader();
        F06Reader f06Reader = new F06Reader();

        Runnable readBDF = () -> {
            bdfReader.readBDF();
        };

        Runnable readF06 = () -> {
            f06Reader.readF06();
        };

        Thread t1 = new Thread(readF06);
        Thread t2 = new Thread(readBDF);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        List<String> chbdyeList = bdfReader.getChbdyeList();
        List<String> hbdyList = f06Reader.getHbdyList();

        hvasciiList.add("ALTAIR ASCII FILE");
        hvasciiList.add("$DELIMITER =	,");
        hvasciiList.add("$SUBCASE = 	1	Subcase	1");
        hvasciiList.add("$BINDING = 	ELEMENT");
        hvasciiList.add("$COLUMN_INFO = 	ENTITY_ID");
        hvasciiList.add("$RESULT_TYPE = APPLIED-LOAD(s), FREE-CONVECTION(s), FORCED-CONVECTION(s), RADIATION(s), TOTAL(s)");

        for (int i = 0; i < chbdyeList.size(); i++) {
            if (chbdyeList.get(i).trim().substring(9, 16).trim().equals(hbdyList.get(i).trim().substring(0, 8).trim()))

            {
                hvasciiList.add(chbdyeList
                        .get(i)
                        .trim()
                        .substring(17, 24) + ','
                        + hbdyList
                        .get(i)
                        .trim()
                        .substring(9)
                        .replace("0.000000E+00", "0")
                        .replace("     ", ",")
                        .replace("  ", "")
                        .replace(" ", ""));
            } else {
                hvasciiList.add("FATAL ERROR during parsing data, check elements configuration and numbering!");
                break;
            }
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("heathvascii.neu"));
        for (String s : hvasciiList) {
            writer.write(s + "\n");
        }
        writer.close();
    }
}
