# A functional iterator library for Java

Provides support for a multitude of sequence manipulation features for both objects and primitives using sequential lazy evaluating iterators inspired by Java streams, Python generators and Scala collections. An compact, immutable `List` alternative (`Seq`) is also provided which comes with lots of convenience methods for piping data using the aforementioned iterators. Since immutability is guaranteed a `Seq` can also contruct the 'perfect' spliterator for parallel streams.

#### Why use this library?

Let me make it clear that the introduction of streams and lambdas in Java 8 was a **significant** improvement to the Java programming language. However I feel that the syntax of transforming streams to concrete collections is far too verbose and makes streams really frustrating to use. This is a library built for *sequential* operations on collections of data which builds on the existing Java `Iterator` interface with an API roughly aligned with that of the stream library. Therefore this is not a library designed to replace streams, but one to complement them and together encourage better (and more enjoyable) programming practices. 

I've spent a large amount of time working with streams and found myself writing variants of the following code an awful lot:

```
List<MyObject> dataCollection = ...;
List<String> dataNames = dataCollection.stream().map(MyObject::toString).collect(Collectors.toList());

```

Is this really the best we can do for a simple mapping operation? Sure we could favourite static imports so we can reduce the size a bit but I found this to interrupt my flow and generally be a bit frustrating. Wouldn't it be nicer (and equally as readable) to have something like this:

```
List<MyObject> dataCollection = ...;
List<String> dataNames = dataCollection.map(MyObject::toString).toList();
```
well I definitely think so.

I also have some gripes with the decision (made long ago) to make mutability the default choice for the `Collection` interface. Clearly immutable should be the default choice and then you can introduce mutable collections separately (as in Scala). Using streams means using mutable collections and so I introduced the `Seq` interface which is essentially a read-only view onto an array which comes with some nice functionality.

Finally the constraints on consuming streams in a custom way can sometimes be prohibitively restrictive for even very simple use cases, an example is drawing a polygon represented by a stream of points onto a JavaFX canvas **without caching the points first**. This is a trivial task with the polygon represented by an *iterator* of points since we can easily apply custom logic in the consumption of the iterator. No such luck with a stream.

To conclude, this library adds functionality in the style of Streams with some tweaks to the API in a way optimised for sequential operations. At a deeper level it trades potential parallelism for convenience, immutability and flexibility in custom consumption. It should be seen as a lightweight complement to Steams, not a replacement.

#### API examples

###### Mapping

``` 
Iter.over("a", "b", "c").map(x -> x + x).toList();           ==> ["aa", "bb", "cc"]
```

###### Filtering

```
Iter.overInts(1, 2, 3).filter(x -> (x % 2) == 0).toArray();  ==> [2]
```

###### Take, takeWhile, drop, dropWhile

```
Seq<String> someStrings = Seq.of("0", "1", "2", "3");

someStrings.take(2).toSet();                                 ==> {"0", "1"}
someStrings.drop(2).toMutableSet();                          ==> {"3", "2"}

someStrings.takeWhile(x -> x < "2").toList();                ==> ["0", "1"]
someStrings.dropWhile(x -> x < "2");                         ==> Seq["2", "3"]
```

###### Building primitive ranges

```
IterRange.to(5).toArray();                                   ==> [0, 1, 2, 3, 4]
IterRange.between(2, 6).toArray();                           ==> [2, 3, 4, 5]
IterRange.partition(0, 1, .2).toArray();                     ==> [0, .2, .4, .6, .8, 1]
```

###### Creating Maps and arbitrary mutable collections

```
Iter.over("a", "b").toMap(x -> x, x -> x + x);               ==> {"a": "aa", "b": "bb"}
Iter.over("0", "1", "2", "3").groupBy(x -> parseInt(x) % 2); ==> {0: ["0", "2"], 1: ["1", "3"]}
Iterate.over("0", "1").toCollection(ArrayList::new);         ==> ArrayList["0", "1"]
```

###### Zipping and enumerating

```
Seq<String> strings = Seq.of("a", "b");
Seq<Integer> integers = Seq.of(1, 2, 3);

strings.flow().zipWith(integers).toSeq();                    ==> Seq[("a", 1), ("b", 2)]
strings.flow().enumerate().toSeq();                          ==> [(0, "a"), (1, "b")]
```

###### Folding
```
Iter.over("0", "1", "2", "3")
.fold(new StringBuilder(), (b, s) -> b.append(s))
.toString();                                                 ==> "0123"
```

#### Building the Jar files and documentation

To us this library you need to build the archives and documentation from this source 
repository. To build the latest version on Windows do the following:

1. Clone the parent repository (this project is only a subproject).
2. Navigate to the directory containing the gradlew executable in the terminal.
3. Run the command `./gradlew :JFlow:clean :JFlow:build` (use `gradlew :JFlow:clean :JFlow:build`` if running windows).

The jars (including source and Javadoc) will be built in `JFlow/build/libs` directory and an uncompressed version of the documentation ready to be viewed in a browser will be built in the `JFlow/build/docs/javadoc` directory.
