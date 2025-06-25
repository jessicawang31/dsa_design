package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@linindex Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    // counter;
    private int count;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
        elements.add(null);
        count = 0;
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public OptimizedHeapMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        elementsToIndex = new HashMap<>(elementsAndPriorities.size());
        elements.add(null);
        count = elementsAndPriorities.size();

        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        // insert method from reference code
        elements.add(new PriorityNode<E>(element, priority));
        elementsToIndex.put(element, ++count);
        swim(count);
    }

    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public double getPriority(E element) {
        int index = elementsToIndex.get(element);
        return elements.get(index).getPriority();
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(1).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        // reference to delMin
        E min = elements.get(1).getElement();
        swap(1, count);
        elementsToIndex.remove(min);
        elements.remove(count--);
        sink(1);
        return min;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        int index = elementsToIndex.get(element);
        double curr = elements.get(index).getPriority();
        elements.get(index).setPriority(priority);

        if (priority < curr) {
            swim(index);
        } else {
            sink(index);
        }
    }

    @Override
    public int size() {
        return elements.size() - 1;
    }

    private void swim(int index) {
        while (index > 1 && greater(index / 2, index)) {
            swap(index / 2, index);
            index = index / 2;
        }
    }

    private void sink(int index) {
        int n = elements.size() - 1;
        while (2 * index <= n) {
            int j = 2 * index;
            if (j < n && greater(j, j + 1)) {
                j++;
            }
            if (!greater(index, j)) {
                break;
            }
            swap(index, j);
            index = j;
        }
    }

    private void swap(int i, int j) {
        PriorityNode<E> swap = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, swap);
        elementsToIndex.put(elements.get(i).getElement(), i);
        elementsToIndex.put(elements.get(j).getElement(), j);
    }

    private boolean greater(int i, int j) {
        return elements.get(i).getPriority() > elements.get(j).getPriority();
    }
}