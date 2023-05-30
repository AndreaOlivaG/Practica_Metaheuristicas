import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        File dir = new File("instances");
        String[] instancesArray = dir.list();
        String absolutePath = dir.getAbsolutePath() + "/";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Results");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("#Paths");
        row.createCell(2).setCellValue("MaxPossibleScore");
        row.createCell(3).setCellValue("T_max");
        row.createCell(4).setCellValue("MaxDistance");
        row.createCell(5).setCellValue("Score");
        row.createCell(6).setCellValue("Time (s)");

        Path experiments_path = Paths.get("experiments/");
        Files.createDirectories(experiments_path);

        if (instancesArray != null) {
            for (int alg = 0; alg < 6; alg++) {
                List<String> instances = new LinkedList<>();
                for (String s : instancesArray) {
                    instances.add(absolutePath + s);
                }
                Collections.sort(instances);

                int i = 1;
                String name = "";
                long startTime, timeToSolution = 0;
                for (String path : instances) {
                    TRP_Instance instance = new TRP_Instance(path);
                    System.out.println(instance.getName());
                    TRP_Solution solution = new TRP_Constructive().constructSolution(instance);
                    startTime = System.nanoTime();
                    switch (alg) {
                        case 0 -> {
                            BVND_1 bvnd_1 = new BVND_1();
                            bvnd_1.improve(solution);
                            timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                            name = "Basic_VND_1.xlsx";
                        }
                        case 1 -> {
                            BVND_2 bvnd_2 = new BVND_2();
                            bvnd_2.improve(solution);
                            timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                            name = "Basic_VND_2.xlsx";
                        }
                        case 2 -> {
                            CVND_1 cvnd_1 = new CVND_1();
                            cvnd_1.improve(solution);
                            timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                            name = "Cyclic_VND_1.xlsx";
                        }
                        case 3 -> {
                            CVND_2 cvnd_2 = new CVND_2();
                            cvnd_2.improve(solution);
                            timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                            name = "Cyclic_VND_2.xlsx";
                        }
                        case 4 -> {
                            PVND_1 pvnd_1 = new PVND_1();
                            pvnd_1.improve(solution);
                            timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                            name = "Pipe_VND_1.xlsx";
                        }
                        case 5 -> {
                            PVND_2 pvnd_2 = new PVND_2();
                            pvnd_2.improve(solution);
                            timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                            name = "Pipe_VND_2.xlsx";
                        }
                    }

                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(instance.getName());
                    row.createCell(1).setCellValue(solution.getInstance().getNumPaths());
                    row.createCell(2).setCellValue(solution.getInstance().getMaxPossibleScore());
                    row.createCell(3).setCellValue(String.valueOf(solution.getMaxTime()));
                    row.createCell(4).setCellValue(solution.getMaxPathTime());
                    row.createCell(5).setCellValue(solution.getScore());
                    row.createCell(6).setCellValue(timeToSolution);
                    i += 1;
                }

                FileOutputStream file = new FileOutputStream(experiments_path + "/" + name);
                workbook.write(file);
                file.close();
            }
        }
    }

}