# phentaho configuration

ic.filter.elements      = 1000              # integer   -> Expected number of elements
ic.filter.probability   = 0.1               # double    -> False positive probability
ic.filter.lookups       = 1440              # integer   -> Number of times to lookups into files
ic.filter.field.hash    = hash              # string    -> hash field
ic.filter.field.time    = timestamp         # string    -> timestamp field
ic.filter.directory     = tmp://ic-filter/  # string    -> URL where to store filters
ic.filter.provider      = VFS               # string    -> filter provider (VFS / MEMORY)
ic.filter.division      = 60                # double    -> The integer result of (${row.timestamp} / ${division}) will be the filter hash code

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