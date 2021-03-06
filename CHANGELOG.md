# Change Log
All notable changes to this project will be documented in this file.
This project does its best to adhere to [Semantic Versioning](http://semver.org/).


--------
### [0.3.0](N/A) - 2020-11-21
#### Added
* `EnhancedListIterator` - a list based peekable iterator

#### Changed
* Last parameter of `StreamSplitFilter.splitFilter()` and `split2Way()` changed from `Function<E, Boolean>` -> `Predicate<E>`

#### Removed
* `EnhancedListBuilderIterator` due to it not being used in any known projects

#### Fixed
* A `IterableLimited` bug which caused it to return the iterator one more time than the limit
* `StreamSplitFilter` not throwing an error when the source collection is not a list and `allowNegativeToFilter` is false and the `mapper` returns `-1`


--------
### [0.2.0](https://github.com/TeamworkGuy2/JStreamish/commit/5018a2a6bde302e03da169d36a383aa905498a09) - 2016-08-25
#### Added
* Added `StreamUtil.forEachPair()` overloads that take BaseStream and Iterator parameters

#### Changed
* Changed `StreamUtil.forEachPair(Collection, Collection, BiConsumer)` -> `forEachPair(Iterable, Iterable, BiConsumer)`
* Switched versions.md format to CHANGELOG.md, see http://keepachangelog.com/

#### Removed
* StreamMap - moved these methods to the jcollection-builders project MapUtil and ListUtil classes

#### Fixed
* Minor `StreamUtil.forEachPair()` optimization check bug which could have caused it to run slower for List arguments that did not implement RandomAccess (e.g. LinkedList) instead of falling back to `iterator()`


--------
### [0.1.2](https://github.com/TeamworkGuy2/JStreamish/commit/5f11bc7ea8d69b0f6d81ac1a5153f85913da1db5) - 2016-02-24
#### Changed
* Moved StringLineSupplier to [JTextParser] (https://github.com/TeamworkGuy2/JTextParser) library
* Moved twg2.streams.test package to separate test directory


--------
### [0.1.1](https://github.com/TeamworkGuy2/JStreamish/commit/ea30287a8782f63b774c8bf5ca4757e2ae9a7c6d) - 2016-02-21
#### Added
* Added optional transform function parameter to EnhancedIterator's static helper fromPath(), fromReader(), and fromUrl() methods.


--------
### [0.1.0](https://github.com/TeamworkGuy2/JStreamish/commit/3fa1743d5a20363ff23a8e79ab3f2c74ca75f902) - 2016-02-21
#### Added
* Versioning of existing code. Including interfaces for closable and peekable iterators, stream transformations, iterator-to-supplier, and enhanced iterators for creating peekable-iterable views of suppliers.
* Added additional constructor flags to StringLineSupplier to allow control of whether newlines are included at the end of the returned lines.
