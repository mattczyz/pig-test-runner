# pig-test-runner
#### Simple way to develop and test Pig scripts locally

## About
The tool helps developers to create Pig scripts with the test first approach. It uses a set of test definitions passed in as configuration. Tests operate on locally stored mocks or samples of real datasets. The output of the processing is verified against the expected result with a customisable result validator. 

To execute tests, internally pig-test-runner uses Pig unit testing framework and JUnit. It can be a part of an automated build process, as well as used within visual tools, e.g. Eclipse JUnit testing, allowing a selective test run. 

It is possible to use external or embedded Hive Metastore to deploy of HQL schema definitions and access HCatalogue from Pig. To support custom output formats, the default test result validator can be overridden by providing a custom implementation.

## Usage

#### Defining tests
e.g. pig-test-def.json

```
[{
    "id": "word-count",
    "file": "examples/word_count/word_count.pig",
    "args": 
    {
        "input": "examples/word_count/input",
        "output": "examples/word_count/output"
    },
    "tests": [
        {
            "args": 
            {
                "output":"examples/word_count/output",
                "expected": "examples/word_count/expected"
            }
        }
    ]
}] 
```

*file -* path to the Pig script file

*args -* arguments passed to the script normally specified with –param flag. Paths must follow the hadoop fs specs. e.g. input=examples/word_count/input.

*tests -* tests definitions

*tests[0].name -* Name of test validator. Default: *DirContentEqual*

*tests[0].args -* Arguments required by test validator e.g. for *DirContentEqual* required args are *expected* and *output*. Paths are specific to local file system or as per test validator requirements.

#### Pig with HCatalog 

```
[{
    "id": "word-count-hive",
    "file": "examples/word_count_hive/word_count_hive.pig",
    "args": 
    {
        "input_table": "mydb.words",
        "output": "examples/word_count_hive/output"
    },
    "hiveCli": [
        ["-e", "dfs -rm -r -f examples/word_count_hive/output"],
        ["-e", "CREATE DATABASE mydb;"],
        [
         "-f", "examples/word_count_hive/words.hql",
         "--hiveconf", "words_path=examples/word_count_hive/input"
        ]
    ],
    "tests": [
        {
            "name": "DirContentEqual",
            "args": 
            {
                "output":"examples/word_count_hive/output",
                "expected": "examples/word_count_hive/expected"
            }
        }
    ]
}]
```

*hiveCli -* list of Hive commands or statements to be executed with Hive Cli before the Pig script run. 

#### Command line execution
Pig-test-runner uses Apache Maven to execute test from a command line. (https://maven.apache.org/install.html).

```
mvn -DtestDefs=examples/test-defs.json test
```

#### Running tests from Eclipse
Pig-test-runner can be executed from Eclipse using the m2e Maven plugin. (http://www.eclipse.org/m2e/).
- File -> Import project
- Maven -> Existing Maven Projects
- Browse to the root of the pig-test-runner directory -> Select pom.xml
- From Package Explorer expand pig.testing.runner -> Right click RunnerTest.java to open context menu
- Run As -> Run configurations 
- Create new JUnit Test and specify VM argument to point to the configuration file e.g. VM argument -DtestDefs=examples/test-defs.json 

#### Pig-test-runner arguments
*-DtestDefs -* path to the configuration file

*-DstopAtInit -* load tests from configuration file and stop without executing the tests

*-Dhive.metastore.uris -* specify external Hive metastore
