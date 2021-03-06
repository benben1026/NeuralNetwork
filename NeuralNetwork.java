/**
 * @author Benjamin
 * @Dec 5 2013
 */

import java.io.*;
import java.util.Scanner;

public class NeuralNetwork {
    public static void main(String[] args){
	int max_iteration = Integer.parseInt(args[2]);
	int small_sample_mode = Integer.parseInt(args[3]);
	int[] layer = null;
	if(small_sample_mode == 1){
	    max_iteration = 3000;
	    int[] la = {30, 20, 6, 1};
	    layer = la;
	}else{
	    max_iteration = 950;
	    int[] la = {30, 30, 30, 1};
	    layer = la;
	}
	
	int[] fileInform = {};
	try{
	    fileInform = findInformOfFile(args[0]);
	}catch(Exception e){
	    System.out.println("File not Found");
	}
	
	int numOfRecord = fileInform[0];
	int numOfTrainSet = (int)Math.floor(0.7 * numOfRecord);
	int numOfSmallSet = (int)Math.floor(0.1 * numOfTrainSet);
	int numOfEvaluateSet = numOfRecord - numOfTrainSet;
	
	DataSet[] smallSet = new DataSet[10];      //10 sets of data to train the NN
	DataSet evaluationSet;
	
	File dataset_file = new File(args[0]);
	File answer_file = new File(args[1]);
	Scanner dataset = null;
	Scanner answer = null;
	try {
	    dataset = new Scanner(dataset_file);
	    answer = new Scanner(answer_file);
	} catch (FileNotFoundException ex) {
	    System.out.println("File not Found");
	}
	
	String[] tempToken;
	int[] inputData = new int[fileInform[1]];
        for(int i = 0; i < 10; i++){
	    smallSet[i] = new DataSet(fileInform[1], numOfSmallSet);
	    for(int j = 0; j < numOfSmallSet; j++){
		tempToken = dataset.nextLine().split(" ");
//		try{
		    for(int k = 0; k < tempToken.length; k++){
			inputData[k] = Integer.parseInt(tempToken[k]);
		    }
//		}catch(NumberFormatException e){
//		    System.out.println("Data missing");
//		    smallSet[i].invalidDataSet();
//		}
		if(answer.hasNextInt()){
		    smallSet[i].addRecord(inputData, answer.nextInt());
		}else{
		    answer.next();
		    smallSet[i].addRecord(inputData, -1);
		}
	    }
	}
	
	evaluationSet = new DataSet(fileInform[1], numOfEvaluateSet);
	for(int i = 0; dataset.hasNext() && answer.hasNext() && i < numOfEvaluateSet; i ++){
	    tempToken = dataset.nextLine().split(" ");
//	    try{
	        for(int k = 0; k < tempToken.length; k++){
		    inputData[k] = Integer.parseInt(tempToken[k]);
		}
//	    }catch(NumberFormatException e){
//	        evaluationSet.invalidDataSet();
//	    }
		if(answer.hasNextInt()){
		    evaluationSet.addRecord(inputData, answer.nextInt());
		}else{
		    answer.next();
		    evaluationSet.addRecord(inputData, -1);
		}
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
//	showData(evaluationSet);
	
	//int[] layer = {30, 30, 30, 1};
	Network net = new Network(layer);
	double error = 0.0;
	double error2 = 0.0;
	double accuracy = 0.0;
	double all_accuracy = 0.0;
	double max_accuracy = 0.0;
	double allAccuracy = 0.0;
	int time = 0;
	int quota = 1;
	String buffer = "";
	String networkLayer = "";
	
//net.showNetwork();
	for(int iteration = 0; iteration < max_iteration; iteration ++){
		for(int trainSet = 0; trainSet < 10; trainSet ++){
			for(int i = 0; i < smallSet[trainSet].getNumOfRecord(); i++){
//showData(smallSet[trainSet]);
			    if(smallSet[trainSet].getAnswer(i) != -1){
				net.process(smallSet[trainSet].getData(i));
				net.backPropagation(smallSet[trainSet].getAnswer(i));
			    }
			}
		}

	    error = 0.0;
	    for(int i = 0; i < evaluationSet.getNumOfRecord(); i++){
		double output = net.process(evaluationSet.getData(i));
		output = output >= 0.5 ? 1 : 0;
		error += Math.abs(output - evaluationSet.getAnswer(i));
	    }
	    accuracy = 1 - (error / evaluationSet.getNumOfRecord());
	    
//	    error2 = 0.0;
//	    for(int j = 0; j < smallSet.length; j++){
//		for(int i = 0; i < smallSet[j].getNumOfRecord(); i++){
//		    double output = net.process(evaluationSet.getData(i));
//		    output = output >= 0.5 ? 1 : 0;
//		    error2 += Math.abs(output - evaluationSet.getAnswer(i));
//		}
//	    }
	    
	    if(accuracy > max_accuracy){
		max_accuracy = accuracy;
		//allAccuracy = 1 - ((error + error2) / fileInform[0]);
		buffer = net.toString();
	    }
	    if(accuracy > 0.7 && quota == 1){
		net.setAlpha(net.getAlpha() / 2);
		quota --;
	    }
//	    if(max_accuracy > 0.8){
//		break;
//	    }
	    //System.out.print("Iteration = " + iteration);
	    //System.out.print(" Alpha = " + net.getAlpha());
	    //System.out.println(" Accuracy = " + accuracy);
	    //System.out.println(" AllAccuracy = " + (1 - ((error + error2) / 1600)));
	    
	}
	
	for(int i = 0; i < layer.length; i++){
	    networkLayer += layer[i] + " ";
	}
	System.out.println(max_accuracy + "\n" + networkLayer + "\n" + buffer);
//	PrintStream f = null;
//	try {
//	    f = new PrintStream(args[2]);
//	} catch (FileNotFoundException ex) {
//	    System.out.println("File not Found");
//	}
//	f.println(allAccuracy + "\n" + networkLayer + "\n" + buffer);
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
    
    public static int evaluate(Network net, DataSet input){
	double error = 0.0;
	for(int j = 0; j < input.getNumOfRecord(); j++){
	    double output = net.process(input.getData(j));
	    output = output >= 0.5 ? 1 : 0;
	    error += Math.abs(output - input.getAnswer(j));
	}
	int t =  input.getNumOfRecord() - (int)error;
	return t;
    }
}
