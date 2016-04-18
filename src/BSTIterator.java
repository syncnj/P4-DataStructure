///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SetTesterMain.java
// File:             BSTIterator.java
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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The Iterator for Binary Search Tree (BST) that is built using Java's Stack
 * class. This iterator steps through the items BST using an INORDER traversal.
 *
 * @author CS367
 */
public class BSTIterator<K> implements Iterator<K> {

    /** Stack to track where the iterator is in the BST*/
    Stack<BSTNode<K>> stack;

    /**
     * Constructs the iterator so that it is initially at the smallest
     * value in the set. Hint: Go to farthest left node and push each node
     * onto the stack while stepping down the BST to get there.
     *
     * @param n the root node of the BST
     */
    public BSTIterator(BSTNode<K> n){
        stack = new Stack<>();
        BSTNode<K> curr= n;
        while (curr!=null){
            stack.push(curr);
            curr = curr.getLeftChild();
        }
    }

    /**
     * Returns true iff the iterator has more items.
     *
     * @return true iff the iterator has more items
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * Returns the next item in the iteration.
     *
     * @return the next item in the iteration
     * @throws NoSuchElementException if the iterator has no more items
     */
    public K next() {
        if (!this.hasNext()){
            throw new NoSuchElementException("iterator has traversed to its end");
        }
        BSTNode<K> returnNode= stack.pop();
        BSTNode<K> curr = returnNode.getRightChild();
        while (curr!=null){
            stack.push(curr);
            curr = curr.getLeftChild();
        }
        return returnNode.getKey();
    }

    /**
     * Not Supported
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
