import java.util.Scanner;
import java.util.ArrayList;


// Implement Red-black binary search tree
public class RedBlackBST {
	// Tree's root node
	private Node root;
	// Template null for further nodes
	private Node TNULL;

	// Node class definition
	private class Node {
		int data; // data inside of the node
		Node parent; // parent node
		Node left, right; // left and right children
		int color; // 1 = Red and 0 = Black
	}
	
	// Initialize empty tree and define TNULL
	public RedBlackBST() {
		TNULL = new Node();
		TNULL.color = 0;
		TNULL.left = null;
		TNULL.right = null;
		root = TNULL;
	}

    /*
    * BST search methods
    */
	// Search the tree for the node with specified key 
	public Node searchTree(int key) {
		return searchTreeFrom(this.root, key);
	}
	// Search for the specified key in the subtree of the given node
	private Node searchTreeFrom(Node node, int key) {
		// If given node is null or key is equal to node's data . . .
		if (node == TNULL || key == node.data) {
			// Specified node was found, return it
			return node;
		}
		// If key is less than node's data, call from node's left child
		if (key < node.data) {
			return searchTreeFrom(node.left, key);
		} else {
			// If key is greater than node's data, call from node's right child
			return searchTreeFrom(node.right, key);
		}
	}

	/*
	* Min and Max methods
	*/
	// Find the node with the minimum key
	public Node minimum(Node node) {
		while (node.left != TNULL) {
			node = node.left;
		}
		return node;
	}
	// Find the node with the maximum key
	public Node maximum(Node node) {
		while (node.right != TNULL) {
			node = node.right;
		}
		return node;
	}

    /*
    * Red-black BST insertion methods
    */
	// Insert new node with data as specified key into tree
	public void insert(int key) {
		// Create new node
		Node node = new Node();
		node.parent = null;
		node.data = key;
		node.left = TNULL;
		node.right = TNULL;
		node.color = 1; // New nodes must be red
		// Node variables for comparisons
		Node temp = null;
		Node currentNode = this.root;
		while (currentNode != TNULL) {
			temp = currentNode;
			// If node's data is less tham currentNode's data, move to left child
			if (node.data < currentNode.data) {
				currentNode = currentNode.left;
			} else {
				// If node's data is greater tham currentNode's data, move to right child
				currentNode = currentNode.right;
			}
		}
		// temp is parent of currentNode
		node.parent = temp;
		// If temp is null then the root must be node
		if (temp == null) {
			root = node;
		// If node's data is less than temp's data, temp's left child becomes node
		} else if (node.data < temp.data) {
			temp.left = node;
		// If node's data is greater than temp's data, temp's right child becomes node
		} else {
			temp.right = node;
		}
		// If new node is a root node, return
		if (node.parent == null){
			// Root must be black
			node.color = 0;
			return;
		}
		// If the grandparent is null, return
		if (node.parent.parent == null) {
			return;
		}
		// Balance tree appropriately
		fixInsert(node);
	}
	// Fix tree according to red-black rules
	private void fixInsert(Node key){
		Node uncle;
        // When key's parent is red . . .
		while (key.parent.color == 1) {
            // If parent is grandparent's right child
			if (key.parent == key.parent.parent.right) {
                // Logic for uncle node being grandparent's left child
				uncle = key.parent.parent.left;
                // Case in which both parent and uncle are red
				if (uncle.color == 1) {
                    // Set parent and uncle to black
					uncle.color = 0;
					key.parent.color = 0;
                    // Set grandparent to red
					key.parent.parent.color = 1;
					key = key.parent.parent;
				} else {
                    // Case in which key is parent's left child and parent is right child of grandparent
					if (key == key.parent.left) {
						key = key.parent;
						rightRotate(key);
					}
                    // Case in which key is parent's right child and parent is right child of grandparent
					key.parent.color = 0;
					key.parent.parent.color = 1;
					leftRotate(key.parent.parent);
				}
            // Mirrored cases below  v
			} else {
                // Logic for uncle node being grandparent's right child
				uncle = key.parent.parent.right;
                // Case in which both parent and uncle are red
				if (uncle.color == 1) {
					uncle.color = 0;
					key.parent.color = 0;
					key.parent.parent.color = 1;
					key = key.parent.parent;	
				} else {
                    // Case in which key is parent's right child and parent is left child of grandparent
					if (key == key.parent.right) {
						key = key.parent;
						leftRotate(key);
					}
					// Case in which key is parent's left child and parent is left child of grandparent
					key.parent.color = 0;
					key.parent.parent.color = 1;
					rightRotate(key.parent.parent);
				}
			}
			if (key == root) {
				break;
			}
		}
		root.color = 0;
	}

	/*
    * Red-black BST deletion methods
    */
	// delete the node from the tree
	public void deleteNode(int data) {
		deleteNodeHelper(this.root, data);
	}
	// Deletion helper
	private void deleteNodeHelper(Node node, int key) {
		// Find the node containing the specified key
        // Node to be deleted
		Node deleteMe = TNULL;
        // Temp nodes
		Node x, y;
		while (node != TNULL){
            // Found node
			if (node.data == key) {
				deleteMe = node;
			}
            // Else move right or left accordingly
			if (node.data <= key) {
				node = node.right;
			} else {
				node = node.left;
			}
		}
		if (deleteMe == TNULL) {
			System.out.println("Could not find this node within the tree.");
			return;
		} 
		y = deleteMe;
		int yOriginalColor = y.color;
        // If the node does not have left child
		if (deleteMe.left == TNULL) {
			x = deleteMe.right;
			transplant(deleteMe, deleteMe.right);
        // If the node does not have right child
		} else if (deleteMe.right == TNULL) {
			x = deleteMe.left;
			transplant(deleteMe, deleteMe.left);
		} else {
			y = minimum(deleteMe.right);
			yOriginalColor = y.color;
			x = y.right;
			if (y.parent == deleteMe) {
				x.parent = y;
			} else {
				transplant(y, y.right);
				y.right = deleteMe.right;
				y.right.parent = y;
			}
			transplant(deleteMe, y);
			y.left = deleteMe.left;
			y.left.parent = y;
			y.color = deleteMe.color;
		}
		if (yOriginalColor == 0){
			fixDelete(x);
		}
	}
	// Fix the tree after the delete operation
	private void fixDelete(Node node) {
		Node sibling;
        // When node is not the root and is black . . .
		while (node != root && node.color == 0) {
			if (node == node.parent.left) {
                // Logic for sibling on the right
				sibling = node.parent.right;
                // Case in which sibling is red
				if (sibling.color == 1) {
                    // Make sibling black and parent red
					sibling.color = 0;
					node.parent.color = 1;
					leftRotate(node.parent);
					sibling = node.parent.right;
				}
                // Case in which both of sibling's children are black
				if (sibling.left.color == 0 && sibling.right.color == 0) {
					sibling.color = 1;
					node = node.parent;
				} else {
                    // Case in which sibling is black with a red left and black right
					if (sibling.right.color == 0) {
						sibling.left.color = 0;
						sibling.color = 1;
						rightRotate(sibling);
						sibling = node.parent.right;
					} 
					// Case in which sibling is black with a red right child
					sibling.color = node.parent.color;
					node.parent.color = 0;
					sibling.right.color = 0;
					leftRotate(node.parent);
					node = root;
				}
            // Mirrored cases below  V
			} else {
				sibling = node.parent.left;
				if (sibling.color == 1) {
					sibling.color = 0;
					node.parent.color = 1;
					rightRotate(node.parent);
					sibling = node.parent.left;
				}
                // Case in which both of sibling's children are black
				if (sibling.right.color == 0 && sibling.right.color == 0) {
					sibling.color = 1;
					node = node.parent;
				} else {
                    // Case in which sibling is black with a red right and black left
					if (sibling.left.color == 0) {
						sibling.right.color = 0;
						sibling.color = 1;
						leftRotate(sibling);
						sibling = node.parent.left;
					} 
					// Case in which sibling is black with a red left child
					sibling.color = node.parent.color;
					node.parent.color = 0;
					sibling.left.color = 0;
					rightRotate(node.parent);
					node = root;
				}
			} 
		}
		node.color = 0;
	}

	/*
	* Red-black BST balancing methods
	*/
	// Rotate left at specified node
	public void leftRotate(Node node) {
		Node temp = node.right;
		node.right = temp.left;
		if (temp.left != TNULL) {
			temp.left.parent = node;
		}
		temp.parent = node.parent;
		if (node.parent == null) {
			this.root = temp;
		} else if (node == node.parent.left) {
			node.parent.left = temp;
		} else {
			node.parent.right = temp;
		}
		temp.left = node;
		node.parent = temp;
	}
	// Rotate right at specified node
	public void rightRotate(Node node) {
		Node temp = node.left;
		node.left = temp.right;
		if (temp.right != TNULL) {
			temp.right.parent = node;
		}
		temp.parent = node.parent;
		if (node.parent == null) {
			this.root = temp;
		} else if (node == node.parent.right) {
			node.parent.right = temp;
		} else {
			node.parent.left = temp;
		}
		temp.right = node;
		node.parent = temp;
	}
    // Place a specified node at another specified node's position
	private void transplant(Node positionNode, Node node){
		if (positionNode.parent == null) {
			root = node;
		} else if (positionNode == positionNode.parent.left){
			positionNode.parent.left = node;
		} else {
			positionNode.parent.right = node;
		}
		node.parent = positionNode.parent;
	}

	/*
	* Printing methods
	*/
	// Print the tree to the terminal
	public void printTree() {
        printTreeHelper(this.root, "", true);
	}
	private void printTreeHelper(Node root, String indent, boolean last) {
	   	if (root != TNULL) {
		   System.out.print(indent);
           // last is true in the case of right nodes
		   if (last) {
		      System.out.print("R----");
              // Increase indentation for representation of levels
		      indent += "     ";
		   } else {
		      System.out.print("L----");
		      indent += "|    ";
		   }
           // Represent color as a string
           String stringColor = root.color == 1?"RED":"BLACK";
		   System.out.println(root.data + "(" + stringColor + ")");
           // Recursive call to get all nodes
		   printTreeHelper(root.left, indent, false);
		   printTreeHelper(root.right, indent, true);
		}
	}

	public static void main(String [] args){
        Scanner input = new Scanner(System.in);
        RedBlackBST bst = new RedBlackBST();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Welcome to my red-black tree visualizer.");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Please enter the values you would like to add to the tree separated by spaces.");
        System.out.println("When you are finished, please type 'end'.");
        System.out.println("------------------------------------------------------------------------------");
        while(input.hasNextInt()){
			int key = input.nextInt();
			bst.insert(key);
		}
        String end = input.next();
        System.out.println("The current tree is:");
    	bst.printTree();
        while(true){
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Please select an option below:");
            System.out.println("------------------------------------------------------------------------------");
			System.out.println("1: Insert a new node");
			System.out.println("2: Delete an existing node");
			System.out.println("3: Quit");
            System.out.println("------------------------------------------------------------------------------");
            int choice = input.nextInt();
            // Insert
            if(choice == 1){
                System.out.println("Please enter the value you would like to insert.");
                int key = input.nextInt();
                bst.insert(key);
            }
            // Delete
            else if(choice == 2){
                System.out.println("Please enter the value you would like to delete.");
                int key = input.nextInt();
                bst.deleteNode(key);
            }
            // Quit
            else if(choice == 3){
                break;
            }
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("The current tree is:");
            bst.printTree();
            System.out.println("------------------------------------------------------------------------------");
			System.out.println("Would you like to make another selection? (y/n)");
            System.out.println("------------------------------------------------------------------------------");
            char answer = input.next().toLowerCase().charAt(0);
			if (answer != 'y')
				break;
        }
	}
}