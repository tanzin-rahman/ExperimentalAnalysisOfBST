package Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class MarkovChainTreeModel {
    private Random randomGenerator = new Random();
    private static double MIN_VALID_VALUE = 0.0000000001;
    private int numberOfElements;
    private int childPerNode;
    private HashMap<String, Double> nodeToNodeProbability;

    public void generateModel(int treeHeight,
                              int childPerNode,
                              double probabilityOfStayingOnSameNode,
                              double probabilityOfGoingToParent,
                              double probabilityOfGoingToChild) {

        this.numberOfElements = (int) Math.pow(childPerNode, treeHeight + 1) - 1;
        this.childPerNode = childPerNode;
        nodeToNodeProbability = new HashMap<>(numberOfElements * 4); // allocate more space to improve performance
        for (int i = 0; i < numberOfElements; i++) {
            //self probability.
            setValue(i, i, probabilityOfStayingOnSameNode);
            int parent = (i - 1) / 2;

            //probability to return to parent node.
            if (isInternalNode(i)) {
                setValue(i, parent, probabilityOfGoingToParent);
            } else if (isLeaf(i)) {
                double probability = probabilityOfGoingToParent + probabilityOfGoingToChild * childPerNode;
                setValue(i, parent, probability);
            }

            //probability to go to a child node.
            for (int j = 1; j <= childPerNode; j++) {
                int child = i * 2 + j;
                if (isRoot(i)) {
                    double probability = probabilityOfGoingToChild + probabilityOfGoingToParent / childPerNode;
                    setValue(i, child, probability);
                } else if (isInternalNode(i)) {
                    setValue(i, child, probabilityOfGoingToChild);
                }
            }
        }
    }

    private boolean isRoot(int index) {
        return index == 0;
    }

    private boolean isLeaf(int index) {
        return index * childPerNode + 1 >= numberOfElements;
    }

    private boolean isInternalNode(int index) {
        return !isRoot(index) && !isLeaf(index);
    }


    public double getValue(int i, int j) {
        String key = i + "," + j;
        return nodeToNodeProbability.getOrDefault(key, 0.0);
    }

    public void setValue(int i, int j, double value) {
        String key = i + "," + j;
        nodeToNodeProbability.put(key, value);
    }

    /*
     * Save model into file for future use.
     */
    public void saveModel(String fileName) {
        BufferedWriter outputFile = null;
        try {
            outputFile = new BufferedWriter(new FileWriter(fileName));
            outputFile.write("" + numberOfElements);
            outputFile.newLine();

            for (int i = 0; i < numberOfElements; i++) {
                for (int j = 0; j < numberOfElements; j++) {
                    double value = getValue(i, j);
                    if (value > MIN_VALID_VALUE) {  // use  mode[i][j] > MIN_VALID_VALUE instead of mode[i][j] == 0 to avoid precision error
                        outputFile.write(i + "\t\t" + j + "\t\t" + getValue(i, j));
                        outputFile.newLine();
                    }
                }
            }
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Load model from file.
     */
    public void loadModel(String fileName) {
        BufferedReader inputFile = null;
        try {
            inputFile = new BufferedReader(new FileReader(fileName));
            numberOfElements = Integer.parseInt(inputFile.readLine());
            nodeToNodeProbability = new HashMap<>(numberOfElements * 4); // allocate more space for performace improvement.
            for (int i = 0; i < numberOfElements; i++) {
                String[] strValues = inputFile.readLine().split(",");
                for (int j = 2; j < strValues.length; j += 2) {
                    int fromIndex = Integer.parseInt(strValues[0]);
                    int toIndex = Integer.parseInt(strValues[0]);
                    double probability = Double.parseDouble(strValues[1]);
                    setValue(fromIndex, toIndex, probability);
                }
            }
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Generate input based on the model.
     */
    public int[] generateSequence(int seqLength) {
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
        //System.out.println("Initial Random Value:" + randomValue);
        double value = getValue(currentMarkovState, currentMarkovState);
        //System.out.println("Value(" + currentMarkovState + "," + currentMarkovState + "): " + value);

        if (randomValue <= value) {
            return currentMarkovState;
        }
        randomValue -= value;
        //System.out.println("Random Value:" + randomValue);

        if (!isRoot(currentMarkovState)) {
            int parent = (currentMarkovState - 1) / 2;
            value = getValue(currentMarkovState, parent);
            //System.out.println("Value(" + currentMarkovState + "," + parent+ "): " + value);
            if (randomValue <= value) {
                return parent;
            }
            randomValue -= value;
            //System.out.println("Random Value:" + randomValue);
        }
        if (!isLeaf(currentMarkovState)) {
            //probability to go to a child node.
            for (int j = 1; j <= childPerNode; j++) {
                int child = currentMarkovState * 2 + j;
                value = getValue(currentMarkovState, child);
                //System.out.println("Value(" + currentMarkovState + "," + child+ "): " + value);
                if (randomValue <= value) {
                    return child;
                }
                randomValue -= value;
                //System.out.println("Random Value:" + randomValue);
            }
        }
        throw new RuntimeException("Unable to find state. Left over value: " + randomValue);
    }
}
