import java.util.Comparator;
import java.util.Objects;

public class BigPriorityQueue<T> implements BigPriorityQueueInterface<T> {

    private final DequeInterface<T> deque;
    private final Comparator<? super T> comparator;

    public BigPriorityQueue() {
        this(null);
    }

    public BigPriorityQueue(Comparator<? super T> comparator) {
        this.deque = new ArrayDeque<>();
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super T>) a).compareTo(b);
    }

    @Override
    public boolean offer(T e) {
        Objects.requireNonNull(e);

        if (peek() == null) {
            deque.offerLast(e);
            return true;
        }

        T head = peek();
        // si non comparable => ClassCastException possible (OK selon spec)
        if (compare(e, head) < 0) deque.offerFirst(e);
        else deque.offerLast(e);

        return true;
    }

    @Override
    public T poll() {
        try {
            return deque.removeFirst();
        } catch (EmptyCollectionException ex) {
            return null;
        }
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            return deque.contains(o);
        } catch (NullPointerException ex) {
            return false;
        }
    }

    @Override
    public T peek() {
        try {
            return deque.peekFirst();
        } catch (EmptyCollectionException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return deque.toString();
    }
}
