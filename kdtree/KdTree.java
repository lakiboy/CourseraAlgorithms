import java.util.Collections;

public class KdTree
{
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;
    
    private static final boolean LEFT  = false;
    private static final boolean RIGHT = true;
    
    private int size;
    private Node root;
    
    private static class Node
    {        
        private Node left;
        private Node right;
        private double xmin, ymin, xmax, ymax, x, y;
        
        /**
         * Constructor for root node.
         */
        Node(Point2D point)
        {
            x = point.x();
            y = point.y();
            
            xmin = 0.0;
            ymin = 0.0;
            xmax = 1.0;
            ymax = 1.0;
        }
        
        /**
         * Constructor for descendant node of the parent.
         */
        Node(double x, double y, Node parent, boolean orientation, boolean position)
        {            
            this.x = x;
            this.y = y;
            
            xmin = parent.xmin; 
            ymin = parent.ymin; 
            xmax = parent.xmax; 
            ymax = parent.ymax;
            
            if (orientation == HORIZONTAL) {
                if (position == LEFT) {
                    xmax = parent.x;
                } else {
                    xmin = parent.x;
                }        
            } else {
                if (position == LEFT) {
                    ymax = parent.y;
                } else {
                    ymin = parent.y;
                }
            }     
        }
        
        /**
         * Test if given rectangle intersects with splitting line of the node.
         */
        boolean intersectsLine(RectHV rect, boolean orientation)
        {
            if (orientation == HORIZONTAL) {
                return xmax >= rect.xmin() && y >= rect.ymin() && rect.xmax() >= xmin && rect.ymax() >= y;
            } else {
                return x >= rect.xmin() && ymax >= rect.ymin() && rect.xmax() >= x && rect.ymax() >= ymin;
            }
        }
        
        /**
         * Which side given rectangle belongs to?
         */
        Node leftOrRight(RectHV rect, boolean orientation)
        {
            if (orientation == HORIZONTAL) {
                return rect.ymax() < y ? left : right;
            } else {
                return rect.xmax() < x ? left : right;
            }
        }
        
        /**
         * Closest distance to the point from node's rectangle.
         */
        double distanceSquaredTo(double pointX, double pointY) 
        {
            double dx = 0.0, dy = 0.0;
            
            if (pointX < xmin) { 
                dx = pointX - xmin;
            } else if (pointX > xmax) {
                dx = pointX - xmax;
            }
            
            if (pointY < ymin) { 
                dy = pointY - ymin;
            } else if (pointY > ymax) { 
                dy = pointY - ymax;
            }
            
            return dx * dx + dy * dy;            
        }
        
        double distanceSquaredToPoint(double pointX, double pointY)
        {
            double dx = x - pointX;
            double dy = y - pointY;
            
            return dx * dx + dy * dy;
        }
        
        /**
         * Test if current node contains the given point.
         */
        boolean containsPoint(double pointX, double pointY) 
        {
            return pointX >= xmin && pointX <= xmax && pointY >= ymin && pointY <= ymax;
        }
        
        boolean pointEquals(double pointX, double pointY)
        {
            return x == pointX && y == pointY;
        }
        
        boolean pointWithin(RectHV rect)
        {
            return rect.contains(createPoint());
        }
        
        Point2D createPoint()
        {
            return new Point2D(x, y);
        }
    }
    
    public KdTree()
    {
    }
    
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    public int size()
    {
        return size;
    }
    
    public void insert(Point2D point)
    {
        if (validate(point).root == null) {
            root = new Node(point);
            size = 1; // Firt node added.
        } else {
            size += insert(root, point, HORIZONTAL); // Increare size by 1 or 0 if point already in the tree.
        }
    }
    
    public boolean contains(Point2D point)
    {
        if (validate(point).isEmpty()) {
            return false;
        }
        
        return contains(root, point, HORIZONTAL);
    }
   
    public void draw()
    {
        draw(root, VERTICAL);   
        StdDraw.show(0);
    }
    
    public Iterable<Point2D> range(RectHV rect)
    {     
        if (validate(rect).isEmpty()) {
            return Collections.emptySet();
        }
        
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, queue, VERTICAL);
        
        return queue;
    }
    
    public Point2D nearest(Point2D point)
    {
        if (validate(point).isEmpty()) {
            return null;
        }
        
        return nearest(root, point, Double.POSITIVE_INFINITY);
    }
    
    public static void main(String[] args)
    {        
        KdTree tree = new KdTree();
        
        tree.insert(new Point2D(0.206107, 0.095492));
        tree.insert(new Point2D(0.975528, 0.654508));
        tree.insert(new Point2D(0.024472, 0.345492));
        tree.insert(new Point2D(0.793893, 0.095492));
        tree.insert(new Point2D(0.793893, 0.904508));
        tree.insert(new Point2D(0.793893, 0.904508));
        tree.insert(new Point2D(0.975528, 0.345492));
        tree.insert(new Point2D(0.206107, 0.904508));
        tree.insert(new Point2D(0.500000, 0.000000));
        tree.insert(new Point2D(0.024472, 0.654508));
        tree.insert(new Point2D(0.500000, 1.000000));
        tree.insert(new Point2D(0.500000, 1.000000));
                        
        StdOut.println("Size: " + tree.size());
        tree.draw();
        
        Point2D nearest = tree.nearest(new Point2D(.81, .30));
        StdOut.println("Nearest: " + nearest);
    }
   
    private KdTree validate(Object object)
    {
        if (object == null) {
            throw new NullPointerException();
        }
        
        return this;
    }
    
    private int insert(Node node, Point2D point, boolean orientation)
    {     
        double x = point.x(), y = point.y();
        
        if (node.pointEquals(x, y)) {
            return 0;
        }
        
        int result = 1;
        
        if ((orientation == HORIZONTAL && x < node.x) || (orientation == VERTICAL && y < node.y)) {
            if (node.left == null) {
                node.left = new Node(x, y, node, orientation, LEFT);
            } else {
                result = insert(node.left, point, !orientation);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(x, y, node, orientation, RIGHT);
            } else {
                result = insert(node.right, point, !orientation);
            }
        }
        
        return result;
    }
    
    private boolean contains(Node node, Point2D point, boolean orientation) 
    {
        if (node == null) {
            return false;
        }
        
        double x = point.x(), y = point.y();
        
        if (node.pointEquals(x, y)) {
            return true;
        }
        
        if ((orientation == HORIZONTAL && x < node.x) || (orientation == VERTICAL && y < node.y)) {
            return contains(node.left, point, !orientation);
        } else {
            return contains(node.right, point, !orientation);
        }    
    }
    
    private void range(Node node, RectHV rect, Queue<Point2D> queue, boolean position)
    {
        if (node == null) {
            return;
        }
        
        // Instead of checking whether the query rectangle intersects the rectangle corresponding to a node, 
        // it suffices to check only whether the query rectangle intersects the splitting line segment: 
        // if it does, then recursively search both subtrees; otherwise, 
        // recursively search the one subtree where points intersecting the query rectangle could be.
        
        if (node.intersectsLine(rect, position)) {
            if (node.pointWithin(rect)) {
                queue.enqueue(node.createPoint());
            }         
            range(node.left, rect, queue, !position);
            range(node.right, rect, queue, !position);
        } else {
            range(node.leftOrRight(rect, position), rect, queue, !position);
        }
    }
    
    private void draw(Node node, boolean orientation)
    {
        if (node == null) {
            return;
        }
        
        double x1 = node.xmin, y1 = node.ymin, x2 = node.xmax, y2 = node.ymax;
       
        // Draw line.
        if (orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            x1 = node.x;
            x2 = node.x;
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            y1 = node.y;
            y2 = node.y;
        }
        StdDraw.setPenRadius(.01);
        StdDraw.line(x1, y1, x2, y2);
        
        // Draw point.
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.02);
        node.createPoint().draw();
        
        draw(node.left, !orientation);
        draw(node.right, !orientation);
    }
    
    private Point2D nearest(Node node, Point2D point, double currentDistance)
    {                     
        if (node == null) {
            return null;
        }
        
        double x = point.x(), y = point.y();
        
        if (node.distanceSquaredTo(x, y) > currentDistance) {
            return null;
        }
        
        // Update min distance.
        double distanceToPoint = node.distanceSquaredToPoint(x, y);
        double minDistance = distanceToPoint < currentDistance ? distanceToPoint : currentDistance;
        
        // Current result.
        Point2D result = null, left, right;
        
        if (node.left != null && node.left.containsPoint(x, y)) {
            
            // Check left first.
            left = nearest(node.left, point, minDistance);
            if (left != null) {
                double distanceToLeft = left.distanceSquaredTo(point);
                if (distanceToLeft < minDistance) {
                    result = left;
                    minDistance = distanceToLeft;
                }
            }

            // Check right with min distance from left search.
            right = nearest(node.right, point, minDistance);
            if (right != null) {
                double distanceToRight = right.distanceSquaredTo(point);
                if (distanceToRight < minDistance) {
                    result = right;
                }
            }
            
        } else {
            
            // Check right first.
            right = nearest(node.right, point, minDistance);
            if (right != null) {
                double distanceToRight = right.distanceSquaredTo(point);
                if (distanceToRight < minDistance) {
                    result = right;
                    minDistance = distanceToRight;
                }
            }
            
            // Check left with min distance from right search.
            left = nearest(node.left, point, minDistance);
            if (left != null) {
                double distanceToLeft = left.distanceSquaredTo(point);
                if (distanceToLeft < minDistance) {
                    result = left;
                }
            }
        }
                
        return result == null ? node.createPoint() : result;
    }
}
