OFXUtil
==========



## Description

OFXUtil is a utility to parse Open Financial Exchange (OFX) version 1.02 files into a java data structure 
contained within the OFXContext container object. It will parse both bank and card message responses.
The utility also allows you to populate an OFXContext object and export it as an OFX file, so the
utility will both import and export OFX files. 

## Usage
To see an example of how the java data structure works look at the OFXImporterTest.java file. 
To see an example of the export function look at the OFXExporterTest.java file. 

## Maintenance
The OFXParser class is designed to be easy to use and easy to extend.  As much as possible, the program automates testing your changes to the code, so you can add to the existing code and use existing unit tests to validate your changes.

The OFX tag mappings are defined in the TagMap class.  You can add elements and 
aggregates to the mappings using the step-by-step instructions found below.  After you have 
changed a tag map, you can validate it using the TagMapAndAggregatesTest class.  You 
don't have to write individual tests for new aggregates or mappings.  All you have to do is 
add a new tagmap to the existing test.  If you add a new handler you should write a test for that.

To add a new aggregate to a mapping:

  1) Create the aggregate extending the AbstractAggregate class.  Recommended naming convention 
     is to prefix the aggregate name with OFX to keep the parser objects distinct from the objects in 
     the calling program.  Each setter should do a dupeCheck to detect malformed OFX.  All fields 
     in the aggregate should be initialized to a non-null value (null object pattern).
     
  2) Add a setter to the parent aggregate that contains your new aggregate.  
     
  3) Add the aggregate and its fields to the TagMap class.
  
  3) Add the new aggregate to the createInstance method in the TagMapsAndAggregatesTest unit test 
     and run the test to validate your tagmap changes and the aggregate.