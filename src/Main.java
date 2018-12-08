import DataStructure.AVLTreeST;
import DataStructure.EveryOtherAccessMoveToFrontBST;
import DataStructure.RedBlackBST;
import DataStructure.SplayBST;
import Utils.*;

public class Main {
    static int treeHeight = 12;
    static int childPerNode = 2;
    static int totalElement = 8191; // 2^13-1
    static int seqLength = 10000;

    public static void main(String[] args) {
        int[] seq1 = generateDataSet1();
        int[] seq2 = generateDataSet2();
        int[] seq3 = generateDataSet3();
        int[] seq4 = generateDataSet4();

        Utils.analizeSeq("DataSet1", seq1, true);
        Utils.analizeSeq("DataSet2", seq2, false);
        Utils.analizeSeq("DataSet3", seq3, false);
        Utils.analizeSeq("DataSet4", seq4, false);

        calculateSearchCost("DataSet1", seq1, true);
        calculateSearchCost("DataSet2", seq2, false);
        calculateSearchCost("DataSet3", seq3, false);
        calculateSearchCost("DataSet4", seq4, false);
    }

    private static void calculateSearchCost(String dataSetName, int[] seq, boolean printHeader) {
        Cost costForAVLTree = searchInAVLTree(dataSetName, totalElement, seq);
        Cost costForRedBlackTree = searchInRedBlackTree(dataSetName, totalElement, seq);
        Cost costForSplayTree = SearchInSplayTree(dataSetName, totalElement, seq);
        Cost costForEveryOtherAccess = searchInEveryOtherAccess(dataSetName, totalElement, seq);
        if (printHeader) {
            System.out.println("DataSet Name, Cost Type, Avl Tree, RedBlack Tree, Splay Tree, EOAMTF");
        }
        System.out.println(dataSetName
                + ", Access Cost,"
                + costForAVLTree.accessCost + ","
                + costForRedBlackTree.accessCost + ","
                + costForSplayTree.accessCost + ","
                + costForEveryOtherAccess.accessCost);
        System.out.println(dataSetName
                + ", Update Cost,"
                + "0,"
                + "0,"
                + costForSplayTree.updateCost + ","
                + costForEveryOtherAccess.updateCost);
    }

    private static int[] generateDataSet1() {
        double bias = 0;
        MarkovChainAllConnectedModel model = new MarkovChainAllConnectedModel();
        int seq[] = model.generateSequence(bias, totalElement, seqLength);
        Utils.saveSeq("seq-1", seq);
        return seq;
    }


    private static int[] generateDataSet2() {
        double bias = 0.5;
        MarkovChainAllConnectedModel model = new MarkovChainAllConnectedModel();
        int seq[] = model.generateSequence(bias, totalElement, seqLength);
        Utils.saveSeq("seq-2", seq);
        return seq;
    }


    private static int[] generateDataSet3() {
        double bias = 0.75;
        MarkovChainAllConnectedModel model = new MarkovChainAllConnectedModel();
        int seq[] = model.generateSequence(bias, totalElement, seqLength);
        Utils.saveSeq("seq-3", seq);
        return seq;
    }

    private static int[] generateDataSet4() {
        MarkovChainTreeModel model = new MarkovChainTreeModel();
        double probabilityOfStayingOnSameNode = .5;
        double probabilityOfGoingToParent = .25;
        double probabilityOfGoingToChild = .25 / childPerNode;
        model.generateModel(treeHeight,
                childPerNode,
                probabilityOfStayingOnSameNode,
                probabilityOfGoingToParent,
                probabilityOfGoingToChild);
        model.saveModel("model-4.txt");
        int seq[] = model.generateSequence(seqLength);
        Utils.saveSeq("seq-4.txt", seq);
        return seq;
    }


    private static Cost searchInAVLTree(String dataSetName, int totalElement, int[] seq) {
        AVLTreeST<Integer, Integer> searchTree = new AVLTreeST<>();
        for (int i = 0; i < totalElement; i++) {
            searchTree.put(i, i);
        }
        searchTree.accessCost = 0;
        for (int i = 0; i < seq.length; i++) {
            searchTree.get(seq[i]);
        }
        return new Cost(searchTree.accessCost, 0);
    }

    private static Cost searchInRedBlackTree(String dataSetName, int totalElement, int[] seq) {
        RedBlackBST<Integer, Integer> searchTree = new RedBlackBST<>();
        for (int i = 0; i < totalElement; i++) {
            searchTree.put(i, i);
        }
        searchTree.accessCost = 0;
        for (int i = 0; i < seq.length; i++) {
            searchTree.get(seq[i]);
        }
        return new Cost(searchTree.accessCost, 0);
    }

    private static Cost SearchInSplayTree(String dataSetName, int totalElement, int[] seq) {
        SplayBST<Integer, Integer> searchTree = new SplayBST<>();
        for (int i = 0; i < totalElement; i++) {
            searchTree.put(i, i);
        }
        searchTree.accessCost = 0;
        searchTree.updateCost = 0;
        for (int i = 0; i < seq.length; i++) {
            searchTree.get(seq[i]);
        }
        return new Cost(searchTree.accessCost, searchTree.updateCost);
    }

    private static Cost searchInEveryOtherAccess(String dataSetName, int totalElement, int[] seq) {
        EveryOtherAccessMoveToFrontBST<Integer, Integer> searchTree = new EveryOtherAccessMoveToFrontBST<>();
        for (int i = 0; i < totalElement; i++) {
            searchTree.put(i, i);
        }
        searchTree.accessCost = 0;
        searchTree.updateCost = 0;
        for (int i = 0; i < seq.length; i++) {
            searchTree.get(seq[i]);
        }
        return new Cost(searchTree.accessCost, searchTree.updateCost);
    }
}
