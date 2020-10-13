#   Source Folder

Reference the [Instrumentation README](./main/java/README.md) for some basic steps to follow as well as helpful tips when instrumenting.

Your instrumentation should be added to the [main/java](./main/java/) folder.

The [example instrumentation](./example-instrumentation/java/targetclass/packagename/) demonstrates how to write instrumentation to be woven into the [original classes](./examples-to-instrument/java/targetclass/packagename/).

By convention of the gradle build script, these examples will not be included in your instrumentation jar.

*For any bugs discovered in the examples, please feel free to submit an [issue](https://newrelic.atlassian.net/secure/CreateIssue!default.jspa) or [pull request](../../../pulls)*

##  Weaving Demonstration
Lets say we want to create a metric representing the size of our ThreadPoolExecutor queues.  The workQueue is a BlockingQueue which has a `size()` method.  Here's an abreviated view of that implementation:

```java
package java.util.concurrent;
// import ...
public class ThreadPoolExecutor extends AbstractExecutorService {
    private final BlockingQueue<Runnable> workQueue;
    protected void beforeExecute(Thread t, Runnable r) {
        // Assume other code here.
    }
}
```

Here is what our instrumentation may look like:

```java
package java.util.concurrent;
// import ...
@Weave
public class ThreadPoolExecutor {
    private BlockingQueue<Runnable> workQueue;
    protected void beforeExecute(Thread t, Runnable r) {
        NewRelic.recordMetric("Java/ThreadPoolExecutor/Thread/" + t.getName() + "/Size", workQueue.size());
        Weaver.callOriginal();
    }
}
```

This would result in instrumented byte code that looks like this:

```java
package java.util.concurrent;
// import ...
public class ThreadPoolExecutor extends AbstractExecutorService {
    private final BlockingQueue<Runnable> workQueue;
    protected void beforeExecute(Thread t, Runnable r) {
        NewRelic.recordMetric("Java/ThreadPoolExecutor/Thread/" + t.getName() + "/Size", workQueue.size());
        // Original code would be added here.
    }
}
```


The following files demonstrate various ways of instrumenting Java classes:

* [BasicClass.java](./examples/java/targetclass/packagename/BasicClass.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/BasicClass.java)
* [BasicInterface.java](./examples/java/targetclass/packagename/BasicInterface.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/BasicInterface.java)
* [BasicSuperClass.java](./examples/java/targetclass/packagename/BasicSuperClass.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/BasicSuperClass.java)
* [CallsOtherMethods.java](./examples/java/targetclass/packagename/CallsOtherMethods.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/CallsOtherMethods.java)
* [CallsUtilityMethods.java](./examples/java/targetclass/packagename/CallsUtilityMethods.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/CallsUtilityMethods.java)
* [NewFieldPrivateMethods.java](./examples/java/targetclass/packagename/NewFieldPrivateMethods.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/NewFieldPrivateMethods.java)
* [ThrowsException.java](./examples/java/targetclass/packagename/ThrowsException.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/ThrowsException.java)
* [WithFieldAndConstructors.java](./examples/java/targetclass/packagename/WithFieldAndConstructors.java) - [(original)](./examples-to-instrument/java/targetclass/packagename/WithFieldAndConstructors.java)
