import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private int size;
    private Node<Item> first;
    private Node<Item> last;

    public Deque()
    {
        // No work needed here.
    }

    public static void main(String[] args)
    {
        Deque<Integer> deque = new Deque<Integer>();

        StdOut.println("Size should be 0:               " + (deque.size() == 0));
        StdOut.println("Deque should be empty:          " + deque.isEmpty());

        deque.addFirst(1);

        StdOut.println("Size should be 1:               " + (deque.size() == 1));
        StdOut.println("Deque should be not empty:      " + !deque.isEmpty());
        StdOut.println("First node matches last:        " + (deque.first == deque.last));
        StdOut.println("Node element is 1:              " + (deque.first.item == 1));
        StdOut.println("Node's next prop is null:       " + (deque.first.next == null));
        StdOut.println("Node's prev prop is null:       " + (deque.first.prev == null));

        deque.addFirst(0);

        StdOut.println("Size should be 2:               " + (deque.size() == 2));
        StdOut.println("First element is 0:             " + (deque.first.item == 0));
        StdOut.println("Second element is 1:            " + (deque.first.next.item == 1));
        StdOut.println("Last element is 1:              " + (deque.last.item == 1));
        StdOut.println("Previous to last element is 0:  " + (deque.last.prev.item == 0));

        int last = deque.removeLast();

        StdOut.println("Last element is 1:              " + (last == 1));
        StdOut.println("Size should be 1:               " + (deque.size() == 1));
        StdOut.println("First node matches last:        " + (deque.first == deque.last));

        int first = deque.removeFirst();

        StdOut.println("First element is 0:             " + (first == 0));
        StdOut.println("Size should be 0:               " + (deque.size() == 0));
        StdOut.println("Deque should be empty:          " + deque.isEmpty());

        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addFirst(0);
        deque.addFirst(-1);
        deque.addFirst(-2);

        StdOut.print("Elements:");
        for (int i : deque) {
            StdOut.print(" " + i);

            // Internal iteration.
            if (i == 0) {
                StdOut.print(" [");
                for (int j : deque) {
                    StdOut.print(" " + j);
                }
                StdOut.print(" ]");
            }
        }
        StdOut.println();
    }

    public Iterator<Item> iterator()
    {
        return new ListIterator();
    }

    /**
     * Deque is empty when size is zero.
     */
    public boolean isEmpty()
    {
        return first == null;
    }

    public int size()
    {
        return size;
    }

    public void addFirst(Item item)
    {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> oldFirst = first;
        first = new Node<Item>(item);
        first.next = oldFirst;

        if (oldFirst == null) {
            last = first;
        } else {
            oldFirst.prev = first; // Link old first back to new first.
        }

        size++;
    }

    public void addLast(Item item)
    {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> oldLast = last;
        last = new Node<Item>(item);
        last.prev = oldLast;

        if (oldLast == null) {
            first = last;
        } else {
            oldLast.next = last; // Link old last next to new last.
        }

        size++;
    }

    public Item removeFirst()
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<Item> oldFirst = first;
        first = first.next;

        // Avoid loitering
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }

        size--;

        return oldFirst.item;
    }

    public Item removeLast()
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<Item> oldLast = last;
        last = last.prev;

        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }

        size--;

        return oldLast.item;
    }

    /**
     * Node with pointers to prev/next items.
     */
    private class Node<Item>
    {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;

        public Node(Item item)
        {
            this.item = item;
        }
    }

    /**
     * Deque iterator.
     */
    private class ListIterator implements Iterator<Item>
    {
        private Node<Item> current = first;

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return current != null;
        }

        public Item next()
        {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;

            return item;
        }
    }
}
