# ParkGuidance-Elastic

The elastic component is designed establish permanent a connection to an ElasticSearch cluster and handle their traffic.

## Correspondence

The entire traffic is handheld through the elastic provided dependency Elasticsearch-Rest-High-Level-Client. 

The only other feature used by parkguidance is the bulkprocessor to handle lots of parkingdata sent to elasticsearch.

All other correspondence is sent through it's Low Level Client as raw HTTP JSON queries.

 >Back to  [README.MD](../README.md)
