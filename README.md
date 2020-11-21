JStreamish
==============

`java.util.stream` utility classes and functions. 
Includes:
* Interfaces for closable and peekable iterators
* Stream transformations such as:
  * Stream traversal via a consumer function, see `StreamUtil.forEachPair()` overloaded methods
  * Stream to Map, List, Set, and Array, see StreamUtil.to*() methods
  * Iterator to Stream, see `StreamUtil.asStream()` overloaded methods
  * Stream to multiple Streams via a predicate/filter, see `StreamSplitFilter.split*()` methods
* Converters:
  * Iterator to Supplier via `IteratorToSupplier`
  * Supplier to Iterator and `PeekableIterator` via `EnhancedIterator`
  * Supplier to ListIterator with list of results via `EnhancedListIterator`
 