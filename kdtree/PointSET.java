import java.util.Collections;
import java.util.TreeSet;

public class PointSET
{
    private final TreeSet<Point2D> set = new TreeSet<Point2D>();
    
    public PointSET()
    {
    }
    
    public boolean isEmpty()
    {
        return set.isEmpty();
    }
    
    public int size()
    {
        return set.size();
    }
    
    public void insert(Point2D point)
    {
        validate(point).set.add(point);
    }
    
    public boolean contains(Point2D point)
    {
        return validate(point).set.contains(point);
    }
    
    public void draw()
    {
        for (Point2D point : set) {
            point.draw();
        }
    }
    
    public Point2D nearest(Point2D point)
    {
        if (validate(point).isEmpty()) {
            return null;
        }
        
        // Find best possible radius to check closest points first.
        double step = (double) 50 / size(), radius = 0;
        
        Point2D result = null;
        boolean lastCheck;
        
        do {
            radius += step;
            
            double xmin = point.x() - radius;
            double ymin = point.y() - radius;
            double xmax = point.x() + radius;
            double ymax = point.y() + radius;
            
            Point2D first = new Point2D(xmin, ymin);
            Point2D last = new Point2D(xmax, ymax);
            
            // This is last iteration.
            lastCheck = xmin <= 0 && ymin <= 0 && xmax >= 1 && ymax >= 1;
            
            TreeSet<Point2D> subSet = (TreeSet<Point2D>) set.subSet(first, true, last, true);
            
            // No points found, increase radius.
            if (subSet.size() == 0) {
                continue;
            }
                        
            double min = Double.POSITIVE_INFINITY;
            double radiusSquare = radius * radius;
            for (Point2D item : subSet) {
                
                double x = item.x() - point.x();
                double y = item.y() - point.y();
                
                // Test point is inside radius unless this is last check.
                if (!lastCheck && (x*x + y*y > radiusSquare)) {
                    continue;
                }
                
                double current = item.distanceSquaredTo(point);
                if (current < min) {
                    result = item;
                    min = current;
                }
            }
        } while (result == null && !lastCheck);
     
        return result;
    }
    
    public Iterable<Point2D> range(RectHV rect)
    {      
        if (validate(rect).isEmpty()) {
            return Collections.emptySet();
        }
        
        Point2D first = new Point2D(rect.xmin(), rect.ymin());
        Point2D last = new Point2D(rect.xmax(), rect.ymax());
                
        Queue<Point2D> q = new Queue<Point2D>();
        
        // Limit points set by rectangle.
        for (Point2D point : set.subSet(first, true, last, true)) {            
            if (rect.contains(point)) {
                q.enqueue(point);
            }
        }
        
        return q;
    }
    
    public static void main(String[] args)
    {
        PointSET set = new PointSET();
        set.insert(new Point2D(0.9, 0.5));
        set.insert(new Point2D(0.2, 0.5));
        set.insert(new Point2D(0.3, 0.5));
        set.insert(new Point2D(0.4, 0.5));
        set.insert(new Point2D(0.1, 0.5));
        set.insert(new Point2D(0.6, 0.5));
        set.insert(new Point2D(0.5, 0.5));
        set.insert(new Point2D(0.7, 0.5));
        
        StdOut.println("Size: " + set.size());
        
        // Draw set.
        StdDraw.setPenRadius(.01);
        set.draw();
        
        // Lookup 
        Point2D lookup = new Point2D(0.4, 0.5);
        
        // Draw lookup point.
        StdDraw.setPenColor(StdDraw.RED);
        lookup.draw();
        
        Point2D nearest = set.nearest(lookup);
        
        // Draw nearest.
        StdDraw.setPenColor(StdDraw.BLUE);
        nearest.draw();
        StdDraw.show(0);
        
        StdOut.println();
    }
    
    private PointSET validate(Object object)
    {
        if (object == null) {
            throw new NullPointerException();
        }
        
        return this;
    }
}
