package com.GUI;

import java.awt.*;
import javax.swing.*;

public class SolutionDisplay{
    private String localDir = System.getProperty("user.dir");
    private String imagePath = localDir + "\\src\\main\\resources\\MatchstickNumbers\\";
    private JFrame solutionFrame;

    //creates a JFrame that will display all the solutions
    public SolutionDisplay() {
        solutionFrame = new JFrame("Solution(s)");
    }
    //creates a JLabel from element and adds it to the JPanel panel
    private JLabel setUpJLabel(String element){
        ImageIcon img = new ImageIcon(imagePath+ element+".png");
        JLabel label = new JLabel("", img, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }
    //converts the solution to a JPanel and adds it to the JFrame
    public void convertToImage(String[] solution) {
        JPanel panelSolution = createJPanel();
        for(int i = 0; i < solution.length;i++){
            panelSolution.add(setUpJLabel(solution[i]));
        }
        solutionFrame.add(panelSolution);
    }
    //creates a JPanel and sets the color to white
    public JPanel createJPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        return panel;
    }
    //sets up the solutionFrame settings and displays it
    public void displayAnswers(int numOfSolutions){
        solutionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        solutionFrame.setBackground(Color.WHITE);
        solutionFrame.setLayout(new GridLayout(numOfSolutions,1));
        solutionFrame.pack();
        solutionFrame.setLocationRelativeTo(null);
        solutionFrame.setVisible(true);
    }
}
