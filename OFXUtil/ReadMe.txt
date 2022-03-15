

OFXParser is designed to be easy to use and easy to extend.  As much as possible, the program
automates testing your changes to the code, so you can add to the existing code and use existing 
unit tests to validate your changes.

The OFX tag mappings are defined in the TagMapVersionxxx classes.  You can add elements and 
aggregates to the mappings using the step-by-step instructions found below.  After you have 
changed a tag map, you can validate it using the TestTagMapsAndAggregates test class.  You 
don't have to write individual tests for new aggregates or mappings.  All you have to do is 
add a new tagmap to the existing test.

The ImportOFX class provides an example of how to use the OFXParser to import a bank statement.  
Copy this code into your program, and you can import a bank statement and get the transactions  
from it.


To add a new aggregate to a mapping:

  1) Create the aggregate extending the AbstractAggregate class.  Recommended naming convention 
     is to prefix the aggregate name with OFX to keep the parser objects distinct from the objects in 
     the calling program.  Each setter should do a dupeCheck to detect malformed OFX.  All fields 
     in the aggregate should be initialized to a non-null value (null object pattern).
     
  2) Add a setter to the aggregate that contains your new aggregate.  
     
  3) Add the aggregate and its fields to the tag map(s).
  
  3) Add the new aggregate to the createInstance method in the TestTagMapsAndAggregates unit test 
     and run the test to validate your tagmap changes and the aggregate.
  