from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer

app = Flask(__name__)
model = SentenceTransformer('all-MiniLM-L6-v2')  # ~384-dim vectors

@app.route("/embed", methods=["POST"])
def embed():
    data = request.get_json()
    texts = data.get("texts", [])
    embeddings = model.encode(texts).tolist()
    return jsonify({"embeddings": embeddings})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5005)
