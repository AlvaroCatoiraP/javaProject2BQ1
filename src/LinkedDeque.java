import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LinkedDeque<E> implements DequeInterface<E> {

    private static class Node<E> {
        E value;
        Node<E> prev, next;
        Node(E value) { this.value = value; }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    public static long nbOp = 0;

    @Override
    public void offerFirst(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        Node<E> n = new Node<>(e);
        if (size == 0) {
            first = last = n;
        } else {
            n.next = first;
            first.prev = n;
            first = n;
        }
        size++;
    }

    @Override
    public void offerLast(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        Node<E> n = new Node<>(e);
        if (size == 0) {
            first = last = n;
        } else {
            n.prev = last;
            last.next = n;
            last = n;
        }
        size++;
    }

    @Override
    public E removeFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        E val = first.value;
        if (size == 1) {
            first = last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        size--;
        return val;
    }

    @Override
    public E removeLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        E val = last.value;
        if (size == 1) {
            first = last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        size--;
        return val;
    }

    @Override
    public E peekFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        return first.value;
    }

    @Override
    public E peekLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        return last.value;
    }

    @Override
    public int size() {
        nbOp++;
        return size;
    }

    @Override
    public boolean contains(Object o) throws NullPointerException {
        nbOp++;
        if (o == null) throw new NullPointerException();
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            if (o.equals(cur.value)) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        nbOp++;
        Objects.requireNonNull(c);
        for (Object x : c) {
            nbOp++;
            if (x == null) continue;
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean offerAll(Collection<? extends E> c) {
        nbOp++;
        Objects.requireNonNull(c);
        boolean changed = false;
        for (E x : c) {
            nbOp++;
            if (x == null) continue;
            offerFirst(x);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean containsIf(Predicate<? super E> filter) {
        nbOp++;
        Objects.requireNonNull(filter);
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            if (filter.test(cur.value)) return true;
        }
        return false;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        nbOp++;
        Objects.requireNonNull(action);
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            action.accept(cur.value);
        }
    }

    @Override
    public Object[] toArray() {
        nbOp++;
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            arr[i++] = cur.value;
        }
        return arr;
    }

    @Override
    public String toString() {
        nbOp++;
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = first;
        while (cur != null) {
            nbOp++;
            sb.append(cur.value);
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
