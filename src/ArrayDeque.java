import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ArrayDeque<E> implements DequeInterface<E> {

    private Object[] elements;
    private int head;
    private int size;

    public static long nbOp = 0;

    public ArrayDeque() { this(16); }

    public ArrayDeque(int capacity) {
        if (capacity <= 0) capacity = 16;
        this.elements = new Object[capacity];
        this.head = 0;
        this.size = 0;
    }

    private int idx(int i) {
        nbOp++;
        return (head + i) % elements.length;
    }

    private void ensureCapacity(int minCapacity) {
        nbOp++;
        if (minCapacity <= elements.length) return;
        int newCap = Math.max(minCapacity, elements.length * 2);
        Object[] newArr = new Object[newCap];
        for (int i = 0; i < size; i++) {
            nbOp++;
            newArr[i] = elements[idx(i)];
        }
        elements = newArr;
        head = 0;
    }

    @Override
    public void offerFirst(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void offerLast(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        elements[idx(size)] = e;
        size++;
    }

    @Override
    public E removeFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return val;
    }

    @Override
    public E removeLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        int lastIndex = idx(size - 1);
        @SuppressWarnings("unchecked")
        E val = (E) elements[lastIndex];
        elements[lastIndex] = null;
        size--;
        return val;
    }

    @Override
    public E peekFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[head];
        return val;
    }

    @Override
    public E peekLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[idx(size - 1)];
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
            if (o.equals(elements[idx(i)])) return true;
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
            E v = (E) elements[idx(i)];
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
            E v = (E) elements[idx(i)];
            action.accept(v);
        }
    }

    @Override
    public Object[] toArray() {
        nbOp++;
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            nbOp++;
            arr[i] = elements[idx(i)];
        }
        return arr;
    }

    @Override
    public String toString() {
        nbOp++;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            nbOp++;
            sb.append(elements[idx(i)]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
