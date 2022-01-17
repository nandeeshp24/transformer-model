 #/bin/sh

curl -F file=@/data/QA/QA/src/main/resources/QALD/8/qald8_train.json -F host=dbpedia http://nel.cs.upb.de:8080/create-training-data
curl -F file=@/data/QA/QA/src/main/resources/QALD/8/qald8_test.json -F host=dbpedia http://nel.cs.upb.de:8080/create-training-data
curl -F file=@/data/QA/QA/src/main/resources/QALD/9/qald9_train.json -F host=dbpedia http://nel.cs.upb.de:8080/create-training-data
curl -F file=@/data/QA/QA/src/main/resources/QALD/9/qald9_test.json -F host=dbpedia http://nel.cs.upb.de:8080/create-training-data
curl -F file=@/data/QA/QA/src/main/resources/QALD/1/qald1_train.json -F host=dbpedia http://nel.cs.upb.de:8080/create-training-data
curl -F file=@/data/QA/QA/src/main/resources/QALD/2/qald2_train.json -F host=wikidata http://nel.cs.upb.de:8080/create-training-data
curl -F file=@/data/QA/QA/src/main/resources/QALD/2/qald2_test.json -F host=wikidata http://nel.cs.upb.de:8080/create-training-data