public class Subset
{
    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);
        String[] items = StdIn.readAllStrings();

        StdRandom.shuffle(items);

        RandomizedQueue<String> q = new RandomizedQueue<String>();

        for (int i = 0; i < k; i++) {
            q.enqueue(items[i]);
        }

        for (String s : q) {
            StdOut.println(s);
        }
    }
}
