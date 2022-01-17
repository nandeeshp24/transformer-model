from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
import jsonlines


def prepare_data(path):
    with jsonlines.open(path) as reader:
        data = []
        for obj in reader:
            data.append(obj)
    return data


def predict(model, tokenizer, src_text):
    batch = tokenizer(src_text, truncation=True, padding="longest", return_tensors="pt")
    translated = model.generate(**batch)
    tgt_text = tokenizer.batch_decode(translated, skip_special_tokens=True)
    return tgt_text


def prepare_summary(model_name, eval_data, summary_file_path, header_flag):
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    model = AutoModelForSeq2SeqLM.from_pretrained(model_name)
    predictions = predict(model, tokenizer, list(map(lambda x: x.get('question'), eval_data)))
    with open(summary_file_path, "a") as fl:
        if header_flag:
            header = "Question\tActual Tokens\tPredicted Tokens\n"
            fl.write(header)
        for i in range(len(eval_data)):
            question = eval_data[i].get('question')
            tokens = eval_data[i].get('tokens')
            pred_1 = predictions[i]
            fl.write(question + "\t" + tokens + "\t" + pred_1 + "\n")


def eval_model(test_file_path, model_path, eval_file_path):
    batch_size = 50
    data = prepare_data(test_file_path)
    prepare_summary(model_path, data[:batch_size], eval_file_path, True)
    print("1")
    i = batch_size
    count = 2
    while i < min(100, len(data)):
        prepare_summary(model_path, data[i:(i + batch_size)], eval_file_path, False)
        print(count)
        count += 1
        if (i + batch_size) <= len(data):
            i += batch_size
        else:
            i = len(data)
    print("Done")


# eval_model("./training_data/qald2_test.json", "./model/qald2", "./qald2_eval.tsv")
# eval_model("./training_data/qald8_test.json", "./model/qald1_v2", "./qald1_eval_v2.tsv")
eval_model("./../QA/qald8_test.json", "./model/qald1", "./qald1_eval.tsv")
eval_model("./../QA/qald2_test.json", "./model/qald2", "./qald2_eval.tsv")
