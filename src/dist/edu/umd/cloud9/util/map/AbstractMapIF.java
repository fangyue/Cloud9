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
 * This class provides a skeletal implementation of the <tt>MapII</tt> interface, to minimize the
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
 * recommendation in the <tt>MapII</tt> interface specification.
 * </p>
 */
public abstract class AbstractMapIF implements MapIF {
  public static float DEFAULT_VALUE = 0.0f;

  /**
   * Sole constructor. (For invocation by subclass constructors, typically implicit.)
   */
  protected AbstractMapIF() {}

  // Query Operations

  @Override
  public int size() {
    return entrySet().size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsValue(float value) {
    Iterator<Entry> i = entrySet().iterator();
    while (i.hasNext()) {
      Entry e = i.next();
      if (value == e.getValue())
        return true;
    }

    return false;
  }

  @Override
  public boolean containsKey(int key) {
    Iterator<Entry> i = entrySet().iterator();
    while (i.hasNext()) {
      Entry e = i.next();
      if (key == e.getKey()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public float get(int key) {
    Iterator<Entry> i = entrySet().iterator();
    while (i.hasNext()) {
      Entry e = i.next();
      if (key == e.getKey()) {
        return e.getValue();
      }
    }
    return DEFAULT_VALUE;
  }

  // Modification Operations

  @Override
  public float put(int key, float value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public float remove(int key) {
    Iterator<Entry> i = entrySet().iterator();
    Entry correctEntry = null;
    while (correctEntry == null && i.hasNext()) {
      Entry e = i.next();
      if (key == e.getKey()) {
        correctEntry = e;
      }
    }

    float oldValue = DEFAULT_VALUE;
    if (correctEntry != null) {
      oldValue = correctEntry.getValue();
      i.remove();
    }
    return oldValue;
  }

  // Bulk Operations

  @Override
  public void putAll(MapIF m) {
    for (MapIF.Entry e : m.entrySet())
      put(e.getKey(), e.getValue());
  }

  @Override
  public void clear() {
    entrySet().clear();
  }

  // Views

  /**
   * Each of these fields are initialized to contain an instance of the appropriate view the first
   * time this view is requested. The views are stateless, so there's no reason to create more than
   * one of each.
   */
  transient volatile Set<Integer> keySet = null;
  transient volatile Collection<Float> values = null;

  @Override
  public Set<Integer> keySet() {
    if (keySet == null) {
      keySet = new AbstractSet<Integer>() {
        public Iterator<Integer> iterator() {
          return new Iterator<Integer>() {
            private Iterator<Entry> i = entrySet().iterator();

            public boolean hasNext() {
              return i.hasNext();
            }

            public Integer next() {
              return i.next().getKey();
            }

            public void remove() {
              i.remove();
            }
          };
        }

        public int size() {
          return AbstractMapIF.this.size();
        }

        public boolean contains(Object k) {
          return AbstractMapIF.this.containsKey((Integer) k);
        }
      };
    }
    return keySet;
  }

  @Override
  public Collection<Float> values() {
    if (values == null) {
      values = new AbstractCollection<Float>() {
        public Iterator<Float> iterator() {
          return new Iterator<Float>() {
            private Iterator<Entry> i = entrySet().iterator();

            public boolean hasNext() {
              return i.hasNext();
            }

            public Float next() {
              return i.next().getValue();
            }

            public void remove() {
              i.remove();
            }
          };
        }

        public int size() {
          return AbstractMapIF.this.size();
        }

        public boolean contains(Object v) {
          return AbstractMapIF.this.containsValue((Float) v);
        }
      };
    }
    return values;
  }

  public abstract Set<Entry> entrySet();

  // Comparison and hashing

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;

    if (!(o instanceof MapII)) {
      return false;
    }

    MapII m = (MapII) o;
    if (m.size() != size()) {
      return false;
    }

    try {
      Iterator<Entry> i = entrySet().iterator();
      while (i.hasNext()) {
        Entry e = i.next();
        int key = e.getKey();
        float value = e.getValue();
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

  @Override
  public int hashCode() {
    int h = 0;
    Iterator<Entry> i = entrySet().iterator();
    while (i.hasNext()) {
      h += i.next().hashCode();
    }
    return h;
  }

  @Override
  public String toString() {
    Iterator<Entry> i = entrySet().iterator();
    if (!i.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      Entry e = i.next();
      int key = e.getKey();
      float value = e.getValue();
      sb.append(key);
      sb.append('=');
      sb.append(value);
      if (!i.hasNext())
        return sb.append('}').toString();
      sb.append(", ");
    }
  }

  protected Object clone() throws CloneNotSupportedException {
    AbstractMapII result = (AbstractMapII) super.clone();
    result.keySet = null;
    result.values = null;
    return result;
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
  public static class SimpleEntry implements Entry, Serializable {
    private static final long serialVersionUID = 5527510751453323102L;

    private final int key;
    private float value;

    public SimpleEntry(int key, float value) {
      this.key = key;
      this.value = value;
    }

    public SimpleEntry(Entry entry) {
      this.key = entry.getKey();
      this.value = entry.getValue();
    }

    @Override
    public int getKey() {
      return key;
    }

    @Override
    public float getValue() {
      return value;
    }

    @Override
    public float setValue(float value) {
      float oldValue = this.value;
      this.value = value;
      return oldValue;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof MapIF.Entry)) {
        return false;
      }
      Entry e = (Entry) o;
      return key == e.getKey() && value == e.getValue();
    }

    @Override
    public int hashCode() {
      return key ^ (int) value;
    }

    @Override
    public String toString() {
      return key + "=" + value;
    }
  }

  /**
   * An Entry maintaining an immutable key and value. This class does not support method
   * <tt>setValue</tt>. This class may be convenient in methods that return thread-safe snapshots of
   * key-value mappings.
   */
  public static class SimpleImmutableEntry implements Entry, Serializable {
    private static final long serialVersionUID = 7043182153810505751L;

    private final int key;
    private final float value;

    public SimpleImmutableEntry(int key, float value) {
      this.key = key;
      this.value = value;
    }

    public SimpleImmutableEntry(Entry entry) {
      this.key = entry.getKey();
      this.value = entry.getValue();
    }

    @Override
    public int getKey() {
      return key;
    }

    @Override
    public float getValue() {
      return value;
    }

    @Override
    public float setValue(float value) {
      throw new UnsupportedOperationException();
    }

    @Override @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
      if (!(o instanceof MapKF.Entry)) {
        return false;
      }
      Entry e = (Entry) o;
      return key == e.getKey() && value == e.getValue();
    }

    @Override
    public int hashCode() {
      return key ^ (int) value;
    }

    @Override
    public String toString() {
      return key + "=" + value;
    }
  }
}
