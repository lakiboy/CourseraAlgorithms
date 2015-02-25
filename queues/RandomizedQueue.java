import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] items;

    // Number of elements in the queue.
    private int size;

    // Next available spot to insert new item.
    private int last;

    public RandomizedQueue()
    {
        items = (Item[]) new Object[2]; // Default array size = 2
    }

    public static void main(String[] args)
    {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

        StdOut.println("Queue size is 0:       " + (q.size() == 0));
        StdOut.println("Queue is empty:        " + q.isEmpty());

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        StdOut.println("Queue size is 3:       " + (q.size() == 3));
        StdOut.println("Queue is not empty:    " + !q.isEmpty());

        for (int i = 0; i < 5; i++) {
            StdOut.println("Random sample element: " + q.sample());
        }

        int r1 = q.dequeue();

        StdOut.println("Queue size is 2:       " + (q.size() == 2));
        StdOut.println("Queue is not empty:    " + !q.isEmpty());

        int r2 = q.dequeue();

        StdOut.println("Queue size is 1:       " + (q.size() == 1));
        StdOut.println("Queue is not empty:    " + !q.isEmpty());

        int r3 = q.dequeue();

        StdOut.println("Queue size is 0:       " + (q.size() == 0));
        StdOut.println("Queue is empty:        " + q.isEmpty());
        StdOut.println("Random elements:       " + r1 + ", " + r2 + ", " + r3);

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);

        StdOut.print("Elements:");
        for (int i : q) {
            StdOut.print(" " + i);

            // Internal iteration.
            if (i == 3) {
                StdOut.print(" [");
                for (int j : q) {
                    StdOut.print(" " + j);
                }
                StdOut.print(" ]");
            }
        }
        StdOut.println();
    }

    public Iterator<Item> iterator()
    {
        // Copy array for iterator to use.
        Item[] copy = (Item[]) new Object[size];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];
        }

        return new RandomIterator(copy);
    }

    /**
     * Queue is empty when size = 0.
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }

    /**
     * Simplest enqueue possible. Insert item to the next available index.
     */
    public void enqueue(Item item)
    {
        if (item == null) {
            throw new NullPointerException();
        }

        // Items array is full -> resize.
        if (size == items.length) {
            resize(items.length * 2);
        }

        items[last++] = item;

        size++;
    }

    public Item dequeue()
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int i = StdRandom.uniform(0, last);
        Item item = items[i];

        last--;
        size--;

        // In case randomly selected element is not last -> swap it with last.
        if (i != last) {
            items[i] = items[last];
            items[last] = null;
        } else {
            items[i] = null; // Avoid loitering.
        }

        // Shrink items array.
        if (size > 0 && size == items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }

    public Item sample()
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // Get index of 0...last.
        int i = StdRandom.uniform(0, last);

        return items[i];
    }

    private void resize(int newSize)
    {
        Item[] copy = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    private class RandomIterator implements Iterator<Item>
    {
        private Item[] elems;
        private int index;

        public RandomIterator(Item[] elems)
        {
            StdRandom.shuffle(elems);

            this.elems = elems;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return index < elems.length;
        }

        public Item next()
        {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return elems[index++];
        }
    }
}
