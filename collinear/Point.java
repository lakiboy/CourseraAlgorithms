import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point>
{
    public final Comparator<Point> SLOPE_ORDER = new SlopeComparator();
    private final int x, y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static void main(String[] args)
    {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(20, 30);
        Point p3 = new Point(30, 30);
        Point p4 = new Point(20, 30);

        //        StdOut.println(p1.compareTo(p2));
        //        StdOut.println(p2.compareTo(p1));
        //        StdOut.println(p2.compareTo(p3));
        //        StdOut.println(p2.compareTo(p4));

        Point[] points = new Point[4];
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;

        Arrays.sort(points, points[2].SLOPE_ORDER);

        for (Point p : points) {
            StdOut.println(p);
        }
    }

    public void draw()
    {
        StdDraw.point(x, y);
    }

    public void drawTo(Point to)
    {
        StdDraw.line(x, y, to.x, to.y);
    }

    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    public double slopeTo(Point p)
    {
        // Treat the slope of a degenerate line segment (between a point and itself) as negative infinity.
        if (compareTo(p) == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        // Treat the slope of a horizontal line segment as positive zero.
        if (y == p.y) {
            return 0.0;
        }

        // Treat the slope of a vertical line segment as positive infinity.
        if (x == p.x) {
            return Double.POSITIVE_INFINITY;
        }

        return (double) (p.y - y) / (double) (p.x - x);
    }

    public int compareTo(Point p)
    {
        if (y < p.y) {
            return -1;
        } else if (y > p.y) {
            return 1;
        } else if (x < p.x) {
            return -1;
        } else if (x > p.x) {
            return 1;
        } else {
            return 0;
        }
    }

    private class SlopeComparator implements Comparator<Point>
    {
        public int compare(Point p1, Point p2)
        {
            return Double.compare(slopeTo(p1), slopeTo(p2));
        }
    }
}
