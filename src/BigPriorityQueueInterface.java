
/**
 * we model a queue with a strange and big priority rule:
 * if the element to add is smaller than the head, it cuts ahead of everyone,
 * otherwise it goes at the end of the queue
 * The elements of our strange priority queue are ordered according to
 * their natural ordering, or by a comparator provided
 * at queue construction time, depending on which constructor is used.
 * A priority queue does not permit null elements.
 * A priority queue relying on natural ordering also does not permit
 * insertion of non-comparable objects (doing so may result in ClassCastException).
 */
public interface BigPriorityQueueInterface<T> {

    /**
     * Inserts the specified element into this priority queue.
     * @param e the element to add
     * @return true if the element was added to this queue, else false
     * @throws ClassCastException if the specified element cannot be compared with elements currently
     * in this priority queue according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(T e);

    /**
     * Retrieves and removes the head of this queue,
     * or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    public T poll();

    /**
     * Returns true if this queue contains the specified element.
     * More formally, returns true if and only if this queue contains at least one element e such that o.equals(e).
     * @param o element whose presence in this queue is to be tested
     * @return true if this queue contains the specified element, false otherwise
     */
    public boolean contains(Object o);

    /**
     * Retrieves, but does not remove,
     * the head of this queue, or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    public T peek();

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object
     */
    String toString();

}
