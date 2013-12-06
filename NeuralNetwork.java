/**
 * @author Benjamin
 * @Dec 5 2013
 */

import java.io.*;
import java.lang.reflect.Array;
import java.util.Scanner;

public class NeuralNetwork {
    public static void main(String[] args) throws FileNotFoundException, IOException {
	int max_iteration = Integer.parseInt(args[3]);
	int small_small_mode = Integer.parseInt(args[4]);
	
	int[] fileInform = findInformOfFile(args[0]);
	int numOfRecord = fileInform[0];
	int numOfTrainSet = (int)Math.floor(0.7 * numOfRecord);
	int numOfSmallSet = (int)Math.floor(0.1 * numOfTrainSet);
	int numOfEvaluateSet = numOfRecord - numOfTrainSet;
	
	System.out.println("numOfRecord = " + numOfRecord + " numOfTrainSet = " + numOfTrainSet + " numOfSmallSet = " + numOfSmallSet + " numOfEvaluateSet = " + numOfEvaluateSet);
	
	//System.out.println(fileInform[0] + " " + fileInform[1]);
	DataSet[] smallSet = new DataSet[10];      //10 sets of data to train the NN
	DataSet evaluationSet;
	
	File dataset_file = new File(args[0]);
	File answer_file = new File(args[1]);
	Scanner dataset  = new Scanner(dataset_file);
	Scanner answer = new Scanner(answer_file);
	
	String[] tempToken;
	int[] inputData = new int[fileInform[1]];
        for(int i = 0; i < 10; i++){
	    smallSet[i] = new DataSet(fileInform[1], numOfSmallSet);
	    for(int j = 0; j < numOfSmallSet; j++){
		tempToken = dataset.nextLine().split(" ");
		try{
		    for(int k = 0; k < tempToken.length; k++){
			inputData[k] = Integer.parseInt(tempToken[k]);
		    }
		}catch(NumberFormatException e){
		    System.out.println("Data missing");
		    smallSet[i].invalidDataSet();
		}
		smallSet[i].addRecord(inputData, answer.nextInt());
	    }
	}
	
	evaluationSet = new DataSet(fileInform[1], numOfEvaluateSet);
	for(int i = 0; dataset.hasNext() && answer.hasNext() && i < numOfEvaluateSet; i ++){
	    tempToken = dataset.nextLine().split(" ");
	    try{
	        for(int k = 0; k < tempToken.length; k++){
		    inputData[k] = Integer.parseInt(tempToken[k]);
		}
	    }catch(NumberFormatException e){
	        evaluationSet.invalidDataSet();
	    }
	    evaluationSet.addRecord(inputData, answer.nextInt());
	}
	
//	showData(smallSet[0]);
//	showData(smallSet[1]);
//	showData(smallSet[2]);
//	showData(smallSet[3]);
//	showData(smallSet[4]);
//	showData(smallSet[5]);
//	showData(smallSet[6]);
//	showData(smallSet[7]);
//	showData(smallSet[8]);
//	showData(smallSet[9]);
//	System.out.println();
//	showData(evaluationSet);
	
	int[] layer = {30, 30, 30, 30, 10, 1};
	Network net = new Network(layer);
	double error = 0;
	double accuracy = 0;
	
	for(int iteration = 0; iteration < max_iteration; iteration ++){
	    for(int testSet = 0; testSet < 10; testSet ++){
		for(int trainSet = 0; trainSet < 10; trainSet ++){
		    if(trainSet != testSet){
			for(int i = 0; i < smallSet[trainSet].getNumOfRecord(); i++){
			    net.process(smallSet[trainSet].getData(i));
			    net.backPropagation(smallSet[trainSet].getAnswer(i));
			}
		    }
		}
		for(int i = 0; i < smallSet[testSet].getNumOfRecord(); i++){
		    double output = net.process(smallSet[testSet].getData(i));
		    output = output >= 0.5 ? 1 : 0;
		    //System.out.println("Error = " + Math.abs(output - smallSet[testSet].getAnswer(i)));
		}
		//System.out.println();
	    }

	    error = 0.0;
	    for(int i = 0; i < evaluationSet.getNumOfRecord(); i++){
		double output = net.process(evaluationSet.getData(i));
		output = output >= 0.5 ? 1 : 0;
		error += Math.abs(output - evaluationSet.getAnswer(i));
	    }
	    double newAccuracy = 1 - (error / evaluationSet.getNumOfRecord());
	    /*
	    if(newAccuracy < accuracy){
		System.out.println("Accuracy = " + newAccuracy);
		break;
	    }
	    */
	    accuracy = newAccuracy;
	    System.out.print("Iteration = " + iteration);
	    System.out.println(" Accuracy = " + accuracy);
	}
//	for(int i = 0; i < max_iteration; i++){
//	    dataset = new Scanner(dataset_file);
//	    answer = new Scanner(answer_file);
//	    while(dataset.hasNextLine()){
//		datasetLine = dataset.nextLine();
//		datasetToken = datasetLine.split(" ");
//		inputData = new int[datasetToken.length];
//		for(int j = 0; j < datasetToken.length; j++){
//		    inputData[j] = Integer.parseInt(datasetToken[j]);
//		}
//		target = answer.nextInt();
//		output = net.process(inputData);
//		net.backPropagation(target);
//		output = output >= 0.5 ? 1 : 0;
//		System.out.println(target - output);
//	    }
//	}
	
	/*
	//net.showNetwork();
	int[] input = {1,2,3};
	double output = net.process(input);
	System.out.println(output);
	for(int i = 0; i < 10 ; i++){
	    net.backPropagation(1.0);
	    output = net.process(input);
	    System.out.println(output);
	}
	*/
    }
    
    public static int[] findInformOfFile(String filename) throws FileNotFoundException, IOException{
	int[] output = new int[2];
	int count = 1;
	String line;
	String[] token = new String[1];
	boolean flag = true;
	
	File f = new File(filename);
	Scanner sca = new Scanner(f);
	while(sca.hasNextLine()){
	    if(flag){
		line = sca.nextLine();
		token = line.split(" ");
		flag = false;
	    }
	    sca.nextLine();
	    count ++;
	}
	
	output[0] = count;
	output[1] = token.length;
	return output;
    }
    
    public static void showData(DataSet input){
	    for(int j = 0; j < input.getNumOfRecord(); j++){
		for(int k = 0; k < input.getData(j).length; k++){
		    System.out.print(input.getData(j)[k]);
		}
		System.out.println(" ans = " + input.getAnswer(j));
	    }
	    System.out.println();
	
    }
}
