IC Filter Plugin
=================

The IC Filter Plugin Project provides support for Bloom Filter. 
It is a plugin for the Pentaho Kettle engine which can be used within Pentaho Data Integration (Kettle).

Building
--------
The IC Filter Plugin is built with Apache Ant and uses Apache Ivy for dependency management. 
All you'll need to get started is Ant 1.7.0 or newer to build the project. 
The build scripts will download Ivy if you do not already have it installed.

    $ git clonegit@git.dev:pentaho/bloomfilter-plugin.git
    $ cd bloomfilter-plugin
    $ ant

This will produce a plugin in ic-filter-plugin/deploydir This archive can then be extracted into your Pentaho Data Integration plugin directory.

Further Reading
---------------
Additional documentation is available on the Community wiki: [Big Data Plugin for Java Developers](http://wiki.pentaho.com/display/BAD/Getting+Started+for+Java+Developers)

License
-------
Licensed under the Apache License, Version 2.0. See LICENSE.txt for more information.
