/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Benajmin
 */

import java.io.*;
import java.lang.reflect.Array;

public class NeuralNetwork {
    public static void main(String[] args) {
	int[] layer = {3,1};
	Network net = new Network(layer);
	net.showNetwork();
	int[] input = {1,2,3};
	double output = net.process(input);
	System.out.println(output);
    }
}
