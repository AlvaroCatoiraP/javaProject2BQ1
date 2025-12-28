import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class BigPriorityQueueTest {

    private <T> BigPriorityQueueInterface<T> newNaturalQueue() {
        return new BigPriorityQueue<>();
    }

    private <T> BigPriorityQueueInterface<T> newComparatorQueue(Comparator<? super T> cmp) {
        return new BigPriorityQueue<>(cmp);
    }

    /** Vide la queue en poll() jusqu'à null, et renvoie la concat en String pour comparer facilement. */
    private static <T> String drainToString(BigPriorityQueueInterface<T> q) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        while (true) {
            T v = q.poll();
            if (v == null) break;
            if (!first) sb.append(", ");
            sb.append(v);
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    @Nested
    class NaturalOrdering {

        @Test
        void etatInitial_queueVide() {
            var q = newNaturalQueue();

            assertNull(q.peek());
            assertNull(q.poll());
            assertEquals("[]", q.toString());
            assertFalse(q.contains(1));
        }

        @Test
        void offer_null_declencheException() {
            var q = newNaturalQueue();
            assertThrows(NullPointerException.class, () -> q.offer(null));
        }

        @Test
        void insertionSimple_devientTete() {
            var q = newNaturalQueue();
            assertTrue(q.offer(10));

            assertEquals(10, q.peek());
            assertTrue(q.contains(10));
            assertEquals("[10]", q.toString());
        }

        @Test
        void plusPetitQueTete_coupeDevant() {
            var q = newNaturalQueue();
            q.offer(10);     // head = 10
            q.offer(20);     // fin
            q.offer(5);      // 5 < 10 => coupe devant

            assertEquals(5, q.peek());
            assertEquals("[5, 10, 20]", q.toString());
            assertEquals("[5, 10, 20]", drainToString(q));
            assertNull(q.poll()); // vide => null
        }

        @Test
        void egalALaTete_vaALaFin() {
            var q = newNaturalQueue();
            q.offer(10);
            q.offer(10);

            assertEquals("[10, 10]", q.toString());
            assertEquals("[10, 10]", drainToString(q));
        }

        @Test
        void plusGrandQueTete_vaALaFin() {
            var q = newNaturalQueue();
            q.offer(10);
            q.offer(30);
            q.offer(20); // 20 >= 10 => fin

            assertEquals("[10, 30, 20]", q.toString());
            assertEquals("[10, 30, 20]", drainToString(q));
        }

        @Test
        void poll_metAJourLaTete() {
            var q = newNaturalQueue();
            q.offer(10);
            q.offer(20);
            q.offer(5); // [5,10,20]

            assertEquals(5, q.poll());
            assertEquals(10, q.peek());
            assertEquals("[10, 20]", q.toString());

            assertEquals(10, q.poll());
            assertEquals(20, q.peek());
            assertEquals("[20]", q.toString());

            assertEquals(20, q.poll());
            assertNull(q.peek());
            assertEquals("[]", q.toString());
        }

        @Test
        void contains_null_retourneFalse() {
            var q = newNaturalQueue();
            q.offer(10);
            assertFalse(q.contains(null));
        }

        @Test
        void insertionObjetNonComparable_provoqueClassCast_surComparaison() {
            // Dans ton code: 1er offer(Object) passe (pas de compare car queue vide)
            // 2e offer(Object) => compare() cast en Comparable => ClassCastException
            @SuppressWarnings({ "rawtypes", "unchecked" })
            BigPriorityQueueInterface raw = newNaturalQueue();

            Object a = new Object();
            Object b = new Object();

            assertTrue(raw.offer(a));
            assertThrows(ClassCastException.class, () -> raw.offer(b));
        }
    }

    @Nested
    class ComparatorOrdering {

        @Test
        void comparator_est_utilise_pour_le_cut() {
            // comparator inversé: "plus petit" = valeur plus GRANDE
            Comparator<Integer> reverse = (a, b) -> Integer.compare(b, a);
            var q = newComparatorQueue(reverse);

            q.offer(10);  // head = 10
            q.offer(5);   // selon reverse, 5 n'est pas "plus petit" que 10 => fin
            q.offer(20);  // selon reverse, 20 est "plus petit" que 10 => coupe devant

            assertEquals("[20, 10, 5]", q.toString());
            assertEquals("[20, 10, 5]", drainToString(q));
        }

        @Test
        void offer_null_interdit_meme_avec_comparator() {
            var q = newComparatorQueue(Comparator.naturalOrder());
            assertThrows(NullPointerException.class, () -> q.offer(null));
        }
    }
}
