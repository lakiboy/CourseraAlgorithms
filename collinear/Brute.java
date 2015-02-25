import java.util.Arrays;

public class Brute
{
    public static void main(String[] args)
    {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        In in = new In(args[0]);
        int total = in.readInt();
        Point[] points = new Point[total];

        // Collect and draw points
        for (int i = 0; i < total; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }

        Arrays.sort(points);

        int i, j, k, l;
        double s1, s2, s3;

        for (i = 0; i < total - 3; i++) {
            for (j = i + 1; j < total - 2; j++) {
                s1 = points[i].slopeTo(points[j]); // P -> Q

                for (k = j + 1; k < total - 1; k++) {
                    s2 = points[j].slopeTo(points[k]); // Q -> R

                    // P -> Q -> R on the same line?
                    if (Double.compare(s1, s2) != 0) {
                        continue;
                    }

                    for (l = k + 1; l < total; l++) {
                        s3 = points[k].slopeTo(points[l]); // R -> S

                        // P -> Q -> R -> S on the same line?
                        if (Double.compare(s2, s3) == 0) {
                            points[i].drawTo(points[l]);
                            StdOut.println(points[i] + " -> " + points[j] + " -> " + points[k] + " -> " + points[l]);
                        }
                    }
                }
            }
        }

        StdDraw.show(0);
    }
}
