/**
 * @author Benjamin
 * @Dec 5 2013
 */

import java.io.*;
import java.math.*;
import java.util.Random;

public class Node {
    private Node[] nextLayer;
    private Node[] previousLayer;
    private double[] weight;
    private double bia;
    private double biaWeight;
    private double input;
    private double a;
    private int numOfNextLayer;
    private double error;
    private double delta;
    private double alpha = 0.0;
    
    public Node(Node[] nextLayer){
	this.nextLayer = nextLayer;
	this.previousLayer = null;
	if(nextLayer != null){
	    this.numOfNextLayer = nextLayer.length;
	}else{
	    this.numOfNextLayer = 0;
	}
	Random ran = new Random();
	this.biaWeight  = ran.nextDouble() - 0.5;
	this.bia = 1.0;
	this.a = 0;
	this.input = this.bia * this.biaWeight;
	this.weight = new double[this.numOfNextLayer];
	for(int i = 0; i < numOfNextLayer; i++){
	    this.weight[i]  = ran.nextDouble() - 0.5;
	}
	this.alpha = 0.05;
	this.error = 0.0;
	this.delta = 0.0;
    }
    
    public double activation(double t){
	return 1 / (1 + Math.exp(-1 * t));
    }
    
    public void process(){
	this.updataA();
	if(this.nextLayer != null){
	    for(int i = 0; i < this.nextLayer.length; i++){
		nextLayer[i].addInput(this.a * weight[i]);
	    }
	}
    }
    
    public void backProcess(){
	if(this.nextLayer == null){
	    this.delta = this.error * this.a * (1 - this.a);
	}else{
	    double sum = 0;
	    this.biaWeight = this.biaWeight - this.getAlpha() * this.a * this.delta;
	    for(int i = 0; i < this.nextLayer.length; i++){
		this.weight[i] = this.weight[i] - this.getAlpha() * this.a * this.nextLayer[i].getDelta();
		sum += this.weight[i] * this.nextLayer[i].delta;
	    }
	    this.delta = sum * this.a * (1 - this.a);
	}
    }
    
    public void activation(){
	this.a = 1 / (1 + Math.exp(-1 * this.a));
    }
    
    public void updataA(){
	this.a = this.activation(this.input + (this.bia * this.biaWeight));
    }
    
    public void initialInput(){
	this.input = 0;
    }
    
    public void addInput(double value){
	input += value;
    }
    
    public void setPrevious(Node[] previousLayer){
	this.previousLayer = previousLayer;
    }
    
    public void setError(double target){
	this.error = this.a - target;
    }
    
    public void setAlpha(double value){
	this.alpha = value;
    }
    
    public double[] getWeight(){
	return this.weight;
    }
    
    public Node[] getNextLayer(){
	return this.nextLayer;
    }
    
    public Node[] getPreviousLayer(){
	return this.previousLayer;
    }
    public double getBiaWeight(){
	return this.biaWeight;
    }
    
    public double getAlpha(){
	return this.alpha;
    }
    
    public double getA(){
	return this.a;
    }
    
    public double getInput(){
	return this.input;
    }
    
    public double getDelta(){
	return this.delta;
    }
}
