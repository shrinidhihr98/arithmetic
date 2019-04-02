package com.math.math;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class questionGenerator {
    final String[] problems;
    final String[] answers;

    //Constructor
    questionGenerator(boolean[] operators_input, int numbers_bound, int numbers_bound_permutation){

        problems = new String[10];
        answers = new String[10];

        ArrayList<String> operatorArray = new ArrayList<>();
        if (operators_input[0]) {
            operatorArray.add("+");
        }
        if (operators_input[1]) {
            operatorArray.add("-");
        }
        if (operators_input[2]) {
            operatorArray.add("*");
        }
        if (operators_input[3]) {
            operatorArray.add("/");
        }
        if(operators_input[4]){
            operatorArray.add("P");
        }
        if(operators_input[5]){
            operatorArray.add("C");
        }

        String TAG = "questionGenerator.java";
        Log.i(TAG, "onCreateView: The operators used are:" + operatorArray);

        Random rand = new Random();
        int solution = 0;

        int i =0;
        ArrayList<String> answersList = new ArrayList<>();
        //for(int i = 0; i<10;i++) {
        while(i<10){
            String operator = operatorArray.get(rand.nextInt(operatorArray.size()));
            int a;
            int b;
            if (operator.equals("P") || operator.equals("C")) {
                a = rand.nextInt(numbers_bound_permutation);
                b = rand.nextInt(numbers_bound_permutation);
            } else {
                a = rand.nextInt(numbers_bound);
                b = rand.nextInt(numbers_bound);
            }

            Log.i(TAG, "onCreateView: a generated:" + a + "\nb generated: " + b);
            switch (operator) {
                case "+":
                    solution = a + b;
                    break;
                case "-":
                    if (a < b) {
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");
                        int j = a;
                        a = b;
                        b = j;
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                    }
                    solution = a - b;
                    break;
                case "*":
                    solution = a * b;
                    break;
                case "/":

                    if (a < b) {
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");

                        int j = a;
                        a = b;
                        b = j;
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                    }

                    if (b == 0) {
                        Log.i(TAG, "Division: B is zero. Setting B to 2.");
                        b = 2;
                    }

                    float possiblesolution = (float) a / b;
                    Log.i(TAG, "onCreateView: Possible solution:" + possiblesolution);

                    while (!(possiblesolution == (a / b))) {
                        Log.i(TAG, "onCreateView: Solution is not an integer. A:" + a + " B:" + b + " Decrementing a.");
                        a--;
                        possiblesolution = (float) a / b;
                    }

                    if (a == b && (a * 2) < numbers_bound) {
                        a = a * 2;
                    }
                    solution = a / b;
                    break;
                case "P":
                    if (a < b) {
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");

                        int j = a;
                        a = b;
                        b = j;
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                    }

                    solution = permutation(a, b);
                    break;
                case "C":
                    if (a < b) {
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");

                        int j = a;
                        a = b;
                        b = j;
                        Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                    }

                    solution = combination(a, b);
                    break;

                default:
                    Log.i(TAG, "Operator not generated! Error!" + operator);
            }

            String problem_generated = a +operator+ b;

            if(answersList.isEmpty()){
                answersList.add(String.valueOf(solution));
                Log.i(TAG, "onCreateView: Problem generated: " + problem_generated);
                Log.i(TAG, "onCreateView: Solution generated: " + solution);

                problems[i] = problem_generated;
                answers[i] = String.valueOf(solution);
                i++;
                answersList.add(String.valueOf(solution));
            }
            if(! answersList.contains(String.valueOf(solution))){
                Log.i(TAG, "QuestionGenerator: onCreateView: Problem generated: " + problem_generated);
                Log.i(TAG, "QuestionGenerator: onCreateView: Solution generated: " + solution);

                problems[i] = problem_generated;
                answers[i] = String.valueOf(solution);
                i++;
                answersList.add(String.valueOf(solution));
            }

        }

        Log.i(TAG, "questionGenerator: Problems generated:"+ Arrays.toString(problems));
        Log.i(TAG, "questionGenerator: Answers generated:"+ Arrays.toString(answers));


    }


    private static int permutation(int a, int b){
        int p = 1;
        for(int i = a; i>(a-b); i--){
            p =p*i;
        }
        return p;
    }

    private static int combination(int a, int b){
        int p = 1;
        for(int i = a; i>(a-b); i--){
            p =p*i;
        }
        int r =1;
        for(int i =1;i<=b;i++){
            r=r*i;
        }

        return p/r;
    }
}
