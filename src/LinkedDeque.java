import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implémentation d'une deque (double-ended queue) basée sur une liste doublement chaînée.
 * - Ajout/retrait en tête et en queue : O(1)
 * - Recherche/itération : O(n)
 *
 * @param <E> type des éléments stockés
 */
public class LinkedDeque<E> implements DequeInterface<E> {

    /**
     * Noeud interne de la liste doublement chaînée.
     * Chaque noeud connaît sa valeur, son précédent et son suivant.
     */
    private static class Node<E> {
        /** Valeur stockée dans le noeud. */
        E value;
        /** Référence vers le noeud précédent. */
        Node<E> prev;
        /** Référence vers le noeud suivant. */
        Node<E> next;

        /** Construit un noeud contenant value. */
        Node(E value) { this.value = value; }
    }

    /** Premier noeud (tête) de la deque. */
    private Node<E> first;

    /** Dernier noeud (queue) de la deque. */
    private Node<E> last;

    /** Nombre d'éléments actuellement présents. */
    private int size;

    /**
     * Compteur d'opérations (instrumentation) : s'incrémente à chaque opération
     * jugée significative (utile pour analyser la complexité).
     */
    public static long nbOp = 0;

    /**
     * Ajoute un élément en tête.
     *
     * @param e élément à ajouter (non null)
     * @throws NullPointerException si e est null
     */
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

    /**
     * Ajoute un élément en queue.
     *
     * @param e élément à ajouter (non null)
     * @throws NullPointerException si e est null
     */
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

    /**
     * Retire et retourne l'élément en tête.
     *
     * @return valeur en tête
     * @throws EmptyCollectionException si la deque est vide
     */
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

    /**
     * Retire et retourne l'élément en queue.
     *
     * @return valeur en queue
     * @throws EmptyCollectionException si la deque est vide
     */
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

    /**
     * Retourne (sans retirer) l'élément en tête.
     *
     * @return valeur en tête
     * @throws EmptyCollectionException si la deque est vide
     */
    @Override
    public E peekFirst() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        return first.value;
    }

    /**
     * Retourne (sans retirer) l'élément en queue.
     *
     * @return valeur en queue
     * @throws EmptyCollectionException si la deque est vide
     */
    @Override
    public E peekLast() throws EmptyCollectionException {
        nbOp++;
        if (size == 0) throw new EmptyCollectionException("Deque vide");
        return last.value;
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
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            if (o.equals(cur.value)) return true;
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
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            if (filter.test(cur.value)) return true;
        }
        return false;
    }

    /**
     * Applique une action à chaque élément dans l'ordre (de first à last).
     *
     * @param action action à exécuter (non null)
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        nbOp++;
        Objects.requireNonNull(action);
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            nbOp++;
            action.accept(cur.value);
        }
    }

    /**
     * Retourne une copie des éléments dans un tableau (ordre logique : first -> last).
     *
     * @return tableau contenant les éléments de la deque
     */
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

    /**
     * Représentation texte de la deque sous la forme [a, b, c].
     *
     * @return chaîne représentant la deque
     */
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
