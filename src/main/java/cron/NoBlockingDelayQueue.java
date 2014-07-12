package cron;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * 非阻塞非线程安全延时队列
 * @yuweiping
 *
 * @param <E>
 */
public class NoBlockingDelayQueue<E extends Delayed> extends AbstractQueue<E> {

    private final PriorityQueue<E> q = new PriorityQueue<E>();


    /**
     * Creates a new <tt>DelayQueue</tt> that is initially empty.
     */
    public NoBlockingDelayQueue() {}

    /**
     * Creates a <tt>DelayQueue</tt> initially containing the elements of the
     * given collection of {@link Delayed} instances.
     *
     * @param c the collection of elements to initially contain
     * @throws NullPointerException if the specified collection or any
     *         of its elements are null
     */
    public NoBlockingDelayQueue(Collection<? extends E> c) {
        this.addAll(c);
    }

    /**
     * Inserts the specified element into this delay queue.
     *
     * @param e the element to add
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     * @throws NullPointerException if the specified element is null
     */
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * Inserts the specified element into this delay queue.
     *
     * @param e the element to add
     * @return <tt>true</tt>
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
    	q.offer(e);
        return true;
    }

    /**
     * Retrieves and removes the head of this queue, or returns <tt>null</tt>
     * if this queue has no elements with an expired delay.
     *
     * @return the head of this queue, or <tt>null</tt> if this
     *         queue has no elements with an expired delay
     */
    public E poll() {
    	E first = q.peek();
        if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0)
            return null;
        else
            return q.poll();
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or
     * returns <tt>null</tt> if this queue is empty.  Unlike
     * <tt>poll</tt>, if no expired elements are available in the queue,
     * this method returns the element that will expire next,
     * if one exists.
     *
     * @return the head of this queue, or <tt>null</tt> if this
     *         queue is empty.
     */
    public E peek() {
    	return q.peek();
    }

    public int size() {
    	return q.size();
    }

    /**
     * Atomically removes all of the elements from this delay queue.
     * The queue will be empty after this call returns.
     * Elements with an unexpired delay are not waited for; they are
     * simply discarded from the queue.
     */
    public void clear() {
    	q.clear();
    }
    
    /**
     * Returns an array containing all of the elements in this queue.
     * The returned array elements are in no particular order.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this queue.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this queue
     */
    public Object[] toArray() {
    	return q.toArray();
    }

    /**
     * Returns an array containing all of the elements in this queue; the
     * runtime type of the returned array is that of the specified array.
     * The returned array elements are in no particular order.
     * If the queue fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this queue.
     *
     * <p>If this queue fits in the specified array with room to spare
     * (i.e., the array has more elements than this queue), the element in
     * the array immediately following the end of the queue is set to
     * <tt>null</tt>.
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>The following code can be used to dump a delay queue into a newly
     * allocated array of <tt>Delayed</tt>:
     *
     * <pre>
     *     Delayed[] a = q.toArray(new Delayed[0]);</pre>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param a the array into which the elements of the queue are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose
     * @return an array containing all of the elements in this queue
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this queue
     * @throws NullPointerException if the specified array is null
     */
    public <T> T[] toArray(T[] a) {
    	return q.toArray(a);
    }

    /**
     * Removes a single instance of the specified element from this
     * queue, if it is present, whether or not it has expired.
     */
    public boolean remove(Object o) {
    	return q.remove(o);
    }

    /**
     * Returns an iterator over all the elements (both expired and
     * unexpired) in this queue. The iterator does not return the
     * elements in any particular order.
     *
     * <p>The returned iterator is a "weakly consistent" iterator that
     * will never throw {@link java.util.ConcurrentModificationException
     * ConcurrentModificationException}, and guarantees to traverse
     * elements as they existed upon construction of the iterator, and
     * may (but is not guaranteed to) reflect any modifications
     * subsequent to construction.
     *
     * @return an iterator over the elements in this queue
     */
    public Iterator<E> iterator() {
        return new Itr(toArray());
    }

    /**
     * Snapshot iterator that works off copy of underlying q array.
     */
    private class Itr implements Iterator<E> {
        final Object[] array; // Array of all elements
        int cursor;           // index of next element to return;
        int lastRet;          // index of last element, or -1 if no such

        Itr(Object[] array) {
            lastRet = -1;
            this.array = array;
        }

        public boolean hasNext() {
            return cursor < array.length;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor >= array.length)
                throw new NoSuchElementException();
            lastRet = cursor;
            return (E)array[cursor++];
        }

        @SuppressWarnings("rawtypes")
		public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            Object x = array[lastRet];
            lastRet = -1;
            // Traverse underlying queue to find == element,
            // not just a .equals element.
            for (Iterator it = q.iterator(); it.hasNext(); ) {
                if (it.next() == x) {
                    it.remove();
                    return;
                }
            }
        }
    }

}

