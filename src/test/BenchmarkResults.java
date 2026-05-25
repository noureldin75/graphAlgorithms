package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class BenchmarkResults {

    private record Row(
            int run,
            String algorithm,
            String distribution,
            int v,
            double timeMs,
            long edgesOrReachable,
            long totalWeightOrV
    ) {}

    private final List<Row> rows = new ArrayList<>();



    public void addResult(int run, String algorithm, String distribution,
                          int v, long timeNanos,
                          long edgesOrReachable, long totalWeightOrV) {
        double timeMs = timeNanos / 1_000_000.0;
        rows.add(new Row(run, algorithm, distribution, v, timeMs,
                edgesOrReachable, totalWeightOrV));
    }


    public void exportToCSV(String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            // Header
            pw.println("Run,Algorithm,Distribution,V,Time_ms,EdgesOrReachable,TotalWeightOrV");

            // Data rows
            for (Row r : rows) {
                pw.printf("%d,%s,%s,%d,%.3f,%d,%d%n",
                        r.run(), r.algorithm(), r.distribution(),
                        r.v(), r.timeMs(),
                        r.edgesOrReachable(), r.totalWeightOrV());
            }

            System.out.println("  CSV exported to: " + filePath);
        } catch (IOException e) {
            System.err.println("  Failed to write CSV: " + e.getMessage());
        }
    }


    public void printTable() {
        System.out.println();
        System.out.printf("  %-5s %-10s %-12s %6s %12s %18s %16s%n",
                "Run", "Algorithm", "Distribution", "V", "Time_ms",
                "EdgesOrReachable", "TotalWeightOrV");
        System.out.println("  " + "-".repeat(85));

        for (Row r : rows) {
            System.out.printf("  %-5d %-10s %-12s %6d %12.3f %18d %16d%n",
                    r.run(), r.algorithm(), r.distribution(),
                    r.v(), r.timeMs(),
                    r.edgesOrReachable(), r.totalWeightOrV());
        }
    }

    public int size() {
        return rows.size();
    }
}
