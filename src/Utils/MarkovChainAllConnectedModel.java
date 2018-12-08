package Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class MarkovChainAllConnectedModel {
    protected Random randomGenerator = new Random();

    private double bias;
    private double otherStateProbability;
    private int numberOfElements;

    /*
     * Generate input based on the model.
     */
    public int[] generateSequence(double bias, int numberOfElement, int seqLength) {
        this.numberOfElements = numberOfElement;
        if (bias > 0) {
            this.bias = bias;
            otherStateProbability = (1.0 - bias) / (numberOfElement - 1);
        } else {
            this.bias = 1.0 / numberOfElement;
            otherStateProbability = 1.0 / numberOfElement;
        }

        int[] seq = new int[seqLength];
        int currentMarkovState = 0;
        for (int i = 0; i < seqLength; i++) {
            currentMarkovState = getNextMarkovState(currentMarkovState);
            seq[i] = currentMarkovState;
        }
        return seq;
    }


    /*
     * Returns the next markov state based on the probability.
     */
    private int getNextMarkovState(int currentMarkovState) {
        double randomValue = randomGenerator.nextDouble();
        if (randomValue <= bias) {
            return currentMarkovState;
        } else {
            randomValue -= bias;
            int index = (int) Math.floor(randomValue / otherStateProbability);
            if (index >= currentMarkovState) {
                index++;
            }
            return Math.min(index, numberOfElements - 1);
        }
    }
}
