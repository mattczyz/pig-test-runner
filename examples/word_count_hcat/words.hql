DROP TABLE mydb.words;
CREATE EXTERNAL TABLE mydb.words (
    word string
)
LOCATION '${hiveconf:words_path}';
