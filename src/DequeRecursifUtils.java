import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public final class DequeRecursifUtils {
    private DequeRecursifUtils() {}

    // Renverse la deque (récursif) en utilisant removeFirst / offerLast
    public static <T> void renverser(DequeInterface<T> q) {
        Objects.requireNonNull(q);
        try {
            T x = q.removeFirst();
            renverser(q);
            q.offerLast(x);
        } catch (EmptyCollectionException ex) {
            // base case: vide
        }
    }

    // Retourne true si au moins un élément respecte le predicate (et restaure la deque)
    public static <T> boolean anyMatch(DequeInterface<T> q, Predicate<? super T> predicate) {
        Objects.requireNonNull(q);
        Objects.requireNonNull(predicate);
        try {
            T x = q.removeFirst();
            boolean ok = predicate.test(x) || anyMatch(q, predicate);
            q.offerFirst(x); // restauration
            return ok;
        } catch (EmptyCollectionException ex) {
            return false;
        }
    }

    // Retourne le min (ordre naturel). Lance NoSuchElementException si deque vide.
    public static <T extends Comparable<T>> T min(DequeInterface<T> q) throws NoSuchElementException {
        Objects.requireNonNull(q);
        try {
            T x = q.removeFirst();
            try {
                T m = min(q);
                q.offerFirst(x); // restauration
                return (x.compareTo(m) <= 0) ? x : m;
            } catch (NoSuchElementException end) {
                q.offerFirst(x);
                return x;
            }
        } catch (EmptyCollectionException ex) {
            throw new NoSuchElementException("Deque vide");
        }
    }
}
