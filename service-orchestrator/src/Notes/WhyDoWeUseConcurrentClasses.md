# Why Do We Use Concurrent Classes in Java?

In Java, when multiple threads operate on shared data structures (like lists, maps, etc.), we need to ensure that the operations are **thread-safe**. If not handled properly, it can lead to data inconsistency, unexpected behaviors, or program crashes.

The Java `java.util.concurrent` package provides thread-safe alternatives for common data structures like:
- `ConcurrentHashMap` (instead of `HashMap`)
- `CopyOnWriteArrayList` (instead of `ArrayList`)
- `ConcurrentLinkedQueue`, etc.

These classes are **optimized for concurrency**, meaning they allow multiple threads to work efficiently without blocking each other unnecessarily.

---

## Problem with Traditional `java.util` Classes

When you use traditional classes like `ArrayList`, `HashMap`, `HashSet`, etc., they are **not thread-safe**.

If multiple threads try to modify them simultaneously without synchronization, it can cause:
- **Data corruption**
- **Race conditions**
- **Unexpected exceptions**

### Example: Using ArrayList in a Multithreaded Environment (Problematic) 

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListThreadProblem {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("List size: " + list.size()); // Expected 2000, but might be less!
    }
}
```

**Issue**: The size might not be 2000 because both threads are modifying the `ArrayList` at the same time without synchronization.


### Problem during Iteration - `ConcurrentModificationException`

When iterating over traditional collections while other threads are modifying them, you may encounter **ConcurrentModificationException**.

Example:

```java
import java.util.ArrayList;
import java.util.List;

public class ConcurrentModificationDemo {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Thread t1 = new Thread(() -> {
            for (Integer value : list) {
                System.out.println(value);
                list.add(4); // Modifying list while iterating
            }
        });

        t1.start();
    }
}
```

**Result**:
```
Exception in thread "Thread-0" java.util.ConcurrentModificationException
```

Because the list structure is modified while being iterated.


---

## Solution 1: Synchronization (But Slower)

You can make traditional classes thread-safe manually:

```java
Collections.synchronizedList(new ArrayList<>());
```

But synchronized collections **block** all other threads during an operation, reducing performance.


---

## Solution 2: Use Concurrent Classes (Recommended)

Concurrent classes provide better concurrency by allowing **more parallelism** and **less locking**.

For example:

```java
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentListDemo {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("List size: " + list.size()); // Always 2000
    }
}
```

**Advantages:**
- No `ConcurrentModificationException` while iterating.
- Thread-safe without manually synchronizing.
- Better performance in highly concurrent systems.

---

## Common Concurrent Classes

| Class | Thread-Safe Alternative For | Usage |
|:-----|:---------------------------|:------|
| `ConcurrentHashMap` | `HashMap` | Highly concurrent map without locking entire map |
| `CopyOnWriteArrayList` | `ArrayList` | Best for lists where reads are more frequent than writes |
| `ConcurrentLinkedQueue` | `Queue` | Non-blocking queue for concurrent environments |
| `ConcurrentSkipListMap` | `TreeMap` | Sorted map supporting concurrent access |

---

# Conclusion

Concurrent classes from `java.util.concurrent` are **optimized for high-concurrency** scenarios where multiple threads operate simultaneously on shared data structures.

They provide better performance and prevent common multithreading issues like race conditions and `ConcurrentModificationException`.

Hence, when building multithreaded systems, it is highly recommended to use concurrent classes instead of traditional collection classes!

---

Hope you found this note useful! 🚀

