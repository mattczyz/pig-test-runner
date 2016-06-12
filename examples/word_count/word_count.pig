fs -rm -r -f $output

words = LOAD '$input' USING PigStorage() AS (word:chararray);

words_group = GROUP words BY word;

word_count = FOREACH words_group GENERATE group, COUNT(words.word);

STORE word_count INTO '$output' USING PigStorage(',');
