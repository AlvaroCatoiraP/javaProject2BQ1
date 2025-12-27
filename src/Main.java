import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // ====== TEST DEQUE (ArrayDeque maison) ======
        DequeInterface<Integer> d = new ArrayDeque<>();
        d.offerLast(10);
        d.offerLast(20);
        d.offerFirst(5);
        d.offerLast(30);

        System.out.println("Deque = " + d); // [5, 10, 20, 30]
        System.out.println("toArray = " + Arrays.toString(d.toArray()));

        System.out.println("removeFirst = " + d.removeFirst()); // 5
        System.out.println("removeLast  = " + d.removeLast());  // 30
        System.out.println("Deque = " + d); // [10, 20]

        // ====== TEST RECURSIF ======
        DequeRecursifUtils.renverser(d);
        System.out.println("renverser => " + d); // [20, 10]

        boolean anyEven = DequeRecursifUtils.anyMatch(d, x -> x % 2 == 0);
        System.out.println("anyMatch even => " + anyEven); // true
        System.out.println("Deque (restaurée) => " + d); // doit rester [20, 10]

        System.out.println("min => " + DequeRecursifUtils.min(d)); // 10

        // ====== TEST BIG PRIORITY QUEUE ======
        BigPriorityQueueInterface<Integer> bpq = new BigPriorityQueue<>();
        bpq.offer(10);  // [10]
        bpq.offer(20);  // [10, 20]
        bpq.offer(5);   // 5 < head(10) => devant => [5, 10, 20]
        bpq.offer(7);   // 7 >= head(5) => fin => [5, 10, 20, 7]
        bpq.offer(1);   // 1 < head(5) => devant => [1, 5, 10, 20, 7]

        System.out.println("BPQ = " + bpq);
        System.out.println("peek = " + bpq.peek()); // 1
        System.out.println("poll = " + bpq.poll()); // 1
        System.out.println("BPQ after poll = " + bpq);
    }
}
