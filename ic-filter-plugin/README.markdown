IC Filter Plugin
=================

The IC Filter Plugin Project provides support for Bloom Filter. 
It is a plugin for the Pentaho Kettle engine which can be used within Pentaho Data Integration (Kettle).

Building
--------
The IC Filter Plugin is built with Apache Ant and uses Apache Ivy for dependency management. 
All you'll need to get started is Ant 1.7.0 or newer to build the project. 
The build scripts will download Ivy if you do not already have it installed.

    $ git clone git@github.com:instaclick/PDI-Plugin-Step-BloomFilter.git
    $ cd bloomfilter-plugin
    $ ant

This will produce a plugin in ic-filter-plugin/deploydir This archive can then be extracted into your Pentaho Data Integration plugin directory.

Developing with Eclipse
---------------
Import ic-filter-plugin into Eclipse
Running the following targets will configure the Eclipse project to reference the required libraries:
ant resolve create-dot-classpath


Pentaho Configuration
---------------------

* Expected number of elements = 1000              # integer   -> Expected number of elements
* False positive probability  = 0.1               # double    -> False positive probability
* Number of lookups           = 1440              # integer   -> Number of times to lookups into files
* Hash Field                  = hash              # string    -> hash field
* Timestamp Field             = timestamp         # string    -> timestamp field
* URL to store filters        = tmp://ic-filter/  # string    -> Filters URI
* Filter Div                  = 60                # double    -> The integer result of (${row.timestamp} / ${division}) will be the filter hash code
                                                  # Epoch times divided by 60 equals epoch minutes

Pentaho Variables
------------------
ic.filter.enabled.provider.hdfs = false # boolean   -> Enable HdfsFilterProvider filter provider
