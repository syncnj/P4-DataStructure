//////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SetTesterMain.java
// File:             BSTreeSetTester.java
// Semester:         CS367 Spring 2016
//
// Author:           Yi Shen yshen59@wisc.edu
// CS Login:         sheny
// Lecturer's Name:  Jim Skrentny
// Lab Section:      N/A
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Yuchen Gu
// Email:            ygu48@wisc.edu
// CS Login:         yuchen
// Lecturer's Name:  Jim Skrentny
// Lab Section:      N/A
//
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SetTesterADT implementation using a Binary Search Tree (BST) as the data
 * structure.
 *
 * <p>The BST rebalances if a specified threshold is exceeded (explained below).
 * If rebalanceThreshold is non-positive (&lt;=0) then the BST DOES NOT
 * rebalance. It is a basic BStree. If the rebalanceThreshold is positive
 * then the BST does rebalance. It is a BSTreeB where the last B means the
 * tree is balanced.</p>
 *
 * <p>Rebalancing is triggered if the absolute value of the balancedFfactor in
 * any BSTNode is &gt;= to the rebalanceThreshold in its BSTreeSetTester.
 * Rebalancing requires the BST to be completely rebuilt.</p>
 *
 * @author CS367
 */
public class BSTreeSetTester <K extends Comparable<K>> implements SetTesterADT<K>{

    /** Root of this tree */
    BSTNode<K> root;

    /** Number of items in this tree*/
    int numKeys;

    /**
     * rebalanceThreshold &lt;= 0 DOES NOT REBALANCE (BSTree).<br>
     * rebalanceThreshold  &gt;0 rebalances the tree (BSTreeB).
      */
    int rebalanceThreshold;

    /**
     * True iff tree is balanced, i.e., if rebalanceThreshold is NOT exceeded
     * by absolute value of balanceFactor in any of the tree's BSTnodes.Note if
     * rebalanceThreshold is non-positive, isBalanced must be true.
     */
    boolean isBalanced;


    /**
     * Constructs an empty BSTreeSetTester with a given rebalanceThreshold.
     *
     * @param rbt the rebalance threshold
     */
    public BSTreeSetTester(int rbt) {
        rebalanceThreshold = rbt;
        numKeys=0;
        isBalanced=true;

    }

    /**
     * Add node to binary search tree. Remember to update the height and
     * balancedFfactor of each node. Also rebalance as needed based on
     * rebalanceThreshold.
     *
     * @param key the key to add into the BST
     * @throws IllegalArgumentException if the key is null
     * @throws DuplicateKeyException if the key is a duplicate
     */
    public void add(K key) {
        if (key==null) throw new IllegalArgumentException("cannot add null key");
        if (root ==null){
            root = new BSTNode<>(key);
        }
        else {
            addHelper(root,key);
        }

        numKeys++;  //update the total count

        if (this.rebalanceThreshold<=0){
            this.isBalanced=true;
        }
        else{
            this.isBalanced=true;
            //set it to balanced before checking
            checkBalance(root);
            if(!isBalanced){
                rebalance();
                //rebalance if needed
            }
        }


    }
    /**
     * helper method for add, used recursively to traverse to the node and add it
     *
     * @param (node) (the root node of the tree to add the key to)
     * @param (key) (the key to add to the tree)
     *
     */
    private void  addHelper (BSTNode<K> node, K key){
        //child height is 0 if the child node is null
        int leftHeight =0;
        int rightHeight =0;

        if (key.compareTo(node.getKey())>0){
            if (node.getRightChild()==null){
                //Base case: it's already the end
                node.setRightChild(new BSTNode<K>(key));
            }
            else {
                //Keep traverse to the child node
                addHelper(node.getRightChild(),key);
            }

            //update height & balanced factor of the new node
            if (node.getLeftChild()!=null){
                leftHeight = node.getLeftChild().getHeight();
            }
            if (node.getRightChild()!=null){
                rightHeight = node.getRightChild().getHeight();
            }
            node.setHeight(1+Math.max(leftHeight,rightHeight));
            node.setBalanceFactor(leftHeight-rightHeight);


        }
        else if (key.compareTo(node.getKey())<0){
            if (node.getLeftChild()==null){
                node.setLeftChild(new BSTNode<K>(key));
            }
            else {
                addHelper(node.getLeftChild(),key);
            }

            //update height & balanceFactor
            if (node.getLeftChild()!=null){
                leftHeight = node.getLeftChild().getHeight();
            }
            if (node.getRightChild()!=null){
                rightHeight = node.getRightChild().getHeight();
            }
            node.setHeight(1+Math.max(leftHeight,rightHeight));
            node.setBalanceFactor(leftHeight-rightHeight);

        }
        else {
            //Same node exist
            throw new DuplicateKeyException();
        }
    }

    /**
     * Recursive method to check if each node is balanced
     *
     * @param (n) (The root node of each recursive call to check if balanced)
     */
    private void checkBalance(BSTNode<K> n){
        //traversed to the end of the tree
        if (n==null) return;

        if (Math.abs(n.getBalanceFactor())>rebalanceThreshold)
        {
            this.isBalanced=false;
        }
        //Keep checking only if it's still balanced
        if (this.isBalanced) {
            checkBalance(n.getLeftChild());
            checkBalance(n.getRightChild());
        }
    }




    /**
     * Rebalances the tree by:
     * 1. Copying all keys in the BST in sorted order into an array.
     *    Hint: Use your BSTIterator.
     * 2. Rebuilding the tree from the sorted array of keys.
     */
    public void rebalance() {
        K[] keys = (K[]) new Comparable[numKeys];
        //Put everything in the array
        Iterator<K> itr = this.iterator();
        int i=0;
        while (itr.hasNext()){
            keys[i]= itr.next();
            i++;
        }

        root = sortedArrayToBST(keys, 0, numKeys-1);

    }

    /**
     * Recursively rebuilds a binary search tree from a sorted array.
     * Reconstruct the tree in a way similar to how binary search would explore
     * elements in the sorted array. The middle value in the sorted array will
     * become the root, the middles of the two remaining halves become the left
     * and right children of the root, and so on. This can be done recursively.
     * Remember to update the height and balanceFactor of each node.
     *
     * @param keys the sorted array of keys
     * @param start the first index of the part of the array used
     * @param stop the last index of the part of the array used
     * @return root of the new balanced binary search tree
     */
    private BSTNode<K> sortedArrayToBST(K[] keys, int start, int stop) {
        if (start == stop){
            return new BSTNode<>(keys[start]);
        }
        if (start>stop) return null;

        int leftHeight =0;
        int rightHeight =0;

        //create new node
        int mid = (start+stop)/2;
        BSTNode<K> newNode = new BSTNode<K>(keys[mid]);

        newNode.setLeftChild(sortedArrayToBST(keys,start,mid-1));
        newNode.setRightChild(sortedArrayToBST(keys,mid+1,stop));

        //update the child height
        if (newNode.getLeftChild()!=null){
            leftHeight = newNode.getLeftChild().getHeight();
        }
        if (newNode.getRightChild()!=null){
            rightHeight = newNode.getRightChild().getHeight();
        }

        //set the height & BalanceFactor
        newNode.setHeight(1+Math.max(leftHeight,rightHeight));
        newNode.setBalanceFactor(leftHeight-rightHeight);

        return newNode;
    }

    /**
     * Returns true iff the key is in the binary search tree.
     *
     * @param key the key to search
     * @return true if the key is in the binary search tree
     * @throws IllegalArgumentException if key is null
     */
    public boolean contains(K key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        return containsHelper(key, root);

    }



    /**
     * Helper method for contains, call itself recursively
     *
     * @param key the key to search.
     * @param node the current node in the recursive call
     * @return true if the the current key is in the tree
     */
    private boolean containsHelper(K key, BSTNode<K> node){
        if (node == null) return false;
        if (key.compareTo(node.getKey())>0){
            return containsHelper(key, node.getRightChild());
        }
        else if (key.compareTo(node.getKey())<0){
            return containsHelper(key,node.getLeftChild());
        }
        else {
            // Same node exists
            return true;
        }
    }
    /**
     * Returns the sorted list of keys in the tree that are in the specified
     * range (inclusive of minValue, exclusive of maxValue). This can be done
     * recursively.
     *
     * @param minValue the minimum value of the desired range (inclusive)
     * @param maxValue the maximum value of the desired range (exclusive)
     * @return the sorted list of keys in the specified range
     * @throws IllegalArgumentException if either minValue or maxValue is
     * null, or minValue is larger than maxValue
     */
    public List<K> subSet(K minValue, K maxValue) {
        if (minValue == null || maxValue == null || minValue.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException("null input");
        }
        List<K> returnList = new ArrayList<>();
        subSetHelper(minValue,maxValue,returnList,root);
        return returnList;

    }

    /**
     * Returns the sorted list of keys in the tree that are in the specified
     * range (inclusive of minValue, exclusive of maxValue).
     *
     * @param minValue the minimum value of the desired range (inclusive)
     * @param maxValue the maximum value of the desired range (exclusive)
     * @param returnList the list to be created and returened
     * @param node the current node to be traversed
     */
    private void subSetHelper (K minValue, K maxValue, List<K> returnList, BSTNode<K> node){
            //Use in order traversal
            if (node == null) return;

            //check the node on the left child
            if (node.getKey().compareTo(minValue)>=0){
                subSetHelper(minValue, maxValue,returnList,node.getLeftChild());
            }
            // visit  the node and check the node on the right side
            if (node.getKey().compareTo(maxValue)<0) {
                returnList.add(node.getKey());
                subSetHelper(minValue, maxValue,returnList,node.getRightChild());
            }


    }




    /**
     * Return an iterator for the binary search tree.
     * @return the iterator
     */
    public Iterator<K> iterator() {
        return new BSTIterator<>(root);
    }

    /**
     * Clears the tree, i.e., removes all the keys in the tree.
     */
    public void clear() {
        root =null;
        numKeys=0;
        isBalanced=true;
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return the number of keys
     */
    public int size() {
        return numKeys;
    }

    /**
     * Displays the top maxNumLevels of the tree. DO NOT CHANGE IT!
     *
     * @param maxDisplayLevels from the top of the BST that will be displayed
     */
    public void displayTree(int maxDisplayLevels) {
        if (rebalanceThreshold > 0) {
            System.out.println("---------------------------" +
                    "BSTreeBSet Display-------------------------------");
        } else {
            System.out.println("---------------------------" +
                    "BSTreeSet Display--------------------------------");   
        }
        displayTreeHelper(root, 0, maxDisplayLevels);
    }

    private void displayTreeHelper(BSTNode<K> n, int curDepth,
                                   int maxDisplayLevels) {
        if(maxDisplayLevels <= curDepth) return;
        if (n == null)
            return;
        for (int i = 0; i < curDepth; i++) {
            System.out.print("|--");
        }
        System.out.println(n.getKey() + "[" + n.getHeight() + "]{" +
                n.getBalanceFactor() + "}");
        displayTreeHelper(n.getLeftChild(), curDepth + 1, maxDisplayLevels);
        displayTreeHelper(n.getRightChild(), curDepth + 1, maxDisplayLevels);
    }
}
