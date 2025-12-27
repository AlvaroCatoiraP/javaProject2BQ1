import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ArrayDequeNaive<E> implements DequeInterface<E> {

    private Object[] elements;
    private int size;

    public static long nbOp = 0;

    public ArrayDequeNaive() {
        this(10);
    }

    public ArrayDequeNaive(int capacity) {
        if (capacity <= 0) capacity = 10;
        this.elements = new Object[capacity];
        this.size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        nbOp++;
        if (minCapacity <= elements.length) return;
        int newCap = Math.max(minCapacity, elements.length * 2);
        Object[] newArr = new Object[newCap];
        for (int i = 0; i < size; i++) {
            nbOp++;
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    @Override
    public void offerFirst(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);

        // shift right
        for (int i = size; i > 0; i--) {
            nbOp++;
            elements[i] = elements[i - 1];
        }
        elements[0] = e;
        size++;
    }

    @Override
    public void offerLast(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        elements[size++] = e;
    }

    @Override
    public E removeFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[0];

        // shift left
        for (int i = 0; i < size - 1; i++) {
            nbOp++;
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return val;
    }

    @Override
    public E removeLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[size - 1];
        elements[size - 1] = null;
        size--;
        return val;
    }

    @Override
    public E peekFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[0];
        return val;
    }

    @Override
    public E peekLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[size - 1];
        return val;
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
        for (int i = 0; i < size; i++) {
            nbOp++;
            if (o.equals(elements[i])) return true;
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
        for (int i = 0; i < size; i++) {
            nbOp++;
            @SuppressWarnings("unchecked")
            E v = (E) elements[i];
            if (filter.test(v)) return true;
        }
        return false;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        nbOp++;
        Objects.requireNonNull(action);
        for (int i = 0; i < size; i++) {
            nbOp++;
            @SuppressWarnings("unchecked")
            E v = (E) elements[i];
            action.accept(v);
        }
    }

    @Override
    public Object[] toArray() {
        nbOp++;
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            nbOp++;
            arr[i] = elements[i];
        }
        return arr;
    }

    @Override
    public String toString() {
        nbOp++;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            nbOp++;
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
