/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Benajmin
 */

public class Network {
    private int[] layer;
    private Node[] inputLayer;
    private Node[] outputLayer;
    
    public Network(int[] layer){
	this.layer = layer;
	Node[] nextLayer = null;
	for(int i = layer.length - 1; i >= 0; i--){
	    Node[] temp = new Node[layer[i]];
	    for(int j = 0; j < layer[i]; j++){
		temp[j] = new Node(nextLayer);
	    }
	    if(i == layer.length - 1){
		outputLayer = temp;
	    }
	    if(nextLayer != null){
		for(int j = 0; j < nextLayer.length; j++){
		    nextLayer[j].setPrevious(temp);
		}
	    }
	    nextLayer = temp;
	}
	this.inputLayer = nextLayer;

    }
    
    public double process(int[] input){
	for(int i = 0; i < this.inputLayer.length; i++){
	    this.inputLayer[i].addInput(input[i]);
	}
	Node[] temp = this.inputLayer;
	for(int i = 0; i < layer.length; i++){
	    for(int j = 0; j < this.layer[i]; j++){
		temp[j].process();
	    }
	    if(temp[0].getNextLayer() != null){
		temp = temp[0].getNextLayer();
	    }
	}
	return temp[0].getA();
    }
    
    public void backPropagation(double target){
	Node[] temp = this.outputLayer;
	for(int i = this.layer.length - 1; i >= 0; i--){
	    for(int j = 0; j < this.layer[i]; j++){
		temp[j].backProcess();
	    }
	    temp = temp[0].getPreviousLayer();
	}
    }
    
    public void showNetwork(){
	Node[] temp = this.inputLayer;
	for(int i = 0; i < layer.length; i++){
	    for(int j = 0; j < this.layer[i]; j++){
		System.out.println(temp[j].getBiaWeight());
		for(int k = 0; k < temp[j].getWeight().length; k++){
		    System.out.println(i+ "|" + j + "|" + k + "|" + temp[j].getWeight()[k]);
		}
	    }
	    temp = temp[0].getNextLayer();
	}
    }
}
