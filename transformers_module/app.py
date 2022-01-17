from flask import Flask, request
from flask_cors import CORS, cross_origin
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

app = Flask(__name__)
cors = CORS(app)
# change the model_name to path to saved model
model_name = "./model/qald1"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSeq2SeqLM.from_pretrained(model_name)


@app.route('/ping', methods=['GET'])
@cross_origin()
def test():
    return "Status:\tOK", 200


@app.route('/extract-tokens', methods=['GET'])
@cross_origin()
def extract_tokens():
    if "question" not in request.args.keys():
        return "Invalid query parameters", 400
    src_text = [request.args.get("question")]
    batch = tokenizer(src_text, truncation=True, padding="longest", return_tensors="pt")
    translated = model.generate(**batch)
    tokens = tokenizer.batch_decode(translated, skip_special_tokens=True)
    return tokens[0], 200
