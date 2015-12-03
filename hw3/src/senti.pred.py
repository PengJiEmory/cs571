import numpy as np
from sklearn.metrics import accuracy_score as ascore
from sklearn.metrics import precision_recall_fscore_support as prf
from sklearn.lda import LDA as lda
from sklearn.linear_model import LogisticRegression as log
from sklearn.neighbors import KNeighborsClassifier as knn

def sentPred(trainfile, testfile, result, report):
    traindata = np.loadtxt(trainfile)
    testdata = np.loadtxt(testfile)

    x_train = traindata[:,1:]
    y_train = traindata[:,0]

    y_pred_stan = traindata[:,-1]
    score_train_stan = ascore(y_train, y_pred_stan)
    rep_train_stan = prf(y_train, y_pred_stan, average=None)

    clf_lda = lda()
    clf_lda.fit(x_train, y_train)
    y_pred_lda = clf_lda.predict(x_train)
    score_train_lda = ascore(y_train, y_pred_lda)
    rep_train_lda = prf(y_train, y_pred_lda, average=None)
    test_pred_lda = clf_lda.predict(testdata)

    clf_log = log()
    clf_log.fit(x_train, y_train)
    y_pred_log = clf_log.predict(x_train)
    score_train_log = ascore(y_train, y_pred_log)
    rep_train_log = prf(y_train, y_pred_log, average=None)
    test_pred_log = clf_log.predict(testdata)

    clf_knn = knn(n_neighbors = 1)
    clf_knn.fit(x_train, y_train)
    y_pred_knn = clf_knn.predict(x_train)
    score_train_knn = ascore(y_train, y_pred_knn)
    rep_train_knn = prf(y_train, y_pred_knn, average=None)
    test_pred_knn = clf_knn.predict(testdata)

    separator = np.array((9,))
    test_pred = np.concatenate((test_pred_lda,separator,test_pred_log,separator,test_pred_knn))
    np.savetxt(result, test_pred, fmt='%i')

    np.savetxt(report, rep_train_stan + rep_train_lda + rep_train_log + rep_train_knn, fmt = '%10.5f')

    f = open(report, 'ab')
    f.write('stan: ' + str(score_train_stan) + '\n')
    f.write('lda: '  + str(score_train_lda)  + '\n')
    f.write('log: '  + str(score_train_log)  + '\n')
    f.write('knn: '  + str(score_train_knn)  + '\n')
    f.close()

sentPred('train.sklearn', 'test.sklearn', 'test.result', 'train.report')
