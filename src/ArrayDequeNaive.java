import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implémentation "naïve" d'une deque basée sur un tableau classique.
 * - Ajout/retrait en fin : O(1) amorti
 * - Ajout/retrait en tête : O(n) à cause des décalages (shift)
 *
 * @param <E> type des éléments stockés
 */
public class ArrayDequeNaive<E> implements DequeInterface<E> {

    private Object[] elements;

    private int size;

    /**
     * Compteur
     */
    public static long nbOp = 0;

    /** Construit une deque avec une capacité initiale par défaut (10). */
    public ArrayDequeNaive() {
        this(10);
    }

    /**
     * Construit une deque avec une capacité initiale donnée.
     * Si la capacité est <= 0, alors 10.
     *
     * @param capacity capacité initiale souhaitée
     */
    public ArrayDequeNaive(int capacity) {
        if (capacity <= 0) capacity = 10;
        this.elements = new Object[capacity];
        this.size = 0;
    }

    /**
     * Garantit une capacité minimale pour pouvoir insérer minCapacity éléments.
     * Si nécessaire, redimensionne le tableau (x2) et recopie les éléments
     * dans le même ordre (indices 0..size-1).
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
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    /**
     * Ajoute un élément en tête.
     * Nécessite de décaler tous les éléments d'une case vers la droite (O(n)).
     *
     * @param e élément à ajouter (non null)
     * @throws NullPointerException si e est null
     */
    @Override
    public void offerFirst(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);

        for (int i = size; i > 0; i--) {
            nbOp++;
            elements[i] = elements[i - 1];
        }
        elements[0] = e;
        size++;
    }

    /**
     * Ajoute un élément en queue (fin du tableau logique).
     *
     * @param e élément à ajouter (non null)
     * @throws NullPointerException si e est null
     */
    @Override
    public void offerLast(E e) throws NullPointerException {
        nbOp++;
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        elements[size++] = e;
    }

    /**
     * Retire et retourne l'élément en tête.
     * Nécessite de décaler tous les éléments d'une case vers la gauche (O(n)).
     *
     * @return élément en tête
     * @throws EmptyCollectionException si la deque est vide
     */
    @Override
    public E removeFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        @SuppressWarnings("unchecked")
        E val = (E) elements[0];

        for (int i = 0; i < size - 1; i++) {
            nbOp++;
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
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
        @SuppressWarnings("unchecked")
        E val = (E) elements[size - 1];
        elements[size - 1] = null;
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
        E val = (E) elements[0];
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
        E val = (E) elements[size - 1];
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
            if (o.equals(elements[i])) return true;
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
     * Ici, l'ajout se fait en tête (offerFirst) : cela inverse l'ordre relatif
     * si c est parcourue de gauche à droite.
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
            E v = (E) elements[i];
            if (filter.test(v)) return true;
        }
        return false;
    }

    /**
     * Applique une action à chaque élément dans l'ordre (0..size-1).
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
            E v = (E) elements[i];
            action.accept(v);
        }
    }

    /**
     * Retourne une copie des éléments dans un tableau (ordre 0..size-1).
     *
     * @return tableau contenant les éléments de la deque
     */
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

    /**
     * Représentation texte de la deque sous la forme [a, b, c].
     *
     * @return chaîne représentant la deque
     */
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
