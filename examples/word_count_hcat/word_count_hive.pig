words = LOAD '$input_table' USING org.apache.hive.hcatalog.pig.HCatLoader();

words_group = GROUP words BY word;

word_count = FOREACH words_group GENERATE group, COUNT(words.word);

STORE word_count INTO '$output' USING PigStorage(',');
