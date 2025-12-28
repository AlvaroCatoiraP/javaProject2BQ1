import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implémentation d'une deque (double-ended queue) basée sur un tableau circulaire.
 * Permet d'ajouter/retirer en tête et en queue en O(1) amorti.
 *
 * @param <E> type des éléments stockés
 */
public class ArrayDeque<E> implements DequeInterface<E> {

    private Object[] elements;

    private int head;

    private int size;

    /**
     * Compteur d'opérations (instrumentation) : s'incrémente à chaque opération
     * jugée significative dans les méthodes (utile pour analyser la complexité).
     */
    public static long nbOp = 0;

    /** Construit une deque avec une capacité initiale par défaut (16). */
    public ArrayDeque() { this(16); }

    /**
     * Construit une deque avec une capacité initiale donnée.
     * Si la capacité est <= 0, on retombe sur 16.
     *
     * @param capacity capacité initiale souhaitée
     */
    public ArrayDeque(int capacity) {
        if (capacity <= 0) capacity = 16;
        this.elements = new Object[capacity];
        this.head = 0;
        this.size = 0;
    }

    /**
     * Convertit un index logique i (0..size-1) en index réel dans le tableau circulaire.
     *
     * @param i index logique (décalé depuis head)
     * @return index réel dans elements
     */
    private int idx(int i) {
        nbOp++;
        return (head + i) % elements.length;
    }

    /**
     * Garantit une capacité minimale pour pouvoir insérer minCapacity éléments.
     * Si nécessaire, redimensionne le tableau (x2) et recopie les éléments
     * dans l'ordre logique (head devient 0).
     *
     * @param minCapacity capacité minimale requise
     */
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

    /**
     * Ajoute un élément en tête de la deque.
     *
     * @param e élément à ajouter (non null)
     * @throws NullPointerException si e est null
     */
    @Override
    public void offerFirst(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    /**
     * Ajoute un élément en queue de la deque.
     *
     * @param e élément à ajouter (non null)
     * @throws NullPointerException si e est null
     */
    @Override
    public void offerLast(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        elements[idx(size)] = e;
        size++;
    }

    /**
     * Retire et retourne l'élément en tête.
     *
     * @return élément en tête
     * @throws EmptyCollectionException si la deque est vide
     */
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

    /**
     * Retire et retourne l'élément en queue.
     *
     * @return élément en queue
     * @throws EmptyCollectionException si la deque est vide
     */
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

    /**
     * Retourne (sans retirer) l'élément en tête.
     *
     * @return élément en tête
     * @throws EmptyCollectionException si la deque est vide
     */
    @Override
    public E peekFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[head];
        return val;
    }

    /**
     * Retourne (sans retirer) l'élément en queue.
     *
     * @return élément en queue
     * @throws EmptyCollectionException si la deque est vide
     */
    @Override
    public E peekLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[idx(size - 1)];
        return val;
    }

    /**
     * Retourne le nombre d'éléments présents.
     *
     * @return taille de la deque
     */
    @Override
    public int size() {
        nbOp++;
        return size;
    }

    /**
     * Indique si la deque contient l'objet o (comparaison via equals).
     *
     * @param o objet recherché (non null)
     * @return true si trouvé, sinon false
     * @throws NullPointerException si o est null
     */
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

    /**
     * Indique si la deque contient tous les éléments de la collection c.
     * Les éléments null de c sont ignorés.
     *
     * @param c collection à tester (non null)
     * @return true si tous les éléments (non null) sont présents, sinon false
     */
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

    /**
     * Ajoute tous les éléments de la collection c dans la deque.
     * Ici, l'ajout se fait en tête (offerFirst) pour chaque élément.
     * Les éléments null sont ignorés.
     *
     * @param c collection source (non null)
     * @return true si au moins un élément a été ajouté, sinon false
     */
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

    /**
     * Vérifie s'il existe au moins un élément qui satisfait le prédicat filter.
     *
     * @param filter prédicat (non null)
     * @return true si un élément valide le prédicat, sinon false
     */
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

    /**
     * Applique une action à chaque élément dans l'ordre logique (de head vers la queue).
     *
     * @param action action à exécuter (non null)
     */
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

    /**
     * Retourne une copie des éléments dans un tableau (ordre logique).
     *
     * @return tableau contenant les éléments de la deque
     */
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

    /**
     * Représentation texte de la deque sous la forme [a, b, c] (ordre logique).
     *
     * @return chaîne représentant la deque
     */
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
