package util;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A generic array-based List class that stores objects of type E.
 * It allows adding, removing, and checking for the existence of elements.
 *
 * @author Richard Li (rl902)
 * @param <E> the type of elements in this list
 */
public class List<E> implements Iterable<E> {
    private E[] objects; // An array to hold elements of type E
    private int size; // The number of elements in the list

    // Constructor
    /**
     * Default constructor initializes an empty list with a capacity of 4.
     */
    @SuppressWarnings("unchecked")
    public List() {
        this.objects = (E[]) new Object[4]; // Create an array with a default size of 4
        this.size = 0; // Start with zero elements
    }

    /**
     * Returns the size of the list.
     *
     * @return the number of elements in the list
     */
    public int size() {
        return size;
    }

    // Helper method to find the index of an element
    /**
     * Finds the index of a specific element in the list.
     *
     * @param e the element to find
     * @return the index of the element, or -1 if not found
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    // Helper method to grow the array size by 4
    /**
     * Grows the size of the array by 4 when it becomes full.
     */
    private void grow() {
        objects = Arrays.copyOf(objects, objects.length + 4); // Increase the size of the array
    }

    /**
     * Checks if the list contains a specific element.
     *
     * @param e the element to check for
     * @return true if the element exists in the list, false otherwise
     */
    public boolean contains(E e) {
        return find(e) != -1; // Return true if the element is found
    }

    /**
     * Adds an element to the list if it doesn't already exist.
     *
     * @param e the element to add
     */
    public void add(E e) {
        if (contains(e)) {
            return; // Do nothing if the element already exists
        }

        if (size == objects.length) {
            grow(); // Grow the array if it's full
        }
        objects[size] = e; // Add the element to the array
        size++;
    }

    /**
     * Removes an element from the list if it exists.
     *
     * @param e the element to remove
     */
    public void remove(E e) {
        int index = find(e);
        if (index == -1) {
            return; // Do nothing if the element is not found
        }

        // Shift elements to the left to remove the element
        for (int i = index; i < size - 1; i++) {
            objects[i] = objects[i + 1];
        }
        objects[size - 1] = null; // Nullify the last element
        size--;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an iterator for the list, allowing iteration over its elements.
     *
     * @return an iterator over the elements in the list
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator(); // Return a new instance of the ListIterator
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element
     * @return the element at the specified index
     */
    public E get(int index) {
        if (index >= 0 && index < size) {
            return objects[index];
        }
        throw new IndexOutOfBoundsException("Invalid index.");
    }

    /**
     * Sets the element at the specified index to the given value.
     *
     * @param index the index at which the element will be set
     * @param e the element to set
     */
    public void set(int index, E e) {
        if (index >= 0 && index < size) {
            objects[index] = e;
        } else {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
    }

    /**
     * Returns the index of the specified element, or -1 if the element is not found.
     *
     * @param e the element to find
     * @return the index of the element, or -1 if not found
     */
    public int indexOf(E e) {
        return find(e);
    }

    // Inner class for the iterator
    private class ListIterator implements Iterator<E> {
        private int current = 0;

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return current < size; // Return true if the current index is within bounds
        }

        /**
         * Returns the next element in the list.
         *
         * @return the next element
         */
        @Override
        public E next() {
            if (hasNext()) {
                return objects[current++]; // Return the current element and move to the next
            }
            return null; // Return null if there are no more elements
        }
    }
}