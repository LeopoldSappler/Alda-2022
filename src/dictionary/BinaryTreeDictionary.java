package dictionary;

import java.util.ArrayList;
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

    private static class Node<K, V> {
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
        if (tempValue == null)
            size++;
        return tempValue;
    }

    private Node<K, V> insertRecursive(Node<K, V> currentNode, K key, V value) {
        // Basisfall: Tree ist leer oder am Ende angekommen
        if (currentNode == null) {
            currentNode = new Node<>(key, value);
            tempValue = null;

            // Einzusetzende Node kleiner als currentNode
        } else if (key.compareTo(currentNode.key) < 0) {
            currentNode.left = insertRecursive(currentNode.left, key, value);
            currentNode.left.parent = currentNode;

            // Einzusetzende Node groesser als currentNode
        } else if (key.compareTo(currentNode.key) > 0) {
            currentNode.right = insertRecursive(currentNode.right, key, value);
            currentNode.right.parent = currentNode;

            // Value existiert schon im Tree
        } else {
            tempValue = currentNode.value;
            currentNode.value = value;
        }
        currentNode = balance(currentNode);
        return currentNode;
    }

    private Node<K,V> balance(Node<K,V> currentNode) {
        if (currentNode == null)
            return null;
        currentNode.height = Math.max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
        if (getBalance(currentNode) == -2) {
            if (getBalance(currentNode.left) <= 0)
                currentNode = rotateRight(currentNode);
            else
                currentNode = rotateLeftRight(currentNode);
        }
        else if (getBalance(currentNode) == 2) {
            if (getBalance(currentNode.right) >= 0)
                currentNode = rotateLeft(currentNode);
            else
                currentNode = rotateRightLeft(currentNode);
        }
        return currentNode;
    }

    private Node<K,V> rotateRight(Node<K,V> currentNode) {
        assert currentNode.left != null;
        Node<K, V> q = currentNode.left;
        currentNode.left = q.right;
        if(currentNode.left != null){
            currentNode.left.parent = currentNode;
        }
        q.right = currentNode;
        q.right.parent = q;
        //q.parent = null;
        currentNode.height = Math.max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K,V> rotateLeft(Node<K,V> currentNode) {
        assert currentNode.right != null;
        Node<K, V> q = currentNode.right;
        currentNode.right = q.left;
        if(currentNode.right != null) {
            currentNode.right.parent = currentNode;
        }
        q.left = currentNode;
        q.left.parent = q;
        //q.parent = null;
        currentNode.height = Math.max(getHeight(currentNode.right), getHeight(currentNode.left)) + 1;
        q.height = Math.max(getHeight(q.right), getHeight(q.left)) + 1;
        return q;
    }

    private Node<K,V> rotateLeftRight(Node<K,V> currentNode) {
        assert currentNode.left != null;
        currentNode.left = rotateLeft(currentNode.left);
        currentNode.left.parent = currentNode;
        return rotateRight(currentNode);
    }

    private Node<K,V> rotateRightLeft(Node<K,V> currentNode) {
        assert currentNode.right != null;
        currentNode.right = rotateRight(currentNode.right);
        currentNode.right.parent = currentNode;
        return rotateLeft(currentNode);
    }

    @Override
    public V search(K key) {
        return searchRecursive(root, key);
    }

    private V searchRecursive(Node<K, V> currentNode, K key) {
        // Basisfall: Root Node ist leer
        // oder am Ende des Trees angekommen => Nicht im Tree vorhanden
        if (currentNode == null)
            return null;

        // Der gesuchte Key ist kleiner als currentNode.key
        if (key.compareTo(currentNode.key) < 0) {
            return searchRecursive(currentNode.left, key);

            // Der gesuchte Key ist groesser als currentNode.key
        } else if (key.compareTo(currentNode.key) > 0) {
            return searchRecursive(currentNode.right, key);

            // Gesuchter Key == currentNode.key, gebe Value zurueck
        } else {
            return currentNode.value;
        }
    }

    @Override
    public V remove(K key) {
        root = removeRecursive(root, key);
        if (root != null)
            root.parent = null;
        if (tempValue != null)
            size--;
        return tempValue;
    }

    private Node<K, V> removeRecursive(Node<K, V> currentNode, K key) {
        // Basisfall: Tree ist leer
        // oder am Ende des Trees angekommen => nicht im Tree vorhanden
        if (currentNode == null)
            tempValue = null;

            // Zur Node navigieren die entfernt werden soll
        else if (key.compareTo(currentNode.key) < 0) {
            currentNode.left = removeRecursive(currentNode.left, key);
            if (currentNode.left != null)
                currentNode.left.parent = currentNode;
        } else if (key.compareTo(currentNode.key) > 0) {
            currentNode.right = removeRecursive(currentNode.right, key);
            if (currentNode.right != null)
                currentNode.right.parent = currentNode;
        }

        // Entfernen der Node, wenn mindestens eins der Children null ist
        else if (currentNode.left == null) {
            tempValue = currentNode.value;
            return currentNode.right;
        }
        else if (currentNode.right == null) {
            tempValue = currentNode.value;
            return currentNode.left;
        }

        // Entfernen der Node, wenn beide Children existieren
        else {
            //Zurueckgeben des alten Werts
            tempValue = currentNode.value;

            // aktuelle Node wird durch kleinsten
            // Key im rechten Subtree von currentNode ersetzt
            Node<K, V> minNode = minNode(currentNode.right);

            currentNode.key = minNode.key;
            currentNode.value = minNode.value;

            // Entfernen von der successorNode
            currentNode.right = removeRecursive(currentNode.right, currentNode.key);
            if (currentNode.right != null)
                currentNode.right.parent = currentNode;
        }

        currentNode = balance(currentNode);
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
        return new Iterator<>() {
            private boolean init = true;
            ArrayList<Entry<K, V>> inOrderArray = new ArrayList<>();
            int counter = 0;

            @Override
            public boolean hasNext() {
                if(init){
                    createList(root);
                    init = false;
                }

                return counter < inOrderArray.size();
            }

            @Override
            public Entry<K, V> next() {
                if(init){
                    createList(root);
                    init = false;
                }

                return inOrderArray.get(counter++);
            }

            private void createList(Node<K, V> node) {
                if (node != null) {
                    inOrderArray.add(new Entry<>(node.key, node.value));

                    createList(node.left);

                    createList(node.right);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private int getHeight(Node<K,V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }

    private int getBalance(Node<K,V> p) {
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }

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
