# Flink - Spark - Streaming - Comparison
A collaboration to get into Flink - and Spark - Streaming, do some examples in the given frameworks and a final comparison between both frameworks. Results are shown in ./sparkflink04.pdf.

Collaborator: [RespectableRuessel](https://github.com/RespectableRuessel)
## Learned
 - Flink in depth with
    - sbt
    - DateSet
    - DataStream
        - state-management
        - different datasources
        - different datasinks
        - window-API with diffrent windows and how it internally works
        - different notions of time
        - Checkpoints
        - Flink - Cluster
        - other related stuff
 - Spark with
    - DataRDD
    - DStream
 - difference between real-time data-processing and near-real-time data-processing
## Prerequisites
 - sbt
 - eclpise
## Setup
 - project consists of 2 individually part
    1. flink-test
       - ```sbt test```
       - ```sbt eclipse```
       - ```sbt run```
    2. spark-test
       - ```sbt test```
       - ```sbt eclipse```
       - ```sbt run```
## TL;DR
--
## Build with
- sbt
## Acknowledgements
 - thanks to my awesome collaborator: [RespectableRuessel](https://github.com/RespectableRuessel)

