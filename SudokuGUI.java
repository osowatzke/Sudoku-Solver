import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SudokuGUI extends JFrame implements ActionListener, ComponentListener{

    // define public variables that can be accessed by Sudoku Solver
    // and actionPerformed function
    public JLabel label;
    public JButton button;
    public JButton button2;
    public JTextField textfield[];
    public int boxes[][] = new int[9][9];
    public boolean validInput;
    public boolean resetFinished;

    // define characteristics of SudokuGUI() object
    public SudokuGUI(){
        // Get screen dimensions
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        // Define original GUI dimensions in relation to screen size
        width = (int)(0.46875*width);
        height = (int)(0.625*height);
        setSize(width,height);
        setMinimumSize(new Dimension(width,height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // do not use a layout manager
        setLayout(null);
        // 81 text fields
        // 1 for each square in Sudoku puzzle
        textfield = new JTextField[81];
        int i;
        // Set a new larger font
        Font font = new Font("SansSerif", Font.PLAIN, width/30);
        for (i = 0; i<81; ++i){
            textfield[i] = new JTextField(1);
            // set position on GUI
            textfield[i].setBounds(width/30+(width/15)*(i%9),width/30+(width/15)*(i/9), (width/15), (width/15));
            // center text of textfield
            textfield[i].setHorizontalAlignment(JTextField.CENTER);
            // use larger font
            textfield[i].setFont(font);
            // If else statement determines border of tiles in sudoku puzzle
            //top border
            if ((i/9)==0){
                // top-left corner
                if ((i%9)==0)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(2, 2, 0, 1,Color.black));
                // top-right corner
                else if ((i%9) == 8)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(2, 0, 0, 2,Color.black));
                // new box
                else if ((i%3) == 0)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(2, 1, 0, 1,Color.black));
                else
                    textfield[i].setBorder(BorderFactory.createMatteBorder(2, 0, 0, 1,Color.black));
            }
            //bottom border
            else if ((i/9)==8){
                // bottom-left corner
                if ((i%9)==0)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 2, 2, 1,Color.black));
                // bottom-right corner
                else if ((i%9) == 8)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 0, 2, 2,Color.black));
                // new box
                else if ((i%3) == 0)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1,Color.black));
                else
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 0, 2, 1,Color.black));
            }
            // remainder of left border
            // corners have already been taken care of 
            else if ((i%9)==0){
                // new box
                if (((i/9)==2)||((i/9)==5))
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1,Color.black));
                else
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 2, 0, 1,Color.black));
            }
            // remainder of right border
            // corners have already been taken care of
            else if ((i%9)==8){
                // new box
                if (((i/9)==2)||((i/9)==5))
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 2,Color.black));
                else
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 0, 0, 2,Color.black));
            }
            //center of puzzle
            else
                // bottom of a box
                if (((i/9)==2)||((i/9)==5))
                    // box corner
                    if (((i%3) == 0))
                        textfield[i].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,Color.black));
                    else
                        textfield[i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1,Color.black));
                // right side of box
                else if ((i%3) == 0)
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,Color.black));
                // everything else
                else 
                    textfield[i].setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1,Color.black));
            // add text field to GUI
            add(textfield[i]);
        }

        // create button for solving and add action listener
        button = new JButton("SOLVE");
        button.setBounds(400, 125, 180, 50);
        button.addActionListener(this);
        add(button);
    
        // create button for clear contents and add action listener
        button2 = new JButton("CLEAR");
        button2.setBounds(400,225, 180, 50);
        button2.addActionListener(this);
        add(button2);

        // create label to appear in case of error
        label = new JLabel("Invalid Input: Please Re-enter Puzzle");
        label.setForeground(Color.red);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 375, 400, 40);
        label.setVisible(false);
        add(label);

        addComponentListener(this);
    }

    public static void main(final String[] args){
        // create a new gui
        SudokuGUI gui = new SudokuGUI();
        // make it visible and 
        gui.setVisible(true);
        // and setup boolean for use in Sudoku Solver
        gui.validInput = false;
        gui.resetFinished = false;
    } 

    public void actionPerformed(final ActionEvent e){
        // If user presses solve button
        if (e.getActionCommand().equals("SOLVE")){
            int i;
            int row, col;
            // loop through each textbox
            for (i = 0; i<81; ++i){
                // determine the row and column that correspond to that textbox
                row = i/9;
                col = i%9;
                // if there is something there
                if (textfield[i].getText().length() != 0){
                    // place integer in boxes array
                    if (textfield[i].getText().length() != 1){
                        label.setVisible(true);
                        return;
                    }
                    else if ((textfield[i].getText().charAt(0) <= '0') || (textfield[i].getText().charAt(0) > '9')){
                        label.setVisible(true);
                        return;
                    }
                    else{
                        boxes[3*(row/3)+col/3][3*(row%3)+col%3] = textfield[i].getText().charAt(0)-'0';
                    }
                }
                // if nothing is there, put 0 in boxes array
                else
                    boxes[3*(row/3)+col/3][3*(row%3)+col%3] = 0;
            }
            // Set gui.validInput to true so SudokuSolver knows it can read the contents of gui.boxes
            validInput = true;

            /* if we want to print contents read from gui
            for (i = 0; i<9; ++i){
                System.out.print('{');
                for (j = 0; j<9;++j){
                    if (j != 0){
                        System.out.print(", "+boxes[i][j]);
                    }
                    else{
                        System.out.print(boxes[i][j]);
                    }
                }
                System.out.println("}");
            }*/

        }
        // If the user presses the CLEAR button
        else if (e.getActionCommand().equals("CLEAR")){
            int i;
            // set gui.resetFinished to true so SudokuSolver
            // knows the user wants to input another puzzle
            resetFinished = true;
            // Clear all contents of GUI
            for (i = 0; i<81; ++i){
                textfield[i].setText("");
            }
        }
    }

    public void componentResized(ComponentEvent e){
        // Get dimensions after resize
        Component component= e.getComponent();
        int Width = component.getWidth();
        int Height = component.getHeight();
        // Find minimum dimension
        int min = ((2*Width/3) > (Height-50)) ? (Height-50):(2*Width/3);
        min = min/10;
        int i;
        // Scale font size
        Font font = new Font("SansSerif", Font.PLAIN, min/2);
        for (i = 0; i < 81; ++i){
            // Scale textfields
            textfield[i].setBounds(min/2 + min*(i%9), min/2 + min*(i/9), min, min);
            textfield[i].setFont(font);
        }
        // Scale buttons
        button.setBounds(10*min,25*min/8,9*min/2,5*min/4);
        button.setFont(new Font("SansSerif", Font.BOLD, 3*min/10));
        button2.setBounds(10*min,45*min/8,9*min/2,5*min/4);
        button2.setFont(new Font("SansSerif", Font.BOLD, 3*min/10));
        // Scale error message
        label.setBounds(0, 75*min/8, min*10,min);
        label.setFont(new Font("SansSerif", Font.PLAIN, 3*min/10));
    }

    // Required as a part of component listener
    public void componentMoved(ComponentEvent e){

    }

    public void componentShown(ComponentEvent e){

    }

    public void componentHidden(ComponentEvent e){

    }
}