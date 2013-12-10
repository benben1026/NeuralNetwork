
import java.io.File;
import java.text.DecimalFormat;

/**
 * @author Benjamin
 * @Dec 5 2013
 */
public class Network {

    private int[] layer;
    private Node[] inputLayer;
    private Node[] outputLayer;

    public Network(int[] layer) {
	this.layer = layer;
	Node[] nextLayer = null;
	for (int i = layer.length - 1; i >= 0; i--) {
	    Node[] temp = new Node[layer[i]];
	    for (int j = 0; j < layer[i]; j++) {
		temp[j] = new Node(nextLayer);
	    }
	    if (i == layer.length - 1) {
		outputLayer = temp;
	    }
	    if (nextLayer != null) {
		for (int j = 0; j < nextLayer.length; j++) {
		    nextLayer[j].setPrevious(temp);
		}
	    }
	    nextLayer = temp;
	}
	this.inputLayer = nextLayer;

    }

    public double process(int[] input) {
	this.initialAllNodeInput();
	for (int i = 0; i < this.inputLayer.length; i++) {
	    this.inputLayer[i].addInput(input[i]);
	}
	Node[] temp = this.inputLayer;
	for (int i = 0; i < layer.length; i++) {
	    for (int j = 0; j < this.layer[i]; j++) {
		temp[j].process();
	    }
	    if (temp[0].getNextLayer() != null) {
		temp = temp[0].getNextLayer();
	    }
	}
	return temp[0].getA();
    }

    public void initialAllNodeInput() {
	for (int i = 0; i < this.inputLayer.length; i++) {
	    this.inputLayer[i].initialInput();
	}
	Node[] temp = this.inputLayer;
	for (int i = 0; i < layer.length; i++) {
	    for (int j = 0; j < this.layer[i]; j++) {
		temp[j].initialInput();
	    }
	    if (temp[0].getNextLayer() != null) {
		temp = temp[0].getNextLayer();
	    }
	}
    }

    public void backPropagation(double target) {
	Node[] temp = this.outputLayer;
	for (int i = this.layer.length - 1; i >= 0; i--) {
	    for (int j = 0; j < this.layer[i]; j++) {
		if (i == this.layer.length - 1) {
		    temp[j].setError(target);
		}
		temp[j].backProcess();
	    }
	    temp = temp[0].getPreviousLayer();
	}
    }

    public void setAlpha(double value){
	for (int i = 0; i < this.inputLayer.length; i++) {
	    this.inputLayer[i].setAlpha(value);
	}
	Node[] temp = this.inputLayer;
	for (int i = 0; i < layer.length; i++) {
	    for (int j = 0; j < this.layer[i]; j++) {
		temp[j].setAlpha(value);
	    }
	    if (temp[0].getNextLayer() != null) {
		temp = temp[0].getNextLayer();
	    }
	}
    }
    
    public double getAlpha(){
	return this.inputLayer[0].getAlpha();
    }
    public void showNetwork() {
	Node[] temp = this.inputLayer;
	for (int i = 0; i < this.layer.length; i++) {
	    for (int j = 0; j < this.layer[i]; j++) {
//System.out.println(temp[j].getBiaWeight());
		//System.out.println(i + "|" + j + "|" + "Input = " + temp[j].getInput());
		//System.out.println(i + "|" + j + "|" + "Answer = " + temp[j].getA());
		for (int k = 0; k < temp[j].getWeight().length; k++) {
		    System.out.println(i + "|" + j + "|" + k + "|" + temp[j].getWeight()[k]);
		}
	    }
	    temp = temp[0].getNextLayer();
	}
    }

    public String toString() {
	String output = "", in_sym, out_sym;
	DecimalFormat df = new DecimalFormat("#.##");
	
	Node[] temp = this.inputLayer;
	for (int i = 0; i < this.layer.length - 1; i++) {
	    if(i == 0){
		in_sym = "I ";
	    }else{
		in_sym = "H ";
	    }
	    if(i == this.layer.length - 2){
		out_sym = "O ";
	    }else{
		out_sym = "H ";
	    }
	    output += in_sym + this.layer[i] + " " + out_sym + this.layer[i + 1] + "\n";
	    for (int j = 0; j < this.layer[i + 1]; j++) {
		for(int k = 0; k < this.layer[i]; k++){
		    //output += df.format(temp[k].getWeight()[j]) + " ";
		    output += temp[k].getWeight()[j] + " ";
		}
		//output += df.format(temp[0].getNextLayer()[j].getBiaWeight()) + "\n";
		output += temp[0].getNextLayer()[j].getBiaWeight() + "\n";
	    }
	    temp = temp[0].getNextLayer();
	}
	return output;
    }
}
