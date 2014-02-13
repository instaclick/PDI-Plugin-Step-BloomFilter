IC Filter Plugin
=================

The purpose of the plugin is to filter out rows for uniqueness over a given amount of time. 

To implement this functionality, the plugin creates a set of bloom filters as files in the filesystem (also supports VFS for using HDFS).

As an input, the plugin expects a unix epoch timestamp to use for time calculations, and a value that will be used to test for uniqueness in the bloom filter.

The timestamp is divided by the divisor which is the time interval for which each bloom filter will be created. The number of lookups describes how far backwards in time we wish to look, which determines which files in the filesystem will be loaded to test against.

As an example, assume that you wanted to check for uniqueness of an input over a period of 24 hours at a 1 minute aggregation. The number of lookups would be 1440  (number of minutes in a day), and the interval would be 60 (seconds).

How the algorithm works
-----------------------

- The input timestamp is first divided by the divisor. 
- A check is made to see if a bloomfilter file exists for the resulting integer
- If the file exists, it is loaded into memory
- If it doesn't exist, a new empty bloom filter is created
- A test is made against the bloom filter for the row interval.
  - If the row exists, it is filtered out
  - If it does not exist, the plugin loads the bloom filter file for the previous interval and repeats the test for each previous interval period N times (number of lookups)
  - If not found in N lookups, it will add the item to the bloom filter for the current row interval, and pass the row
- At the end of execution, bloom filters which have been modified are flushed to storage.

Various caching techniques are employed to ensure speediness. This filter will handle late arriving data, however it cannot replay a stream that has been previously processed as the bloom filters persist (no rows would be passed as none of them would be unique).


Building
--------
The IC Filter Plugin is built with maven for dependency management. 
All you'll need to get started is maven.

    $ git clone git@github.com:instaclick/PDI-Plugin-Step-BloomFilter.git
    $ cd PDI-Plugin-Step-BloomFilter/ic-filter-plugin
    $ mvn package

This will produce a plugin in ic-filter-plugin/target/ic-filter-plugin-pdi-{version}.tar,
This archive can then be extracted into your Pentaho Data Integration plugin directory.
