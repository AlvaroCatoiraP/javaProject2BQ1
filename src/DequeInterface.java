import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A linear collection that supports element insertion and removal at both ends.
 * The name deque is short for "double ended queue".
 */
public interface DequeInterface<E> {

    void offerFirst(E e) throws NullPointerException;

    void offerLast(E e) throws NullPointerException;

    E removeFirst() throws EmptyCollectionException;

    E removeLast() throws EmptyCollectionException;

    E peekFirst() throws EmptyCollectionException;

    E peekLast() throws EmptyCollectionException;

    int size();

    boolean contains(Object o) throws NullPointerException;

    String toString();

    boolean containsAll(Collection<?> c);

    boolean offerAll(Collection<? extends E> c);

    boolean containsIf(Predicate<? super E> filter);

    void forEach(Consumer<? super E> action);

    Object[] toArray();
}
