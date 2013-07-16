# pentaho configuration

# Configuration
Expected number of elements = 1000              # integer   -> Expected number of elements
False positive probability  = 0.1               # double    -> False positive probability
Number of lookups           = 1440              # integer   -> Number of times to lookups into files
Hash Field                  = hash              # string    -> hash field
Timestamp Field             = timestamp         # string    -> timestamp field
URL to store filters        = tmp://ic-filter/  # string    -> Filters URI
Filter Div                  = 60                # double    -> The integer result of (${row.timestamp} / ${division}) will be the filter hash code
                                                # Epoch times divided by 60 equals epoch minutes
# Variables
ic.filter.enabled.provider.hdfs = false # boolean   -> Enable HdfsFilterProvider filter provider

#### expected logging
#### - IC Filter.0 - Filter URI (tmp://ic-filter/)
#### - IC Filter.0 - Expected Number Of Elements (1000)
#### - IC Filter.0 - False Positive Probability (0.1)
#### - IC Filter.0 - Time Div (60)
#### - IC Filter.0 - Number Of Lookups (1440)
#### - IC Filter.0 - 1000 - Ignore row : 04ef781c2a66a09ba4ad8e7f1e74afa7
#### - IC Filter.0 - Flush filters to vfs directory
#### - IC Filter.0 - Signaling 'output done' to 1 output rowsets.
#### - IC Filter.0 - Finished processing (I=0, O=0, R=1000, W=999, U=0, E=0)