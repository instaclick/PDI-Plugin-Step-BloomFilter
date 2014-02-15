IC Filter Plugin
=================

The IC Filter Plugin Project provides support for Bloom Filter. 
It is a plugin for the Pentaho Kettle engine which can be used within Pentaho Data Integration (Kettle).

Building
--------
The IC Filter Plugin is built with Apache Ant and uses Apache Ivy for dependency management. 
All you'll need to get started is Ant 1.7.0 or newer to build the project. 
The build scripts will download Ivy if you do not already have it installed.

    $ git clone https://github.com/instaclick/PDI-Plugin-Step-BloomFilter.git
    $ cd PDI-Plugin-Step-BloomFilter
    $ ant

This will produce a plugin in ic-filter-plugin/deploydir This archive can then be extracted into your Pentaho Data Integration plugin directory.

Download Packages
-----------------
https://github.com/instaclick/pdi-marketplace-packages


PDI Step Configuration
-----------------------

| Property                          | Description                                   | Default           |
| ----------------------------------|:---------------------------------------------:|------------------:|
| Unique Fields                     | Unique fields                                 |                   |
| Timestamp field                   | Timestamp field used to distribute the row    | 0.001             |
| Timestamp window size             | Timestamp window size to distribute rows      | 60 (seconds)      |
| Number of lookups                 | Number of times to lookup for filter          | 1440              |
| URI to store filters              | VFS folder to store filters                   | tmp://ic-filter/  |
| Filter Type                       | Filter data structure (BLOOM/MAP)             | BLOOM             |
| Hash Function                     | Unique fields hash function                   | NONE              |
| Expected number of elements       | Bloom filter expected number of elements      | 1000              |
| False positive probability        | Bloom filter False positive probability       | 0.001             |
| Make the step transactional       | Commit filter changes only noting else fails  | unchecked         |
| Always pass the row               | Always pass the row add a flag field          | unchecked         |
| Unique flag Field                 | Unique flag added to the row                  | is_unique         |
