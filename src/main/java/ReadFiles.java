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

        String bdfName = "18075b_2019-04-08_00c00_Cabdoor_thermal_outer_frame_korekcja_konwekcji.bdf";
        String f06Name = "18075b_2019-04-08_00c00_Cabdoor_thermal_outer_frame_korekcja_konwekcji.f06";

        List<String> chbdyeList;
        List<String> hbdyList;

        try (Stream<String> stream = Files.lines(Paths.get(bdfName))) {
            chbdyeList = stream.filter(p -> p.startsWith("CHBDYE  ")).sorted().collect(Collectors.toList());
        }

        try (Stream<String> stream = Files.lines(Paths.get(f06Name))) {
            hbdyList = stream.filter(p -> p.startsWith("                ") & p.contains("      0.000000E+00     ")).sorted().collect(Collectors.toList());
        }

//        System.out.println(chbdyeList.get(0));
//        System.out.println(chbdyeList.get(1));
//        System.out.println(chbdyeList.get(chbdyeList.size() - 1));
//
//        System.out.println(hbdyList.get(0));
//        System.out.println(hbdyList.get(1));
//        System.out.println(hbdyList.get(hbdyList.size() - 1));

//        System.out.println(chbdyeList.get(0).trim().substring(9, 16));
//        System.out.println(chbdyeList.get(0).trim().substring(17, 24));
//        System.out.println(hbdyList.get(0).trim().substring(0, 8));

//        System.out.println(hbdyList.get(0).trim().substring(9).replace("     ", ","));

        List<String> hvasciiList = new ArrayList<>();

        for (int i = 0; i < chbdyeList.size(); i++) {
            if (chbdyeList.get(i).trim().substring(9, 16).trim().equals(hbdyList.get(i).trim().substring(0, 8).trim())) {
                hvasciiList.add(chbdyeList.get(i).trim().substring(17, 24) + ',' + hbdyList.get(i).trim().substring(9).replace("     ", ",").trim());
            } else {
                System.out.println("error");
                break;
            }
        }
        System.out.println(System.currentTimeMillis());
        Files.write(Paths.get("hvascii.neu"), hvasciiList);
        System.out.println(System.currentTimeMillis());

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("hvascii_v2.neu")))
        {
            for (String s : hvasciiList) {
                writer.write(s + "\n");
            }
        }
        System.out.println(System.currentTimeMillis());
    }


}
