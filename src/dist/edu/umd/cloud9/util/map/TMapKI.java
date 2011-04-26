// From jdk-6u23-fcs-src-b05-jrl-12_nov_2010.jar

package edu.umd.cloud9.util.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Red-Black tree based {@link NavigableMap} implementation. The map is sorted according to the
 * {@linkplain Comparable natural ordering} of its keys, or by a {@link Comparator} provided at map
 * creation time, depending on which constructor is used.
 * 
 * <p>
 * This implementation provides guaranteed log(n) time cost for the <tt>containsKey</tt>,
 * <tt>get</tt>, <tt>put</tt> and <tt>remove</tt> operations. Algorithms are adaptations of those in
 * Cormen, Leiserson, and Rivest's <I>Introduction to Algorithms</I>.
 * 
 * <p>
 * Note that the ordering maintained by a sorted map (whether or not an explicit comparator is
 * provided) must be <i>consistent with equals</i> if this sorted map is to correctly implement the
 * <tt>Map</tt> interface. (See <tt>Comparable</tt> or <tt>Comparator</tt> for a precise definition
 * of <i>consistent with equals</i>.) This is so because the <tt>Map</tt> interface is defined in
 * terms of the equals operation, but a map performs all key comparisons using its
 * <tt>compareTo</tt> (or <tt>compare</tt>) method, so two keys that are deemed equal by this method
 * are, from the standpoint of the sorted map, equal. The behavior of a sorted map <i>is</i>
 * well-defined even if its ordering is inconsistent with equals; it just fails to obey the general
 * contract of the <tt>Map</tt> interface.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If multiple threads access a
 * map concurrently, and at least one of the threads modifies the map structurally, it <i>must</i>
 * be synchronized externally. (A structural modification is any operation that adds or deletes one
 * or more mappings; merely changing the value associated with an existing key is not a structural
 * modification.) This is typically accomplished by synchronizing on some object that naturally
 * encapsulates the map. If no such object exists, the map should be "wrapped" using the
 * {@link Collections#synchronizedSortedMap Collections.synchronizedSortedMap} method. This is best
 * done at creation time, to prevent accidental unsynchronized access to the map:
 * 
 * <pre>
 *   SortedMap m = Collections.synchronizedSortedMap(new TreeMap(...));
 * </pre>
 * 
 * <p>
 * The iterators returned by the <tt>iterator</tt> method of the collections returned by all of this
 * class's "collection view methods" are <i>fail-fast</i>: if the map is structurally modified at
 * any time after the iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> method, the iterator will throw a {@link ConcurrentModificationException}. Thus,
 * in the face of concurrent modification, the iterator fails quickly and cleanly, rather than
 * risking arbitrary, non-deterministic behavior at an undetermined time in the future.
 * 
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it is, generally
 * speaking, impossible to make any hard guarantees in the presence of unsynchronized concurrent
 * modification. Fail-fast iterators throw <tt>ConcurrentModificationException</tt> on a best-effort
 * basis. Therefore, it would be wrong to write a program that depended on this exception for its
 * correctness: <i>the fail-fast behavior of iterators should be used only to detect bugs.</i>
 * 
 * <p>
 * All <tt>Map.Entry</tt> pairs returned by methods in this class and its views represent snapshots
 * of mappings at the time they were produced. They do <em>not</em> support the
 * <tt>Entry.setValue</tt> method. (Note however that it is possible to change mappings in the
 * associated map using <tt>put</tt>.)
 * 
 * <p>
 * This class is a member of the <a href="{@docRoot} /../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 * 
 * @param <K>
 *          the type of keys maintained by this map
 * @param <V>
 *          the type of mapped values
 * 
 * @author Josh Bloch and Doug Lea
 * @version 1.73, 05/10/06
 * @see Map
 * @see HashMap
 * @see Hashtable
 * @see Comparable
 * @see Comparator
 * @see Collection
 * @since 1.2
 */

public class TMapKI<K> extends AbstractMapKI<K> implements NavigableMapKI<K>, Cloneable,
    Serializable {
  /**
   * The comparator used to maintain order in this tree map, or null if it uses the natural ordering
   * of its keys.
   * 
   * @serial
   */
  private final Comparator<? super K> comparator;

  private transient Entry<K> root = null;

  /**
   * The number of entries in the tree
   */
  private transient int size = 0;

  /**
   * The number of structural modifications to the tree.
   */
  private transient int modCount = 0;

  /**
   * Constructs a new, empty tree map, using the natural ordering of its keys. All keys inserted
   * into the map must implement the {@link Comparable} interface. Furthermore, all such keys must
   * be <i>mutually comparable</i>: <tt>k1.compareTo(k2)</tt> must not throw a
   * <tt>ClassCastException</tt> for any keys <tt>k1</tt> and <tt>k2</tt> in the map. If the user
   * attempts to put a key into the map that violates this constraint (for example, the user
   * attempts to put a string key into a map whose keys are integers), the
   * <tt>put(Object key, Object value)</tt> call will throw a <tt>ClassCastException</tt>.
   */
  public TMapKI() {
    comparator = null;
  }

  /**
   * Constructs a new, empty tree map, ordered according to the given comparator. All keys inserted
   * into the map must be <i>mutually comparable</i> by the given comparator:
   * <tt>comparator.compare(k1,
   * k2)</tt> must not throw a <tt>ClassCastException</tt> for any keys <tt>k1</tt> and <tt>k2</tt>
   * in the map. If the user attempts to put a key into the map that violates this constraint, the
   * <tt>put(Object
   * key, Object value)</tt> call will throw a <tt>ClassCastException</tt>.
   * 
   * @param comparator
   *          the comparator that will be used to order this map. If <tt>null</tt>, the
   *          {@linkplain Comparable natural ordering} of the keys will be used.
   */
  public TMapKI(Comparator<? super K> comparator) {
    this.comparator = comparator;
  }

  /**
   * Constructs a new tree map containing the same mappings as the given map, ordered according to
   * the <i>natural ordering</i> of its keys. All keys inserted into the new map must implement the
   * {@link Comparable} interface. Furthermore, all such keys must be <i>mutually comparable</i>:
   * <tt>k1.compareTo(k2)</tt> must not throw a <tt>ClassCastException</tt> for any keys <tt>k1</tt>
   * and <tt>k2</tt> in the map. This method runs in n*log(n) time.
   * 
   * @param m
   *          the map whose mappings are to be placed in this map
   * @throws ClassCastException
   *           if the keys in m are not {@link Comparable}, or are not mutually comparable
   * @throws NullPointerException
   *           if the specified map is null
   */
  public TMapKI(MapKI<? extends K> m) {
    comparator = null;
    putAll(m);
  }

  /**
   * Constructs a new tree map containing the same mappings and using the same ordering as the
   * specified sorted map. This method runs in linear time.
   * 
   * @param m
   *          the sorted map whose mappings are to be placed in this map, and whose comparator is to
   *          be used to sort this map
   * @throws NullPointerException
   *           if the specified map is null
   */
  public TMapKI(SortedMapKI<K> m) {
    comparator = m.comparator();
    try {
      buildFromSorted(m.size(), m.entrySet().iterator(), null, AbstractMapKI.DEFAULT_VALUE);
    } catch (java.io.IOException cannotHappen) {
    } catch (ClassNotFoundException cannotHappen) {
    }
  }

  // Query Operations

  /**
   * Returns the number of key-value mappings in this map.
   * 
   * @return the number of key-value mappings in this map
   */
  public int size() {
    return size;
  }

  /**
   * Returns <tt>true</tt> if this map contains a mapping for the specified key.
   * 
   * @param key
   *          key whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the specified key
   * @throws ClassCastException
   *           if the specified key cannot be compared with the keys currently in the map
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   */
  public boolean containsKey(K key) {
    return getEntry(key) != null;
  }

  /**
   * Returns <tt>true</tt> if this map maps one or more keys to the specified value. More formally,
   * returns <tt>true</tt> if and only if this map contains at least one mapping to a value
   * <tt>v</tt> such that <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will
   * probably require time linear in the map size for most implementations.
   * 
   * @param value
   *          value whose presence in this map is to be tested
   * @return <tt>true</tt> if a mapping to <tt>value</tt> exists; <tt>false</tt> otherwise
   * @since 1.2
   */
  public boolean containsValue(Object value) {
    for (Entry<K> e = getFirstEntry(); e != null; e = successor(e))
      if (valEquals(value, e.value))
        return true;
    return false;
  }

  /**
   * Returns the value to which the specified key is mapped, or {@code null} if this map contains no
   * mapping for the key.
   * 
   * <p>
   * More formally, if this map contains a mapping from a key {@code k} to a value {@code v} such
   * that {@code key} compares equal to {@code k} according to the map's ordering, then this method
   * returns {@code v}; otherwise it returns {@code null}. (There can be at most one such mapping.)
   * 
   * <p>
   * A return value of {@code null} does not <i>necessarily</i> indicate that the map contains no
   * mapping for the key; it's also possible that the map explicitly maps the key to {@code null}.
   * The {@link #containsKey containsKey} operation may be used to distinguish these two cases.
   * 
   * @throws ClassCastException
   *           if the specified key cannot be compared with the keys currently in the map
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   */
  public int get(K key) {
    Entry<K> p = getEntry(key);
    return (p == null ? null : p.value);
  }

  public Comparator<? super K> comparator() {
    return comparator;
  }

  /**
   * @throws NoSuchElementException
   *           {@inheritDoc}
   */
  public K firstKey() {
    return key(getFirstEntry());
  }

  /**
   * @throws NoSuchElementException
   *           {@inheritDoc}
   */
  public K lastKey() {
    return key(getLastEntry());
  }

  /**
   * Copies all of the mappings from the specified map to this map. These mappings replace any
   * mappings that this map had for any of the keys currently in the specified map.
   * 
   * @param map
   *          mappings to be stored in this map
   * @throws ClassCastException
   *           if the class of a key or value in the specified map prevents it from being stored in
   *           this map
   * @throws NullPointerException
   *           if the specified map is null or the specified map contains a null key and this map
   *           does not permit null keys
   */
  @SuppressWarnings("unchecked")
  public void putAll(MapKI<? extends K> map) {
    int mapSize = map.size();
    if (size == 0 && mapSize != 0 && map instanceof SortedMapKI) {
      Comparator<? extends K> c = ((SortedMapKI) map).comparator();
      if (c == comparator || (c != null && c.equals(comparator))) {
        ++modCount;
        try {
          buildFromSorted(mapSize, map.entrySet().iterator(), null, AbstractMapKI.DEFAULT_VALUE);
        } catch (java.io.IOException cannotHappen) {
        } catch (ClassNotFoundException cannotHappen) {
        }
        return;
      }
    }

    for (MapKI.Entry<? extends K> e : map.entrySet()) {
      put(e.getKey(), e.getValue());
    }
    // super.putAll(map);
  }

  /**
   * Returns this map's entry for the given key, or <tt>null</tt> if the map does not contain an
   * entry for the key.
   * 
   * @return this map's entry for the given key, or <tt>null</tt> if the map does not contain an
   *         entry for the key
   * @throws ClassCastException
   *           if the specified key cannot be compared with the keys currently in the map
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   */
  @SuppressWarnings("unchecked")
  final Entry<K> getEntry(K key) {
    // Offload comparator-based version for sake of performance.
    if (comparator != null)
      return getEntryUsingComparator(key);
    if (key == null)
      throw new NullPointerException();
    Comparable<? super K> k = (Comparable<? super K>) key;
    Entry<K> p = root;
    while (p != null) {
      int cmp = k.compareTo(p.key);
      if (cmp < 0) {
        p = p.left;
      } else if (cmp > 0) {
        p = p.right;
      } else {
        return p;
      }
    }
    return null;
  }

  /**
   * Version of getEntry using comparator. Split off from getEntry for performance. (This is not
   * worth doing for most methods, that are less dependent on comparator performance, but is
   * worthwhile here.)
   */
  final Entry<K> getEntryUsingComparator(K k) {
    Comparator<? super K> cpr = comparator;
    if (cpr != null) {
      Entry<K> p = root;
      while (p != null) {
        int cmp = cpr.compare(k, p.key);
        if (cmp < 0) {
          p = p.left;
        } else if (cmp > 0) {
          p = p.right;
        } else {
          return p;
        }
      }
    }
    return null;
  }

  /**
   * Gets the entry corresponding to the specified key; if no such entry exists, returns the entry
   * for the least key greater than the specified key; if no such entry exists (i.e., the greatest
   * key in the Tree is less than the specified key), returns <tt>null</tt>.
   */
  final Entry<K> getCeilingEntry(K key) {
    Entry<K> p = root;
    while (p != null) {
      int cmp = compare(key, p.key);
      if (cmp < 0) {
        if (p.left != null)
          p = p.left;
        else
          return p;
      } else if (cmp > 0) {
        if (p.right != null) {
          p = p.right;
        } else {
          Entry<K> parent = p.parent;
          Entry<K> ch = p;
          while (parent != null && ch == parent.right) {
            ch = parent;
            parent = parent.parent;
          }
          return parent;
        }
      } else
        return p;
    }
    return null;
  }

  /**
   * Gets the entry corresponding to the specified key; if no such entry exists, returns the entry
   * for the greatest key less than the specified key; if no such entry exists, returns
   * <tt>null</tt>.
   */
  final Entry<K> getFloorEntry(K key) {
    Entry<K> p = root;
    while (p != null) {
      int cmp = compare(key, p.key);
      if (cmp > 0) {
        if (p.right != null)
          p = p.right;
        else
          return p;
      } else if (cmp < 0) {
        if (p.left != null) {
          p = p.left;
        } else {
          Entry<K> parent = p.parent;
          Entry<K> ch = p;
          while (parent != null && ch == parent.left) {
            ch = parent;
            parent = parent.parent;
          }
          return parent;
        }
      } else
        return p;

    }
    return null;
  }

  /**
   * Gets the entry for the least key greater than the specified key; if no such entry exists,
   * returns the entry for the least key greater than the specified key; if no such entry exists
   * returns <tt>null</tt>.
   */
  final Entry<K> getHigherEntry(K key) {
    Entry<K> p = root;
    while (p != null) {
      int cmp = compare(key, p.key);
      if (cmp < 0) {
        if (p.left != null)
          p = p.left;
        else
          return p;
      } else {
        if (p.right != null) {
          p = p.right;
        } else {
          Entry<K> parent = p.parent;
          Entry<K> ch = p;
          while (parent != null && ch == parent.right) {
            ch = parent;
            parent = parent.parent;
          }
          return parent;
        }
      }
    }
    return null;
  }

  /**
   * Returns the entry for the greatest key less than the specified key; if no such entry exists
   * (i.e., the least key in the Tree is greater than the specified key), returns <tt>null</tt>.
   */
  final Entry<K> getLowerEntry(K key) {
    Entry<K> p = root;
    while (p != null) {
      int cmp = compare(key, p.key);
      if (cmp > 0) {
        if (p.right != null)
          p = p.right;
        else
          return p;
      } else {
        if (p.left != null) {
          p = p.left;
        } else {
          Entry<K> parent = p.parent;
          Entry<K> ch = p;
          while (parent != null && ch == parent.left) {
            ch = parent;
            parent = parent.parent;
          }
          return parent;
        }
      }
    }
    return null;
  }

  /**
   * Associates the specified value with the specified key in this map. If the map previously
   * contained a mapping for the key, the old value is replaced.
   * 
   * @param key
   *          key with which the specified value is to be associated
   * @param value
   *          value to be associated with the specified key
   * 
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no
   *         mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map
   *         previously associated <tt>null</tt> with <tt>key</tt>.)
   * @throws ClassCastException
   *           if the specified key cannot be compared with the keys currently in the map
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   */
  public int put(K key, int value) {
    Entry<K> t = root;
    if (t == null) {
      // TBD:
      // 5045147: (coll) Adding null to an empty TreeSet should
      // throw NullPointerException
      //
      // compare(key, key); // type check
      root = new Entry<K>(key, value, null);
      size = 1;
      modCount++;
      return AbstractMapKI.DEFAULT_VALUE;
    }
    int cmp;
    Entry<K> parent;
    // split comparator and comparable paths
    Comparator<? super K> cpr = comparator;
    if (cpr != null) {
      do {
        parent = t;
        cmp = cpr.compare(key, t.key);
        if (cmp < 0) {
          t = t.left;
        } else if (cmp > 0) {
          t = t.right;
        } else {
          return t.setValue(value);
        }
      } while (t != null);
    } else {
      if (key == null) {
        throw new NullPointerException();
      }
      Comparable<? super K> k = (Comparable<? super K>) key;
      do {
        parent = t;
        cmp = k.compareTo(t.key);
        if (cmp < 0) {
          t = t.left;
        } else if (cmp > 0) {
          t = t.right;
        } else {
          return t.setValue(value);
        }
      } while (t != null);
    }
    Entry<K> e = new Entry<K>(key, value, parent);
    if (cmp < 0)
      parent.left = e;
    else
      parent.right = e;
    fixAfterInsertion(e);
    size++;
    modCount++;
    return AbstractMapKI.DEFAULT_VALUE;
  }

  /**
   * Removes the mapping for this key from this TMap if present.
   *
   * @param key key for which mapping should be removed
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no
   *         mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map
   *         previously associated <tt>null</tt> with <tt>key</tt>.)
   * @throws ClassCastException
   *           if the specified key cannot be compared with the keys currently in the map
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   */
  public int remove(K key) {
    Entry<K> p = getEntry(key);
    if (p == null) {
      return AbstractMapKI.DEFAULT_VALUE;
    }

    int oldValue = p.value;
    deleteEntry(p);
    return oldValue;
  }

  /**
   * Removes all of the mappings from this map. The map will be empty after this call returns.
   */
  public void clear() {
    modCount++;
    size = 0;
    root = null;
  }

  /**
   * Returns a shallow copy of this <tt>TMap</tt> instance. (The keys and values themselves are not
   * cloned.)
   * 
   * @return a shallow copy of this map
   */
  public Object clone() {
    TMapKI<K> clone = null;
    try {
      clone = (TMapKI<K>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    }

    // Put clone into "virgin" state (except for comparator)
    clone.root = null;
    clone.size = 0;
    clone.modCount = 0;
    clone.entrySet = null;
    clone.navigableKeySet = null;
    clone.descendingMap = null;

    // Initialize clone with our mappings
    try {
      clone.buildFromSorted(size, entrySet().iterator(), null, AbstractMapKI.DEFAULT_VALUE);
    } catch (java.io.IOException cannotHappen) {
    } catch (ClassNotFoundException cannotHappen) {
    }

    return clone;
  }

  // NavigableMap API methods

  /**
   * @since 1.6
   */
  public MapKI.Entry<K> firstEntry() {
    return exportEntry(getFirstEntry());
  }

  /**
   * @since 1.6
   */
  public MapKI.Entry<K> lastEntry() {
    return exportEntry(getLastEntry());
  }

  /**
   * @since 1.6
   */
  public MapKI.Entry<K> pollFirstEntry() {
    Entry<K> p = getFirstEntry();
    MapKI.Entry<K> result = exportEntry(p);
    if (p != null) {
      deleteEntry(p);
    }
    return result;
  }

  /**
   * @since 1.6
   */
  public MapKI.Entry<K> pollLastEntry() {
    Entry<K> p = getLastEntry();
    MapKI.Entry<K> result = exportEntry(p);
    if (p != null) {
      deleteEntry(p);
    }
    return result;
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public MapKI.Entry<K> lowerEntry(K key) {
    return exportEntry(getLowerEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public K lowerKey(K key) {
    return keyOrNull(getLowerEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public MapKI.Entry<K> floorEntry(K key) {
    return exportEntry(getFloorEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public K floorKey(K key) {
    return keyOrNull(getFloorEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public MapKI.Entry<K> ceilingEntry(K key) {
    return exportEntry(getCeilingEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public K ceilingKey(K key) {
    return keyOrNull(getCeilingEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public MapKI.Entry<K> higherEntry(K key) {
    return exportEntry(getHigherEntry(key));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if the specified key is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @since 1.6
   */
  public K higherKey(K key) {
    return keyOrNull(getHigherEntry(key));
  }

  // Views

  /**
   * Fields initialized to contain an instance of the entry set view the first time this view is
   * requested. Views are stateless, so there's no reason to create more than one.
   */
  private transient EntrySet entrySet = null;
  private transient KeySet<K> navigableKeySet = null;
  private transient NavigableMapKI<K> descendingMap = null;

  /**
   * Returns a {@link Set} view of the keys contained in this map. The set's iterator returns the
   * keys in ascending order. The set is backed by the map, so changes to the map are reflected in
   * the set, and vice-versa. If the map is modified while an iteration over the set is in progress
   * (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are
   * undefined. The set supports element removal, which removes the corresponding mapping from the
   * map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
   * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support the <tt>add</tt> or
   * <tt>addAll</tt> operations.
   */
  public Set<K> keySet() {
    return navigableKeySet();
  }

  /**
   * @since 1.6
   */
  public NavigableSet<K> navigableKeySet() {
    KeySet<K> nks = navigableKeySet;
    return (nks != null) ? nks : (navigableKeySet = new KeySet(this));
  }

  /**
   * @since 1.6
   */
  public NavigableSet<K> descendingKeySet() {
    return descendingMap().navigableKeySet();
  }

  /**
   * Returns a {@link Collection} view of the values contained in this map. The collection's
   * iterator returns the values in ascending order of the corresponding keys. The collection is
   * backed by the map, so changes to the map are reflected in the collection, and vice-versa. If
   * the map is modified while an iteration over the collection is in progress (except through the
   * iterator's own <tt>remove</tt> operation), the results of the iteration are undefined. The
   * collection supports element removal, which removes the corresponding mapping from the map, via
   * the <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>, <tt>removeAll</tt>,
   * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the <tt>add</tt> or
   * <tt>addAll</tt> operations.
   */
  public Collection<Integer> values() {
    Collection<Integer> vs = values;
    return (vs != null) ? vs : (values = new Values());
  }

  /**
   * Returns a {@link Set} view of the mappings contained in this map. The set's iterator returns
   * the entries in ascending key order. The set is backed by the map, so changes to the map are
   * reflected in the set, and vice-versa. If the map is modified while an iteration over the set is
   * in progress (except through the iterator's own <tt>remove</tt> operation, or through the
   * <tt>setValue</tt> operation on a map entry returned by the iterator) the results of the
   * iteration are undefined. The set supports element removal, which removes the corresponding
   * mapping from the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>
   * , <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the <tt>add</tt> or
   * <tt>addAll</tt> operations.
   */
  public Set<MapKI.Entry<K>> entrySet() {
    EntrySet es = entrySet;
    return (es != null) ? es : (entrySet = new EntrySet());
  }

  /**
   * @since 1.6
   */
  public NavigableMapKI<K> descendingMap() {
    NavigableMapKI<K> km = descendingMap;
    return (km != null) ? km :
      (descendingMap = new DescendingSubMapKI<K>(this, true, null, true, true, null, true));
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if <tt>fromKey</tt> or <tt>toKey</tt> is null and this map uses natural ordering, or
   *           its comparator does not permit null keys
   * @throws IllegalArgumentException
   *           {@inheritDoc}
   * @since 1.6
   */
  public NavigableMapKI<K> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
    return new AscendingSubMapKI<K>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if <tt>toKey</tt> is null and this map uses natural ordering, or its comparator does
   *           not permit null keys
   * @throws IllegalArgumentException
   *           {@inheritDoc}
   * @since 1.6
   */
  public NavigableMapKI<K> headMap(K toKey, boolean inclusive) {
    return new AscendingSubMapKI<K>(this, true, null, true, false, toKey, inclusive);
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if <tt>fromKey</tt> is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @throws IllegalArgumentException
   *           {@inheritDoc}
   * @since 1.6
   */
  public NavigableMapKI<K> tailMap(K fromKey, boolean inclusive) {
    return new AscendingSubMapKI<K>(this, false, fromKey, inclusive, true, null, true);
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if <tt>fromKey</tt> or <tt>toKey</tt> is null and this map uses natural ordering, or
   *           its comparator does not permit null keys
   * @throws IllegalArgumentException
   *           {@inheritDoc}
   */
  public SortedMapKI<K> subMap(K fromKey, K toKey) {
    return subMap(fromKey, true, toKey, false);
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if <tt>toKey</tt> is null and this map uses natural ordering, or its comparator does
   *           not permit null keys
   * @throws IllegalArgumentException
   *           {@inheritDoc}
   */
  public SortedMapKI<K> headMap(K toKey) {
    return headMap(toKey, false);
  }

  /**
   * @throws ClassCastException
   *           {@inheritDoc}
   * @throws NullPointerException
   *           if <tt>fromKey</tt> is null and this map uses natural ordering, or its comparator
   *           does not permit null keys
   * @throws IllegalArgumentException
   *           {@inheritDoc}
   */
  public SortedMapKI<K> tailMap(K fromKey) {
    return tailMap(fromKey, true);
  }

  // View class support

  class Values extends AbstractCollection<Integer> {
    public Iterator<Integer> iterator() {
      return new ValueIterator(getFirstEntry());
    }

    public int size() {
      return TMapKI.this.size();
    }

    public boolean contains(Object o) {
      return TMapKI.this.containsValue(o);
    }

    public boolean remove(Object o) {
      for (Entry<K> e = getFirstEntry(); e != null; e = successor(e)) {
        if (valEquals(e.getValue(), o)) {
          deleteEntry(e);
          return true;
        }
      }
      return false;
    }

    public void clear() {
      TMapKI.this.clear();
    }
  }

  class EntrySet extends AbstractSet<MapKI.Entry<K>> {
    public Iterator<MapKI.Entry<K>> iterator() {
      return new EntryIterator(getFirstEntry());
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
      if (!(o instanceof MapKI.Entry)) {
        return false;
      }
      MapKI.Entry<K> entry = (MapKI.Entry<K>) o;
      int value = entry.getValue();
      Entry<K> p = getEntry(entry.getKey());
      return p != null && valEquals(p.getValue(), value);
    }

    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
      if (!(o instanceof MapKI.Entry)) {
        return false;
      }
      MapKI.Entry<K> entry = (MapKI.Entry<K>) o;
      int value = entry.getValue();
      Entry<K> p = getEntry(entry.getKey());
      if (p != null && valEquals(p.getValue(), value)) {
        deleteEntry(p);
        return true;
      }
      return false;
    }

    public int size() {
      return TMapKI.this.size();
    }

    public void clear() {
      TMapKI.this.clear();
    }
  }

  /*
   * Unlike Values and EntrySet, the KeySet class is static, delegating to a NavigableMap to allow
   * use by SubMaps, which outweighs the ugliness of needing type-tests for the following Iterator
   * methods that are defined appropriately in main versus submap classes.
   */

  Iterator<K> keyIterator() {
    return new KeyIterator(getFirstEntry());
  }

  Iterator<K> descendingKeyIterator() {
    return new DescendingKeyIterator(getLastEntry());
  }

  static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final NavigableMapKI<E> m;

    KeySet(NavigableMapKI<E> map) {
      m = map;
    }

    @Override @SuppressWarnings("unchecked")
    public Iterator<E> iterator() {
      if (m instanceof TMapKI) {
        return ((TMapKI<E>) m).keyIterator();
      } else {
        return (Iterator<E>) (((TMapKI.NavigableSubMapKI) m).keyIterator());
      }
    }

    @Override @SuppressWarnings("unchecked")
    public Iterator<E> descendingIterator() {
      if (m instanceof TMapKI) {
        return ((TMapKI<E>) m).descendingKeyIterator();
      } else {
        return (Iterator<E>) (((TMapKI.NavigableSubMapKI) m).descendingKeyIterator());
      }
    }

    @Override @SuppressWarnings("unchecked") public boolean contains(Object o) { return m.containsKey((E) o); }
    @Override public int size() { return m.size(); }
    @Override public boolean isEmpty() { return m.isEmpty(); }
    @Override public void clear() { m.clear(); }
    @Override public E lower(E e) { return m.lowerKey(e); }
    @Override public E floor(E e) { return m.floorKey(e); }
    @Override public E ceiling(E e) { return m.ceilingKey(e); }
    @Override public E higher(E e) { return m.higherKey(e); }
    @Override public E first() { return m.firstKey(); }
    @Override public E last() { return m.lastKey(); }
    @Override public Comparator<? super E> comparator() { return m.comparator(); }

    @Override
    public E pollFirst() {
      MapKI.Entry<E> e = m.pollFirstEntry();
      return e == null ? null : e.getKey();
    }

    @Override
    public E pollLast() {
      MapKI.Entry<E> e = m.pollLastEntry();
      return e == null ? null : e.getKey();
    }

    @Override @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
      int oldSize = size();
      m.remove((E) o);
      return size() != oldSize;
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
      TreeSet<E> set = new TreeSet<E>();

      for (MapKI.Entry<E> e : m.subMap(fromElement, fromInclusive, toElement, toInclusive).entrySet()) {
        set.add(e.getKey());
      }
      return set;
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
      TreeSet<E> set = new TreeSet<E>();

      for (MapKI.Entry<E> e : m.headMap(toElement, inclusive).entrySet()) {
        set.add(e.getKey());
      }
      return set;
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
      TreeSet<E> set = new TreeSet<E>();

      for (MapKI.Entry<E> e : m.tailMap(fromElement, inclusive).entrySet()) {
        set.add(e.getKey());
      }
      return set;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
      return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
      return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
      return tailSet(fromElement, true);
    }

    @Override
    public NavigableSet<E> descendingSet() {
      TreeSet<E> set = new TreeSet<E>();

      for (MapKI.Entry<E> e : m.descendingMap().entrySet()) {
        set.add(e.getKey());
      }
      return set;
    }
  }

  /**
   * Base class for TMapKI Iterators
   */
  abstract class PrivateEntryIterator<T> implements Iterator<T> {
    Entry<K> next;
    Entry<K> lastReturned;
    int expectedModCount;

    PrivateEntryIterator(Entry<K> first) {
      expectedModCount = modCount;
      lastReturned = null;
      next = first;
    }

    @Override
    public final boolean hasNext() {
      return next != null;
    }

    final Entry<K> nextEntry() {
      Entry<K> e = next;
      if (e == null) {
        throw new NoSuchElementException();
      }
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      next = successor(e);
      lastReturned = e;
      return e;
    }

    final Entry<K> prevEntry() {
      Entry<K> e = next;
      if (e == null) {
        throw new NoSuchElementException();
      }
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      next = predecessor(e);
      lastReturned = e;
      return e;
    }

    @Override
    public void remove() {
      if (lastReturned == null) {
        throw new IllegalStateException();
      }
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      // deleted entries are replaced by their successors
      if (lastReturned.left != null && lastReturned.right != null) {
        next = lastReturned;
      }
      deleteEntry(lastReturned);
      expectedModCount = modCount;
      lastReturned = null;
    }
  }

  final class EntryIterator extends PrivateEntryIterator<MapKI.Entry<K>> {
    EntryIterator(Entry<K> first) {
      super(first);
    }

    @Override
    public MapKI.Entry<K> next() {
      return nextEntry();
    }
  }

  final class ValueIterator extends PrivateEntryIterator<Integer> {
    ValueIterator(Entry<K> first) {
      super(first);
    }

    @Override
    public Integer next() {
      return nextEntry().value;
    }
  }

  final class KeyIterator extends PrivateEntryIterator<K> {
    KeyIterator(Entry<K> first) {
      super(first);
    }

    @Override
    public K next() {
      return nextEntry().key;
    }
  }

  final class DescendingKeyIterator extends PrivateEntryIterator<K> {
    DescendingKeyIterator(Entry<K> first) {
      super(first);
    }

    @Override
    public K next() {
      return prevEntry().key;
    }
  }

  // Little utilities

  /**
   * Compares two keys using the correct comparison method for this TMap.
   */
  @SuppressWarnings("unchecked")
  final int compare(Object k1, Object k2) {
    return comparator == null ? ((Comparable<? super K>) k1).compareTo((K) k2) : comparator
        .compare((K) k1, (K) k2);
  }

  /**
   * Test two values for equality. Differs from o1.equals(o2) only in that it copes with
   * <tt>null</tt> o1 properly.
   */
  final static boolean valEquals(Object o1, Object o2) {
    return (o1 == null ? o2 == null : o1.equals(o2));
  }

  /**
   * Return SimpleImmutableEntry for entry, or null if null
   */
  static <K> MapKI.Entry<K> exportEntry(TMapKI.Entry<K> e) {
    return e == null ? null : new SimpleImmutableEntry<K>(e);
  }

  /**
   * Return key for entry, or null if null
   */
  static <K> K keyOrNull(TMapKI.Entry<K> e) {
    return e == null ? null : e.key;
  }

  /**
   * Returns the key corresponding to the specified Entry.
   * 
   * @throws NoSuchElementException
   *           if the Entry is null
   */
  static <K> K key(Entry<K> e) {
    if (e == null) {
      throw new NoSuchElementException();
    }
    return e.key;
  }

  // SubMaps

  /**
   * @serial include
   */
  static abstract class NavigableSubMapKI<K> extends AbstractMapKI<K> implements NavigableMapKI<K>,
      Serializable {
    private static final long serialVersionUID = -6456145503079362221L;

    // The backing map.
    final TMapKI<K> m;
    transient volatile Collection<Integer> values = null;

    /**
     * Endpoints are represented as triples (fromStart, lo, loInclusive) and (toEnd, hi,
     * hiInclusive). If fromStart is true, then the low (absolute) bound is the start of the backing
     * map, and the other values are ignored. Otherwise, if loInclusive is true, lo is the inclusive
     * bound, else lo is the exclusive bound. Similarly for the upper bound.
     */
    final K lo, hi;
    final boolean fromStart, toEnd;
    final boolean loInclusive, hiInclusive;

    NavigableSubMapKI(TMapKI<K> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd,
        K hi, boolean hiInclusive) {
      if (!fromStart && !toEnd) {
        if (m.compare(lo, hi) > 0)
          throw new IllegalArgumentException("fromKey > toKey");
      } else {
        if (!fromStart) // type check
          m.compare(lo, lo);
        if (!toEnd)
          m.compare(hi, hi);
      }

      this.m = m;
      this.fromStart = fromStart;
      this.lo = lo;
      this.loInclusive = loInclusive;
      this.toEnd = toEnd;
      this.hi = hi;
      this.hiInclusive = hiInclusive;
    }

    // internal utilities

    final boolean tooLow(Object key) {
      if (!fromStart) {
        int c = m.compare(key, lo);
        if (c < 0 || (c == 0 && !loInclusive))
          return true;
      }
      return false;
    }

    final boolean tooHigh(Object key) {
      if (!toEnd) {
        int c = m.compare(key, hi);
        if (c > 0 || (c == 0 && !hiInclusive))
          return true;
      }
      return false;
    }

    final boolean inRange(Object key) {
      return !tooLow(key) && !tooHigh(key);
    }

    final boolean inClosedRange(Object key) {
      return (fromStart || m.compare(key, lo) >= 0) && (toEnd || m.compare(hi, key) >= 0);
    }

    final boolean inRange(Object key, boolean inclusive) {
      return inclusive ? inRange(key) : inClosedRange(key);
    }

    /*
     * Absolute versions of relation operations. Subclasses map to these using like-named "sub"
     * versions that invert senses for descending maps
     */

    final TMapKI.Entry<K> absLowest() {
      TMapKI.Entry<K> e = (fromStart ? m.getFirstEntry() :
        (loInclusive ? m.getCeilingEntry(lo) : m.getHigherEntry(lo)));
      return (e == null || tooHigh(e.key)) ? null : e;
    }

    final TMapKI.Entry<K> absHighest() {
      TMapKI.Entry<K> e = (toEnd ? m.getLastEntry() :
        (hiInclusive ? m.getFloorEntry(hi) : m.getLowerEntry(hi)));
      return (e == null || tooLow(e.key)) ? null : e;
    }

    final TMapKI.Entry<K> absCeiling(K key) {
      if (tooLow(key)) {
        return absLowest();
      }
      TMapKI.Entry<K> e = m.getCeilingEntry(key);
      return (e == null || tooHigh(e.key)) ? null : e;
    }

    final TMapKI.Entry<K> absHigher(K key) {
      if (tooLow(key)) {
        return absLowest();
      }
      TMapKI.Entry<K> e = m.getHigherEntry(key);
      return (e == null || tooHigh(e.key)) ? null : e;
    }

    final TMapKI.Entry<K> absFloor(K key) {
      if (tooHigh(key)) {
        return absHighest();
      }
      TMapKI.Entry<K> e = m.getFloorEntry(key);
      return (e == null || tooLow(e.key)) ? null : e;
    }

    final TMapKI.Entry<K> absLower(K key) {
      if (tooHigh(key)) {
        return absHighest();
      }
      TMapKI.Entry<K> e = m.getLowerEntry(key);
      return (e == null || tooLow(e.key)) ? null : e;
    }

    /** Returns the absolute high fence for ascending traversal */
    final TMapKI.Entry<K> absHighFence() {
      return (toEnd ? null : (hiInclusive ? m.getHigherEntry(hi) : m.getCeilingEntry(hi)));
    }

    /** Return the absolute low fence for descending traversal */
    final TMapKI.Entry<K> absLowFence() {
      return (fromStart ? null : (loInclusive ? m.getLowerEntry(lo) : m.getFloorEntry(lo)));
    }

    // Abstract methods defined in ascending vs descending classes
    // These relay to the appropriate absolute versions

    abstract TMapKI.Entry<K> subLowest();

    abstract TMapKI.Entry<K> subHighest();

    abstract TMapKI.Entry<K> subCeiling(K key);

    abstract TMapKI.Entry<K> subHigher(K key);

    abstract TMapKI.Entry<K> subFloor(K key);

    abstract TMapKI.Entry<K> subLower(K key);

    /** Returns ascending iterator from the perspective of this submap */
    abstract Iterator<K> keyIterator();

    /** Returns descending iterator from the perspective of this submap */
    abstract Iterator<K> descendingKeyIterator();

    // public methods

    public boolean isEmpty() {
      return (fromStart && toEnd) ? m.isEmpty() : entrySet().isEmpty();
    }

    public int size() {
      return (fromStart && toEnd) ? m.size() : entrySet().size();
    }

    public final boolean containsKey(K key) {
      return inRange(key) && m.containsKey(key);
    }

    public final int put(K key, int value) {
      if (!inRange(key))
        throw new IllegalArgumentException("key out of range");
      return m.put(key, value);
    }

    public final int get(K key) {
      return !inRange(key) ? null : m.get(key);
    }

    public final int remove(K key) {
      return !inRange(key) ? null : m.remove(key);
    }

    public final MapKI.Entry<K> ceilingEntry(K key) {
      return exportEntry(subCeiling(key));
    }

    public final K ceilingKey(K key) {
      return keyOrNull(subCeiling(key));
    }

    public final MapKI.Entry<K> higherEntry(K key) {
      return exportEntry(subHigher(key));
    }

    public final K higherKey(K key) {
      return keyOrNull(subHigher(key));
    }

    public final MapKI.Entry<K> floorEntry(K key) {
      return exportEntry(subFloor(key));
    }

    public final K floorKey(K key) {
      return keyOrNull(subFloor(key));
    }

    public final MapKI.Entry<K> lowerEntry(K key) {
      return exportEntry(subLower(key));
    }

    public final K lowerKey(K key) {
      return keyOrNull(subLower(key));
    }

    public final K firstKey() {
      return key(subLowest());
    }

    public final K lastKey() {
      return key(subHighest());
    }

    public final MapKI.Entry<K> firstEntry() {
      return exportEntry(subLowest());
    }

    public final MapKI.Entry<K> lastEntry() {
      return exportEntry(subHighest());
    }

    public final MapKI.Entry<K> pollFirstEntry() {
      TMapKI.Entry<K> e = subLowest();
      MapKI.Entry<K> result = exportEntry(e);
      if (e != null)
        m.deleteEntry(e);
      return result;
    }

    public final MapKI.Entry<K> pollLastEntry() {
      TMapKI.Entry<K> e = subHighest();
      MapKI.Entry<K> result = exportEntry(e);
      if (e != null)
        m.deleteEntry(e);
      return result;
    }

    // Views
    transient NavigableMapKI<K> descendingMapView = null;
    transient EntrySetView entrySetView = null;
    transient KeySet<K> navigableKeySetView = null;

    public final NavigableSet<K> navigableKeySet() {
      KeySet<K> nksv = navigableKeySetView;
      return (nksv != null) ? nksv : (navigableKeySetView = new TMapKI.KeySet<K>(this));
    }

    public final Set<K> keySet() {
      return navigableKeySet();
    }

    public NavigableSet<K> descendingKeySet() {
      return descendingMap().navigableKeySet();
    }

    public final SortedMapKI<K> subMap(K fromKey, K toKey) {
      return subMap(fromKey, true, toKey, false);
    }

    public final SortedMapKI<K> headMap(K toKey) {
      return headMap(toKey, false);
    }

    public final SortedMapKI<K> tailMap(K fromKey) {
      return tailMap(fromKey, true);
    }

    // View classes

    abstract class EntrySetView extends AbstractSet<MapKI.Entry<K>> {
      private transient int size = -1, sizeModCount;

      public int size() {
        if (fromStart && toEnd)
          return m.size();
        if (size == -1 || sizeModCount != m.modCount) {
          sizeModCount = m.modCount;
          size = 0;
          Iterator<MapKI.Entry<K>> i = iterator();
          while (i.hasNext()) {
            size++;
            i.next();
          }
        }
        return size;
      }

      public boolean isEmpty() {
        TMapKI.Entry<K> n = absLowest();
        return n == null || tooHigh(n.key);
      }

      public boolean contains(Object o) {
        if (!(o instanceof MapKI.Entry))
          return false;
        MapKI.Entry<K> entry = (MapKI.Entry<K>) o;
        K key = entry.getKey();
        if (!inRange(key)) {
          return false;
        }
        TMapKI.Entry<K> node = m.getEntry(key);
        return node != null && valEquals(node.getValue(), entry.getValue());
      }

      public boolean remove(Object o) {
        if (!(o instanceof MapKI.Entry))
          return false;
        MapKI.Entry<K> entry = (MapKI.Entry<K>) o;
        K key = entry.getKey();
        if (!inRange(key))
          return false;
        TMapKI.Entry<K> node = m.getEntry(key);
        if (node != null && valEquals(node.getValue(), entry.getValue())) {
          m.deleteEntry(node);
          return true;
        }
        return false;
      }
    }

    /**
     * Iterators for SubMaps
     */
    abstract class SubMapIterator<T> implements Iterator<T> {
      TMapKI.Entry<K> lastReturned;
      TMapKI.Entry<K> next;
      final K fenceKey;
      int expectedModCount;

      SubMapIterator(TMapKI.Entry<K> first, TMapKI.Entry<K> fence) {
        expectedModCount = m.modCount;
        lastReturned = null;
        next = first;
        fenceKey = fence == null ? null : fence.key;
      }

      public final boolean hasNext() {
        return next != null && next.key != fenceKey;
      }

      final TMapKI.Entry<K> nextEntry() {
        TMapKI.Entry<K> e = next;
        if (e == null || e.key == fenceKey)
          throw new NoSuchElementException();
        if (m.modCount != expectedModCount)
          throw new ConcurrentModificationException();
        next = successor(e);
        lastReturned = e;
        return e;
      }

      final TMapKI.Entry<K> prevEntry() {
        TMapKI.Entry<K> e = next;
        if (e == null || e.key == fenceKey)
          throw new NoSuchElementException();
        if (m.modCount != expectedModCount)
          throw new ConcurrentModificationException();
        next = predecessor(e);
        lastReturned = e;
        return e;
      }

      final void removeAscending() {
        if (lastReturned == null)
          throw new IllegalStateException();
        if (m.modCount != expectedModCount)
          throw new ConcurrentModificationException();
        // deleted entries are replaced by their successors
        if (lastReturned.left != null && lastReturned.right != null)
          next = lastReturned;
        m.deleteEntry(lastReturned);
        lastReturned = null;
        expectedModCount = m.modCount;
      }

      final void removeDescending() {
        if (lastReturned == null)
          throw new IllegalStateException();
        if (m.modCount != expectedModCount)
          throw new ConcurrentModificationException();
        m.deleteEntry(lastReturned);
        lastReturned = null;
        expectedModCount = m.modCount;
      }

    }

    final class SubMapEntryIterator extends SubMapIterator<MapKI.Entry<K>> {
      SubMapEntryIterator(TMapKI.Entry<K> first, TMapKI.Entry<K> fence) {
        super(first, fence);
      }

      public MapKI.Entry<K> next() {
        return nextEntry();
      }

      public void remove() {
        removeAscending();
      }
    }

    final class SubMapKeyIterator extends SubMapIterator<K> {
      SubMapKeyIterator(TMapKI.Entry<K> first, TMapKI.Entry<K> fence) {
        super(first, fence);
      }

      public K next() {
        return nextEntry().key;
      }

      public void remove() {
        removeAscending();
      }
    }

    final class DescendingSubMapEntryIterator extends SubMapIterator<MapKI.Entry<K>> {
      DescendingSubMapEntryIterator(TMapKI.Entry<K> last, TMapKI.Entry<K> fence) {
        super(last, fence);
      }

      public MapKI.Entry<K> next() {
        return prevEntry();
      }

      public void remove() {
        removeDescending();
      }
    }

    final class DescendingSubMapKeyIterator extends SubMapIterator<K> {
      DescendingSubMapKeyIterator(TMapKI.Entry<K> last, TMapKI.Entry<K> fence) {
        super(last, fence);
      }

      public K next() {
        return prevEntry().key;
      }

      public void remove() {
        removeDescending();
      }
    }
  }

  /**
   * @serial include
   */
  static final class AscendingSubMapKI<K> extends NavigableSubMapKI<K> {
    private static final long serialVersionUID = 912986545866124060L;

    AscendingSubMapKI(TMapKI<K> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd,
        K hi, boolean hiInclusive) {
      super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
    }

    public Comparator<? super K> comparator() {
      return m.comparator();
    }

    public NavigableMapKI<K> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      if (!inRange(fromKey, fromInclusive))
        throw new IllegalArgumentException("fromKey out of range");
      if (!inRange(toKey, toInclusive))
        throw new IllegalArgumentException("toKey out of range");
      return new AscendingSubMapKI(m, false, fromKey, fromInclusive, false, toKey, toInclusive);
    }

    public NavigableMapKI<K> headMap(K toKey, boolean inclusive) {
      if (!inRange(toKey, inclusive))
        throw new IllegalArgumentException("toKey out of range");
      return new AscendingSubMapKI(m, fromStart, lo, loInclusive, false, toKey, inclusive);
    }

    public NavigableMapKI<K> tailMap(K fromKey, boolean inclusive) {
      if (!inRange(fromKey, inclusive))
        throw new IllegalArgumentException("fromKey out of range");
      return new AscendingSubMapKI(m, false, fromKey, inclusive, toEnd, hi, hiInclusive);
    }

    public NavigableMapKI<K> descendingMap() {
      NavigableMapKI<K> mv = descendingMapView;
      return (mv != null) ? mv : (descendingMapView = new DescendingSubMapKI(m, fromStart, lo,
          loInclusive, toEnd, hi, hiInclusive));
    }

    Iterator<K> keyIterator() {
      return new SubMapKeyIterator(absLowest(), absHighFence());
    }

    Iterator<K> descendingKeyIterator() {
      return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
    }

    final class AscendingEntrySetView extends EntrySetView {
      public Iterator<MapKI.Entry<K>> iterator() {
        return new SubMapEntryIterator(absLowest(), absHighFence());
      }
    }

    public Set<MapKI.Entry<K>> entrySet() {
      EntrySetView es = entrySetView;
      return (es != null) ? es : new AscendingEntrySetView();
    }

    TMapKI.Entry<K> subLowest() {
      return absLowest();
    }

    TMapKI.Entry<K> subHighest() {
      return absHighest();
    }

    TMapKI.Entry<K> subCeiling(K key) {
      return absCeiling(key);
    }

    TMapKI.Entry<K> subHigher(K key) {
      return absHigher(key);
    }

    TMapKI.Entry<K> subFloor(K key) {
      return absFloor(key);
    }

    TMapKI.Entry<K> subLower(K key) {
      return absLower(key);
    }
  }

  /**
   * @serial include
   */
  static final class DescendingSubMapKI<K> extends NavigableSubMapKI<K> {
    private static final long serialVersionUID = 912986545866120460L;

    DescendingSubMapKI(TMapKI<K> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd,
        K hi, boolean hiInclusive) {
      super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
    }

    private final Comparator<? super K> reverseComparator = Collections.reverseOrder(m.comparator);

    public Comparator<? super K> comparator() {
      return reverseComparator;
    }

    public NavigableMapKI<K> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      if (!inRange(fromKey, fromInclusive))
        throw new IllegalArgumentException("fromKey out of range");
      if (!inRange(toKey, toInclusive))
        throw new IllegalArgumentException("toKey out of range");
      return new DescendingSubMapKI(m, false, toKey, toInclusive, false, fromKey, fromInclusive);
    }

    public NavigableMapKI<K> headMap(K toKey, boolean inclusive) {
      if (!inRange(toKey, inclusive))
        throw new IllegalArgumentException("toKey out of range");
      return new DescendingSubMapKI(m, false, toKey, inclusive, toEnd, hi, hiInclusive);
    }

    public NavigableMapKI<K> tailMap(K fromKey, boolean inclusive) {
      if (!inRange(fromKey, inclusive))
        throw new IllegalArgumentException("fromKey out of range");
      return new DescendingSubMapKI(m, fromStart, lo, loInclusive, false, fromKey, inclusive);
    }

    public NavigableMapKI<K> descendingMap() {
      NavigableMapKI<K> mv = descendingMapView;
      return (mv != null) ? mv : (descendingMapView = new AscendingSubMapKI(m, fromStart, lo,
          loInclusive, toEnd, hi, hiInclusive));
    }

    Iterator<K> keyIterator() {
      return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
    }

    Iterator<K> descendingKeyIterator() {
      return new SubMapKeyIterator(absLowest(), absHighFence());
    }

    final class DescendingEntrySetView extends EntrySetView {
      public Iterator<MapKI.Entry<K>> iterator() {
        return new DescendingSubMapEntryIterator(absHighest(), absLowFence());
      }
    }

    public Set<MapKI.Entry<K>> entrySet() {
      EntrySetView es = entrySetView;
      return (es != null) ? es : new DescendingEntrySetView();
    }

    TMapKI.Entry<K> subLowest() {
      return absHighest();
    }

    TMapKI.Entry<K> subHighest() {
      return absLowest();
    }

    TMapKI.Entry<K> subCeiling(K key) {
      return absFloor(key);
    }

    TMapKI.Entry<K> subHigher(K key) {
      return absLower(key);
    }

    TMapKI.Entry<K> subFloor(K key) {
      return absCeiling(key);
    }

    TMapKI.Entry<K> subLower(K key) {
      return absHigher(key);
    }
  }

  /**
   * This class exists solely for the sake of serialization compatibility with previous releases of
   * TMap that did not support NavigableMap. It translates an old-version SubMap into a new-version
   * AscendingSubMap. This class is never otherwise used.
   * 
   * @serial include
   */
  private class SubMapKI extends AbstractMapKI<K> implements SortedMapKI<K>, Serializable {
    private static final long serialVersionUID = -6520786458950516097L;
    private boolean fromStart = false, toEnd = false;
    private K fromKey, toKey;

    private Object readResolve() {
      return new AscendingSubMapKI(TMapKI.this, fromStart, fromKey, true, toEnd, toKey, false);
    }

    public Set<MapKI.Entry<K>> entrySet() {
      throw new InternalError();
    }

    public K lastKey() {
      throw new InternalError();
    }

    public K firstKey() {
      throw new InternalError();
    }

    public SortedMapKI<K> subMap(K fromKey, K toKey) {
      throw new InternalError();
    }

    public SortedMapKI<K> headMap(K toKey) {
      throw new InternalError();
    }

    public SortedMapKI<K> tailMap(K fromKey) {
      throw new InternalError();
    }

    public Comparator<? super K> comparator() {
      throw new InternalError();
    }
  }

  // Red-black mechanics

  private static final boolean RED = false;
  private static final boolean BLACK = true;

  /**
   * Node in the Tree. Doubles as a means to pass key-value pairs back to user (see MapKI.Entry).
   */

  static final class Entry<K> implements MapKI.Entry<K> {
    K key;
    int value;
    Entry<K> left = null;
    Entry<K> right = null;
    Entry<K> parent;
    boolean color = BLACK;

    /**
     * Make a new cell with given key, value, and parent, and with <tt>null</tt> child links, and
     * BLACK color.
     */
    Entry(K key, int value, Entry<K> parent) {
      this.key = key;
      this.value = value;
      this.parent = parent;
    }

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public K getKey() {
      return key;
    }

    /**
     * Returns the value associated with the key.
     * 
     * @return the value associated with the key
     */
    public int getValue() {
      return value;
    }

    /**
     * Replaces the value currently associated with the key with the given value.
     * 
     * @return the value associated with the key before this method was called
     */
    public int setValue(int value) {
      int oldValue = this.value;
      this.value = value;
      return oldValue;
    }

    public boolean equals(Object o) {
      if (!(o instanceof MapKI.Entry))
        return false;
      MapKI.Entry<?> e = (MapKI.Entry<?>) o;

      return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
    }

    public int hashCode() {
      int keyHash = (key == null ? 0 : key.hashCode());
      return keyHash ^ value;
    }

    public String toString() {
      return key + "=" + value;
    }
  }

  /**
   * Returns the first Entry in the TMap (according to the TMap's key-sort function). Returns null
   * if the TMap is empty.
   */
  final Entry<K> getFirstEntry() {
    Entry<K> p = root;
    if (p != null)
      while (p.left != null)
        p = p.left;
    return p;
  }

  /**
   * Returns the last Entry in the TMap (according to the TMap's key-sort function). Returns null if
   * the TMap is empty.
   */
  final Entry<K> getLastEntry() {
    Entry<K> p = root;
    if (p != null)
      while (p.right != null)
        p = p.right;
    return p;
  }

  /**
   * Returns the successor of the specified Entry, or null if no such.
   */
  static <K> TMapKI.Entry<K> successor(Entry<K> t) {
    if (t == null)
      return null;
    else if (t.right != null) {
      Entry<K> p = t.right;
      while (p.left != null)
        p = p.left;
      return p;
    } else {
      Entry<K> p = t.parent;
      Entry<K> ch = t;
      while (p != null && ch == p.right) {
        ch = p;
        p = p.parent;
      }
      return p;
    }
  }

  /**
   * Returns the predecessor of the specified Entry, or null if no such.
   */
  static <K> Entry<K> predecessor(Entry<K> t) {
    if (t == null)
      return null;
    else if (t.left != null) {
      Entry<K> p = t.left;
      while (p.right != null)
        p = p.right;
      return p;
    } else {
      Entry<K> p = t.parent;
      Entry<K> ch = t;
      while (p != null && ch == p.left) {
        ch = p;
        p = p.parent;
      }
      return p;
    }
  }

  /**
   * Balancing operations.
   * 
   * Implementations of rebalancings during insertion and deletion are slightly different than the
   * CLR version. Rather than using dummy nilnodes, we use a set of accessors that deal properly
   * with null. They are used to avoid messiness surrounding nullness checks in the main algorithms.
   */

  private static <K, V> boolean colorOf(Entry<K> p) {
    return (p == null ? BLACK : p.color);
  }

  private static <K, V> Entry<K> parentOf(Entry<K> p) {
    return (p == null ? null : p.parent);
  }

  private static <K, V> void setColor(Entry<K> p, boolean c) {
    if (p != null)
      p.color = c;
  }

  private static <K, V> Entry<K> leftOf(Entry<K> p) {
    return (p == null) ? null : p.left;
  }

  private static <K, V> Entry<K> rightOf(Entry<K> p) {
    return (p == null) ? null : p.right;
  }

  /** From CLR */
  private void rotateLeft(Entry<K> p) {
    if (p != null) {
      Entry<K> r = p.right;
      p.right = r.left;
      if (r.left != null)
        r.left.parent = p;
      r.parent = p.parent;
      if (p.parent == null)
        root = r;
      else if (p.parent.left == p)
        p.parent.left = r;
      else
        p.parent.right = r;
      r.left = p;
      p.parent = r;
    }
  }

  /** From CLR */
  private void rotateRight(Entry<K> p) {
    if (p != null) {
      Entry<K> l = p.left;
      p.left = l.right;
      if (l.right != null)
        l.right.parent = p;
      l.parent = p.parent;
      if (p.parent == null)
        root = l;
      else if (p.parent.right == p)
        p.parent.right = l;
      else
        p.parent.left = l;
      l.right = p;
      p.parent = l;
    }
  }

  /** From CLR */
  private void fixAfterInsertion(Entry<K> x) {
    x.color = RED;

    while (x != null && x != root && x.parent.color == RED) {
      if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
        Entry<K> y = rightOf(parentOf(parentOf(x)));
        if (colorOf(y) == RED) {
          setColor(parentOf(x), BLACK);
          setColor(y, BLACK);
          setColor(parentOf(parentOf(x)), RED);
          x = parentOf(parentOf(x));
        } else {
          if (x == rightOf(parentOf(x))) {
            x = parentOf(x);
            rotateLeft(x);
          }
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          rotateRight(parentOf(parentOf(x)));
        }
      } else {
        Entry<K> y = leftOf(parentOf(parentOf(x)));
        if (colorOf(y) == RED) {
          setColor(parentOf(x), BLACK);
          setColor(y, BLACK);
          setColor(parentOf(parentOf(x)), RED);
          x = parentOf(parentOf(x));
        } else {
          if (x == leftOf(parentOf(x))) {
            x = parentOf(x);
            rotateRight(x);
          }
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          rotateLeft(parentOf(parentOf(x)));
        }
      }
    }
    root.color = BLACK;
  }

  /**
   * Delete node p, and then rebalance the tree.
   */
  private void deleteEntry(Entry<K> p) {
    modCount++;
    size--;

    // If strictly internal, copy successor's element to p and then make p
    // point to successor.
    if (p.left != null && p.right != null) {
      Entry<K> s = successor(p);
      p.key = s.key;
      p.value = s.value;
      p = s;
    } // p has 2 children

    // Start fixup at replacement node, if it exists.
    Entry<K> replacement = (p.left != null ? p.left : p.right);

    if (replacement != null) {
      // Link replacement to parent
      replacement.parent = p.parent;
      if (p.parent == null)
        root = replacement;
      else if (p == p.parent.left)
        p.parent.left = replacement;
      else
        p.parent.right = replacement;

      // Null out links so they are OK to use by fixAfterDeletion.
      p.left = p.right = p.parent = null;

      // Fix replacement
      if (p.color == BLACK)
        fixAfterDeletion(replacement);
    } else if (p.parent == null) { // return if we are the only node.
      root = null;
    } else { // No children. Use self as phantom replacement and unlink.
      if (p.color == BLACK)
        fixAfterDeletion(p);

      if (p.parent != null) {
        if (p == p.parent.left)
          p.parent.left = null;
        else if (p == p.parent.right)
          p.parent.right = null;
        p.parent = null;
      }
    }
  }

  /** From CLR */
  private void fixAfterDeletion(Entry<K> x) {
    while (x != root && colorOf(x) == BLACK) {
      if (x == leftOf(parentOf(x))) {
        Entry<K> sib = rightOf(parentOf(x));

        if (colorOf(sib) == RED) {
          setColor(sib, BLACK);
          setColor(parentOf(x), RED);
          rotateLeft(parentOf(x));
          sib = rightOf(parentOf(x));
        }

        if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
          setColor(sib, RED);
          x = parentOf(x);
        } else {
          if (colorOf(rightOf(sib)) == BLACK) {
            setColor(leftOf(sib), BLACK);
            setColor(sib, RED);
            rotateRight(sib);
            sib = rightOf(parentOf(x));
          }
          setColor(sib, colorOf(parentOf(x)));
          setColor(parentOf(x), BLACK);
          setColor(rightOf(sib), BLACK);
          rotateLeft(parentOf(x));
          x = root;
        }
      } else { // symmetric
        Entry<K> sib = leftOf(parentOf(x));

        if (colorOf(sib) == RED) {
          setColor(sib, BLACK);
          setColor(parentOf(x), RED);
          rotateRight(parentOf(x));
          sib = leftOf(parentOf(x));
        }

        if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
          setColor(sib, RED);
          x = parentOf(x);
        } else {
          if (colorOf(leftOf(sib)) == BLACK) {
            setColor(rightOf(sib), BLACK);
            setColor(sib, RED);
            rotateLeft(sib);
            sib = leftOf(parentOf(x));
          }
          setColor(sib, colorOf(parentOf(x)));
          setColor(parentOf(x), BLACK);
          setColor(leftOf(sib), BLACK);
          rotateRight(parentOf(x));
          x = root;
        }
      }
    }

    setColor(x, BLACK);
  }

  private static final long serialVersionUID = 919286545866124006L;

  /**
   * Save the state of the <tt>TMap</tt> instance to a stream (i.e., serialize it).
   * 
   * @serialData The <i>size</i> of the TMap (the number of key-value mappings) is emitted (int),
   *             followed by the key (Object) and value (Object) for each key-value mapping
   *             represented by the TMap. The key-value mappings are emitted in key-order (as
   *             determined by the TMap's Comparator, or by the keys' natural ordering if the TMap
   *             has no Comparator).
   */
  private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
    // Write out the Comparator and any hidden stuff
    s.defaultWriteObject();

    // Write out size (number of Mappings)
    s.writeInt(size);

    // Write out keys and values (alternating)
    for (Iterator<MapKI.Entry<K>> i = entrySet().iterator(); i.hasNext();) {
      MapKI.Entry<K> e = i.next();
      s.writeObject(e.getKey());
      s.writeObject(e.getValue());
    }
  }

  /**
   * Reconstitute the <tt>TMap</tt> instance from a stream (i.e., deserialize it).
   */
  private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
    // Read in the Comparator and any hidden stuff
    s.defaultReadObject();

    // Read in size
    int size = s.readInt();

    buildFromSorted(size, null, s, 0);
  }

  /** Intended to be called only from TreeSet.readObject */
  void readTreeSet(int size, ObjectInputStream s, int defaultVal) throws IOException,
      ClassNotFoundException {
    buildFromSorted(size, null, s, defaultVal);
  }

  /** Intended to be called only from TreeSet.addAll */
  void addAllForTreeSet(SortedSet<? extends K> set, int defaultVal) {
    try {
      buildFromSorted(set.size(), set.iterator(), null, defaultVal);
    } catch (java.io.IOException cannotHappen) {
    } catch (ClassNotFoundException cannotHappen) {
    }
  }

  /**
   * Linear time tree building algorithm from sorted data. Can accept keys and/or values from
   * iterator or stream. This leads to too many parameters, but seems better than alternatives. The
   * four formats that this method accepts are:
   * 
   * 1) An iterator of Map.Entries. (it != null, defaultVal == null). 2) An iterator of keys. (it !=
   * null, defaultVal != null). 3) A stream of alternating serialized keys and values. (it == null,
   * defaultVal == null). 4) A stream of serialized keys. (it == null, defaultVal != null).
   * 
   * It is assumed that the comparator of the TMap is already set prior to calling this method.
   * 
   * @param size
   *          the number of keys (or key-value pairs) to be read from the iterator or stream
   * @param it
   *          If non-null, new entries are created from entries or keys read from this iterator.
   * @param str
   *          If non-null, new entries are created from keys and possibly values read from this
   *          stream in serialized form. Exactly one of it and str should be non-null.
   * @param defaultVal
   *          if non-null, this default value is used for each value in the map. If null, each value
   *          is read from iterator or stream, as described above.
   * @throws IOException
   *           propagated from stream reads. This cannot occur if str is null.
   * @throws ClassNotFoundException
   *           propagated from readObject. This cannot occur if str is null.
   */
  private void buildFromSorted(int size, Iterator it, ObjectInputStream str, int defaultVal)
      throws java.io.IOException, ClassNotFoundException {
    this.size = size;
    root = buildFromSorted(0, 0, size - 1, computeRedLevel(size), it, str, defaultVal);
  }

  /**
   * Recursive "helper method" that does the real work of the previous method. Identically named
   * parameters have identical definitions. Additional parameters are documented below. It is
   * assumed that the comparator and size fields of the TMap are already set prior to calling this
   * method. (It ignores both fields.)
   *
   * @param level the current level of tree. Initial call should be 0.
   * @param lo the first element index of this subtree. Initial should be 0.
   * @param hi the last element index of this subtree. Initial should be size-1.
   * @param redLevel
   *          the level at which nodes should be red. Must be equal to computeRedLevel for tree of
   *          this size.
   */
  private final Entry<K> buildFromSorted(int level, int lo, int hi, int redLevel, Iterator it,
      ObjectInputStream str, int defaultVal) throws IOException, ClassNotFoundException {
    /*
     * Strategy: The root is the middlemost element. To get to it, we have to first recursively
     * construct the entire left subtree, so as to grab all of its elements. We can then proceed
     * with right subtree.
     * 
     * The lo and hi arguments are the minimum and maximum indices to pull out of the iterator or
     * stream for current subtree. They are not actually indexed, we just proceed sequentially,
     * ensuring that items are extracted in corresponding order.
     */

    if (hi < lo) {
      return null;
    }

    int mid = (lo + hi) / 2;

    Entry<K> left = null;
    if (lo < mid) {
      left = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
    }

    // extract key and/or value from iterator or stream
    K key;
    int value;
    if (it != null) {
      key = (K) it.next();
      value = defaultVal;
    } else { // use stream
      key = (K) str.readObject();
      value = defaultVal;
    }

    Entry<K> middle = new Entry<K>(key, value, null);

    // color nodes in non-full bottommost level red
    if (level == redLevel)
      middle.color = RED;

    if (left != null) {
      middle.left = left;
      left.parent = middle;
    }

    if (mid < hi) {
      Entry<K> right = buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
      middle.right = right;
      right.parent = middle;
    }

    return middle;
  }

  /**
   * Find the level down to which to assign all nodes BLACK. This is the last `full' level of the
   * complete binary tree produced by buildTree. The remaining nodes are colored RED. (This makes a
   * `nice' set of color assignments wrt future insertions.) This level number is computed by
   * finding the number of splits needed to reach the zeroeth node. (The answer is ~lg(N), but in
   * any case must be computed by same quick O(lg(N)) loop.)
   */
  private static int computeRedLevel(int sz) {
    int level = 0;
    for (int m = sz - 1; m >= 0; m = m / 2 - 1) {
      level++;
    }
    return level;
  }
}
