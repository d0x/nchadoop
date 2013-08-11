# nchadoop is a *ncdu* like file browser for Hadoop to identify wasted disc space

It can scans folders on hdfs and procudes are cli like this:

```
 --- /user/christian/thrift/
                        /..
   20.9MiB [##########]  libtestgencpp.a
    6.5MiB [###       ]  DebugProtoTest_types.o
    4.6MiB [##        ]  ThriftTest_types.o
    3.9MiB [#         ]  Benchmark
    1.3MiB [          ]  ThriftTest_extras.o
    1.2MiB [          ]  DebugProtoTest_extras.o
  728.0KiB [          ]  OptionalRequiredTest_types.o
  560.0KiB [          ]  libprocessortest.a
  252.0KiB [          ]  ChildService.o
  216.0KiB [          ]  ParentService.o
   96.0KiB [          ]  proc_types.o
```
