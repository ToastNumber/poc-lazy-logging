POC - Lazy Logging
==================
Unnecessary logging can have an impact on your application's performance. A common
example of this is using `DEBUG` logs when your application's log level
is set to `INFO`.

Consider the following statement:

```java
LOG.debug("Unable to parse the xml document: " + someLargeXMLFile);
```

The string concatenation is still evaluated irrespective of the logging
level. A common fix to this is to use an if statement like follows:

```java
if (LOG.isDebugEnabled()) {
    LOG.debug("Unable to parse the xml document: " + someLargeXMLFile);
}
```

However, this makes the code less readable and forces the developer to
make two changes if the log level should be changed for this statement.
It is also easy to forget this check every time there is a log line.

An alternative solution is to provide a `Supplier` to the logger, which
supplies the message to log. For example:

```java
LOG.debug(() -> "Unable to parse the xml document: " + someLargeXMLFile)
```

Then the `debug` method should do the `isDebugEnabled` checking. This 
method prevents the string concatenation being executed unless it is required.
The `debug` method would look something like this:

```java
void debug(Supplier<String> messageSupplier) {
    if (isDebugEnabled()) {
        logger.debug(messageSupplier.get());
    }
}
```

This project has some code which benchmarks 

- the naive original method
- the lazy logging method
- the if-debug-then-log method

*High test volume, small log message concatenation*:
```
| Title      | Time taken | Ops/s      |
|------------|------------|------------|
|   Lazy cat |   54299776 |   18416282 | <-- lazy logging with a concatenation
|    If stmt |   54585521 |   18319876 | <-- if statement before logging
|  Naive cat |  130886319 |    7640217 | <-- normal logging with a concatenation

Lazy cat  : |||||||||||||||||||||||||||||||||||||||||||||||||| 18416282
If stmt   : ||||||||||||||||||||||||||||||||||||||||||||||||| 18319876
Naive cat : |||||||||||||||||||| 7640217
```

*Small test volume, large log message concatenation*:
```
| Title      | Time taken | Ops/s      |
|------------|------------|------------|
|    If stmt |    3200896 |     312412 |
|   Lazy cat |    3704180 |     269965 |
|  Naive cat |    6808277 |     146880 |

If stmt   : |||||||||||||||||||||||||||||||||||||||||||||||||| 312412
Lazy cat  : ||||||||||||||||||||||||||||||||||||||||||| 269965
Naive cat : ||||||||||||||||||||||| 146880
```

Conclusion
----------
The most important result is that **lazy logging is more performant that normal logging when string concatenation involved**.

There is a [pull request](https://github.com/qos-ch/slf4j/pull/70) on the 
SLF4J GitHub project with these changes. Until this is merged, you will 
either have to use an if statement to check what the logging level is, or
write a wrapper API to forward the result from the supplier to the logging
framework.
