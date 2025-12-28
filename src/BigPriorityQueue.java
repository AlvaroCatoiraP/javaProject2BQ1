import java.util.Comparator;
import java.util.Objects;

/**
 * File à priorité "bizarre" :
 * - si l'élément à ajouter est plus petit que la tête, il passe devant tout le monde (en tête)
 * - sinon, il va en fin de file (en queue)
 *
 * L'ordre est basé soit sur l'ordre naturel (Comparable), soit sur un Comparator fourni au constructeur.
 *
 * @param <T> type des éléments stockés
 */
public class BigPriorityQueue<T> implements BigPriorityQueueInterface<T> {

    /** Structure interne utilisée pour stocker les éléments (deque). */
    private final DequeInterface<T> deque;

    /** Comparator optionnel (si null, on utilise l'ordre naturel via Comparable). */
    private final Comparator<? super T> comparator;

    /** Construit une file à priorité utilisant l'ordre naturel (Comparable). */
    public BigPriorityQueue() {
        this(null);
    }

    /**
     * Construit une file à priorité en utilisant un comparator.
     *
     * @param comparator comparator à utiliser (peut être null pour l'ordre naturel)
     */
    public BigPriorityQueue(Comparator<? super T> comparator) {
        this.deque = new ArrayDeque<>();
        this.comparator = comparator;
    }

    /**
     * Compare deux éléments selon le comparator fourni, sinon selon l'ordre naturel.
     * Peut lever ClassCastException si l'ordre naturel est utilisé et que T n'est pas Comparable.
     *
     * @param a premier élément
     * @param b second élément
     * @return résultat de comparaison (<0, 0, >0)
     */
    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super T>) a).compareTo(b);
    }

    /**
     * Insère l'élément e dans la file.
     * Règle :
     * - si la file est vide, on ajoute en queue
     * - sinon, si e < tête, on ajoute en tête
     * - sinon, on ajoute en queue
     *
     * @param e élément à ajouter (non null)
     * @return true (l'ajout réussit toujours si e est valide)
     * @throws NullPointerException si e est null
     * @throws ClassCastException possible si e n'est pas comparable (ordre naturel)
     */
    @Override
    public boolean offer(T e) {
        Objects.requireNonNull(e);

        if (peek() == null) {
            deque.offerLast(e);
            return true;
        }

        T head = peek();
        if (compare(e, head) < 0) deque.offerFirst(e);
        else deque.offerLast(e);

        return true;
    }

    /**
     * Retire et retourne l'élément en tête.
     *
     * @return tête de la file, ou null si la file est vide
     */
    @Override
    public T poll() {
        try {
            return deque.removeFirst();
        } catch (EmptyCollectionException ex) {
            return null;
        }
    }

    /**
     * Indique si l'objet o est présent dans la file.
     *
     * @param o objet recherché
     * @return true si trouvé, sinon false (et false si o est null)
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            return deque.contains(o);
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Retourne (sans retirer) l'élément en tête.
     *
     * @return tête de la file, ou null si la file est vide
     */
    @Override
    public T peek() {
        try {
            return deque.peekFirst();
        } catch (EmptyCollectionException ex) {
            return null;
        }
    }

    /**
     * Retourne une représentation texte de la file (déléguée à la deque interne).
     *
     * @return chaîne représentant le contenu de la file
     */
    @Override
    public String toString() {
        return deque.toString();
    }
}
