--------
####0.1.2
date: 2016-2-24

commit: ?

* Moved StringLineSupplier to [JTextParser] (https://github.com/TeamworkGuy2/JTextParser) library
* Moved twg2.streams.test package to separate test directory


--------
####0.1.1
date: 2016-2-21

commit: ea30287a8782f63b774c8bf5ca4757e2ae9a7c6d

* Added optional transform function parameter to EnhancedIterator's static helper fromPath(), fromReader(), and fromUrl() methods.


--------
####0.1.0
date: 2016-2-21

commit: 3fa1743d5a20363ff23a8e79ab3f2c74ca75f902

* Versioning of existing code. Including interfaces for closable and peekable iterators, stream transformations, iterator-to-supplier, and enhanced iterators for creating peekable-iterable views of suppliers.
* Added additional constructor flags to StringLineSupplier to allow control of whether newlines are included at the end of the returned lines.