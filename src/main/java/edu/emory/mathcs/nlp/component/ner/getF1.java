package edu.emory.mathcs.nlp.component.ner;

/**
 * Created by Peng on 10/10/15.
 * getF1 takes two arrays of NERNode and returns the
 * F1-score of the named entities with exact match
 *
 * F1 = 2 * (precision * recall) / (precision + recall)
 * precision = TruePositive / (TruePositive + FalsePositive)
 * recal = TruePositive / (TruePositive + FalseNegative)
 */
public class getF1 {
    public void getF1(NERNode[] gold, NERNode[] sys) {
        int f1score;
        int precision;
        int recall;
        int truePositive = 0;
        int last_match = -1;
        for (int i = 0; gold.length > i; i++) {
            for (int j = last_match + 1; j < sys.length; j++) {
                if (gold[i].isNamedEntityTag(sys[j].getNamedEntityTag())) {
                    truePositive++;
                    last_match = j;
                    break;
                }
            }
        }
        precision = truePositive / sys.length;
        recall = truePositive / gold.length;
        f1score = 2 * precision * recall / (precision + recall);
    }
}
