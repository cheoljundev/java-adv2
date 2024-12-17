package io.file.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FIleCopyMainV3 {


    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        Path source = Path.of("temp/copy.dat");
        Path target = Path.of("temp/copy_new.dat");
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        // Files.copy는 파일의 데이터를 자바로 불러오지 않고, 운영 체제의 파일 복사 기능을 사용한다.

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
