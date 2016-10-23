INSERT OVERWRITE TABLE ${hiveconf:output_table}
  SELECT word, COUNT(word)
    FROM ${hiveconf:input_table}
GROUP BY word;
