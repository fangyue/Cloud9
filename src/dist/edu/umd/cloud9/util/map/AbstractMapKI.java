// From jdk-6u23-fcs-src-b05-jrl-12_nov_2010.jar

package edu.umd.cloud9.util.map;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * This class provides a skeletal implementation of the <tt>MapKI</tt> interface, to minimize the
 * effort required to implement this interface.
 * </p>
 *
 * <p>
 * To implement an unmodifiable map, the programmer needs only to extend this class and provide an
 * implementation for the <tt>entrySet</tt> method, which returns a set-view of the map's mappings.
 * Typically, the returned set will, in turn, be implemented atop <tt>AbstractSet</tt>. This set
 * should not support the <tt>add</tt> or <tt>remove</tt> methods, and its iterator should not
 * support the <tt>remove</tt> method.
 * </p>
 *
 * <p>
 * To implement a modifiable map, the programmer must additionally override this class's
 * <tt>put</tt> method (which otherwise throws an <tt>UnsupportedOperationException</tt>), and the
 * iterator returned by <tt>entrySet().iterator()</tt> must additionally implement its
 * <tt>remove</tt> method.
 * </p>
 *
 * <p>
 * The programmer should generally provide a void (no argument) and map constructor, as per the
 * recommendation in the <tt>MapKI</tt> interface specification.
 * </p>
 */
public abstract class AbstractMapKI<K> implements MapKI<K> {
  /**
   * Sole constructor. (For invocation by subclass constructors, typically implicit.)
   */
  protected AbstractMapKI() {}

  // Query Operations

  public int size() {
    return entrySet().size();
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean containsValue(int value) {
    Iterator<Entry<K>> i = entrySet().iterator();
    while (i.hasNext()) {
      Entry<K> e = i.next();
      if (value == e.getValue())
        return true;
    }

    return false;
  }

  public boolean containsKey(K key) {
    Iterator<Entry<K>> i = entrySet().iterator();
    if (key == null) {
      while (i.hasNext()) {
        Entry<K> e = i.next();
        if (e.getKey() == null) {
          return true;
        }
      }
    } else {
      while (i.hasNext()) {
        Entry<K> e = i.next();
        if (key.equals(e.getKey())) {
          return true;
        }
      }
    }
    return false;
  }

  public int get(K key) {
    Iterator<Entry<K>> i = entrySet().iterator();
    if (key == null) {
      while (i.hasNext()) {
        Entry<K> e = i.next();
        if (e.getKey() == null) {
          return e.getValue();
        }
      }
    } else {
      while (i.hasNext()) {
        Entry<K> e = i.next();
        if (key.equals(e.getKey())) {
          return e.getValue();
        }
      }
    }
    return DEFAULT_VALUE;
  }

  // Modification Operations

  public int put(K key, int value) {
    throw new UnsupportedOperationException();
  }

  public int remove(K key) {
    Iterator<Entry<K>> i = entrySet().iterator();
    Entry<K> correctEntry = null;
    if (key == null) {
      while (correctEntry == null && i.hasNext()) {
        Entry<K> e = i.next();
        if (e.getKey() == null) {
          correctEntry = e;
        }
      }
    } else {
      while (correctEntry == null && i.hasNext()) {
        Entry<K> e = i.next();
        if (key.equals(e.getKey())) {
          correctEntry = e;
        }
      }
    }

    int oldValue = DEFAULT_VALUE;
    if (correctEntry != null) {
      oldValue = correctEntry.getValue();
      i.remove();
    }
    return oldValue;
  }

  // Bulk Operations

  public void putAll(MapKI<? extends K> m) {
    for (Entry<? extends K> e : m.entrySet())
      put(e.getKey(), e.getValue());
  }

  public void clear() {
    entrySet().clear();
  }

  // Views

  /**
   * Each of these fields are initialized to contain an instance of the appropriate view the first
   * time this view is requested. The views are stateless, so there's no reason to create more than
   * one of each.
   */
  transient volatile Set<K> keySet = null;
  transient volatile Collection<Integer> values = null;

  public Set<K> keySet() {
    if (keySet == null) {
      keySet = new AbstractSet<K>() {
        public Iterator<K> iterator() {
          return new Iterator<K>() {
            private Iterator<Entry<K>> i = entrySet().iterator();

            public boolean hasNext() {
              return i.hasNext();
            }

            public K next() {
              return i.next().getKey();
            }

            public void remove() {
              i.remove();
            }
          };
        }

        public int size() {
          return AbstractMapKI.this.size();
        }

        @SuppressWarnings("unchecked")
        public boolean contains(Object k) {
          return AbstractMapKI.this.containsKey((K) k);
        }
      };
    }
    return keySet;
  }

  public Collection<Integer> values() {
    if (values == null) {
      values = new AbstractCollection<Integer>() {
        public Iterator<Integer> iterator() {
          return new Iterator<Integer>() {
            private Iterator<Entry<K>> i = entrySet().iterator();

            public boolean hasNext() {
              return i.hasNext();
            }

            public Integer next() {
              return i.next().getValue();
            }

            public void remove() {
              i.remove();
            }
          };
        }

        public int size() {
          return AbstractMapKI.this.size();
        }

        public boolean contains(Object v) {
          return AbstractMapKI.this.containsValue((Integer) v);
        }
      };
    }
    return values;
  }

  public abstract Set<Entry<K>> entrySet();

  // Comparison and hashing

  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (o == this)
      return true;

    if (!(o instanceof MapKI)) {
      return false;
    }

    MapKI<K> m = (MapKI<K>) o;
    if (m.size() != size()) {
      return false;
    }

    try {
      Iterator<Entry<K>> i = entrySet().iterator();
      while (i.hasNext()) {
        Entry<K> e = i.next();
        K key = e.getKey();
        int value = e.getValue();
        if (value != m.get(key)) {
          return false;
        }
      }
    } catch (ClassCastException unused) {
      return false;
    } catch (NullPointerException unused) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int h = 0;
    Iterator<Entry<K>> i = entrySet().iterator();
    while (i.hasNext()) {
      h += i.next().hashCode();
    }
    return h;
  }

  public String toString() {
    Iterator<Entry<K>> i = entrySet().iterator();
    if (!i.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      Entry<K> e = i.next();
      K key = e.getKey();
      int value = e.getValue();
      sb.append(key == this ? "(this Map)" : key);
      sb.append('=');
      sb.append(value);
      if (!i.hasNext())
        return sb.append('}').toString();
      sb.append(", ");
    }
  }

  @SuppressWarnings("unchecked")
  protected Object clone() throws CloneNotSupportedException {
    AbstractMapKI<K> result = (AbstractMapKI<K>) super.clone();
    result.keySet = null;
    result.values = null;
    return result;
  }

  private static boolean eq(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  // Implementation Note: SimpleEntry and SimpleImmutableEntry are distinct unrelated classes, even
  // though they share some code. Since you can't add or subtract final-ness of a field in a
  // subclass, they can't share representations, and the amount of duplicated code is too small to
  // warrant exposing a common abstract class.

  /**
   * An Entry maintaining a key and a value. The value may be changed using the <tt>setValue</tt>
   * method. This class facilitates the process of building custom map implementations. For example,
   * it may be convenient to return arrays of <tt>SimpleEntry</tt> instances in method
   * <tt>entrySet().toArray</tt>.
   */
  public static class SimpleEntry<K> implements Entry<K>, Serializable {
    private static final long serialVersionUID = -6560229993033595828L;

    private final K key;
    private int value;

    public SimpleEntry(K key, int value) {
      this.key = key;
      this.value = value;
    }

    public SimpleEntry(Entry<? extends K> entry) {
      this.key = entry.getKey();
      this.value = entry.getValue();
    }

    public K getKey() {
      return key;
    }

    public int getValue() {
      return value;
    }

    public int setValue(int value) {
      int oldValue = this.value;
      this.value = value;
      return oldValue;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
      if (!(o instanceof Entry)) {
        return false;
      }
      Entry e = (Entry) o;
      return eq(key, e.getKey()) && value == e.getValue();
    }

    public int hashCode() {
      return (key == null ? 0 : key.hashCode()) ^ value;
    }

    public String toString() {
      return key + "=" + value;
    }
  }

  /**
   * An Entry maintaining an immutable key and value. This class does not support method
   * <tt>setValue</tt>. This class may be convenient in methods that return thread-safe snapshots of
   * key-value mappings.
   */
  public static class SimpleImmutableEntry<K> implements Entry<K>, Serializable {
    private static final long serialVersionUID = 5850113248055442458L;

    private final K key;
    private final int value;

    public SimpleImmutableEntry(K key, int value) {
      this.key = key;
      this.value = value;
    }

    public SimpleImmutableEntry(Entry<? extends K> entry) {
      this.key = entry.getKey();
      this.value = entry.getValue();
    }

    public K getKey() {
      return key;
    }

    public int getValue() {
      return value;
    }

    public int setValue(int value) {
      throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
      if (!(o instanceof Entry)) {
        return false;
      }
      Entry e = (Entry) o;
      return eq(key, e.getKey()) && value == e.getValue();
    }

    public int hashCode() {
      return (key == null ? 0 : key.hashCode()) ^ value;
    }

    public String toString() {
      return key + "=" + value;
    }
  }
}