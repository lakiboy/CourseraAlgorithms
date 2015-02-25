import java.util.Arrays;

public class Fast
{
    public static void main(String[] args)
    {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        In in = new In(args[0]);
        int total = in.readInt(), i;
        Point[] points = new Point[total];
        Point[] aux = new Point[total];

        // Collect and draw points
        for (i = 0; i < total; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            aux[i] = points[i];
            points[i].draw();
        }

        if (total < 4) {
            return;
        }

        for (i = 0; i < total; i++) {
            Arrays.sort(aux, points[i].SLOPE_ORDER);

            int j, size;
            double prevSlope = calcSlope(aux, 0, 1), currentSlope;

            // Size starts at 2 as two points always on the same line.
            for (j = 2, size = 2; j < total; j++) {

                currentSlope = calcSlope(aux, j - 1, j);

                // 3 points on the same line -> increase size.
                if (doubleEquals(prevSlope, currentSlope)) {
                    size++;
                } else {
                    // j is not on the same line with j - 1 and j - 2. Look for collected figure.
                    if (size >= 4) {
                        printFigure(aux, j - size, j);
                    }

                    // This is the key! Always start comparison with points[i].
                    exchange(aux, j - 1, j - size);

                    // New figure starts at 2 again.
                    size = 2;

                    // Slope between points[i] and current point.
                    prevSlope = calcSlope(aux, j - 1, j);
                }
            }

            // Ensure we don't miss last figure.
            if (size >= 4) {
                printFigure(aux, j - size, j);
            }
        }

        StdDraw.show(0);
    }

    private static void printFigure(Point[] points, int start, int end)
    {
        assert onTheSameLine(points, start, end);

        int size = end - start;
        Point[] figure = new Point[size];

        // Copy points to figure.
        for (int i = 0; i < size; i++) {
            figure[i] = points[i + start];
        }

        Arrays.sort(figure);

        // Can print only if current point is the smallest one.
        if (figure[0] == points[start]) {
            for (Point p : figure) {
                if (p == figure[0]) {
                    StdOut.print(p);
                } else {
                    StdOut.print(" -> " + p);
                }
            }
            StdOut.println();

            figure[0].drawTo(figure[size - 1]);
        }
    }

    /**
     * Test if [start..end) points in the array on the same line.
     */
    private static boolean onTheSameLine(Point[] points, int start, int end)
    {
        for (int i = start + 2; i < end; i++) {
            if (!onTheSameLine(points, start, i - 1, i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Test if 3 points on the same line.
     */
    private static boolean onTheSameLine(Point[] points, int i, int j, int k)
    {
        return doubleEquals(calcSlope(points, i, j), calcSlope(points, j, k));
    }

    /**
     * Helper function to calculate slope between two points in array.
     */
    private static double calcSlope(Point[] points, int i, int j)
    {
        return points[i].slopeTo(points[j]);
    }

    /**
     * Helper function to compare doubles for equality.
     */
    private static boolean doubleEquals(double a, double b)
    {
        return Double.compare(a, b) == 0;
    }

    private static void exchange(Point[] points, int i, int j)
    {
        Point tmp = points[i];
        points[i] = points[j];
        points[j] = tmp;
    }
}
