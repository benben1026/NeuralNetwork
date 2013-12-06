/**
 * @author Benjamin
 * @Dec 5 2013
 */
public class DataSet {
    private int[][] data;
    private int[] answer;
    private int dataLength;
    private int numOfRecord;
    private int nextRecord;
    private boolean ifValid;
    
    public DataSet(int length, int numOfRecord){
	this.dataLength = length;
	this.numOfRecord = numOfRecord;
	this.data = new int[this.numOfRecord][this.dataLength];
	this.answer = new int[this.numOfRecord];
	this.nextRecord = 0;
	this.ifValid = true;
    }
    
    public void addRecord(int[] newRecord, int answer){
	if(newRecord.length == this.dataLength){
	    this.answer[this.nextRecord] = answer;
	    System.arraycopy(newRecord, 0, this.data[this.nextRecord], 0, this.dataLength);
	    this.nextRecord ++;
	}else{
	    System.out.println("Error: the length of input data is not correct");
	}
    }
    
    public int[] getData(int index){
	if(index < this.numOfRecord){
	    return this.data[index];
	}else{
	    System.out.println("Array out of bound");
	    int[] temp = new int[this.dataLength];
	    return temp;
	}
    }
    public int getAnswer(int index){
	return this.answer[index];
    }
    public int getNumOfRecord(){
	return this.numOfRecord;
    }
    public void invalidDataSet(){
	this.ifValid = false;
    }
}
