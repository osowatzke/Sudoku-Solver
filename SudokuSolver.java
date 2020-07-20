import java.io.File;                    // The first three imports are really only needed
import java.io.FileNotFoundException;   // if you are reading values from a text file
import java.util.Scanner;
import java.util.ArrayList;

// user-defined data type to hold guesses
// one guess in each list is correct
// all others are false
class guess{
    ArrayList<Integer> box;     // Lists are added for variable size
    ArrayList<Integer> index;
    int num;
    ArrayList <Boolean> valid;
}

// Class to solve SudokuSolver
public class SudokuSolver {

    // Function to read sudoku puzzle from text file
    // Used for troubleshooting
    // Expects bracket enclosed list for each box
    // Ex: {1,3,5,6,7,9,8,0,0}
    public static int[][] getStrings() throws FileNotFoundException{
        int i, j, k;
        // define new integer array for sudoku puzzle
        int boxes[][] = new int[9][9];
        // Read lines from "Sudoku.txt" file
        File file = new File("c:/Users/osowa/Documents/Java/SudokuSolver/Sudoku.txt");
        Scanner scan = new Scanner(file);
        // Read each line from text file
        for (i = 0; i < 9; ++i){
            j = k = 0;
            // boolean variable allows you to omit 0's
            // all values unitialized are set to 0
            boolean value = true;
            // Convert line to Character array
            char test[] = scan.nextLine().toCharArray();
            // Add data to puzzle while '}' is not found in line
            while(test[j] != '}'){
                // If the puzzle holds a value between 1 and 9
                // Add integer value to boxes array
                if ((test[j] > '0') && (test[j] <= '9')){
                    boxes[i][k] = test[j] - '0';
                    // Integer value has already been set
                    value = false;
                }
                // Initialize next value if there is a comma
                else if (test[j] == ','){
                    ++k;
                    // If no new value is set
                    // Integer in boxes array is set to 0
                    if (value == true){
                        boxes[i][k] = 0;
                    }
                    // Integer value needs to be set
                    value = true;
                }
                // move to next value in character array
                ++j;
            }
            // initialize all unitialized values to 0
            while (k < 8){
                boxes[i][++k] = 0;
            }
        }
        // Close scanner
        scan.close();
        // Return boxes array
        // Contains all values in original sudoku puzzle
        return boxes;
    }

    // print formattted sudoku puzzle to terminal window
    public static void displayContents(int boxes[][]){
        int i, j;
        // for loop goes through all rows
        for (i = 0; i<9;++i){
            // print newline after every third box
            if ((i%3)== 0){
                System.out.print("\n");
            }
            // for loop goes through all columns
            for (j = 0; j<9;++j){
                // Print additional white space after each box
                // Except the first
                if ((j%3 == 0) && (j!= 0)){
                    System.out.print(" ");
                }
                // Output each value with additional white space
                System.out.print(" "+ boxes[(j/3)+3*(i/3)][(j%3)+3*(i%3)]+ " ");
            }
            // move to a new line after each row
            System.out.print("\n");
        }
    }

    // If only one item is missing in array add last item to array
    public static void missing(int array[]){
        // keep track of number of zeros in array
        int numZeros = 0;
        int index = -1;
        // list holds all possible values
        int list[] = {1,2,3,4,5,6,7,8,9};
        int i;
        // set all values in list to zero that are in user-provided array
        for (i = 0; i<9; ++i){
            if (array[i]!=0){
                list[array[i]-1]=0;
            }
            else{
                // increment number of zeros in user-provided array
                ++numZeros;
                // return if there is more than 1 zero
                if (numZeros > 1){
                    return;
                }
                // else hold onto index in array
                index = i;
            }
        }
        // if only one zero is found
        if (numZeros == 1){
            for (i = 0; i<9;++i){
                // set array value that was 0 to the missing number
                if (list[i] != 0){
                    array[index]=list[i];
                    return;
                }
            }
        }
    }

    // Make column array from array of boxes
    public static int[] formColumnArray(int col, int boxes[][]){
        // box number
        int num1 = col/3;
        // index number
        int num2 = col%3;
        int items = 0;
        // create new array
        int array[] = new int[9];
        // add 9 items to column array
        while(items < 9){
            // add number to column array
            array[items] = boxes[num1][num2];
            // if a new box is reached change box number
            // reset index number
            if (num2 > 5){
                num2 = col%3;
                num1+=3;
            }
            // if no new box is reached
            // increment index number by 3
            else {
                num2+=3;
            }
            // move onto new item in column array
            ++items;
        }
        return array;
    }

    // Make row array from array of boxes
    public static int[] formRowArray(int row, int boxes[][]){
        int items = 0;
        int num1,num2;
        // new array to hold row contents
        int array[] = new int[9];
        // num1 holds box number
        num1 = (row/3)*3;
        // num2 holds index number
        num2 = (row%3)*3;
        // add 9 items to row array
        while(items < 9){
            // add number to row array
            array[items] = boxes[num1][num2];
            // if new box is reached increment box number
            // reset index number
            if (num2 >= ((row%3)*3)+2){
                num2 = (row%3)*3;
                ++num1;
            }
            // If no new box is reached
            // increment index number by 1
            else {
                ++num2;
            }
            // Move onto new item in row array
            ++items;
        }
        return array;
    }

    // return updated column array to array of boxes
    public static void placeColinBoxes(int col, int array[], int boxes[][]){
        int items = 0;
        // box number
        int num1 = col/3;
        // index number
        int num2 = col%3;
        while(items < 9){
            // Only update number if number is different
            if (array[items] != boxes[num1][num2]){
                boxes[num1][num2]= array[items]; 
            }
            // If a new box is reached change box number
            // Reset index number
            if (num2 > 5){
                num2 = col%3;
                num1 += 3;
            }
            // if no new box is reached
            // increment index number by 3
            else {
                num2+=3;
            }
            // Move onto next item in column array
            ++items;
        }
    }

    // return updated row array to array of boxes
    public static void placeRowinBoxes(int row, int array[], int boxes[][]){
        int items = 0;
        int num1,num2;
        // num1 holds box number
        num1 = (row/3)*3;
        // num2 holds row number
        num2 = (row%3)*3;
        // For all 9 items in row array
        while(items < 9){
            // Set Corresponding item in boxes to number in row array
            boxes[num1][num2] = array[items];
            // If new box is reached, change box number
            // reset index number
            if (num2 >= ((row%3)*3)+2){
                num2 = (row%3)*3;
                ++num1;
            }
            // If no new box is reached
            // increment index number by 1
            else {
                ++num2;
            }
            // Move onto next item in row array
            ++items;
        }
    }

    // Check all solvable columns
    public static int checkVertical(int boxes[][]){
        // Define a new array to hold numbers in column
        int array[] = new int[9];
        int i,j,k;
        // min holds minimum number of zeros in a column (not 0)
        // initialized as largest possible value
        int min = 9;
        // Check through all 9 columns
        for (i = 0; i<9; ++i){
            // form an array for each column
            array = formColumnArray(i, boxes);
            for (j = 1; j<=9;++j){
                // if a number is not in the column
                if (inList(j, array) == false){
                    // hold onto the empty spaces of the array
                    int zeros[] = emptySpaces(array);
                    // hold is initialized as -1
                    // will hold the value of a valid empty space
                    int hold = -1;
                    // out determines holds # of ruled out spaces
                    int out = 0;
                    // check through all the empty spaces of the array
                    for (k =0; k < zeros.length; ++k){
                        // form row array
                        int rowArray[] = formRowArray(zeros[k], boxes);
                        // determine box number
                        int box = 3*(zeros[k]/3)+(i/3);
                        // if the number is in the same row or box
                        if ((inList(j, rowArray))||(inList(j, boxes[box]))){
                            // increment number of ruled out spaces
                            ++out;
                        }
                        // if the number is potentially in the empty space
                        else{
                            // hold onto the index of that value
                            hold = zeros[k];
                        } 
                    }
                    // Keep track of minimum number of spaces for all numbers
                    // This is used to deteremine when to guess
                    if (((zeros.length-out)<min) && ((zeros.length-out)>0)) {
                        min = zeros.length-out;
                    }
                    // if there is only one valid space for a particular number
                    if ((out+1) == zeros.length && (hold!=-1)){
                        // place that number in the array
                        array[hold]=j;
                    }
                }
            }
            // if there is only one missing number place that number in the array
            missing(array);
            // place the column array back into the boxes array
            placeColinBoxes(i, array, boxes);
        }
        // return the minimum number of empty spaces
        return min;
    }

    // Check all solvable rows
    public static int checkHorizontal(int boxes[][]){
        int i,j,k;
        // Define a new array to hold the numbers in a row
        int array[] = new int[9];
        // min holds minimum number of zeros in a column (not 0)
        // initialized as largest possible value
        int min = 9;
        // Check through all 9 rows
        for (i = 0; i<9; ++i){
            // form each row array
            array = formRowArray(i, boxes);
            // check the numbers 1 through 9
            for (j = 1; j<=9;++j){
                // only consider numbers not in the array
                if (inList(j, array) == false){
                    // find the empty spaces in the array
                    int zeros[] = emptySpaces(array);
                    // hold is initialized as -1 
                    // eventually will hold the index of a number we want to put into the row array
                    int hold=-1;
                    // out keeps track of the number of empty spaces we have ruled out
                    int out = 0;
                    // Check through all the empty spaces
                    for (k =0; k < zeros.length; ++k){
                        // form the column array that aligns with that space
                        int colArray[] = formColumnArray(zeros[k], boxes);
                        // find the box number that aligns with that space
                        int box = (zeros[k]/3)+3*(i/3);
                        // If the number is in the column or box
                        if ((inList(j, colArray))||(inList(j, boxes[box]))){
                            // increment the number of ruled out spaces
                            ++out;
                        }
                        // If not hold onto the index of that number
                        else{
                            hold = zeros[k];
                        } 
                    }
                    // min holds the minimum number of spaces possible as a position for any number
                    // important for figuring out when to guess
                    if (((zeros.length-out)<min) && ((zeros.length-out)>0)) {
                        min = zeros.length-out;
                    }
                    // If only one number is possible for a particular number
                    if ((out+1) == zeros.length && (hold!=-1)){
                        // add that number to the row array
                        array[hold]=j;
                    }
                }
            }
            // If only one number is missing in a row array
            // Add that number to the row array
            missing(array);
            // Place the row array into the boxes array
            placeRowinBoxes(i, array, boxes);
        }
        // return min to main
        return min;
    }

    // Check all solvable boxes
    public static int checkBoxes(int boxes[][]){
        int i,j,k;
        // min holds minimum number of zeros in a column (not 0)
        // initialized as largest possible value
        int min = 9;
        // check through all boxes
        for (i = 0; i<9; ++i){
            // check all numbers 1 through 9
            for (j = 1; j<=9;++j){
                // only consider numbers not in the box
                if (inList(j, boxes[i]) == false){
                    // array holds the empty spaces in the box
                    int zeros[] = emptySpaces(boxes[i]);
                    // hold is intialized to -1 
                    // eventually will hold index of a variable we wish to add to a box
                    int hold = -1;
                    // out is initialized to 0
                    // holds the number of spaces we have ruled out for a particular number
                    int out = 0;
                    // Check all the empty spaces
                    for (k =0; k < zeros.length; ++k){
                        // form column and row array that align with space
                        int row = 3*(i/3)+zeros[k]/3;
                        int col = 3*(i%3)+zeros[k]%3;
                        int rowArray[] = formRowArray(row, boxes);
                        int colArray[] = formColumnArray(col, boxes);
                        // if the number is in the aligned column or row
                        if ((inList(j, colArray))||(inList(j, rowArray))){
                            // increment out
                            ++out;
                        }
                        // if not, hold onto index of that space
                        else{
                            hold = zeros[k];
                        } 
                    }
                    // min holds the minimum number of spaces possible as a position for any number
                    // important for figuring out when to guess
                    if (((zeros.length-out)<min) && ((zeros.length-out)>0)) {
                        min = zeros.length-out;
                    }
                    // if there is only one place for a number to go in a box
                    // place that number in the box
                    if (((out+1) == zeros.length) && (hold != -1)){
                        boxes[i][hold]=j;
                    }
                }
            }
            // if only one space is missing in a box
            // fill in the number that goes in that space
            missing(boxes[i]);
        }
        // return min to main
        return min;
    }

    // Check if a number is in the list
    public static boolean inList(int num, int list[]){
        int i;
        // go through all values in the list
        for (i = 0; i<list.length;++i){
            // return true if number is in list
            if (list[i] == num){
                return true;
            }
        }
        // false if not
        return false;
    }

    // return list of empty tiles
    public static int [] emptySpaces(int list[]){
        int i;
        int count = 0;
        // count all empty spaces in the list
        for (i = 0; i<9;++i){
            if (list[i] == 0){
                ++count;
            }
        }
        // form an array with the same size as the number of empty spaces
        int zeros[] = new int[count];
        count = 0;
        // fill in the numbers of that list with the indices of the zeros in
        // the user-provided list
        for (i = 0; i<9; ++i){
            if (list[i] == 0){
                zeros[count++]=i;
            }
        }
        // return the list containing the indices of the zeros
        return zeros;
    }

    // check for zeros in sudoku puzzle
    // used to determine when to end the game
    public static boolean zeros(int boxes[][]){
        int i, j;
        // check each space in the sudoku puzzle
        for (i = 0; i<9;++i){
            for (j = 0; j<9; ++j){
                // if any are zero return true
                if (boxes[i][j] == 0){
                    return true;
                }
            }
        }
        // if not return false
        return false;
    }

    // guess a value in the sudoku puzzle
    public static void makeGuess(int minBox, ArrayList<guess> attempt, int boxes[][], ArrayList<int[][]> forFail) {
        // initialize a guess variable
        attempt.add(guessBox(minBox, boxes));
        // intialize a new [9][9] array to hold a copy of boxes
        // just in case guess is wrong
        int boxesCopy[][] = new int[9][9];
        // copy current contents of sudoku puzzle to array
        copy(boxesCopy, boxes);
        // add array to list
        forFail.add(boxesCopy);
        // place the guess in the sudoku puzzle
        boxes[attempt.get(attempt.size()-1).box.get(0)][attempt.get(attempt.size()-1).index.get(0)] = attempt.get(attempt.size()-1).num;
    }

    // copy the contents of b2 to b1
    public static void copy(int[][] b1, int[][] b2){
        int i, j;
        // copy the value at each index of b2 to the corresponding 
        // index of b1
        for (i = 0; i<9; ++i){
            for (j = 0; j<9; ++j){
                b1[i][j] = b2[i][j];
            }
        }
    }

    // Function that will returns puzzle to original state if guess is false
    // and then makes the next logical guess
    public static void RescindGuess(ArrayList<guess> attempt, int boxes[][], ArrayList<int[][]> forFail){
        int i;
        // num holds last guess
        guess num = attempt.get(attempt.size()-1);
        // len holds the number of alternate possibilities in the last guess
        int len = (num.box).size();
        // Check through all alternate possibilites
        for (i = 0; i<len; ++i){
            // set the last one to false
            if(num.valid.get(i) == true){
                num.valid.set(i, false);
                break;
            }
        }
        // if there are still alternate possibilities
        if ((i+1) < len){
            // reset boxes to correct state
            copy(boxes, forFail.get(forFail.size()-1));
            // choose next alternate possibility
            boxes[num.box.get(i+1)][num.index.get(i+1)] = num.num;
        }
        // if there are no longer alternate possibilities
        // guess before current one was incorrect
        else {
            // remove last guess
            attempt.remove(attempt.size()-1);
            // remove last saved game state
            forFail.remove(forFail.size()-1);
            // call function recursively to set guess before to false
            // and determine the next alternate possibility
            RescindGuess(attempt,boxes,forFail);
        }
    }

    // function to set all values in array to 0
    public static int[] initArray(int list[]){
        int i;
        for (i = 0; i<9; ++i){
            list[i] = 0;
        }
        return list;
    }

    // Function that makes a guess called by makeGuess
    public static guess guessBox(int min, int boxes[][]){
        int i,j,k;
        int n = 0;
        // Check through all boxes
        for (i = 0; i<9; ++i){
            // Check numbers 1 through 9
            for (j =1; j<=9; ++j){
                // if number is not in box
                if (!(inList(j, boxes[i]))){
                    // find the empty spaces in box
                    int zeros[] = emptySpaces(boxes[i]);
                    // define array of zeros will eventually hold possible indices
                    // in the box for the particular number
                    int index[] = new int[9];
                    index = initArray(index);
                    // n counts the number of spaces the number can be in
                    // initialized to zero
                    n = 0;
                    // check through all the empty spaces
                    for (k =0; k < zeros.length; ++k){
                        // form row and column arrays that align with that space
                        int row = 3*(i/3)+zeros[k]/3;
                        int col = 3*(i%3)+zeros[k]%3;
                        int rowArray[] = formRowArray(row, boxes);
                        int colArray[] = formColumnArray(col, boxes);
                        // if the number is not in the row or column array
                        if (!((inList(j, colArray))||(inList(j, rowArray)))){
                            // store that number in the index array
                            index[n] = zeros[k];
                            // increment n
                            ++n;
                        }
                    }
                    // if n is equally to min
                    // best guess
                    if (n == min){
                        // define a new guess
                        guess attempt = new guess();
                        // guess contains a list of possible boxes
                        attempt.box = new ArrayList<Integer>();
                        // a list of possible indices
                        attempt.index = new ArrayList<Integer>();
                        // a list to state whether each alternate guess is valid
                        attempt.valid = new ArrayList<Boolean>();
                        // guess contains number corresponding to best guess
                        attempt.num = j;
                        for (n = 0; n<min; ++n){
                            // all guesses are at different indices in the same box
                            attempt.box.add(i);
                            attempt.index.add(index[n]);
                            // and are initialized as valid
                            attempt.valid.add(true);
                        }
                        // return guess to makeGuess 
                        return attempt;
                    }
                }
            }
        }
        // if there is no possible guess return null
        return null;
    }

    // function to determine if two values within a list are the same
    public static boolean twoSame(int list[]){
        int i,j;
        // compare different values
        // not that i and j are offest from one another
        for (i = 0; i<9;++i){
            for (j = i+1; j<9; ++j){
                // if they are the same and not empty
                if ((list[i] == list[j]) && (list[i] != 0)){
                    return true;    // return true
                }
            }
        }
        return false; // if there are not matches, return false
    }

    // Function to check whether a solution and/or guess is valid
    public static boolean checkForFailure(int boxes[][]){
        int i;
        // check through all boxes, rows, and columns
        for (i = 0; i<9;++i){
            // if there are any identical numbers return true
            if (twoSame(boxes[i])){
                return true;
            }
            if (twoSame(formRowArray(i, boxes))){
                return true;
            }
            if (twoSame(formColumnArray(i, boxes))){
                return true;
            }
        }
        // if there are not identical numbers return false
        return false;
    }

    // add FileNotFoundException if using getStrings() to read input from the textfile
    public static void main(String[] args) throws InterruptedException{ 
        // create new SudokuGUI() object
        SudokuGUI gui = new SudokuGUI();
        // make it visible and not resizable                                
        gui.setVisible(true);
        //gui.setResizable(false);
        // loop forever
        // loop is broken when gui is exited
        while (true){
            // loop until user has hit SOLVE
            while (gui.validInput == false){
                // Thread.sleep() is used to sync threads
                Thread.sleep(100);
            }
            // gui.validInput is set false to wait for next user SOLVE
            gui.validInput = false;
            // get the contents of the gui
            int boxes[][] = gui.boxes;
            // check for invalid input
            if (checkForFailure(boxes)){
                gui.label.setVisible(true);
                continue;
            }
            gui.label.setVisible(false);
            // If you would rather receive input from text file use line below
            //int boxes[][] = getStrings();
            // Output original puzzle
            System.out.println("Original Puzzle");
            displayContents(boxes);
            // Variables hold the minimum number of options when trying to solve
            // a box, a row, or a column respectively
            int minBox, minRow, minCol;
            // initialize list of guesses
            ArrayList <guess> attempt = new ArrayList<guess>();
            // initialize list to hold puzzles before guesses
            // in case guess is wrong
            ArrayList <int[][]> forFail = new ArrayList<int[][]>();
            // Loop until there are no empty spaces left in puzzle
            while (zeros(boxes)){
                // Solve boxes, rows, and columns using simplest algorithms
                minBox = checkBoxes(boxes);
                minRow = checkHorizontal(boxes);
                minCol = checkVertical(boxes);
                // if simplest algorithms don't work
                if (minCol > 1 && minRow > 1 && minBox > 1){
                    // make a guess
                    makeGuess(minBox, attempt, boxes, forFail);
                }
                gui.label.setVisible(false);
                // if the guess was wrong
                if (checkForFailure(boxes)){
                    // remove the guess and go to the next alternate possibility
                    RescindGuess(attempt, boxes, forFail);
                }
            }
            // Output solution to terminal window
            System.out.println("Solution");
            displayContents(boxes);
            // Place solution in gui for user
            solution(boxes, gui);
            // Wait for user to press CLEAR before proceeding with another solve
            while (gui.resetFinished == false){
                Thread.sleep(100);
            }
            // set to false for end of next solve
            gui.resetFinished = false;
        }
    }

    // Function to place solved sudoku puzzle in gui
    public static void solution(int[][] boxes, SudokuGUI gui){
        int i;
        int row, col;
        // go through all possible spaces
        for (i = 0; i<81; ++i){
            // determine row and column of that space
            row = i/9;
            col = i%9;
            // Use that information to transfer values from the boxes array to the GUI
            gui.textfield[i].setText(Integer.toString(boxes[3*(row/3)+col/3][3*(row%3)+col%3]));
        }
    }
}
