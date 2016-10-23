DROP TABLE mydb.word_count;
CREATE EXTERNAL TABLE mydb.word_count (
    word string,
    count int
)
LOCATION '${hiveconf:word_count_path}';
