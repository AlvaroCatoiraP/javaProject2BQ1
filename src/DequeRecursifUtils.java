import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Classe utilitaire (statique) contenant des opérations récursives sur une DequeInterface.
 * Les méthodes modifient temporairement la deque (via remove/offer) puis la restaurent,
 * sauf renverser() qui inverse réellement l'ordre.
 */
public final class DequeRecursifUtils {

    /** Constructeur privé : empêche l'instanciation (utilitaire statique). */
    private DequeRecursifUtils() {}

    /**
     * Renverse la deque de manière récursive en utilisant removeFirst() / offerLast().
     * Principe :
     * - on retire la tête
     * - on renverse le reste
     * - on remet l'élément retiré en queue
     *
     * @param q deque à renverser (non null)
     * @param <T> type des éléments
     * @throws NullPointerException si q est null
     */
    public static <T> void renverser(DequeInterface<T> q) {
        Objects.requireNonNull(q);
        try {
            T x = q.removeFirst();
            renverser(q);
            q.offerLast(x);
        } catch (EmptyCollectionException ex) {
        }
    }

    /**
     * Indique si au moins un élément de la deque vérifie le prédicat.
     * La méthode est récursive et restaure la deque dans son état initial.
     * Principe :
     * - retirer la tête x
     * - tester predicate sur x OU sur le reste (récursion)
     * - remettre x en tête (restauration)
     *
     * @param q deque à tester (non null)
     * @param predicate prédicat de test (non null)
     * @param <T> type des éléments
     * @return true si un élément valide le prédicat, sinon false
     * @throws NullPointerException si q ou predicate est null
     */
    public static <T> boolean anyMatch(DequeInterface<T> q, Predicate<? super T> predicate) {
        Objects.requireNonNull(q);
        Objects.requireNonNull(predicate);
        try {
            T x = q.removeFirst();
            boolean ok = predicate.test(x) || anyMatch(q, predicate);
            q.offerFirst(x);
            return ok;//
        } catch (EmptyCollectionException ex) {
            return false;
        }
    }

    /**
     * Retourne le minimum (ordre naturel) des éléments de la deque.
     * La méthode est récursive et restaure la deque dans son état initial.
     *
     * @param q deque (non null)
     * @param <T> type comparable (ordre naturel)
     * @return le plus petit élément
     * @throws NoSuchElementException si la deque est vide
     * @throws NullPointerException si q est null
     */
    public static <T extends Comparable<T>> T min(DequeInterface<T> q) throws NoSuchElementException {
        Objects.requireNonNull(q);
        try {
            T x = q.removeFirst();
            try {
                T m = min(q);
                q.offerFirst(x);
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
