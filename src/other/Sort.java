package other;

/**
 * A generic Sort class for sorting elements in a List.
 */
public class Sort {

    /**
     * Sorts the given list of elements in ascending order based on their natural ordering.
     *
     * @param list the list to sort
     * @param <E> the type of elements in the list, must implement Comparable
     */
    public static <E extends Comparable<E>> void sort(List<E> list) {
        for (int i = 1; i < list.size(); i++) {
            E temp = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).compareTo(temp) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, temp);
        }
    }
}