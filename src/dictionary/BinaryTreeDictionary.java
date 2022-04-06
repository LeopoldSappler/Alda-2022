// O. Bittel
// 22.02.2017
package dictionary;

import java.util.Iterator;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys, 
 * or by a Comparator provided at set creation time, depending on which constructor is used. 
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 * 
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V>{

    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }
    
    private Node<K, V> root = null;
    private V tempValue;
    private int size = 0;

    @Override
    public V insert(K key, V value) {
        root = insertRecursive(root, key, value);
        root.parent = null;
        size++;
        return tempValue;
    }

    private Node<K, V> insertRecursive(Node<K, V> currentNode, K key, V value) {
        if (currentNode == null) {
            currentNode = new Node<>(key, value);
            tempValue = null;
        } else if (key.compareTo(currentNode.key) < 0) {
            currentNode.left = insertRecursive(currentNode.left, key, value);
            currentNode.left.parent = currentNode;
        } else if (key.compareTo(currentNode.key) > 0) {
            currentNode.right = insertRecursive(currentNode.right, key, value);
            currentNode.right.parent = currentNode;
        } else {
            tempValue = currentNode.value;
            currentNode.value = value;
        }
        return currentNode;
    }

    @Override
    public V search(K key) {
        return searchRecursive(root, key);
    }

    private V searchRecursive(Node<K, V> currentNode, K key) {
        if (currentNode == null)
            return null;
        if (key.compareTo(currentNode.key) < 0) {
            return searchRecursive(currentNode.left, key);
        } else if (key.compareTo(currentNode.key) > 0) {
            return searchRecursive(currentNode.right, key);
        } else {
            return currentNode.value;
        }
    }

    @Override
    public V remove(K key) {
        root = removeRecursive(root, key);
        size--;
        return tempValue;
    }

    private Node<K, V> removeRecursive(Node<K, V> currentNode, K key) {
        // Basisfall: Tree ist leer
        if (currentNode == null)
            tempValue = null;

        // Zur Node navigieren die entfernt werden soll
        else if (key.compareTo(currentNode.key) < 0) {
            currentNode.left = removeRecursive(currentNode.left, key);
        } else if (key.compareTo(currentNode.key) > 0) {
            currentNode.right = removeRecursive(currentNode.right, key);
        }

        // Entfernen der Node, wenn mindestens eins der Children null ist
        else if (currentNode.left == null)
            return currentNode.right;
        else if (currentNode.right == null)
            return currentNode.left;

        // Entfernen der Node, wenn beide Children nicht null sind
        else {
            Node successorParent = currentNode;
            Node successor = minNode(currentNode.right);

        }

        return currentNode;
    }

    private Node<K, V> minNode(Node<K, V> currentNode) {
        if (currentNode.left != null)
            return minNode(currentNode.left);
        return currentNode;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
    // ...

	/**
	 * Pretty prints the tree
	 */
	public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }
}
