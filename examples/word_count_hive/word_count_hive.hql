INSERT OVERWRITE DIRECTORY "${hiveconf:output}"
  SELECT word, COUNT(word)
    FROM ${hiveconf:input_table}
GROUP BY word;
