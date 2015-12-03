import numpy as np
import sklearn as sk
import nltk
from nltk.util import ngrams

cls = []
cls += [nltk.data.load("/home/Peng/nltk_data/classifiers/movie_reviews_sklearn.BernoulliNB.pickle")]
cls += [nltk.data.load("/home/Peng/nltk_data/classifiers/movie_reviews_sklearn.MultinomialNB.pickle")]
cls += [nltk.data.load("/home/Peng/nltk_data/classifiers/movie_reviews_sklearn.LogisticRegression.pickle")]
cls += [nltk.data.load("/home/Peng/nltk_data/classifiers/movie_reviews_sklearn.LinearSVC.pickle")]
cls += [nltk.data.load("/home/Peng/nltk_data/classifiers/movie_reviews_sklearn.NuSVC.pickle")]

def sentiAna(inputfile, outputfile, classifiers):
    inp = open(inputfile, 'r')
    out = open(outputfile, 'w')
    line = inp.readline()

    while line != "":
        sentence = line.split()[2:]
        output = ""
        feats = dict([(word, True) for word in sentence + list(ngrams(sentence, 2))])
        for classifier in classifiers:
            label = classifier.classify(feats)
            if label == 'neg':
                output += " 1"
            elif label == 'pos':
                output += " 3"
            else:
                output += " 2"

        out.write(output + "\n")
        line = inp.readline()

    inp.close()
    out.close()

sentiAna('test.data', 'test.sklearn', cls)
